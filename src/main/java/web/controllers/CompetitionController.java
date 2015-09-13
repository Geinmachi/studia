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
import entities.MatchType;
import entities.Score;
import entities.Team;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import mot.utils.CMG;
import mot.services.CompetitionService;
import mot.services.CompetitionServiceLocal;
import web.utils.DisplayPageEnum;

/**
 *
 * @author java
 */
@SessionScoped
public class CompetitionController implements Serializable {

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

    public void addCompetitor(Competitor competitor) {
        service.addCompetitor(competitor);
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

    public CompetitorMatch saveCompetitorScore(CompetitorMatch cmg) {
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

}
