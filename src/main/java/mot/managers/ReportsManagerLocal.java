/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.managers;

import entities.Competitor;
import exceptions.ApplicationException;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import mot.interfaces.CompetitionPodiumData;
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
}
