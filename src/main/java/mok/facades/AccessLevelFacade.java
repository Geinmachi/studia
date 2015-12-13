/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mok.facades;

import entities.AccessLevel;
import exceptions.AccessLevelEditException;
import exceptions.ApplicationException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author java
 */
@Stateless(name = "mokAccessLevelFacade")
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AccessLevelFacade extends AbstractFacade<AccessLevel> implements AccessLevelFacadeLocal {

    @PersistenceContext(unitName = "mok_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AccessLevelFacade() {
        super(AccessLevel.class);
    }

    @Override
    public AccessLevel createWithReturn(AccessLevel accessLevel) throws ApplicationException {
        try {
            em.persist(accessLevel);
            em.flush();
        } catch (Throwable e) {
            throw AccessLevelEditException.unknown5001();
        }
        return accessLevel;
    }

    @Override
    public void edit(AccessLevel entity) throws ApplicationException {
        try {
            super.edit(entity);
            em.flush();
        } catch (OptimisticLockException e) {
            throw AccessLevelEditException.optimisticLock();
        }
    }

}
