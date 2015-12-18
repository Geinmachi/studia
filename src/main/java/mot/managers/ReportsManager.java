/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.managers;

import entities.Competitor;
import exceptions.ApplicationException;
import exceptions.InvalidPlaceException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import mot.facades.CompetitionFacadeLocal;
import mot.facades.CompetitorFacadeLocal;
import mot.facades.ScoreFacadeLocal;
import mot.interfaces.CompetitionPodiumData;
import mot.interfaces.ReportPlacementData;
import mot.models.CompetitionPodium;
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

    @EJB
    private CompetitionFacadeLocal competitionFacade;

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
}
