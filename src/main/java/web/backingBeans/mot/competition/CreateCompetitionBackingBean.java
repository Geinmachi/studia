/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans.mot.competition;

import web.utils.BracketCreation;
import entities.Competition;
import entities.CompetitionType;
import entities.Competitor;
import exceptions.ApplicationException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;
//import javax.enterprise.context.RequestScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.DualListModel;
import web.converters.interfaces.CompetitorConverterData;
import web.utils.JsfUtils;

/**
 *
 * @author java
 */
@Named(value = "createCompetitionBackingBean")
@ViewScoped
public class CreateCompetitionBackingBean extends CompetitionBackingBean implements Serializable, CompetitorConverterData {

    @Inject
    private BracketCreation bracketCreator;

    private final Competition competition = new Competition();

    private List<Competitor> competitorList = new ArrayList<>(); // for converter

    private List<CompetitionType> competitionTypes = new ArrayList<>();

    private DualListModel competitors;

    private boolean duplicatedCompetitorFlag;
    
    private boolean competitionNameConstrains;

    private CompetitionType selectedCompetitionType;

    private Competitor selectedToRemove;

    private Competitor selectedToAdd;

    private boolean isCompetitorsAmountValid;

    public CreateCompetitionBackingBean() {
    }

    public Competition getCompetition() {
        return competition;
    }

    @Override
    public List<Competitor> getCompetitorList() {
        return competitorList;
    }

    public List<CompetitionType> getCompetitionTypes() {
        return competitionTypes;
    }

    public CompetitionType getSelectedCompetitionType() {
        return selectedCompetitionType;
    }

    public DualListModel getCompetitors() {
        return competitors;
    }

    public void setCompetitors(DualListModel competitors) {
        this.competitors = competitors;
    }

    public boolean isDuplicatedCompetitorFlag() {
        return duplicatedCompetitorFlag;
    }

    public boolean isCompetitionNameConstrains() {
        return competitionNameConstrains;
    }

    public boolean isIsCompetitorsAmountValid() {
        return isCompetitorsAmountValid;
    }

    public void setSelectedCompetitionType(CompetitionType selectedCompetitionType) {
        this.selectedCompetitionType = selectedCompetitionType;
    }

    public Competitor getSelectedToRemove() {
        return selectedToRemove;
    }

    public void setSelectedToRemove(Competitor selectedToRemove) {
        this.selectedToRemove = selectedToRemove;
    }

    public Competitor getSelectedToAdd() {
        return selectedToAdd;
    }

    public void setSelectedToAdd(Competitor selectedToAdd) {
        this.selectedToAdd = selectedToAdd;
    }

    public boolean getIsCompetitorsAmountValid() {
        return isCompetitorsAmountValid;
    }

    public BracketCreation getBracketCreator() {
        return bracketCreator;
    }

    @PostConstruct
    private void init() {
        System.out.println("CReateCompetitionBB#init() " + this);
        competitorList = controller.getAllCompetitors();

        List<Competitor> competitorsSource = competitorList;
        List<Competitor> comeptitorsTarget = new ArrayList<>();

        competitors = new DualListModel(competitorsSource, comeptitorsTarget);

        competitionTypes = controller.getAllCompetitionTypes();
        for (int i = 0; i < 16; i++) {
            comeptitorsTarget.add(competitorList.get(i));
        }
        competition.setCompetitionName("ddd");
        competition.setEndDate(new Date());
        competition.setStartDate(new Date());
    }

    public String onFlowProcess(FlowEvent event) {
        if (event.getOldStep().equals("firstStep")) {
            if (duplicatedCompetitorFlag || !isCompetitorsAmountValid || !competitionNameConstrains) {
                System.out.println("ONFLOW duplicated");
        //            return event.getOldStep();
            }
            bracketCreator.createEmptyBracket(competitors.getTarget());

        } else if (event.getNewStep().equals("firstStep")) {

            RequestContext.getCurrentInstance().update("msgs");
            bracketCreator.clearLists();
        }
        return event.getNewStep();
    }

    public String createCompetition() {
        try {
            competition.setIdCompetitionType(selectedCompetitionType);
            bracketCreator.updateBracket();
            controller.createCompetition(competition, bracketCreator.getCompetitorMatchGroupList());
            return "/index.xhtml?faces-redirect=true";
        } catch (ApplicationException e) {
            JsfUtils.addErrorMessage(e.getLocalizedMessage(), " ", null);
            Logger.getLogger(CreateCompetitionBackingBean.class.getName()).log(Level.SEVERE, null, e);
        } catch (Exception e) {
            System.out.println("Wyjatek przy twrzoeniu");
            Logger.getLogger(CreateCompetitionBackingBean.class.getName()).log(Level.SEVERE, null, e);
        }

        return null;
    }

    public void checkDuplicate() {
        isCompetitorsAmountValid = controller.validateCompetitorsAmount(competitors.getTarget().size()); // do jsf mesage
//        JsfUtils.addErrorMessage(null, null, null);

        Competitor duplicatedCompetitor = controller.vlidateCompetitorDuplicate((List<Competitor>) competitors.getTarget());

        if (duplicatedCompetitor != null) {
            System.out.println("Duplicated competitor");
            JsfUtils.addErrorMessage("Competition contains duplicated competitor",
                    duplicatedCompetitor.getIdPersonalInfo().getFirstName() + " "
                    + duplicatedCompetitor.getIdPersonalInfo().getLastName(), null);
            duplicatedCompetitorFlag = true;

            UIInput pickList = (UIInput) FacesContext.getCurrentInstance().getViewRoot().findComponent("createCompetitionForm:competitorPickList");
            pickList.setValid(false);

            return;
        }

        duplicatedCompetitorFlag = false;
    }

    public void globalValueChanged (ValueChangeEvent e) {
        System.out.println("NOWA WARTOSC " + e.getNewValue());
    }
    
    public void competitionConstraints(AjaxBehaviorEvent event) {
        
            UIInput input = ((UIInput)FacesContext.getCurrentInstance().getViewRoot().findComponent("createCompetitionForm:competitionName"));
            
            System.out.println("Input " + input.getClientId());
            System.out.println("Input " + input.getValidators());
            
            input.processValidators(FacesContext.getCurrentInstance());
            
            System.out.println("Competition name ajax " + competition.getCompetitionName());
            System.out.println("Competition global ajax " + competition.isGlobal());
            System.out.println("SUBMITTED VALUE " + ((UIInput)event.getComponent()).getSubmittedValue());
//        event.getComponent().processValidators(FacesContext.getCurrentInstance());
//        try {
//            checkCompetitionNameConstraints();
//            competitionNameConstrains = true;
//        } catch (ApplicationException ex) {
//            competitionNameConstrains = false;
//            
//            UIInput input = (UIInput) event.getComponent();
//            input.setValid(false);
//            
//            JsfUtils.addErrorMessage(ex.getLocalizedMessage(), " ", input.getClientId());
//            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
//            
//            Logger.getLogger(CreateCompetitionBackingBean.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
    
    public void checkCompetitionNameConstraints(String competitionName) throws ApplicationException {
//            System.out.println("Competition name throssss " + competition.getCompetitionName());
        competition.setCompetitionName(competitionName);
        controller.checkCompetitionConstraints(competition);
    }
}
