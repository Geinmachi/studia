/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.managers;

import entities.Competitor;
import exceptions.ApplicationException;
import java.util.List;
import java.util.concurrent.Future;
import javax.ejb.Remote;
import mot.interfaces.CompetitionPodiumData;
import mot.interfaces.CompetitorMatchesEntryStatistics;
import mot.interfaces.ReportPlacementData;

/**
 *
 * @author java
 */
@Remote
public interface ReportsManagerLocal {

    public List<Competitor> getGlobalCompetitors() throws ApplicationException;

    public ReportPlacementData getReportPlacements(Competitor competitor) throws ApplicationException;

    public List<? extends CompetitionPodiumData> generateCompetitionPodiumStatistics() throws ApplicationException;

    public Future<List<CompetitorMatchesEntryStatistics>> generateCompetitorMatchesStatistics(Competitor competitor);
    
    String getNoCompetitorInMatchMark();
}
