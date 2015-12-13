/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mok.facades;

import entities.AccessLevel;
import entities.Administrator;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author java
 */
@Stateless(name = "mokAdministratorFacade")
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AdministratorFacade extends AbstractFacade<Administrator> implements AdministratorFacadeLocal {
    @PersistenceContext(unitName = "mok_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AdministratorFacade() {
        super(Administrator.class);
    }

    @Override
    public AccessLevel createWithReturn(Administrator administrator) {
        em.persist(administrator);
        em.flush();
        
        return administrator;
    }
    
}
