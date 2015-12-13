/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans.mok;

import entities.Account;
import entities.PersonalInfo;
import exceptions.ApplicationException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import web.backingBeans.mot.competition.CreateCompetitionBackingBean;
import web.utils.JsfUtils;
import web.utils.PageConstants;

/**
 *
 * @author java
 */
@Named(value = "registerBackingBean")
@RequestScoped
public class RegisterBackingBean extends AccountBackingBean {
    
    private Account account;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    /**
     * Creates a new instance of RegisterBackingBean
     */
    public RegisterBackingBean() {
    }
    
    @PostConstruct
    private void init() {
        account = new Account();
        PersonalInfo personalInfo = new PersonalInfo();
        account.setIdPersonalInfo(personalInfo);
    }
    
    public String register() {
        try {
            controller.register(account);
            
            return JsfUtils.successPageRedirect(PageConstants.ROOT_INDEX);
        } catch (ApplicationException e) {
            JsfUtils.addErrorMessage(e, null);
            Logger.getLogger(RegisterBackingBean.class.getName()).log(Level.SEVERE, null, e);
        } catch (Exception e) {
            System.out.println("Wyjatek przy rejestracji");
            Logger.getLogger(RegisterBackingBean.class.getName()).log(Level.SEVERE, null, e);
        }
        return "";
    }
}
