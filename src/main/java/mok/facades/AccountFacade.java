/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mok.facades;

import entities.AccessLevel;
import entities.Account;
import exceptions.AccessLevelEditException;
import exceptions.ApplicationException;
import java.util.ArrayList;
import java.util.List;
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
@Stateless(name = "mokAccountFacade")
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AccountFacade extends AbstractFacade<Account> implements AccountFacadeLocal {

    @PersistenceContext(unitName = "mok_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AccountFacade() {
        super(Account.class);
    }

    @Override
    public Account createWithReturn(Account newAccount) throws ApplicationException {
        em.persist(newAccount);
        em.flush();
        
        return newAccount;
    }

    @Override
    public Account findAndInitializeAccessLevels(int idAccount) {
        Account entity = em.find(Account.class, idAccount);
        entity.getAccessLevelList().size();

        return entity;
    }

    @Override
    public List<AccessLevel> findAccountAccessLevelList(int idAccount) {
        Query q = em.createNamedQuery("Account.findByIdAccount");

        q.setParameter("idAccount", idAccount);
        return new ArrayList<>(((Account) q.getSingleResult()).getAccessLevelList());
    }

}
