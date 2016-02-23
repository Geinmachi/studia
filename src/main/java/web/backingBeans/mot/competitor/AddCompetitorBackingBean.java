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
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import web.backingBeans.mot.competition.CompetitionBackingBean;
import web.converters.ConverterHelper;
import web.converters.interfaces.ConverterDataAccessor;
import web.utils.JsfUtils;
import web.utils.PageConstants;

/**
 *
 * @author java
 */
@Named(value = "addCompetitorBackingBean")
@ConverterHelper(viewId = PageConstants.ORGANIZER_ADD_COMPETITOR)
@ViewScoped
public class AddCompetitorBackingBean extends CompetitionBackingBean implements ConverterDataAccessor<Team>, Serializable {

    private final Competitor competitor = new Competitor();

    private final PersonalInfo personalInfo = new PersonalInfo();

    private boolean isGlobal = false;

    private List<Team> teamList;

    private Team selectedTeam;

    public AddCompetitorBackingBean() {
    }

    public Competitor getCompetitor() {
        System.out.println("Get competitor;");
        return competitor;
    }

    public PersonalInfo getPersonalInfo() {
        return personalInfo;
    }

    public boolean isIsGlobal() {
        System.out.println("isGlobal getter");
        return isGlobal;
    }

    public void setIsGlobal(boolean isGlobal) {
        this.isGlobal = isGlobal;
    }

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
        System.out.println("Addd logger+++ " + logger);
        logger.log(Level.INFO, "INIT AddCompetitorBB " + System.currentTimeMillis());
        competitor.setIdPersonalInfo(personalInfo);
        try {
            //        personalInfo.setCompetitor(competitor);
            teamList = controller.findUserAllowedTeams();
        } catch (ApplicationException ex) {
            ex.printStackTrace();
            Logger.getLogger(AddCompetitorBackingBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (teamList != null) {
            System.out.println("ADddbacking bean team list size " + teamList.size() + " i hashcode " + teamList.hashCode());
        }
    }

    public String addCompetitor() {
        try {
            competitor.setIdTeam(selectedTeam);
            controller.addCompetitor(competitor, isGlobal);
            FacesContext.getCurrentInstance().getViewRoot().getViewId();
            return JsfUtils.successPageRedirect(PageConstants.ORGANIZER_ADD_COMPETITOR);
        } catch (ApplicationException e) {
            JsfUtils.addErrorMessage(e, null);
            System.out.println("Application Exception ");
        } catch (Exception e) {
            System.out.println("Exception in AddCompetitorBB#addCompetitor");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Team> getFetchedData() {
        System.out.println("Fechuje z Addcompetitor ---------------------");
        return teamList;
    }

    @Inject
    private void testIN() {
        System.out.println("Inject z addompetitorbeana");
    }
    
}
