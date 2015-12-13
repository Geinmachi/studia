/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.facades;

import entities.GroupName;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author java
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class GroupNameFacade extends AbstractFacade<GroupName> implements GroupNameFacadeLocal {
    @PersistenceContext(unitName = "mot_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GroupNameFacade() {
        super(GroupName.class);
    }

    @Override
    public GroupName createWithReturn(GroupName entity) {
        em.persist(entity);
        em.flush();
        
        return entity;
    }
    
}
