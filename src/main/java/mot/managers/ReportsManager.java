/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.managers;

import entities.Competitor;
import entities.CompetitorMatch;
import exceptions.ApplicationException;
import exceptions.InvalidPlaceException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import mot.facades.CompetitionFacadeLocal;
import mot.facades.CompetitorFacadeLocal;
import mot.facades.CompetitorMatchFacadeLocal;
import mot.facades.ScoreFacadeLocal;
import mot.interfaces.CompetitionPodiumData;
import mot.interfaces.ReportPlacementData;
import mot.models.CompetitionPodium;
import mot.models.CompetitorMatchesStatisticsMarkerEvent;
import mot.models.ReportPlacements;
import mot.services.CompetitionService;
import mot.timers.CompetitorMatchesStatisticsTimerLocal;

/**
 *
 * @author java
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class ReportsManager implements ReportsManagerLocal {

    @Resource
    SessionContext sessionContext;

    @Inject
    private Event<CompetitorMatchesStatisticsMarkerEvent> competitorMatchesEvent;

    @EJB
    private CompetitorFacadeLocal competitorFacade;

    @EJB
    private ScoreFacadeLocal scoreFacade;

    @EJB
    private CompetitionFacadeLocal competitionFacade;

    @EJB
    private CompetitorMatchFacadeLocal competitorMatchFacade;

    @EJB
    private CompetitorMatchesStatisticsTimerLocal competitorMatchesTimer;

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

    @Override
    public List<? extends CompetitionPodiumData> generateCompetitionPodiumStatistics() throws ApplicationException {
        List<Object[]> fetchedData = competitionFacade.getCompetitionPodiumStatistics();

        List<String> competitionNames = getUniqueCompetitionNamesInCompetitionPodiumStatistics(fetchedData);
        List<CompetitionPodium> statistics = fillCompetitorsInCompetitionPodiumStatistics(fetchedData, competitionNames);

        for (CompetitionPodium cpd : statistics) {
            cpd.setCompetitorsCount(scoreFacade.findCompetitorsCountByGlobalCompetitionName(cpd.getCompetitionName()));
        }

        return statistics;
    }

    private List<String> getUniqueCompetitionNamesInCompetitionPodiumStatistics(List<Object[]> fetchedData) {
        Set<String> competitionNames = new LinkedHashSet<>();
        for (Object[] obejcts : fetchedData) {
            competitionNames.add((String) obejcts[0]);
        }

        return new ArrayList<>(competitionNames);
    }

    private List<CompetitionPodium> fillCompetitorsInCompetitionPodiumStatistics(List<Object[]> fetchedData, List<String> competitionNames) throws InvalidPlaceException {
        List<CompetitionPodium> statistics = new ArrayList<>();
        for (String competitionName : competitionNames) {
            CompetitionPodium competitionPodium = new CompetitionPodium();
            competitionPodium.setCompetitionName(competitionName);
            statistics.add(competitionPodium);

            for (Object[] objects : fetchedData) {
                if (competitionName.equals(objects[0])) {
                    System.out.println("wstawia " + objects[2] + " na miejsce " + objects[1] + " do zawodow " + objects[0]);
                    competitionPodium.setCompetitor((short) objects[1], (Competitor) objects[2]);

                    if (competitionPodium.getFirstPlaceCompetitor() != null
                            && competitionPodium.getSecondPlaceCompetitor() != null
                            && competitionPodium.getThirdPlaceCompetitor() != null) {
                        break;
                    }
                }
            }
        }

        return statistics;
    }

    @Override
    @Asynchronous
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Future<List<CompetitorMatch>> generateCompetitorMatchesStatistics(Competitor competitor) {
//        competitorMatchesTimer.startTimer();
//        Future<List<CompetitorMatch>> asyncResult = competitorMatchFacade.findCompetitorMatchStatistics(competitor.getIdCompetitor());
//        System.out.println("Event -----");
//        competitorMatchesEvent.fire(new CompetitorMatchesStatisticsMarkerEvent());
//        System.out.println("Event ++++++");

        return new AsyncResult<>(competitorMatchFacade.findCompetitorMatchStatistics(competitor.getIdCompetitor()));
    }

//    public void creditPayment(@Observes CompetitorMatchesStatisticsMarkerEvent event) {
//        System.out.println("Przyszedl event w EJB" + event.toString());
//    }
//    @Timeout
//    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
//    public void timeout(Timer timer) {
//        System.out.println("Timer wykonany");
//        if (competitorMatchStatistics != null) {
//            System.out.println("jeszce nie skonczylo pobierac");
//        } else {
//            System.out.println("pobieranie zakonczone, wylacza timer");
//            timer.cancel();
//
//        }
//    }
    @Override
    @Asynchronous
    public Future<String> asyncTest() {

        String result = "test";
        for (int i = 0; i < 20; i++) {
            result += i;
            System.out.println("asyncTest ---- " + i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(ReportsManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return new AsyncResult<>(result);
    }
}
