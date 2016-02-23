/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans.mok;

import entities.Account;
import exceptions.ApplicationException;
import javax.inject.Named;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import web.utils.JsfUtils;
import web.utils.PageConstants;

/**
 *
 * @author java
 */
@Named(value = "editUserBackingBean")
@ViewScoped
public class EditUserBackingBean extends AccountBackingBean implements Serializable {

    private Account account;

    public Account getAccount() {
        return account;
    }

    /**
     * Creates a new instance of EditUserBackingBean
     */
    public EditUserBackingBean() {
    }

    @PostConstruct
    private void init() {
        account = controller.getEditingAccount();
    }

    public String saveAccount() {
        try {
            controller.editAccount();
            return JsfUtils.successPageRedirect(PageConstants.ADMIN_USERS_LIST);
        } catch (ApplicationException e) {
            JsfUtils.addErrorMessage(e, "editUserForm");
            controller.storeEditingAccount(account);
        }
        
        return null;
    }
}
