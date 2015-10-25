/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.facades;

import entities.AccessLevel;
import entities.Competitor;
import exceptions.ApplicationException;
import exceptions.CompetitorCreationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author java
 */
@Stateless
public class CompetitorFacade extends AbstractFacade<Competitor> implements CompetitorFacadeLocal {

    @PersistenceContext(unitName = "mot_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CompetitorFacade() {
        super(Competitor.class);
    }

    @Override
    public Competitor findAndInitializeGroups(Integer idCompetitor) {
        Competitor entity = em.find(Competitor.class, idCompetitor);
        entity.getGroupCompetitorList().size();
        
        return entity;
    }

    @Override
    public Competitor editWithReturn(Competitor competitor) {
        Competitor entity = em.merge(competitor);
        em.flush();

        return entity;
    }

    @Override
    public List<Competitor> findAllTeamless() {
        Query q = em.createNamedQuery("Competitor.findAllTeamless");
        
        List<Competitor> sortedCompetitorList = new ArrayList<>((List<Competitor>) q.getResultList());
        Collections.sort(sortedCompetitorList);
        
        return sortedCompetitorList;
    }

    @Override
    public void competitorConstraints(Competitor competitor) throws ApplicationException {
        Query q = null;

        AccessLevel creator = competitor.getIdCreator();

        if (creator == null) {
            q = em.createNamedQuery("Competitor.findByFirstnameLastnameGlobal");
        } else {
            q = em.createNamedQuery("Competitor.findByFirstnameLastnameCreator");
            q.setParameter("idCreator", creator.getIdAccessLevel());
        }

        q.setParameter("firstName", competitor.getIdPersonalInfo().getFirstName());
        q.setParameter("lastName", competitor.getIdPersonalInfo().getLastName());

        try {
            Competitor c = (Competitor) q.getSingleResult();
            em.flush();
        } catch (NoResultException e) {
            System.out.println("No existing competitor found with given criteria, entitled to create");
            return;
        } catch (NonUniqueResultException e) {
            if (creator == null) {
                throw new CompetitorCreationException("Global competitor with given first and last name already exists");
            } else {
                throw new CompetitorCreationException("You have already created private comptetitor with given first and last name");
            }
        }
    }

    @Override
    public List<Competitor> findUserCompetitors(AccessLevel accessLevel) {
        Query q = em.createNamedQuery("Competitor.findUserCompetitors");
        q.setParameter("idAccessLevel", accessLevel.getIdAccessLevel());

        return (List<Competitor>) q.getResultList();
    }

    @Override
    public Competitor findCompetitorById(int idCompetitor) {
        Query q = em.createNamedQuery("Competitor.findByIdCompetitor");

        q.setParameter("idCompetitor", idCompetitor);

        return (Competitor) q.getSingleResult();
    }

    @Override
    public void create(Competitor entity) throws ApplicationException {
        em.persist(entity);
        em.flush();
        
        competitorConstraints(entity);
    }

    @Override
    public void edit(Competitor entity) throws ApplicationException {
        em.merge(entity);
        em.flush();

        competitorConstraints(entity);
    }

    @Override
    public List<Competitor> findAllAllowedTeamless(int idAccessLevel) {
        Query q = em.createNamedQuery("Competitor.findAllAllowedTeamless");
        q.setParameter("idAccessLevel", idAccessLevel);
        
        List<Competitor> sortedList = new ArrayList<>(q.getResultList());
        Collections.sort(sortedList);
        
        return sortedList;
    }

}
