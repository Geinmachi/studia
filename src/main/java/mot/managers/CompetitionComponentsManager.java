/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.managers;

import ejbCommon.TrackerInterceptor;
import entities.AccessLevel;
import entities.Account;
import entities.Competitor;
import entities.Organizer;
import entities.Team;
import exceptions.ApplicationException;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import mot.facades.AccountFacadeLocal;
import mot.facades.CompetitorFacadeLocal;
import mot.facades.TeamFacadeLocal;
import utils.ConvertUtil;

/**
 *
 * @author java
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors({TrackerInterceptor.class})
public class CompetitionComponentsManager implements CompetitionComponentsManagerLocal {

    @Resource
    private SessionContext sessionContext;

    @EJB
    private TeamFacadeLocal teamFacade;

    @EJB
    private CompetitorFacadeLocal competitorFacade;
    
    @EJB
    private AccountFacadeLocal accountFacade;

    @Override
    public void createTeam(Team team) {
        team = teamFacade.createWithReturn(team);

        System.out.println("Competitors size " + team.getCompetitorList().size());
        System.out.println("TEAM NAME " + team.getTeamName());

        for (Competitor c : team.getCompetitorList()) {
            Competitor fetchedCompetitor = competitorFacade.find(c.getIdCompetitor());

            if (fetchedCompetitor.getIdTeam() != null) {
                throw new IllegalStateException("Competitor has alredy team, cannot add to another one");
            }

            fetchedCompetitor.setIdTeam(team);
            fetchedCompetitor = competitorFacade.editWithReturn(fetchedCompetitor);

            int index = team.getCompetitorList().indexOf(c);
            team.getCompetitorList().set(index, fetchedCompetitor);
//            fetchedCompetitors.add(fetchedCompetitor);
        }

    }

    @Override
    public List<Competitor> getAllTeamlessCompetitors() {
        return competitorFacade.findAllTeamless();
    }

    @Override
    public void addCompetitor(Competitor competitor, boolean global) throws ApplicationException {
        
        if (!global) {
            Account loggedUser = accountFacade.findByLogin(sessionContext.getCallerPrincipal().getName());
            AccessLevel organizer = ConvertUtil.getSpecAccessLevelFromAccount(loggedUser, Organizer.class);
            
            competitor.setIdCreator(organizer);
        }
        
        competitorFacade.competitorConstraints(competitor);
        
        competitorFacade.create(competitor);
    }

}
