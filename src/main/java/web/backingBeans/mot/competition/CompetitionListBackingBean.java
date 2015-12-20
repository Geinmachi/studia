/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans.mot.competition;

import entities.Competition;
import exceptions.ApplicationException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import web.controllers.CompetitionController;
import web.utils.DisplayPageEnum;

/**
 *
 * @author java
 */
@Named(value = "competitionListBackingBean")
@ViewScoped
public class CompetitionListBackingBean extends CompetitionBackingBean implements Serializable{

    private List<Competition> competitionList;

    public CompetitionListBackingBean() {
    }

    public List<Competition> getCompetitionList() {
        return competitionList;
    }

    public DisplayPageEnum getDisplayPageEnum(String type) {
        return DisplayPageEnum.valueOf(type);
    }

    @PostConstruct
    private void init() {

        try {
            competitionList = controller.findCompetitionsToDisplay();
        } catch (ApplicationException ex) {
            Logger.getLogger(CompetitionListBackingBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException("CannotInitializeBean");
        }
    }

    public String displayCompetition(Competition competition, DisplayPageEnum type) {
        controller.setDisplayedCompetition(competition, type);
        System.out.println("ZMIANAA " + Character.toUpperCase(type.toString().toLowerCase().charAt(0)) + type.toString().substring(1).toLowerCase());
        return "competition" + Character.toUpperCase(type.toString().toLowerCase().charAt(0)) + type.toString().substring(1).toLowerCase() + ".xhtml?faces-redirect=true";
    }
}
