/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans.mot.team;

import entities.Competitor;
import entities.Team;
import exceptions.ApplicationException;
import exceptions.TeamCreationException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.model.DualListModel;
import web.utils.JsfUtils;

/**
 *
 * @author java
 */
@Named(value = "editTeamBackingBean")
@ViewScoped
public class EditTeamBackingBean extends TeamBackingBean implements Serializable {

    private Team team;

    public Team getTeam() {
        return team;
    }

    public boolean isDuplicatedCompetitorFlag() {
        return duplicatedCompetitorFlag;
    }

    public DualListModel getCompetitors() {
        return competitors;
    }

    public void setCompetitors(DualListModel competitors) {
        this.competitors = competitors;
    }

    @Override
    public List<Competitor> getCompetitorList() {
        return competitorList;
    }
    
    /**
     * Creates a new instance of EditTeamBackingBean
     */
    public EditTeamBackingBean() {
    }
    
    @PostConstruct
    private void init() {
        System.out.println("EditTeamBackingBean#init() " + this);
        team = controller.getEditingTeam();

        competitorList = controller.getAllTeamlessCompetitors();
        
        List<Competitor> competitorsSource = new ArrayList<>(competitorList); // competitorList will also include selected competitors
        List<Competitor> comeptitorsTarget = team.getCompetitorList();
        
        competitorList.addAll(team.getCompetitorList());

        competitors = new DualListModel(competitorsSource, comeptitorsTarget);
    }

    @Override
    public void checkDuplicate() {
        super.checkDuplicate(); 
    }
    
    public String editTeam() {
        try {
            team.setCompetitorList(competitors.getTarget());
            controller.editTeam(team);
            return "/index.xhtml?faces-redirect=true";
        } catch (ApplicationException e) {
            System.out.println("APPECEPTION " + e.getLocalizedMessage());
            JsfUtils.addErrorMessage(e.getLocalizedMessage(), null, null);
        } catch (Exception e) {
            System.out.println("Wyjatek przy edycji");
            Logger.getLogger(EditTeamBackingBean.class.getName()).log(Level.SEVERE, null, e);
        }
        
        return null;
    }
}
