/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans;

import entities.Competition;
import java.io.Serializable;
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
@Named(value = "competitionListBackingBean")
@ViewScoped
public class CompetitionListBackingBean implements Serializable{

    @Inject
    private CompetitionController controller;
    
    private List<Competition> competitionList;
    
    public CompetitionListBackingBean() {
    }

    public List<Competition> getCompetitionList() {
        return competitionList;
    }
    
    @PostConstruct
    private void init() {
        
        competitionList = controller.findAllCompetitions();
    }
    
    public String displayCompetition(Competition competition) {
        
        return "competitionDetails.xhtml";
    }
}
