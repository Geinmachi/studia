/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.controllers;

import entities.Competition;
import entities.CompetitionType;
import entities.Competitor;
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
@Named(value = "competitionController")
@SessionScoped
public class CompetitionController implements Serializable {

    @EJB
    private CompetitionServiceLocal service;
    
    public CompetitionController() {
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
    
    public void createCompetition(Competition competition) {
        service.createCompetition(competition);
    }
    
    public CompetitionType findCompetitionTypeById(int id) {
        return service.findCompetitionTypeById(id);
    }
}
