/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans;

import entities.Competitor;
import entities.Team;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.model.DualListModel;
import web.controllers.CompetitionController;
import web.converters.CompetitorConverterData;

/**
 *
 * @author java
 */
@Named(value = "createTeamBackingBean")
@ViewScoped
public class CreateTeamBackingBean implements Serializable, CompetitorConverterData{

    private final Team team = new Team();
    
    private DualListModel competitors;
    
    @Inject
    private CompetitionController controller;

    public Team getTeam() {
        return team;
    }

    public DualListModel getCompetitors() {
        return competitors;
    }

    public void setCompetitors(DualListModel competitors) {
        this.competitors = competitors;
    }
    
    /**
     * Creates a new instance of CreateTeamBackingBean
     */
    public CreateTeamBackingBean() {
    }

    
    @PostConstruct
    private void init() {
        System.out.println("CreateTeamBackingBean#init() " + this);
        
        List<Competitor> competitorsSource = controller.getAllTeamlessCompetitors();
        List<Competitor> comeptitorsTarget = new ArrayList<>();
        
        competitors = new DualListModel(competitorsSource, comeptitorsTarget);
        
    }
    
    @Override
    public List<Competitor> getCompetitorList() {
        return competitors.getSource();
    }
    
    public String createTeam() {
        team.setCompetitorList(competitors.getTarget());
        
        try {
            System.out.println("Competitors size BB " + team.getCompetitorList().size());
            controller.createTeam(team);
            return "/index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            System.out.println("Wyjatek przy twrzoeniu");
            Logger.getLogger(CreateTeamBackingBean.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }
    
}
