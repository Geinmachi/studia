/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans.mok;

import entities.Account;
import entities.PersonalInfo;
import exceptions.ApplicationException;
import exceptions.LoginAlreadyExistsException;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import web.utils.JsfUtils;
import web.utils.PageConstants;

/**
 *
 * @author java
 */
@Named(value = "registerBackingBean")
@RequestScoped
public class RegisterBackingBean extends AccountBackingBean {
    
    private static final String FORM_ID = "registerForm";
    
    private static final String LOGIN_FIELD_ID = FORM_ID + ":login";

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
//        
//        CopyWriter c = (CopyWriter)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("copyWriter");
//        System.out.println("zawartosc: " + c.getCopy());
    }

    public String register() {
        try {
            controller.register(account);

            return JsfUtils.successPageRedirect(PageConstants.ROOT_INDEX);
        } catch (LoginAlreadyExistsException e) {
            JsfUtils.addErrorMessage(e, LOGIN_FIELD_ID);
            logger.severe(e.getMessage());
            
            ((UIInput)FacesContext.getCurrentInstance().getViewRoot().findComponent(LOGIN_FIELD_ID)).setValid(false);
        } catch (ApplicationException e) {
            JsfUtils.addErrorMessage(e, null);
            logger.severe(e.getMessage());
        } catch (Exception e) {
            System.out.println("Wyjatek przy rejestracji");
            logger.severe(e.getMessage());
        }
        return "";
    }
}
