/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans;

import entities.Competition;
import entities.Competitor;
import entities.CompetitorMatchGroup;
import entities.GroupCompetition;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import web.controllers.CompetitionController;
import web.models.DashboardPanel;

/**
 *
 * @author java
 */
@Named(value = "manageCompetitionBackingBean")
@ViewScoped
public class ManageCompetitionBackingBean implements Serializable {

    @Inject
    private CompetitionController controller;

    @Inject
    private BracketCreationBackingBean bracketCreator;

    private List<Competitor> competitorList = new ArrayList<>();

    private List<CompetitorMatchGroup> cmgList = new ArrayList<>();

    private Competition competition;

    /**
     * Creates a new instance of ManageCompetitionBacking
     */
    public ManageCompetitionBackingBean() {
    }

    public BracketCreationBackingBean getBracketCreator() {
        return bracketCreator;
    }

    public Competition getCompetition() {
        return competition;
    }

//    public List<CompetitorMatchGroup> getCmgList() {
//        return cmgList;
//    }

    @PostConstruct
    private void init() {
        competition = controller.getEditingCompetition();
        if (competition == null) {
            throw new IllegalStateException("There is no competition to edit");
        }
        cmgList = controller.getCompetitionCMGMappings(competition);
        bracketCreator.recreateBracket(cmgList);
    }

    public void saveScore(CompetitorMatchGroup cmg) {
        System.out.println("Wykonal sie save z inpalce " + cmg.getCompetitorMatchScore());
        System.out.println("Zawodnika " + cmg.getIdCompetitor());
        try {
            CompetitorMatchGroup advancedCMG = controller.saveCompetitorScore(cmg);
            if (advancedCMG != null) {
                cmgList.add(advancedCMG);
                bracketCreator.addAdvancedCompetitor(advancedCMG);
            }
            System.out.println("Przeszlo all");
        } catch (IllegalStateException ise) {
            if (ise.getMessage().equals("Too big number")) {
                cmg.setCompetitorMatchScore((short) 0);
            }
        }
    }

    public void saveScore2(CompetitorMatchGroup cmg) {
        System.out.println("Wykonal sie save222 z inputTexta " + cmg.getIdCompetitor());
//        for(Competi)
        
//        controller.saveCompetitorScore();
    }
//    private void getCMGMappers() { 
//        for (GroupCompetition gc : competition.getGroupCompetitionList()) {
//            for (CompetitorMatchGroup cmg : gc.getIdGroup().getCompetitorMatchGroupList()) {
//                cmgList.add(cmg);
//            }
//        }
//    }
}
