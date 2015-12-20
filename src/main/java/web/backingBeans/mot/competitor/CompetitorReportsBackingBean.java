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
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import mot.interfaces.CompetitionPodiumData;
import web.backingBeans.mot.competition.CompetitionBackingBean;
import web.controllers.CompetitionController;
import web.utils.JsfUtils;
import web.utils.PageConstants;

/**
 *
 * @author java
 */
@Named(value = "competitorReportsBackingBean")
@ViewScoped
public class CompetitorReportsBackingBean extends CompetitionBackingBean implements Serializable {

    @Inject
    private PlacementReportBackingBean placementReport;

    @Inject
    private MatchesReportBackingBean matchesReport;

    private List<Competitor> competitorList;

    public List<Competitor> getCompetitorList() {
        return competitorList;
    }

    /**
     * Creates a new instance of CompetitorReportsBackingBean
     */
    public CompetitorReportsBackingBean() {
    }

    @PostConstruct
    private void init() {
        try {
            competitorList = controller.getGlobalCompetitors();
        } catch (ApplicationException ex) {
            Logger.getLogger(CompetitorReportsBackingBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException("Cannot init a bean");
        }
    }

    public String displayPlacementReport(Competitor competitor) {
        placementReport.initValues(competitor);
//        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("competitor", competitor);
//        System.out.println("Competitor z puta " + ((Competitor) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("competitor")).getIdPersonalInfo().getFirstName());
        return PageConstants.getPage(PageConstants.ROOT_PLACEMENT_REPORT, true);
    }

    public String displayMatchesReport(Competitor competitor) {
        if (matchesReport.initValues(competitor)) {
            return PageConstants.getPage(PageConstants.ROOT_MATCHES_REPORT, true);
        } else {
            return null;
        }
//        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("competitor", competitor);
//        System.out.println("Competitor z puta " + ((Competitor) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("competitor")).getIdPersonalInfo().getFirstName());
//        return PageConstants.getPage(PageConstants.ROOT_PLACEMENT_REPORT, true);
    }
}
