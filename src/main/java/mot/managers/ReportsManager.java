/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.managers;

import entities.Competitor;
import exceptions.ApplicationException;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import mot.facades.CompetitorFacadeLocal;
import mot.facades.ScoreFacadeLocal;
import mot.interfaces.ReportPlacementData;
import mot.models.ReportPlacements;

/**
 *
 * @author java
 */
@Stateless
public class ReportsManager implements ReportsManagerLocal {
    
    @EJB
    private CompetitorFacadeLocal competitorFacade;
    
    @EJB
    private ScoreFacadeLocal scoreFacade;

    @Override
    public List<Competitor> getGlobalCompetitors() throws ApplicationException {
        return competitorFacade.findGlobalCompetitors();
    }

    @Override
    public ReportPlacementData getReportPlacements(Competitor competitor) throws ApplicationException {
        ReportPlacements reportPlacements = new ReportPlacements();
        
        reportPlacements.setFirstPlaces(scoreFacade.findPlacementCount(competitor.getIdCompetitor(), 1));
        reportPlacements.setSecondPlaces(scoreFacade.findPlacementCount(competitor.getIdCompetitor(), 2));
        reportPlacements.setThirdPlaces(scoreFacade.findPlacementCount(competitor.getIdCompetitor(), 3));
        reportPlacements.setParticipateCount(scoreFacade.findParticipateCount(competitor.getIdCompetitor()));
        
        return reportPlacements;
    }
}
