/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.managers;

import ejbCommon.TrackerInterceptor;
import entities.Competitor;
import entities.Team;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import mot.facades.CompetitorFacadeLocal;
import mot.facades.TeamFacadeLocal;

/**
 *
 * @author java
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors({TrackerInterceptor.class})
public class CompetitionComponentsManager implements CompetitionComponentsManagerLocal {
    
    @EJB
    private TeamFacadeLocal teamFacade;
    
    @EJB
    private CompetitorFacadeLocal competitorFacade;
    
    @Override
    public void createTeam(Team team) {
        List<Competitor> fetchedCompetitors = new ArrayList<>();
        
        teamFacade.create(team);
        
        for (Competitor c : team.getCompetitorList()) {
            Competitor fetchedCompetitor = competitorFacade.find(c.getIdCompetitor());
            fetchedCompetitor.setIdTeam(team);
            fetchedCompetitor = competitorFacade.editWithReturn(fetchedCompetitor);
            
            int index = team.getCompetitorList().indexOf(c);
            team.getCompetitorList().set(index, fetchedCompetitor);
//            fetchedCompetitors.add(fetchedCompetitor);
        }
        
    }
    
}
