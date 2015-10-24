/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.facades;

import entities.Account;
import exceptions.AccountException;
import exceptions.ApplicationException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

/**
 *
 * @author java
 */
@Stateless
public class AccountFacade extends AbstractFacade<Account> implements AccountFacadeLocal {

    @PersistenceContext(unitName = "mot_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AccountFacade() {
        super(Account.class);
    }

    @Override
    public Account findByLogin(String login) throws ApplicationException {
        Query q = em.createNamedQuery("Account.findByLogin");
        q.setParameter("login", login);

        Account entity = null;
        
        try {
            entity = (Account) q.getSingleResult();
            entity.getAccessLevelList().size();

            em.flush();
        } catch (NoResultException e) {
            throw new AccountException("User with given name does not exist", e.getCause());
        } catch (PersistenceException e) {
            throw e;
        }

        return entity;
    }

}
