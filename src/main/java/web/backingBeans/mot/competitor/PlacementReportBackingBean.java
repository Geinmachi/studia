/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans.mot.competitor;

import entities.Competitor;
import exceptions.ApplicationException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import mot.interfaces.ReportPlacementData;
import web.backingBeans.mot.competition.CompetitionBackingBean;
import web.controllers.CompetitionController;
import web.utils.PageConstants;

/**
 *
 * @author java
 */
@Named(value = "placementReportBackingBean")
@SessionScoped
public class PlacementReportBackingBean extends CompetitionBackingBean implements Serializable {

    private Competitor selectedCompetitor;

    private ReportPlacementData reportPlacements;

    public Competitor getSelectedCompetitor() {
        return selectedCompetitor;
    }

    public ReportPlacementData getReportPlacements() {
        return reportPlacements;
    }

    /**
     * Creates a new instance of PlacementReportBackingBean
     */
    public PlacementReportBackingBean() {
    }

//    @PostConstruct
//    private void init() {
//        try {
//            selectedCompetitor = ((Competitor) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("competitor"));
//            if (selectedCompetitor != null) {
//                System.out.println("selectddddd " + selectedCompetitor.getIdPersonalInfo().getFirstName());
//                reportPlacements = controller.getReportPlacements(selectedCompetitor);
//            }
//
//        } catch (Exception ex) {
//            Logger.getLogger(PlacementReportBackingBean.class.getName()).log(Level.SEVERE, null, ex);
//            throw new IllegalStateException("Cannot init a bean");
//        }
//    }

    public void initValues(Competitor competitor) {
        try {
            selectedCompetitor = competitor;
            if (selectedCompetitor != null) {
                reportPlacements = controller.getReportPlacements(selectedCompetitor);
            }
        } catch (ApplicationException e) {
            Logger.getLogger(PlacementReportBackingBean.class.getName()).log(Level.SEVERE, null, e);
            throw new IllegalStateException("Cannot init a bean");
        }
    }
}
