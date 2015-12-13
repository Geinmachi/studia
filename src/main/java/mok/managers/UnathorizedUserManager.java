/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mok.managers;

import ejbCommon.TrackerInterceptor;
import entities.AccessLevel;
import entities.Account;
import entities.Organizer;
import exceptions.ApplicationException;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import mok.facades.AccountFacadeLocal;
import mok.facades.OrganizerFacadeLocal;
import utils.ConvertUtil;

/**
 *
 * @author java
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors({TrackerInterceptor.class})
public class UnathorizedUserManager implements UnathorizedUserManagerLocal {

    @EJB
    private AccountFacadeLocal accountFacade;
    
    @EJB
    private OrganizerFacadeLocal organizerFacade;
    
    @Override
    public void register(Account account) throws ApplicationException {
        Account newAccount = new Account();
        
        newAccount.setIdPersonalInfo(account.getIdPersonalInfo());
        newAccount.setLogin(account.getLogin());
        newAccount.setIsActive(true);
        newAccount.setIsConfirmed(true);
        newAccount.setPassword(ConvertUtil.generateSHA512HashFromPassword(account.getPassword()));
        
        newAccount = accountFacade.createWithReturn(newAccount);
        
        Organizer organizer = new Organizer();
        organizer.setIsActive(true);
        organizer.setIdAccount(newAccount);
        
        newAccount.getAccessLevelList().add(organizer);
        
        organizerFacade.create(organizer);
    }
}
