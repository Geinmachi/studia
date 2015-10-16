/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans.mot.team;

import entities.Team;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import web.backingBeans.mot.competition.CompetitionBackingBean;

/**
 *
 * @author java
 */
@Named(value = "teamListBackingBean")
@ViewScoped
public class TeamListBackingBean extends CompetitionBackingBean implements Serializable {

    private List<Team> teamList;

    public List<Team> getTeamList() {
        return teamList;
    }
    
    /**
     * Creates a new instance of TeamListBackingBean
     */
    public TeamListBackingBean() {
    }
    
    @PostConstruct
    private void init() {
        teamList = controller.getTeamsToEdit();
    }
    
    public String show(Team team) {
        controller.storeTeam(team);
        
        return "/edition/editTeam.xhtml?faces-redirect";
    }
}
