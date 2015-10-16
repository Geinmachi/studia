/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.facades;

import entities.AccessLevel;
import entities.Competitor;
import entities.Team;
import exceptions.ApplicationException;
import exceptions.TeamCreationException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
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
    public Team createWithReturn(Team team) throws ApplicationException {
        try {
            em.persist(team);
            em.flush();

            team.setCompetitorList(new ArrayList<>(team.getCompetitorList()));
        } catch (PersistenceException e) {
            if (e.getMessage().contains("team_uniq_team_name")) {
                throw new TeamCreationException("Team with this name already exists");
            }
        } catch (Exception e) {
            System.out.println("INNy wyjatek " + e.getMessage());
            System.out.println("KLASA " + e.getClass());
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
        } catch (PersistenceException e) {
            if (e.getMessage().contains("team_uniq_team_name")) {
                throw new TeamCreationException("Team with this name already exists");
            }
        } catch (Exception e) {
            System.out.println("INNy wyjatek " + e.getMessage());
            System.out.println("KLASA " + e.getClass());
        }

    }

}
