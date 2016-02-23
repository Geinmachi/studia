/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mok.services;

import ejb.common.AbstractService;
import ejb.common.TrackerInterceptor;
import entities.AccessLevel;
import entities.Account;
import exceptions.ApplicationException;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import mok.managers.AuthorizedUserManagerLocal;
import mok.managers.UnathorizedUserManagerLocal;
import org.primefaces.component.message.Message;
import java.util.concurrent.Future;
import javax.annotation.security.RolesAllowed;

/**
 *
 * @author java
 */
@Stateful
@Interceptors({TrackerInterceptor.class})
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class AccountService extends AbstractService implements AccountServiceLocal {

    @EJB
    private UnathorizedUserManagerLocal unathorizedUserManager;

    @EJB
    private AuthorizedUserManagerLocal authorizedUserManager;

    private Account storedAccount;

    @Override
    public void register(Account account) throws ApplicationException {
        unathorizedUserManager.register(account);
    }

    @Override
    public List<Account> getUsersList() {
        return authorizedUserManager.getUsersList();
    }

    @Override
    public Account getAccountToEdit(int idAccount) {
        storedAccount = authorizedUserManager.getAccountToEdit(idAccount);
        return storedAccount;
    }

    @Override
    public void editAccessLevels(List<AccessLevel> accessLevelList) throws ApplicationException {
        authorizedUserManager.editAccessLevels(accessLevelList, storedAccount);
        storedAccount = null;
    }

    @Override
    public void changeActiveStatus(Account account) throws ApplicationException {
        authorizedUserManager.changeActiveStatus(account);
    }

    @Override
    public void editAccount(Account editingAccount) throws ApplicationException {
        authorizedUserManager.editAccount(editingAccount, storedAccount);
        storedAccount = null;
    }

}
