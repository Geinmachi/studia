/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans;

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
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.RequestScoped;
import javax.inject.Named;
//import javax.enterprise.context.RequestScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import mot.services.CompetitionService;
import org.primefaces.event.FlowEvent;
import web.controllers.CompetitionController;
import web.qualifiers.CompetitionCreation;
import web.utils.ConverterData;

/**
 *
 * @author java
 */
//@CompetitionCreation
@Named(value = "createCompetitionBackingBean")
@ViewScoped
public class CreateCompetitionBackingBean implements Serializable, ConverterData {

    @Inject
    private CompetitionController controller;

    @Inject
    private BracketCreation bracketCreator;

    private final Competition competition = new Competition();

    private List<Competitor> competitorList = new ArrayList<>();

    private final List<Competitor> selectedCompetitors = new ArrayList<>();

    private List<CompetitionType> competitionTypes = new ArrayList<>();

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

    public List<Competitor> getSelectedCompetitors() {
        return selectedCompetitors;
    }

    public List<CompetitionType> getCompetitionTypes() {
        return competitionTypes;
    }

    public CompetitionType getSelectedCompetitionType() {
        return selectedCompetitionType;
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
        System.out.println("CreateCompetitionBackingBean#init ");
        System.out.println("HASCODE beana " + this.toString());
        competitorList = controller.getAllCompetitors();
        competitionTypes = controller.getAllCompetitionTypes();
        for (int i = 0; i < 16; i++) {
            selectedCompetitors.add(competitorList.get(i));
        }
        competition.setCompetitionName("ddd");
        competition.setEndDate(new Date());
        competition.setStartDate(new Date());
    }

    @PreDestroy
    private void destroy() {
        System.out.println("CreateCompetitionBackingBean#destroyyyyy ");
        System.out.println("HASCODE beana " + this.toString());
    }
    
    public String onFlowProcess(FlowEvent event) {
        if (event.getOldStep().equals("firstStep")) {
            bracketCreator.createEmptyBracket(selectedCompetitors);

        } else if (event.getNewStep().equals("firstStep")) {
            bracketCreator.clearLists();
        }
        return event.getNewStep();
    }

    public void addCompetitor() {
        if (selectedToAdd != null) {
            selectedCompetitors.add(selectedToAdd);
            competitorList.remove(selectedToAdd);
            isCompetitorsAmountValid = controller.validateCompetitorsAmount(selectedCompetitors.size());
            selectedToAdd = null;
        }
    }

    public void removeCompetitor() {
        if (selectedToRemove != null) {
            competitorList.add(selectedToRemove);
            selectedCompetitors.remove(selectedToRemove);
            isCompetitorsAmountValid = controller.validateCompetitorsAmount(selectedCompetitors.size());
            selectedToRemove = null;
        }
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
}
