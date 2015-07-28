/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.managers;

import entities.AccessLevel;
import entities.Account;
import entities.Competition;
import entities.CompetitorMatchGroup;
import entities.Organizer;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import mot.facades.AccountFacadeLocal;
import mot.facades.CompetitionFacadeLocal;
import mot.facades.CompetitorMatchGroupFacadeLocal;
import utils.ConvertUtil;

/**
 *
 * @author java
 */
@Stateless
public class ManageCompetitionManager implements ManageCompetitionManagerLocal {

    @Resource
    private SessionContext sessionContext;

    @EJB
    private AccountFacadeLocal accountFacade;

    @EJB
    private CompetitionFacadeLocal competitionFacade;
    
    @EJB
    private CompetitorMatchGroupFacadeLocal cmgFacade;

    @Override
    public List<Competition> getLoggedUserCompetition() {
        Account loggedUser = accountFacade.findByLogin(sessionContext.getCallerPrincipal().getName());
        AccessLevel organizer = ConvertUtil.getSpecAccessLevelFromAccount(loggedUser, Organizer.class);

        return competitionFacade.findUserCompetitionsByIdAccessLevel(organizer.getIdAccessLevel());
    }

    @Override
    public Competition storeCompetition(Competition competition) {
        Competition fetchedCompetition = competitionFacade.findAndInitializeGCLists(competition.getIdCompetition());
        
        if (!fetchedCompetition.equals(competition)) { // after page laod and before button click competition was modified
            throw new IllegalStateException("In the meantime competition was modyfied");
        }
        
        return fetchedCompetition;
    }

    @Override
    public List<CompetitorMatchGroup> getCompetitionCMGMappings(Competition competition) {
        return cmgFacade.getCompetitionCMGMappingsByCompetitionId(competition.getIdCompetition());
    }

}
