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
import exceptions.TeamCreationException;
import java.util.Comparator;
import java.util.List;
import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.Query;
import mot.facades.AccountFacadeLocal;
import mot.facades.CompetitorFacadeLocal;
import mot.facades.TeamFacadeLocal;
import mot.services.CompetitionService;
import utils.ConvertUtil;
import utils.ResourceBundleUtil;

/**
 *
 * @author java
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors({TrackerInterceptor.class})
@DeclareRoles({"Administrator","Organizer"})
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
    public void createTeam(Team team) throws ApplicationException {
        team = teamFacade.createWithReturn(team);

        System.out.println("Competitors size " + team.getCompetitorList().size());
        System.out.println("TEAM NAME " + team.getTeamName());

        if(vlidateCompetitorDuplicate(team.getCompetitorList()) != null) {
            throw new TeamCreationException("Team can't contain duplicated competitors (private and global)");
        }

        for (Competitor c : team.getCompetitorList()) {
            Competitor fetchedCompetitor = competitorFacade.find(c.getIdCompetitor());

            if (fetchedCompetitor.getIdTeam() != null) {
                throw new TeamCreationException("Competitor has alredy team, cannot add to another one");
            }

            fetchedCompetitor.setIdTeam(team);
            fetchedCompetitor = competitorFacade.editWithReturn(fetchedCompetitor);

            int index = team.getCompetitorList().indexOf(c);
            team.getCompetitorList().set(index, fetchedCompetitor);
        }

    }

    /**
     *
     * @param competitorList
     * @return Null if validation passes or duplicated competitor when failed
     */
    @Override
    public Competitor vlidateCompetitorDuplicate(List<Competitor> competitorList) {
        
        competitorList.sort(new Comparator<Competitor>() {

            @Override
            public int compare(Competitor o1, Competitor o2) {
                return (o1.getIdPersonalInfo().getFirstName() + o1.getIdPersonalInfo().getLastName()).compareTo(
                        o2.getIdPersonalInfo().getFirstName() + o2.getIdPersonalInfo().getLastName());
            }

        });

        for (int i = 1; i < competitorList.size(); i++) {
            if ((competitorList.get(i - 1).getIdPersonalInfo().getFirstName() + competitorList.get(i - 1).getIdPersonalInfo().getLastName()).compareTo(
                    (competitorList.get(i).getIdPersonalInfo().getFirstName() + competitorList.get(i).getIdPersonalInfo().getLastName())) == 0) {
                System.out.println("THE SAME ");
                System.out.println(competitorList.get(i - 1).getIdPersonalInfo().getFirstName() + competitorList.get(i - 1).getIdPersonalInfo().getLastName());
                System.out.println(competitorList.get(i).getIdPersonalInfo().getFirstName() + competitorList.get(i).getIdPersonalInfo().getLastName());
                return competitorList.get(i);
            }
        }
        
        return null;
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

    @Override
    public List<Competitor> getCompetitionsToEdit() {
        List<Competitor> competitorList = null;
        
        if (sessionContext.isCallerInRole(ResourceBundleUtil.getResourceBundleBusinessProperty(CompetitionService.ADMIN_PROPERTY_KEY))) {
            competitorList = competitorFacade.findAll();
        } else {
            Account loggedUser = accountFacade.findByLogin(sessionContext.getCallerPrincipal().getName());
            AccessLevel organizer = ConvertUtil.getSpecAccessLevelFromAccount(loggedUser, Organizer.class);
            
            competitorList = competitorFacade.findUserCompetitors(organizer);
        }
        
        return competitorList;
    }

    @Override
    public Competitor findCompetitorById(int idCompetitor) {
        return competitorFacade.findCompetitorById(idCompetitor);
    }

    @Override
    public void editCompetitor(Competitor editingCompetitor, Competitor competitor) {
        if (editingCompetitor == null || competitor == null) {
            throw new IllegalStateException("There is no object to edit");
        }
        if (!editingCompetitor.getIdCompetitor().equals(competitor.getIdCompetitor())) {
            throw new IllegalStateException("Wrong object");
        }
        
        editingCompetitor.getIdPersonalInfo().setFirstName(competitor.getIdPersonalInfo().getFirstName());
        editingCompetitor.getIdPersonalInfo().setLastName(competitor.getIdPersonalInfo().getLastName());
        editingCompetitor.setIdTeam(competitor.getIdTeam());
        
        competitorFacade.edit(editingCompetitor);
    }

}
