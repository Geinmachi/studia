/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans;

import entities.Competitor;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import web.controllers.CompetitionController;

/**
 *
 * @author java
 */
@Named(value = "editCompetitorBackingBean")
@RequestScoped
public class EditCompetitorBackingBean {

    @Inject
    private CompetitionController controller;
    
    private Competitor competitor;

    public Competitor getCompetitor() {
        return competitor;
    }
    
    
    /**
     * Creates a new instance of EditCompetitorBackingBean
     */
    public EditCompetitorBackingBean() {
    }
    
    @PostConstruct
    private void init() {
        competitor = controller.getEditingCompetitor();
    }
    
    public String edit() {
        
        return "index.xhtml?faces-redirect=true";
    }
}
