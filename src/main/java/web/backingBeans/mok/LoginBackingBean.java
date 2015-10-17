/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans.mok;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import utils.ConvertUtil;

/**
 *
 * @author java
 */
@Named(value = "loginBackingBean")
@RequestScoped
public class LoginBackingBean {

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

    public void login() {

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        try {
            request.login(username, password);
//            return "/index.xhtml?faces-redirect=true";
        } catch (ServletException ex) {
            Logger.getLogger(LoginBackingBean.class.getName()).log(Level.SEVERE, null, ex);
//            return null;
        }
    }

    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            request.logout();
            System.out.println("Wylogowano");
            return "/index.xhtml?faces-redirect=true";
        } catch (ServletException e) {
            System.out.println("Niewylogowano \n " + e.getMessage());
            context.addMessage(null, new FacesMessage("Logout failed."));
            return null;
        }
    }
}
