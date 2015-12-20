/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mok.managers;

import entities.Account;
import exceptions.ApplicationException;
import javax.ejb.Remote;

/**
 *
 * @author java
 */
@Remote
public interface UnathorizedUserManagerLocal {

    public void register(Account account) throws ApplicationException;
    
}
