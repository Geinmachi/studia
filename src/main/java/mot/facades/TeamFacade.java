/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.facades;

import entities.AccessLevel;
import entities.Competition;
import entities.Competitor;
import entities.Team;
import exceptions.ApplicationException;
import exceptions.CompetitorCreationException;
import exceptions.TeamCreationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

/**
 *
 * @author java
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class TeamFacade extends AbstractFacade<Team> implements TeamFacadeLocal {

    @PersistenceContext(unitName = "mot_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TeamFacade() {
        super(Team.class);
    }

    @Override
    public void teamContraints(Team team) throws TeamCreationException {
        Query q = null;

        AccessLevel creator = team.getIdCreator();

        if (creator == null) {
            q = em.createNamedQuery("Team.findByTeamNameGlobal");
        } else {
            q = em.createNamedQuery("Team.findByTeamNameOrganizer");
            q.setParameter("idCreator", creator.getIdAccessLevel());
        }

        q.setParameter("teamName", team.getTeamName());

        try {
            Team t = (Team) q.getSingleResult();
            em.flush();
        } catch (NoResultException e) {
            System.out.println("No existing team found with given criteria, entitled to create");
        } catch (NonUniqueResultException e) {
            if (creator == null) {
                throw new TeamCreationException("Global team with given name already exists");
            } else {
                throw new TeamCreationException("You have already created private team with given name");
            }

        }
    }

    @Override
    public Team createWithReturn(Team team) throws ApplicationException {
        try {
            em.persist(team);
            em.flush();
            
            teamContraints(team);
            team.setCompetitorList(new ArrayList<>(team.getCompetitorList()));
        } catch (PersistenceException e) {

            throw e;
//            if (e.getMessage().contains("team_uniq_team_name")) {
//                throw new TeamCreationException("Team with this name already exists");
//            }
        }

        return team;
    }

    @Override
    public List<Team> findUserTeams(AccessLevel accessLevel) {
        Query q = em.createNamedQuery("Team.findUserTeams");
        q.setParameter("idAccessLevel", accessLevel.getIdAccessLevel());

        return (List<Team>) q.getResultList();
    }

    @Override
    public Team findAndInitializeCompetitors(Integer idTeam) {
        Team team = em.find(Team.class, idTeam);
        team.getCompetitorList().size();

        em.flush();

        return team;
    }

    @Override
    public void edit(Team entity) throws ApplicationException {
        try {
            em.merge(entity);
            em.flush();

            teamContraints(entity);
        } catch (PersistenceException e) {
            throw e;
//            if (e.getMessage().contains("team_uniq_team_name")) {
//                throw new TeamCreationException("Team with this name already exists");
//            }
        }

    }

    @Override
    public List<Team> findAll() {
        List<Team> sortedTeamList =  super.findAll();
        Collections.sort(sortedTeamList);
        
        return sortedTeamList;
    }

    @Override
    public List<Team> findAllAllowed(int idAccessLevel) {
        Query q = em.createNamedQuery("Team.findAllAllowed");
        q.setParameter("idAccessLevel", idAccessLevel);
        
        List<Team> sortedTeamList = new ArrayList<>(q.getResultList());
        Collections.sort(sortedTeamList);
        
        return sortedTeamList;
    }

}
