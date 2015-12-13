/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mok.facades;

import entities.PersonalInfo;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author java
 */
@Stateless(name = "mokPersonalInfoFacade")
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class PersonalInfoFacade extends AbstractFacade<PersonalInfo> implements PersonalInfoFacadeLocal {
    @PersistenceContext(unitName = "mok_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PersonalInfoFacade() {
        super(PersonalInfo.class);
    }
    
}
