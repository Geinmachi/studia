/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.facades;

import entities.Matchh;
import exceptions.ApplicationException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author java
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class MatchhFacade extends AbstractFacade<Matchh> implements MatchhFacadeLocal {
    @PersistenceContext(unitName = "mot_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MatchhFacade() {
        super(Matchh.class);
    }
    
    @Override
    public Matchh createWithReturn(Matchh entity) {
        em.persist(entity);
        em.flush();
        return entity;
    }

    @Override
    public Matchh findAndInitializeTypes(Object id) {
        Matchh match = em.find(Matchh.class, id);
        match.getMatchMatchTypeList().size();
        match.getCompetition().getGroupDetailsList().size();
        return match;
    }

    @Override
    public Matchh findByMatchNumberAndIdCompetition(short matchNumber, int idCompetition) {
        Query q = em.createNamedQuery("Matchh.findByMatchNumberAndIdCompetition");
        q.setParameter("matchNumber", matchNumber);
        q.setParameter("idCompetition", idCompetition);
        return (Matchh) q.getSingleResult();
    }
}
