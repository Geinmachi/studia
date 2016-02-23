/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mok.managers;

import entities.AccessLevel;
import entities.Account;
import exceptions.ApplicationException;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author java
 */
@Remote
public interface AuthorizedUserManagerLocal {

    public List<Account> getUsersList();

    public Account getAccountToEdit(int idAccount);

    public void editAccessLevels(List<AccessLevel> receivedAccessLevelList, Account storedAccount) throws ApplicationException;

    public void changeActiveStatus(Account account) throws ApplicationException;

    public void editAccount(Account editingAccount, Account storedAccount) throws ApplicationException;
    
}
