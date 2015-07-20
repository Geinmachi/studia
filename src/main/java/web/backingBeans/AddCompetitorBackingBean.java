/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans;

import entities.Competitor;
import entities.PersonalInfo;
import entities.Team;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import web.controllers.CompetitionController;

/**
 *
 * @author java
 */
@Named(value = "addCompetitorBackingBean")
@RequestScoped
public class AddCompetitorBackingBean {

    @Inject
    private CompetitionController controller;

    private final Competitor competitor = new Competitor();

    private final PersonalInfo personalInfo = new PersonalInfo();

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
//        personalInfo.setCompetitor(competitor);
        teamList = controller.findAllTeams();
    }

    public String addCompetitor() {
        try {
            controller.addCompetitor(competitor);
            return "/index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            System.out.println("Exception ");
            e.printStackTrace();
            return null;
        }
    }
}
