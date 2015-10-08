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
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.model.DualListModel;
import web.controllers.CompetitionController;
import web.qualifiers.TeamCreation;
import web.utils.ConverterData;

/**
 *
 * @author java
 */
//@TeamCreation
@Named(value = "createTeamBackingBean")
@ViewScoped
public class CreateTeamBackingBean implements Serializable, ConverterData {

    @Inject
    private CompetitionController controller;
    
    private DualListModel<Competitor> competitors;

    private final Team team = new Team();

    public Team getTeam() {
        return team;
    }

    public DualListModel<Competitor> getCompetitors() {
        return competitors;
    }

    public void setCompetitors(DualListModel<Competitor> competitors) {
        this.competitors = competitors;
    }

    @Override
    public List<Competitor> getCompetitorList() {
        return competitors.getSource();
    }
    
    @PostConstruct
    private void init() {
        List<Competitor> competitorsSource = controller.getAllCompetitors();
        List<Competitor> competitorsTarget = new ArrayList<>();
        
        competitors = new DualListModel<Competitor>(competitorsSource, competitorsTarget);
        
    }
    
    public CreateTeamBackingBean() {
    }


}
