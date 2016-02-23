/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mok.facades;

import entities.AccessLevel;
import entities.Account;
import exceptions.AccountEditException;
import exceptions.ApplicationException;
import exceptions.LoginAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

/**
 *
 * @author java
 */
@Stateless(name = "mokAccountFacade")
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AccountFacade extends AbstractFacade<Account> implements AccountFacadeLocal {
    
    private static final String UNIQUE_LOGIN_CONSTRAINT_NAME = "account_uniq_account_login";

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
        try {
            em.persist(newAccount);
            em.flush();
        } catch (PersistenceException e) {
            if (e.getMessage().contains(UNIQUE_LOGIN_CONSTRAINT_NAME)) {
                throw LoginAlreadyExistsException.loginAlreadyExists(newAccount.getLogin(), e);
            }
            
            throw e;
        }
        
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

    @Override
    public void edit(Account entity) throws ApplicationException {
        try {
            em.merge(entity);
            System.out.println("W fasadzie dane imie:" + entity.getIdPersonalInfo().getFirstName() + " nazwisko " + entity.getIdPersonalInfo().getLastName());
            em.flush();
        } catch (OptimisticLockException e) {
            throw AccountEditException.optimisticLock(null, entity);
        }
    }

}
