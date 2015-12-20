/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans.mot.competitor;

import entities.Competitor;
import exceptions.ApplicationException;
import java.io.Serializable;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import mot.interfaces.ReportPlacementData;
import web.backingBeans.async.AsynchronousTask;
import web.backingBeans.async.AsynchronousTaskImpl;
import web.backingBeans.mot.competition.CompetitionAsyncBackingBean;

/**
 *
 * @author java
 */
@Named(value = "placementReportBackingBean")
@SessionScoped
public class PlacementReportBackingBean extends CompetitionAsyncBackingBean implements Serializable {

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
            Future<String> asyncResult = controller.asyncTest();
            AsynchronousTask asyncTask = new AsynchronousTaskImpl<>(asyncResult, "Asyncs test", "lololo");
            async.addTask(asyncTask);
            
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
