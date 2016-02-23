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
import javax.enterprise.inject.Any;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import web.backingBeans.mot.competition.CompetitionBackingBean;
import web.qualifiers.BackingBean;
import web.utils.PageConstants;

/**
 *
 * @author java
 */
@Named(value = "competitorReportsBackingBean")
@ViewScoped
public class CompetitorReportsBackingBean extends CompetitionBackingBean implements Serializable {

//    @Inject
//    private StatelessTest test;
//    
//    @Inject
//    private TestJMS jms;
//    
//    @Inject
//    private AccessControl1Local ac;
    
    @Inject
    @Any
    private PlacementReportBackingBean placementReport;

    @Inject
    @Any
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
//        test.write();
        try {
            competitorList = controller.getGlobalCompetitors();
        } catch (ApplicationException ex) {
            logger.log(Level.SEVERE, null, ex);
            throw new IllegalStateException("Cannot init a bean");
        }
//        jms.sendMessageTest1("moja wiadomosc " + System.currentTimeMillis());
//        ac.runAsExample();
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
