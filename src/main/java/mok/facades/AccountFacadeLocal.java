/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mok.facades;

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
public interface AccountFacadeLocal {

    void create(Account account);

    void edit(Account account) throws ApplicationException;

    void remove(Account account);

    Account find(Object id);

    List<Account> findAll();

    List<Account> findRange(int[] range);

    int count();

    public Account createWithReturn(Account newAccount) throws ApplicationException;

    public Account findAndInitializeAccessLevels(int idAccount);

    public List<AccessLevel> findAccountAccessLevelList(int idAccount);
    
}
