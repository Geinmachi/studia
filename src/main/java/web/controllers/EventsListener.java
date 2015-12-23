/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.controllers;

import java.io.Serializable;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.event.TransactionPhase;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import mot.models.CompetitorMatchesStatisticsMarkerEvent;
import web.utils.JsfUtils;

/**
 *
 * @author java
 */
@RequestScoped
public class EventsListener implements Serializable {
    
    @Inject
    private CompetitionController controller;

    /**
     * Creates a new instance of EventListener
     */
    public EventsListener() {
    }

    public void competitorMatchesStatistics(@Observes CompetitorMatchesStatisticsMarkerEvent event) {
        System.out.println("------Przyszedl event request scope " + event + " thread name " + Thread.currentThread().getName());
//        controller.test();
        JsfUtils.addSuccessMessage("Pobrano1", "rekordy", "eventCatcher");
        JsfUtils.addSuccessMessage("Pobrano2", "rekordy", "globalContainer");
        JsfUtils.addSuccessMessage("Pobrano3", "rekordy", null);
//        RequestContext.getCurrentInstance().update("eventGrowl");
        System.out.println("Test");
//        controller.test();
//        System.out.println("MEssages " + FacesContext.getCurrentInstance().getMessageList().size());
    }

    public void processQualifiedPayload(@Observes(notifyObserver = Reception.ALWAYS) String event) {
        System.out.println("JEst evnt ");
    }
}
