/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans.mok;

import entities.Account;
import exceptions.ApplicationException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import utils.ResourceBundleUtil;
import web.utils.JsfUtils;
import web.utils.PageConstants;

/**
 *
 * @author java
 */
@Named(value = "usersListBackingBean")
@ViewScoped
public class UsersListBackingBean extends AccountBackingBean implements Serializable {

    private List<Account> accountList;

    public List<Account> getAccountList() {
        return accountList;
    }
    
    /**
     * Creates a new instance of UsersListBackingBean
     */
    public UsersListBackingBean() {
    }
    
    @PostConstruct
    private void init() {
        accountList = new ArrayList<>(controller.getUserList());
        Collections.sort(accountList);
        
    }
    
    public String changeAccessLevel(Account account) {
        controller.storeEditingAccount(account);
        return PageConstants.getPage(PageConstants.ADMIN_EDIT_ACCESS_LEVEL, true);
    }
    
    public String edit(Account account) {
        controller.storeEditingAccount(account);
        return PageConstants.getPage(PageConstants.ADMIN_EDIT_USER, true);
    }
    
    public void changeActiveStatus(Account account) {
        System.out.println("changeActive statucs " + account.getIdPersonalInfo().getLastName() + " stsaust " + account.getIsActive());
        try {
            controller.changeActiveStatus(account);
            String msgSummary = account.getIsActive() ? ResourceBundleUtil.getResourceBundleProperty("userActivated") : ResourceBundleUtil.getResourceBundleProperty("userDeactivated");
            JsfUtils.addSuccessMessage(msgSummary,
                    account.getIdPersonalInfo().getFirstName() + " " + account.getIdPersonalInfo().getLastName(),
                    "usersListForm");
        } catch (ApplicationException ex) {
            logger.log(Level.SEVERE, "exception on changing user active status {0}", ex.getMessage());
        }
    }
}
