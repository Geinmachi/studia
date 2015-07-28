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
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import web.controllers.CompetitionController;

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

    public List<CompetitorMatchGroup> getCmgList() {
        return cmgList;
    }
    
    @PostConstruct
    private void init() {
        competition = controller.getEditingCompetition();
        if (competition == null) {
            throw new IllegalStateException("There is no competition to edit");
        }
        cmgList = controller.getCompetitionCMGMappings(competition);
        bracketCreator.recreateBracket(cmgList);
    }
    
//    private void getCMGMappers() { 
//        for (GroupCompetition gc : competition.getGroupCompetitionList()) {
//            for (CompetitorMatchGroup cmg : gc.getIdGroup().getCompetitorMatchGroupList()) {
//                cmgList.add(cmg);
//            }
//        }
//    }
}
