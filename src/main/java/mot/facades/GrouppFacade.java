/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.facades;

import entities.Groupp;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author java
 */
@Stateless
public class GrouppFacade extends AbstractFacade<Groupp> implements GrouppFacadeLocal {
    @PersistenceContext(unitName = "mot_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GrouppFacade() {
        super(Groupp.class);
    }

    @Override
    public Groupp createWithReturn(Groupp entity) {
        em.persist(entity);
        em.flush();
//        em.refresh(entity);
        System.out.println("PO FLUUUSH " + entity);
        return entity;
    }
    
}