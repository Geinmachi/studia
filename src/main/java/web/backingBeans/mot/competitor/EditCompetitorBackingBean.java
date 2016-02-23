/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans.mot.competitor;

import entities.Competitor;
import entities.Team;
import exceptions.ApplicationException;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.view.ViewScoped;
import web.backingBeans.mot.competition.CompetitionBackingBean;
import web.converters.ConverterHelper;
import web.converters.interfaces.ConverterDataAccessor;
import web.utils.JsfUtils;
import web.utils.PageConstants;

/**
 *
 * @author java
 */
@Named(value = "editCompetitorBackingBean")
@ConverterHelper(viewId = PageConstants.EDIT_EDIT_COMPETITOR)
@ViewScoped
public class EditCompetitorBackingBean extends CompetitionBackingBean implements ConverterDataAccessor<Team>, Serializable {

    private Competitor competitor;

    private List<Team> teamList;

    public Competitor getCompetitor() {
        return competitor;
    }

    public List<Team> getTeamList() {
        return teamList;
    }

    /**
     * Creates a new instance of EditCompetitorBackingBean
     */
    public EditCompetitorBackingBean() {
    }

    @PostConstruct
    private void init() {
        logger.log(Level.INFO, "EditCOmpetitorBackingBean INITTTTTTTTTTTT");
        System.out.println("Init 8 razy?");
        competitor = controller.getEditingCompetitor();

        if (competitor == null) {
            JsfUtils.addErrorMessage("There is no competitor to edit", " ", null);

            return;
        }

        try {
            teamList = controller.findUserAllowedTeams();
        } catch (ApplicationException ex) {
            ex.printStackTrace();
            Logger.getLogger(EditCompetitorBackingBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String edit() {
        try {
            controller.editCompetitor(competitor);
            return JsfUtils.successPageRedirect(PageConstants.getPage(PageConstants.EDIT_COMPETITOR_LIST, true));
        } catch (ApplicationException e) {
            JsfUtils.addErrorMessage(e, null);
            System.out.println("Application Exception ");
        } catch (Exception e) {

            System.out.println("Exception in EditCompetitorBB#edit");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Team> getFetchedData() {
        return teamList;
    }
}
