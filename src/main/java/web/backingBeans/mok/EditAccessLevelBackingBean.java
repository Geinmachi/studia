/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans.mok;

import entities.AccessLevel;
import entities.Account;
import entities.Administrator;
import entities.Organizer;
import exceptions.ApplicationException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import web.utils.JsfUtils;
import web.utils.PageConstants;

/**
 *
 * @author java
 */
@Named(value = "editAccessLevelBackingBean")
@ViewScoped
public class EditAccessLevelBackingBean extends AccountBackingBean implements Serializable {

    private Account editingAccount;

    private List<AccessLevel> accessLevelList;

    private List<AccessLevel> allAccessLevelsList;

    public Account getEditingAccount() {
        return editingAccount;
    }

    public List<AccessLevel> getAllAccessLevelsList() {
        return allAccessLevelsList;
    }

    public void setAllAccessLevelsList(List<AccessLevel> allAccessLevelsList) {
        this.allAccessLevelsList = allAccessLevelsList;
    }

    /**
     * Creates a new instance of EditAccessLevelBackingBean
     */
    public EditAccessLevelBackingBean() {
    }

    @PostConstruct
    private void init() {
        initAccessLevels();
        editingAccount = controller.getEditingAccount();
        accessLevelList = editingAccount.getAccessLevelList();
        rewriteAccessLevelsValue();
    }

    private void initAccessLevels() {
        allAccessLevelsList = new ArrayList<>();
        allAccessLevelsList.add(new Organizer());
        allAccessLevelsList.add(new Administrator());
    }

    private void rewriteAccessLevelsValue() {
        for (AccessLevel al : accessLevelList) {
            for (AccessLevel aal : allAccessLevelsList) {
                if (al.getClass().isAssignableFrom(aal.getClass())) {
                    aal.setIsActive(al.getIsActive());

                    break;
                }
            }
        }
    }

    public String saveAccessLevels() {
        for (AccessLevel al : allAccessLevelsList) {
            boolean found = false;
            for (AccessLevel fetchedAccessLevel : accessLevelList) {
                if (al.getClass().isAssignableFrom(fetchedAccessLevel.getClass())) {
                    fetchedAccessLevel.setIsActive(al.getIsActive());
                    found = true;

                    break;
                }
            }

            if (!found && al.getIsActive()) {
                accessLevelList.add(al);
            }
        }

        try {
            accessLevelList.forEach(p -> System.out.println("BB: " + p.getDiscriminatorValue() + " " + p.getIsActive()));
            controller.editAccessLevels(accessLevelList);
            System.out.println("Wykonalo sie bez wjatku");
            return JsfUtils.successPageRedirect(PageConstants.ADMIN_USERS_LIST);
        } catch (ApplicationException e) {
            logger.log(Level.SEVERE, e.getMessage());
            JsfUtils.addErrorMessage(e, "editAccessLevelForm");
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            JsfUtils.addErrorMessage(e, "editAccessLevelForm");
        }
        
        System.out.println("Jakis wyjatek");

        return null;
    }
}
