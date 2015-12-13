/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans.mok;

import entities.Account;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
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
        accountList = controller.getUserList();
    }
    
    public String changeAccessLevel(Account account) {
        controller.storeEditingAccount(account);
        return PageConstants.getPage(PageConstants.ADMIN_EDIT_ACCESS_LEVEL, true);
    }
}
