/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.managers;

import entities.Competitor;
import entities.CompetitorMatch;
import exceptions.ApplicationException;
import java.util.List;
import java.util.concurrent.Future;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.enterprise.event.Observes;
import mot.interfaces.CompetitionPodiumData;
import mot.interfaces.ReportPlacementData;
import mot.models.CompetitorMatchesStatisticsMarkerEvent;

/**
 *
 * @author java
 */
@Remote
public interface ReportsManagerLocal {

    public List<Competitor> getGlobalCompetitors() throws ApplicationException;

    public ReportPlacementData getReportPlacements(Competitor competitor) throws ApplicationException;

    public List<? extends CompetitionPodiumData> generateCompetitionPodiumStatistics() throws ApplicationException;

    public Future<List<CompetitorMatch>> generateCompetitorMatchesStatistics(Competitor competitor);

    public Future<String> asyncTest();
}
