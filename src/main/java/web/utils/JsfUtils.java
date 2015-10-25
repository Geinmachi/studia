
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.utils;

import java.security.Principal;
import java.util.Locale;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author java
 */
public class JsfUtils {

    public static ExternalContext context() {
        return FacesContext.getCurrentInstance().getExternalContext();
    }

//    public static void addErrorMessage(Exception ex, String defaultMsg, String componentId) {
//        String msg = ex.getLocalizedMessage();
//        if (msg != null && msg.length() > 0) {
//            addErrorMessage(ResourceBundleUtils.getResourceBundleLanguageProperty("exception"), ResourceBundleUtils.getResourceBundleLanguageProperty(msg), componentId);
//        } else {
//            addErrorMessage(ResourceBundleUtils.getResourceBundleLanguageProperty("error"), defaultMsg, componentId);
//        }
//    }

    public static void addErrorMessage(String msg, String details, String componentId) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, details);
        FacesContext.getCurrentInstance().addMessage(componentId, facesMsg);
//        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        
    }

    public static void addSuccessMessage(String msg, String details, String componentId) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, details);
        FacesContext.getCurrentInstance().addMessage(componentId, facesMsg);
//        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
    }

    public static void invalidateSession() {
        context().invalidateSession();
    }

    public static Locale getLocale() {
        return context().getRequestLocale();
    }
    
    public static String successPageRedirect(String backPage) {
        context().getFlash().put("donePageBack", backPage);
        
        return PageConstants.getPage(PageConstants.ROOT_DONE, true);
    }
    
    public static String getCompetitionUrlParameter() {
        Map<String, String> paramMap = context().getRequestParameterMap();
        return paramMap.get("competition");
    }
}
