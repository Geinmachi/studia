/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.services;

import entities.Competition;
import entities.CompetitionType;
import entities.Competitor;
import entities.Team;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import mot.facades.CompetitionTypeFacadeLocal;
import mot.facades.CompetitorFacadeLocal;
import mot.facades.TeamFacadeLocal;
import mot.managers.CreateCompetitionManagerLocal;

/**
 *
 * @author java
 */
@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class CompetitionService implements CompetitionServiceLocal {

    @EJB
    private CreateCompetitionManagerLocal createCompetitionManager;
    
    @EJB
    private TeamFacadeLocal teamFacade;

    @EJB
    private CompetitorFacadeLocal competitorFacade;
    
    @EJB
    private CompetitionTypeFacadeLocal competitionTypeFacade;

    @Override
    public List<Team> findAllTeams() {
        return teamFacade.findAll();
    }

    @Override
    public void addCompetitor(Competitor competitor) {
        competitorFacade.create(competitor);
    }

    @Override
    public List<Competitor> getAllCompetitors() {
        return competitorFacade.findAll();
    }

    @Override
    public List<CompetitionType> getAllCompetitionTypes() {
        return competitionTypeFacade.findAll();
    }

    @Override
    public Competitor findCopetitorById(Integer id) {
        return competitorFacade.find(id);
    }

    @Override
    public boolean validateCompetitorsAmount(int amount) {
        return createCompetitionManager.validateCompetitorsAmount(amount);
    }

    @Override
    public void createCompetition(Competition competition, List<Competitor> competitors) {
        createCompetitionManager.createCompetition(competition, competitors);
    }

    @Override
    public CompetitionType findCompetitionTypeById(int id) {
        return competitionTypeFacade.find(id);
    }
}
