/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans.mot.competition;

import entities.Competition;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import web.controllers.CompetitionController;

/**
 *
 * @author java
 */
@Named(value = "chooseCompetitionBackingBean")
@ViewScoped
public class ChooseCompetitionBackingBean extends CompetitionBackingBean implements Serializable {
    
    private List<Competition> loggedUserCompetitions;
    
    /**
     * Creates a new instance of ChooseCompetitionBackingBean
     */
    public ChooseCompetitionBackingBean() {
    }

    public List<Competition> getLoggedUserCompetitions() {
        return loggedUserCompetitions;
    }
    
    @PostConstruct
    private void init() {
        loggedUserCompetitions = controller.getLoggedUserCompetitions();
    }
    
    public String manageCompetition(Competition competition) {
        try {
            controller.storeCompetition(competition);
            return "manageCompetition?faces-redirect=true";
        } catch (Exception e) {
            System.out.println("WYJAAAAAAAAAAAAATEK ChooseCompetitionBackingBaen " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
