/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mok.services;

import ejbCommon.TrackerInterceptor;
import entities.AccessLevel;
import entities.Account;
import exceptions.ApplicationException;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import mok.managers.AuthorizedUserManagerLocal;
import mok.managers.UnathorizedUserManagerLocal;

/**
 *
 * @author java
 */
@Stateful
@Interceptors({TrackerInterceptor.class})
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class AccountService implements AccountServiceLocal {

    @EJB
    private UnathorizedUserManagerLocal unathorizedUserManager;
    
    @EJB
    private AuthorizedUserManagerLocal authorizedUserManager;
    
    private Account editingAccount;

    @Override
    public void register(Account account) throws ApplicationException {
        unathorizedUserManager.register(account);
    }

    @Override
    public List<Account> getUserList() {
        return authorizedUserManager.getUserList();
    }

    @Override
    public Account getAccountToEdit(int idAccount) {
        editingAccount = authorizedUserManager.getAccountToEdit(idAccount);
        return editingAccount;
    }

    @Override
    public void editAccessLevels(List<AccessLevel> accessLevelList) throws ApplicationException {
        authorizedUserManager.editAccessLevels(accessLevelList, editingAccount);
        editingAccount = null;
    }
    
}
