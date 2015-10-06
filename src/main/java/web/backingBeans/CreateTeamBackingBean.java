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
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import web.controllers.CompetitionController;
import web.converters.CompetitorConverterData;

/**
 *
 * @author java
 */
@Named(value = "createTeamBackingBean")
@SessionScoped
public class CreateTeamBackingBean implements Serializable, CompetitorConverterData{

    private String na = "abcd";
    
    @Inject
    private CompetitionController controller;

    public String getNa() {
        return na;
    }

    public void setNa(String na) {
        this.na = na;
    }
    
    @PostConstruct
    private void init() {
        System.out.println("CreateTeamBackingBean#init() " + this);
    }
    /**
     * Creates a new instance of CreateTeamBackingBean
     */
    public CreateTeamBackingBean() {
    }

    @Override
    public List<Competitor> getCompetitorList() {
        return controller.getAllCompetitors();
    }
    
}
