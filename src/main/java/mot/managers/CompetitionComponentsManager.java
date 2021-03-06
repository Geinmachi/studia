/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.managers;

import ejb.common.TrackerInterceptor;
import entities.AccessLevel;
import entities.Account;
import entities.Competitor;
import entities.Organizer;
import entities.Team;
import exceptions.ApplicationException;
import exceptions.TeamCreationException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    public void createTeam(Team team, boolean global) throws ApplicationException {

        if (!global) {
            Account loggedUser = accountFacade.findByLogin(sessionContext.getCallerPrincipal().getName());
            AccessLevel organizer = ConvertUtil.getSpecAccessLevelFromAccount(loggedUser, Organizer.class);

            team.setIdCreator(organizer);
        }

        team = teamFacade.createWithReturn(team);

        System.out.println("Competitors size " + team.getCompetitorList().size());
        System.out.println("TEAM NAME " + team.getTeamName());

        if (validateCompetitorDuplicate(team.getCompetitorList()) != null) {
            throw TeamCreationException.duplicatedCompetitors();
        }

        for (Competitor c : team.getCompetitorList()) {
            Competitor fetchedCompetitor = competitorFacade.find(c.getIdCompetitor());

            if (fetchedCompetitor.getIdTeam() != null) {
                throw TeamCreationException.teamfulCompetitor();
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
    public Competitor validateCompetitorDuplicate(List<Competitor> competitorList) {
        for (Object c : competitorList) {
            System.out.println("validateCompetitorDuplicate " + c + " class" + c.getClass());
        }
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
    public List<Competitor> getAllAllowedTeamlessCompetitors() throws ApplicationException {
        if (sessionContext.isCallerInRole(ResourceBundleUtil.getResourceBundleBusinessProperty(CompetitionService.ADMIN_PROPERTY_KEY))) {
            return competitorFacade.findAllTeamless();
        } else {
            Account loggedUser = accountFacade.findByLogin(sessionContext.getCallerPrincipal().getName());
            AccessLevel organizer = ConvertUtil.getSpecAccessLevelFromAccount(loggedUser, Organizer.class);

            return competitorFacade.findAllAllowedTeamless(organizer.getIdAccessLevel());
        }
    }

    @Override
    public void addCompetitor(Competitor competitor, boolean global) throws ApplicationException {

        if (!global) {
            Account loggedUser = accountFacade.findByLogin(sessionContext.getCallerPrincipal().getName());
            AccessLevel organizer = ConvertUtil.getSpecAccessLevelFromAccount(loggedUser, Organizer.class);

            competitor.setIdCreator(organizer);
        }

        competitorFacade.create(competitor);
    }

    @Override
    public List<Competitor> getCompetitorsToEdit() throws ApplicationException {
        List<Competitor> competitorList = new ArrayList<>();

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
    public void editCompetitor(Competitor editingCompetitor, Competitor competitor) throws ApplicationException {
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

    @Override
    public List<Team> getTeamsToEdit() throws ApplicationException {

        List<Team> teamList = null;

        if (sessionContext.isCallerInRole(ResourceBundleUtil.getResourceBundleBusinessProperty(CompetitionService.ADMIN_PROPERTY_KEY))) {
            teamList = teamFacade.findAll();
        } else {
            Account loggedUser = accountFacade.findByLogin(sessionContext.getCallerPrincipal().getName());
            AccessLevel organizer = ConvertUtil.getSpecAccessLevelFromAccount(loggedUser, Organizer.class);

            teamList = teamFacade.findUserTeams(organizer);
        }

        return teamList;
    }

    @Override
    public Team findTeamById(Integer idTeam) {
        return teamFacade.findAndInitializeCompetitors(idTeam);
    }

    @Override
    public void editTeam(Team editingTeam, Team team) throws ApplicationException {
        if (editingTeam == null || team == null) {
            throw new IllegalStateException("There is no object to edit");
        }
        if (!editingTeam.getIdTeam().equals(team.getIdTeam())) {
            throw new IllegalStateException("Wrong object");
        }
        // jsete final, sprwadzic z new

        editingTeam.setTeamName(team.getTeamName());
        editingTeam.setCompetitorList(team.getCompetitorList());

        validateCompetitorDuplicate(editingTeam.getCompetitorList());

        for (Competitor c : editingTeam.getCompetitorList()) {
            c.setIdTeam(editingTeam);
            competitorFacade.edit(c);
        }

        teamFacade.edit(editingTeam);
    }

    @Override
    public List<Team> findUserAllowedTeams() throws ApplicationException {
        if (sessionContext.isCallerInRole(ResourceBundleUtil.getResourceBundleBusinessProperty(CompetitionService.ADMIN_PROPERTY_KEY))) {
            return teamFacade.findAll();
        } else {
            Account loggedUser = accountFacade.findByLogin(sessionContext.getCallerPrincipal().getName());
            AccessLevel organizer = ConvertUtil.getSpecAccessLevelFromAccount(loggedUser, Organizer.class);

            return teamFacade.findAllAllowed(organizer.getIdAccessLevel());
        }
    }

}
