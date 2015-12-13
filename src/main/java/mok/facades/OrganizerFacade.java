/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mok.facades;

import entities.Organizer;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author java
 */
@Stateless(name = "mokOrganizerFacade")
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class OrganizerFacade extends AbstractFacade<Organizer> implements OrganizerFacadeLocal {
    @PersistenceContext(unitName = "mok_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OrganizerFacade() {
        super(Organizer.class);
    }
    
}
