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
import entities.CompetitorMatch;
import entities.GroupName;
import entities.Matchh;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.inject.Named;
//import javax.enterprise.context.RequestScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import mot.services.CompetitionService;
import org.primefaces.component.picklist.PickList;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.DualListModel;
import web.controllers.CompetitionController;
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

    private CompetitionType selectedCompetitionType;

    private Competitor selectedToRemove;

    private Competitor selectedToAdd;

    private boolean isCompetitorsAmountValid;

    public CreateCompetitionBackingBean() {
    }

    public Competition getCompetition() {
        return competition;
    }

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
            if (duplicatedCompetitorFlag || !isCompetitorsAmountValid ) {
                System.out.println("ONFLOW duplicated");
                return event.getOldStep();
            }
            bracketCreator.createEmptyBracket(competitors.getTarget());

        } else if (event.getNewStep().equals("firstStep")) {
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
        } catch (Exception e) {
            System.out.println("Wyjatek przy twrzoeniu");
            Logger.getLogger(CreateCompetitionBackingBean.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
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
            
            UIInput pickList = (UIInput)FacesContext.getCurrentInstance().getViewRoot().findComponent("createCompetitionForm:competitorPickList");
            pickList.setValid(false);
            
            return;
        }

        duplicatedCompetitorFlag = false;
    }
}
