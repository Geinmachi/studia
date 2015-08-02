/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.controllers;

import entities.Competition;
import entities.CompetitionType;
import entities.Competitor;
import entities.CompetitorMatchGroup;
import entities.MatchType;
import entities.Team;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import mot.services.CompetitionService;
import mot.services.CompetitionServiceLocal;

/**
 *
 * @author java
 */
@SessionScoped
public class CompetitionController implements Serializable {

    @EJB
    private CompetitionServiceLocal service;
    
    private Competition editingCompetition;
    
    public CompetitionController() {
    }

    public Competition getEditingCompetition() {
        return editingCompetition;
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
    
    public void createCompetition(Competition competition, List<CompetitorMatchGroup> competitorMatchGroupList) {
        service.createCompetition(competition, competitorMatchGroupList);
    }
    
    public CompetitionType findCompetitionTypeById(int id) {
        return service.findCompetitionTypeById(id);
    }
    
    public List<CompetitorMatchGroup> generateEmptyBracket(List<Competitor> competitors){
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

    public List<CompetitorMatchGroup> getCompetitionCMGMappings(Competition competition) {
        return service.getCompetitionCMGMappings(competition);
    }

    public CompetitorMatchGroup saveCompetitorScore(CompetitorMatchGroup cmg) {
        return service.saveCompetitorScore(cmg);
    }

}
