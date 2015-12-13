/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.facades;

import entities.AccessLevel;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author java
 */
@Stateless(name = "motAccessLevelFacade")
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AccessLevelFacade extends AbstractFacade<AccessLevel> implements AccessLevelFacadeLocal {
    @PersistenceContext(unitName = "mot_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AccessLevelFacade() {
        super(AccessLevel.class);
    }
    
}
