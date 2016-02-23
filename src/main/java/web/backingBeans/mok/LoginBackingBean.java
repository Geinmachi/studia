/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans.mok;

import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import utils.Hashids;
import utils.ResourceBundleUtil;
import web.utils.JsfUtils;
import web.utils.PageConstants;

/**
 *
 * @author java
 */
@Named(value = "loginBackingBean")
@RequestScoped
public class LoginBackingBean {

    private static final String GLOBAL_CONTAINER_ID = "globalContainer";

    private String username;

    private String password;

    public LoginBackingBean() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        try {
            request.login(username, password);
            String view = context.getViewRoot().getViewId();

            return view + "?faces-redirect=true";
        } catch (ServletException ex) {
            Logger.getLogger(LoginBackingBean.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtils.addErrorMessage(ResourceBundleUtil.getResourceBundleProperty("loginFailed"),
                    ResourceBundleUtil.getResourceBundleProperty("loginFailedDetails"),
                    GLOBAL_CONTAINER_ID);
        }

        return null;
    }

    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            request.logout();
            request.getSession().invalidate();
            System.out.println("Wylogowano");

            return PageConstants.getPage(PageConstants.ROOT_INDEX, true);
        } catch (ServletException e) {
            System.out.println("Niewylogowano \n " + e.getMessage());
            context.addMessage(null, new FacesMessage("Logout failed."));

            return null;
        }
    }
}
