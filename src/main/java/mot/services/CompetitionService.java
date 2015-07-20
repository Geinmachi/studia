/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.services;

import entities.Competitor;
import entities.Team;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import mot.facades.CompetitorFacadeLocal;
import mot.facades.TeamFacadeLocal;

/**
 *
 * @author java
 */
@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class CompetitionService implements CompetitionServiceLocal {

    @EJB
    private TeamFacadeLocal teamFacade;
    
    @EJB
    private CompetitorFacadeLocal competitorFacade;
    
    @Override
    public List<Team> findAllTeams() {
        return teamFacade.findAll();
    }

    @Override
    public void addCompetitor(Competitor competitor) {

        competitorFacade.create(competitor);
    }
}
