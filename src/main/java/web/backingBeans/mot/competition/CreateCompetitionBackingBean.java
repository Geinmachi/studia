/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans.mot.competition;

import web.utils.BracketCreation;
import entities.Competition;
import entities.Competitor;
import entities.MatchMatchType;
import entities.MatchType;
import exceptions.ApplicationException;
import exceptions.NotAllowedMatchTypeException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import mot.interfaces.CMG;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.DualListModel;
import web.converters.ConverterHelper;
import web.converters.interfaces.ConverterDataAccessor;
import web.converters.interfaces.MatchTypeConverterAccessor;
import web.utils.JsfUtils;
import web.utils.PageConstants;
import web.validators.CompetitorsDualListSetter;
import web.validators.DuplicatedCompetitors;

/**
 *
 * @author java
 */
@Named(value = "createCompetitionBackingBean")
@ConverterHelper(viewId = PageConstants.ORGANIZER_CREATE_COMPETITION)
@ViewScoped
public class CreateCompetitionBackingBean extends CompetitionBackingBean
        implements ConverterDataAccessor<Competitor>, Serializable, CompetitorsDualListSetter,
        DuplicatedCompetitors, MatchTypeConverterAccessor {

    @Inject
    private BracketCreation bracketCreator;

    private final Competition competition = new Competition();

    private List<Competitor> competitorList = new ArrayList<>(); // for converter

//    private List<CompetitionType> competitionTypes = new ArrayList<>();

    private DualListModel competitors;

    private boolean duplicatedCompetitorFlag;

    private boolean competitionNameConstrains;

    private boolean isCompetitorsAmountValid;

    public CreateCompetitionBackingBean() {
    }

    public Competition getCompetition() {
        return competition;
    }

//    @Override
    public List<Competitor> getCompetitorList() {
        return competitorList;
    }

    public DualListModel getCompetitors() {
        return competitors;
    }

    @Override
    public void setCompetitors(DualListModel competitors) {
        this.competitors = competitors;
    }

    public boolean isDuplicatedCompetitorFlag() {
        return duplicatedCompetitorFlag;
    }

    @Override
    public void setDuplicatedCompetitorFlag(boolean duplicatedCompetitorFlag) {
        this.duplicatedCompetitorFlag = duplicatedCompetitorFlag;
    }

    public boolean isCompetitionNameConstrains() {
        return competitionNameConstrains;
    }

    public boolean isIsCompetitorsAmountValid() {
        return isCompetitorsAmountValid;
    }

    public boolean getIsCompetitorsAmountValid() {
        return isCompetitorsAmountValid;
    }

    public BracketCreation getBracketCreator() {
        return bracketCreator;
    }

    @PostConstruct
    private void init() {
        logger.log(Level.INFO, "Test logera, init createCompetitionBackingBeana");
        System.out.println("CReateCompetitionBB#init() " + this);
        competitorList = controller.getAllCompetitors();

        List<Competitor> competitorsSource = competitorList;
        List<Competitor> comeptitorsTarget = new ArrayList<>();

        competitors = new DualListModel(competitorsSource, comeptitorsTarget);
    }

    public String onFlowProcess(FlowEvent event) {
        if (event.getOldStep().equals("firstStep")) {
            bracketCreator.createEmptyBracket(competitors.getTarget());

        } else if (event.getNewStep().equals("firstStep")) {
            RequestContext.getCurrentInstance().update("msgs");
            bracketCreator.clearLists();
        }

        return event.getNewStep();
    }

    public String createCompetition() {
        try {
//            competition.setIdCompetitionType(selectedCompetitionType);
//            competition.setIdCompetitionType(competitionTypes.get(0));

            bracketCreator.assignMatchTypes();
            controller.createCompetition(competition, bracketCreator.getCompetitorMatchGroupList());

            return JsfUtils.successPageRedirect(PageConstants.ORGANIZER_CREATE_COMPETITION);
        } catch (ApplicationException e) {
            JsfUtils.addErrorMessage(e, null);
            Logger.getLogger(CreateCompetitionBackingBean.class.getName()).log(Level.SEVERE, null, e);
        } catch (Exception e) {
            System.out.println("Wyjatek przy twrzoeniu");
            Logger.getLogger(CreateCompetitionBackingBean.class.getName()).log(Level.SEVERE, null, e);
        }

        return null;
    }

    public void checkDuplicate() {
        isCompetitorsAmountValid = controller.validateCompetitorsAmount(competitors.getTarget().size()); // do jsf mesage

        Competitor duplicatedCompetitor = controller.validateCompetitorDuplicate((List<Competitor>) competitors.getTarget());

        if (duplicatedCompetitor != null) {
            System.out.println("Duplicated competitor");
            JsfUtils.addErrorMessage("Competition contains duplicated competitor",
                    duplicatedCompetitor.getIdPersonalInfo().getFirstName() + " "
                    + duplicatedCompetitor.getIdPersonalInfo().getLastName(), null);
            duplicatedCompetitorFlag = true;

            System.out.println("CHECK DUP msgs " + FacesContext.getCurrentInstance().getMessageList().size());

            return;
        }

        duplicatedCompetitorFlag = false;
    }

    public void globalValueChanged(ValueChangeEvent e) {
        System.out.println("NOWA WARTOSC " + e.getNewValue());
    }

    public void checkCompetitionConstraints() throws ApplicationException {
        controller.checkCompetitionConstraints(competition);
    }

    @Override
    public List<Competitor> getFetchedData() {
        return competitorList;
    }

    @Override
    public List<MatchType> getMatchTypeList() {
        return bracketCreator.getMatchTypeList();
    }
}
