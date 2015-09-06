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
import entities.Team;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import mot.utils.CMG;
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
        System.out.println("FD SIZEEEE " + editingCompetition.getGroupDetailsList().size());
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
    
    public void createCompetition(Competition competition, List<CMG> competitorMatchGroupList) {
        service.createCompetition(competition, competitorMatchGroupList);
    }
    
    public CompetitionType findCompetitionTypeById(int id) {
        return service.findCompetitionTypeById(id);
    }
    
    public List<CMG> generateEmptyBracket(List<Competitor> competitors){
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

    public List<CompetitorMatch> findCMGByIdMatch(Integer idMatch) {
        return service.findCMGByIdMatch(idMatch);
    }

    public List<Competition> findAllCompetitions() {
        return service.findAllCompetitions();
    }

}
