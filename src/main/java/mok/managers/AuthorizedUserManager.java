/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mok.managers;

import ejbCommon.TrackerInterceptor;
import entities.AccessLevel;
import entities.Account;
import entities.Administrator;
import exceptions.AccessLevelEditException;
import exceptions.ApplicationException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import mok.facades.AccessLevelFacadeLocal;
import mok.facades.AccountFacadeLocal;

/**
 *
 * @author java
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AuthorizedUserManager implements AuthorizedUserManagerLocal {

    @EJB
    private AccountFacadeLocal accountFacade;

    @EJB
    private AccessLevelFacadeLocal accessLevelFacade;

//    @EJB
//    private AdministratorFacadeLocal administratorFacade;
    @Override
    public List<Account> getUserList() {
        return accountFacade.findAll();
    }

    @Override
    public Account getAccountToEdit(int idAccount) {
        return accountFacade.findAndInitializeAccessLevels(idAccount);
    }

    @Override
    public void editAccessLevels(List<AccessLevel> receivedAccessLevelList, Account storedAccount) throws ApplicationException {
        if (receivedAccessLevelList == null) {
            throw AccessLevelEditException.nullReceivedList();
        }

        List<AccessLevel> storedAccessLevelList = null;
        if (storedAccount == null || (storedAccessLevelList = storedAccount.getAccessLevelList()) == null) {
            throw AccessLevelEditException.nullStoredObject();
        }
        checkReceivedAccessLevelList(receivedAccessLevelList, storedAccessLevelList);
//        List<AccessLevel> fetchedAccessLevelList = accountFacade.findAccountAccessLevelList(storedAccount.getIdAccount());

        for (AccessLevel ral : receivedAccessLevelList) {
            boolean exists = false;
            for (AccessLevel sal : storedAccessLevelList) {
                if (ral.getClass().isAssignableFrom(sal.getClass())) {
                    System.out.println("SAL przed " + sal.getIsActive());
                    sal.setIsActive(ral.getIsActive());
                    System.out.println("SAL po " + sal.getIsActive());
                    exists = true;
                    System.out.println("Przed edycja, id " + sal.getIdAccount() + " wersja " + sal.getVersion());
                    accessLevelFacade.edit(sal);
                    
                    break;
                }
            }
            if (!exists) {
                System.out.println("Chce utworzyc " + ral.getDiscriminatorValue() + " " + ral.getIdAccount());
                storedAccessLevelList.add(createNewAccessLevel(ral, storedAccount));
            }
        }
    }

    private AccessLevel createNewAccessLevel(AccessLevel accessLevel, Account account) throws ApplicationException {
        AccessLevel newAccessLevel = null;
        try {
            Object accessLevelObject = accessLevel.getClass().getConstructor().newInstance();
            newAccessLevel = accessLevel.getClass().cast(accessLevelObject);
            newAccessLevel.setIsActive(accessLevel.getIsActive());
            newAccessLevel.setIdAccount(account);
            
            if (newAccessLevel instanceof Administrator) {
                ((Administrator) newAccessLevel).setAssignedDate(new Date());
            }
           
            return accessLevelFacade.createWithReturn(newAccessLevel);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            Logger.getLogger(AuthorizedUserManager.class.getName()).log(Level.SEVERE, "nawalila refleksja", ex);
        }

        return null;
    }

    private void checkReceivedAccessLevelList(List<AccessLevel> receivedAccessLevelList, List<AccessLevel> storedAccessLevelList) throws AccessLevelEditException {
        for (AccessLevel sal : storedAccessLevelList) {
            boolean exists = false;
            for (AccessLevel ral : receivedAccessLevelList) {
                if (sal.equals(ral)) {
                    exists = true;

                    break;
                }
            }

            if (!exists) {
                throw AccessLevelEditException.missingAccessLevel(sal);
            }
        }
    }
}