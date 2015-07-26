/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.facades;

import entities.CompetitorMatchGroup;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author java
 */
@Stateless
public class CompetitorMatchGroupFacade extends AbstractFacade<CompetitorMatchGroup> implements CompetitorMatchGroupFacadeLocal {
    @PersistenceContext(unitName = "mot_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CompetitorMatchGroupFacade() {
        super(CompetitorMatchGroup.class);
    }

    @Override
    public CompetitorMatchGroup createWithReturn(CompetitorMatchGroup entity) {
        em.persist(entity);
        em.flush();
        return entity;
    }
    
}
