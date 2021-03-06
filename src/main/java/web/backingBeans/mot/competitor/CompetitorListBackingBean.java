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
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import web.backingBeans.mot.competition.CompetitionBackingBean;
import web.controllers.CompetitionController;
import web.utils.PageConstants;

/**
 *
 * @author java
 */
@Named(value = "competitorListBackingBean")
@ViewScoped
public class CompetitorListBackingBean extends CompetitionBackingBean implements Serializable {
    
    private List<Competitor> competitorList;

    public List<Competitor> getCompetitorList() {
        return competitorList;
    }
    
    /**
     * Creates a new instance of CompetitorListBackingBean
     */
    public CompetitorListBackingBean() {
    }
    
    @PostConstruct
    private void init() {
        try {
            competitorList = controller.getCompetitorsToEdit();
        } catch (ApplicationException ex) {
            Logger.getLogger(CompetitorListBackingBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException("Cannot init a bean");
        }
    }
    
    public String show(Competitor competitor) {
        controller.storeCompetitor(competitor);
        
        return PageConstants.getPage(PageConstants.EDIT_EDIT_COMPETITOR, true);
    }
}
