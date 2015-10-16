/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans.mot.competitor;

import entities.Competitor;
import entities.Team;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import web.backingBeans.mot.competition.CompetitionBackingBean;
import web.controllers.CompetitionController;
import web.converters.interfaces.TeamConverterData;

/**
 *
 * @author java
 */
@Named(value = "editCompetitorBackingBean")
@RequestScoped
public class EditCompetitorBackingBean extends CompetitionBackingBean implements TeamConverterData {

    private Competitor competitor;

    private List<Team> teamList;

    public Competitor getCompetitor() {
        return competitor;
    }

    @Override
    public List<Team> getTeamList() {
        return teamList;
    }

    /**
     * Creates a new instance of EditCompetitorBackingBean
     */
    public EditCompetitorBackingBean() {
    }

    @PostConstruct
    private void init() {
        competitor = controller.getEditingCompetitor();
        teamList = controller.findAllTeams();
    }

    public String edit() {
        try {
            controller.editCompetitor(competitor);
            return "/index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            System.out.println("EXception " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
}
