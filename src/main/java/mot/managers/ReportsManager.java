/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.managers;

import ejb.common.TrackerInterceptor;
import entities.Competitor;
import exceptions.ApplicationException;
import exceptions.InvalidPlaceException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import mot.facades.CompetitionFacadeLocal;
import mot.facades.CompetitorFacadeLocal;
import mot.facades.CompetitorMatchFacadeLocal;
import mot.facades.ScoreFacadeLocal;
import mot.interfaces.CompetitionPodiumData;
import mot.interfaces.CompetitorMatchesEntryStatistics;
import mot.interfaces.CompetitorMatchesStatistics;
import mot.interfaces.ReportPlacementData;
import mot.models.CompetitionPodium;
import mot.models.CompetitorMatchesEntryStatisticsImpl;
import mot.models.CompetitorMatchesStatisticsImpl;
import mot.models.ReportPlacements;

/**
 *
 * @author java
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class ReportsManager implements ReportsManagerLocal {

    private static final Logger logger = Logger.getLogger(ReportsManager.class.getName());

    @Resource
    private SessionContext sessionContext;

    @EJB
    private CompetitorFacadeLocal competitorFacade;

    @EJB
    private ScoreFacadeLocal scoreFacade;

    @EJB
    private CompetitionFacadeLocal competitionFacade;

    @EJB
    private CompetitorMatchFacadeLocal competitorMatchFacade;

    private static final String NO_COMPETITOR_IN_MATCH_MARK = "-";

    private static final int COMPETITOR_MATCH_STATISTICS_PARTS = 10;

    @Override
    public String getNoCompetitorInMatchMark() {
        return NO_COMPETITOR_IN_MATCH_MARK;
    }

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
    public Future<List<CompetitorMatchesEntryStatistics>> generateCompetitorMatchesStatistics(Competitor competitor) {
        List<Object[]> resultList = new ArrayList<>();

        int recordsCount = competitorMatchFacade.findCompetitorMatchStatisticsCount(competitor.getIdCompetitor());

        int limit = (int) Math.floor(recordsCount / COMPETITOR_MATCH_STATISTICS_PARTS);
        if (limit < recordsCount) {
            limit = recordsCount;
        }

        for (int i = 0; i < recordsCount; i += limit) {
            if (sessionContext.wasCancelCalled()) {
                logger.log(Level.INFO, "Anulowalo pobieranie raportu zawodnika o id {0}",
                        competitor.getIdCompetitor());
                return new AsyncResult<>(null);
            }
            resultList.addAll(competitorMatchFacade
                    .findPartialCompetitorMatchStatistics(competitor.getIdCompetitor(), limit, i));
        }

        sortCmeResult(resultList);

        List<CompetitorMatchesEntryStatistics> cmeStatisticsList = transformCmeStatistics(resultList, competitor);

        return new AsyncResult<>(cmeStatisticsList);
    }

    private void sortCmeResult(List<Object[]> resultList) {
        Collections.sort(resultList, new Comparator<Object[]>() { // sorts by idMatch
            @Override
            public int compare(Object[] o1, Object[] o2) {
                return Long.compare((int) (o1[3]), (int) (o2[3]));
            }

        });
    }

    private List<CompetitorMatchesEntryStatistics> transformCmeStatistics(List<Object[]> fetchedStatistics, Competitor competitor) {
        List<CompetitorMatchesEntryStatistics> cmeStatisticsList = new ArrayList<>();

        for (int i = 0; i < fetchedStatistics.size() - 1; i += 2) {
            Object[] firstCompetitorData = fetchedStatistics.get(i);
            System.out.println("II: " + i + "pierwszy " + firstCompetitorData[1]);
            Object[] secondCompetitorData = fetchedStatistics.get(i + 1);
            System.out.println("II: " + i + "drugi " + secondCompetitorData[1]);

//        for (Object[] objects : resultList) {
            CompetitorMatchesStatistics firstCMS = new CompetitorMatchesStatisticsImpl(createCompetitorMatchesStatistics(firstCompetitorData[0], firstCompetitorData[1]));
            CompetitorMatchesStatistics secondCMS = new CompetitorMatchesStatisticsImpl(createCompetitorMatchesStatistics(secondCompetitorData[0], secondCompetitorData[1]));

            List<CompetitorMatchesStatistics> matchData = new ArrayList<>();

            boolean isFirstCompetitorSearchedOne = false;
            if (!(firstCMS.competitiorFirstName().equals(NO_COMPETITOR_IN_MATCH_MARK)) && ((Competitor) firstCompetitorData[1]).getIdCompetitor().equals(competitor.getIdCompetitor())) { // if first competitor is searched one, put him first in the list
                isFirstCompetitorSearchedOne = true;
            }

            matchData.add(secondCMS);
            matchData.add(isFirstCompetitorSearchedOne ? 0 : 1, firstCMS);

            CompetitorMatchesEntryStatistics cmeStatistics = createCompetitorMatchesEntryStatistics((String) fetchedStatistics.get(i)[2], matchData);
            cmeStatisticsList.add(new CompetitorMatchesEntryStatisticsImpl(cmeStatistics));
        }

        return cmeStatisticsList;
    }

    private CompetitorMatchesStatistics createCompetitorMatchesStatistics(Object score, Object competitor) {
        return new CompetitorMatchesStatistics() {
            @Override
            public short score() {
                short tmpScore = 0;

                if (score != null) {
                    try {
                        tmpScore = (short) score;
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "Cannot cast score {0} to short{1}", new Object[]{score, e});
                    }
                }

                return tmpScore;
            }

            @Override
            public String competitiorFirstName() {
                String tmpName = "-";

                if (competitor != null) {
                    try {
                        tmpName = ((Competitor) competitor).getIdPersonalInfo().getFirstName();
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "Cannot get first name from competitor {0}", new Object[]{competitor});
                    }
                }

                return tmpName;
            }

            @Override
            public String competitiorLastName() {
                String tmpSurname = "";

                if (competitor != null) {
                    try {
                        tmpSurname = ((Competitor) competitor).getIdPersonalInfo().getLastName();
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "Cannot get last name from competitor {0}", new Object[]{competitor});
                    }
                }

                return tmpSurname;
            }
        };
    }

    private CompetitorMatchesEntryStatistics createCompetitorMatchesEntryStatistics(String competitionName, List<CompetitorMatchesStatistics> matchData) {
        return new CompetitorMatchesEntryStatistics() {

            @Override
            public String competitionName() {
                return competitionName;
            }

            @Override
            public List<CompetitorMatchesStatistics> matchData() {
                return matchData;
            }

        };
    }
}
