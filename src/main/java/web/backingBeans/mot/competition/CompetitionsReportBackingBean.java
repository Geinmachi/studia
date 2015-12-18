/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans.mot.competition;

import exceptions.ApplicationException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import mot.interfaces.CompetitionPodiumData;
import mot.models.CompetitionPodium;
import web.utils.JsfUtils;

/**
 *
 * @author java
 */
@Named(value = "competitionsReportBackingBean")
@ViewScoped
public class CompetitionsReportBackingBean extends CompetitionBackingBean implements Serializable {

    private static final Logger logger = Logger.getLogger(CompetitionsReportBackingBean.class.getName());
    
    private List<CompetitionPodiumData> competitionPodiumList;

    public List<CompetitionPodiumData> getCompetitionPodiumList() {
        return competitionPodiumList;
    }
    
    /**
     * Creates a new instance of CompetitionsReportBackingBean
     */
    public CompetitionsReportBackingBean() {
    }
    
    @PostConstruct
    private void init() {
        try {
            competitionPodiumList = (List<CompetitionPodiumData>) controller.generateCompetitionPodiumStatistics();
        } catch (ApplicationException e) {
            logger.log(Level.SEVERE, "Applicaiton exception {0}", e);
            JsfUtils.addErrorMessage(e, null);
        }
    }
    
}
