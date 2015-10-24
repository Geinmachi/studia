/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans.mot.team;

import entities.Team;
import exceptions.ApplicationException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        try {
            teamList = controller.getTeamsToEdit();
        } catch (ApplicationException ex) {
            Logger.getLogger(TeamListBackingBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException("Cannot init a bean");
        }
    }
    
    public String show(Team team) {
        controller.storeTeam(team);
        
        return "/edit/editTeam.xhtml?faces-redirect=true";
    }
}
