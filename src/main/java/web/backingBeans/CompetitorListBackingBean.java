/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans;

import entities.Competitor;
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
@Named(value = "competitorListBackingBean")
@ViewScoped
public class CompetitorListBackingBean implements Serializable {

    @Inject
    private CompetitionController controller;
    
    private List<Competitor> competitorList;

    public List<Competitor> getCompetitorList() {
        return competitorList;
    }
    
    /**
     * Creates a new instance of CompetitorListBackingBean
     */
    public CompetitorListBackingBean() {
    }
    
    @PostConstruct
    private void init() {
        competitorList = controller.getCompetitorsToEdit();
    }
    
    public String show(Competitor competitor) {
        controller.storeCompetitor(competitor);
        
        return "editCompetitor.xhtml?faces-redirect=true";
    }
}
