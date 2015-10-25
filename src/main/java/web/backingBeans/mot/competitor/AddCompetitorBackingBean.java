/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans.mot.competitor;

import entities.Competitor;
import entities.PersonalInfo;
import entities.Team;
import exceptions.ApplicationException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import web.backingBeans.mot.competition.CompetitionBackingBean;
import web.controllers.CompetitionController;
import web.converters.interfaces.TeamConverterData;
import web.utils.JsfUtils;
import web.utils.PageConstants;

/**
 *
 * @author java
 */
@Named(value = "addCompetitorBackingBean")
@RequestScoped
public class AddCompetitorBackingBean extends CompetitionBackingBean implements TeamConverterData {

    private final Competitor competitor = new Competitor();

    private final PersonalInfo personalInfo = new PersonalInfo();

    private boolean isGlobal = false;

    private List<Team> teamList;

    private Team selectedTeam;

    public AddCompetitorBackingBean() {
    }

    public Competitor getCompetitor() {
        return competitor;
    }

    public PersonalInfo getPersonalInfo() {
        return personalInfo;
    }

    public boolean isIsGlobal() {
        return isGlobal;
    }

    public void setIsGlobal(boolean isGlobal) {
        this.isGlobal = isGlobal;
    }

    @Override
    public List<Team> getTeamList() {
        return teamList;
    }

    public Team getSelectedTeam() {
        return selectedTeam;
    }

    public void setSelectedTeam(Team selectedTeam) {
        this.selectedTeam = selectedTeam;
    }

    @PostConstruct
    private void init() {
        competitor.setIdPersonalInfo(personalInfo);
        try {
            //        personalInfo.setCompetitor(competitor);
            teamList = controller.findUserAllowedTeams();
        } catch (ApplicationException ex) {
            ex.printStackTrace();
            Logger.getLogger(AddCompetitorBackingBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String addCompetitor() {
        try {
            competitor.setIdTeam(selectedTeam);
            controller.addCompetitor(competitor, isGlobal);
            
            return JsfUtils.successPageRedirect(PageConstants.ORGANIZER_ADD_COMPETITOR);
        } catch (ApplicationException e) {
            JsfUtils.addErrorMessage(e.getLocalizedMessage(), null, null);
            
            System.out.println("Application Exception ");
        } catch (Exception e) {

            System.out.println("Exception in AddCompetitorBB#addCompetitor");
            e.printStackTrace();
        }

        return null;
    }
}
