/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans;

import entities.Competition;
import entities.CompetitionType;
import entities.Competitor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import mot.services.CompetitionService;
import org.primefaces.event.FlowEvent;
import web.controllers.CompetitionController;

/**
 *
 * @author java
 */
@Named(value = "createCompetitionBackingBean")
@ViewScoped
public class CreateCompetitionBackingBean implements Serializable {

    @Inject
    private CompetitionController controller;

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

    @PostConstruct
    private void init() {
        competitorList = controller.getAllCompetitors();
        competitionTypes = controller.getAllCompetitionTypes();
        for (int i = 0; i <16; i++) {
            selectedCompetitors.add(competitorList.get(i));
        }
        competition.setCompetitionName("ddd");
        competition.setEndDate(new Date());
        competition.setStartDate(new Date());
    }

    public void onFlowProcess(FlowEvent event) {
            if (event.getNewStep().equals("secondStep")) {
                
        }
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
            controller.createCompetition(competition, selectedCompetitors);
            return "/index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            System.out.println("Wyjatek przy twrzoeniu");
            Logger.getLogger(CreateCompetitionBackingBean.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }

    }
}