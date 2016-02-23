/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.controllers;

import entities.AccessLevel;
import entities.Account;
import exceptions.ApplicationException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.Future;
import javax.ejb.EJB;
import mok.services.AccountServiceLocal;

/**
 *
 * @author java
 */
@Named(value = "accountController")
@SessionScoped
public class AccountController implements Serializable {

    @EJB
    private AccountServiceLocal service;
    
    private Account editingAccount;
    
    public AccountController() {
    }

    public Account getEditingAccount() {
        return editingAccount;
    }

    public Account storeEditingAccount(Account account) {
        editingAccount = service.getAccountToEdit(account.getIdAccount());
        return editingAccount;
    }
    
    public void register(Account account) throws ApplicationException {
        service.register(account);
    }

    public List<Account> getUserList() {
        return service.getUsersList();
    }

    public void editAccessLevels(List<AccessLevel> accessLevelList) throws ApplicationException {
        service.editAccessLevels(accessLevelList);
    }

    public void changeActiveStatus(Account account) throws ApplicationException {
        service.changeActiveStatus(account);
    }

    public void editAccount() throws ApplicationException {
        service.editAccount(editingAccount);
        editingAccount = null;
    }
    
}
