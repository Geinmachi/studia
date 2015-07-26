/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.facades;

import entities.Matchh;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author java
 */
@Stateless
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
    public void create(Matchh entity) {
        em.persist(entity);
        em.flush();
         
    }
    
    @Override
    public Matchh createWithReturn(Matchh entity) {
        em.persist(entity);
        em.flush();
        return entity;
    }
}
