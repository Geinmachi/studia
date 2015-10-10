/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.controllers;

import entities.Competition;
import entities.CompetitionType;
import entities.Competitor;
import entities.CompetitorMatch;
import entities.MatchMatchType;
import entities.MatchType;
import entities.Matchh;
import entities.Score;
import entities.Team;
import exceptions.ApplicationException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import mot.interfaces.CMG;
import mot.services.CompetitionService;
import mot.services.CompetitionServiceLocal;
import mot.interfaces.CurrentMatchType;
import mot.interfaces.InactivateMatch;
import web.models.DashboardPanel;
import web.utils.DisplayPageEnum;

/**
 *
 * @author java
 */
@SessionScoped
public class CompetitionController implements Serializable {

    private static final long serialVersionUID = 645645987823402129L;

    @EJB
    private CompetitionServiceLocal service;

    private Competition editingCompetition;

    private Competition displayedCompetition;

    private DisplayPageEnum pageType;

    public CompetitionController() {
    }

    public Competition getEditingCompetition() {
        System.out.println("FD SIZEEEE " + editingCompetition.getGroupDetailsList().size());
        return editingCompetition;
    }

    public Competition getDisplayedCompetition(DisplayPageEnum type) {
        System.out.println("TYPP pram " + type + " zmienna " + pageType);
        if (type.equals(pageType)) {
            return service.getInitializedCompetition(displayedCompetition.getIdCompetition());
        } else {
            return null;
        }
    }

    public void setDisplayedCompetition(Competition competition, DisplayPageEnum type) {
        this.displayedCompetition = competition;
        this.pageType = type;
    }

    public DisplayPageEnum getPageType() {
        return pageType;
    }

    public void setPageType(DisplayPageEnum pageType) {
        this.pageType = pageType;
    }

    public List<Team> findAllTeams() {

        return service.findAllTeams();
    }

    public void addCompetitor(Competitor competitor, boolean global) throws ApplicationException {
        service.addCompetitor(competitor, global);
    }

    public List<Competitor> getAllCompetitors() {
        return service.getAllCompetitors();
    }

    public List<CompetitionType> getAllCompetitionTypes() {
        return service.getAllCompetitionTypes();
    }

    public Competitor findCompetitorById(Integer id) {
        return service.findCopetitorById(id);
    }

    public boolean validateCompetitorsAmount(int amount) {
        return service.validateCompetitorsAmount(amount);
    }

    public void createCompetition(Competition competition, List<CMG> competitorMatchGroupList) {
        service.createCompetition(competition, competitorMatchGroupList);
    }

    public CompetitionType findCompetitionTypeById(int id) {
        return service.findCompetitionTypeById(id);
    }

    public List<CMG> generateEmptyBracket(List<Competitor> competitors) {
        return service.generateEmptyBracket(competitors);
    }

    public List<MatchType> getEndUserMatchTypes() {
        return service.getEndUserMatchTypes();
    }

    public List<Competition> getLoggedUserCompetitions() {
        return service.getLoggedUserCompetitions();
    }

    public void storeCompetition(Competition competition) {
        editingCompetition = service.storeCompetition(competition);
    }

    public List<CMG> getCompetitionCMGMappings(Competition competition) {
        return service.getCompetitionCMGMappings(competition);
    }

    public Map<String, CompetitorMatch> saveCompetitorScore(CompetitorMatch cmg)  throws ApplicationException{
        return service.saveCompetitorScore(cmg);
    }

    public List<CompetitorMatch> findCompetitorMatchByIdMatch(Integer idMatch) {
        return service.findCompeitorMatchByIdMatch(idMatch);
    }

    public List<Competition> findAllCompetitions() {
        return service.findAllCompetitions();
    }

    public List<Score> findCompetitionScores(Competition competition) {
        return service.findCompetitionScores(competition.getIdCompetition());
    }

    public Map<Competitor, Integer> getCompetitionResults(Competition competition) {
        return service.getCompetitionResults(competition.getIdCompetition());
    }

    public MatchMatchType updateMatchType(Matchh match) {
        return service.updateMatchType(match);
    }

    public InactivateMatch disableMatch(InactivateMatch inactivateMatch) {
        return service.disableMatch(inactivateMatch);
    }

    public CurrentMatchType assignCurrentMatchType(CurrentMatchType cmt) {
        return service.assignCurrentMatchType(cmt);
    }

    public CompetitorMatch advanceCompetitor(CompetitorMatch competitorMatch) {
        return service.advanceCompetitor(competitorMatch);
    }

    public void saveCompetitionGeneralInfo(Competition competition) {
        editingCompetition = service.saveCompetitionGeneralInfo(competition);
    }

    public void createTeam(Team team) {
        service.createTeam(team);
    }

    public List<Competitor> getAllTeamlessCompetitors() {
        return service.getAllTeamlessCompetitors();
    }
}
