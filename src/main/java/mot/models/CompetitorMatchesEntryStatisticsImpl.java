/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.models;

import java.io.Serializable;
import java.util.List;
import mot.interfaces.CompetitorMatchesEntryStatistics;
import mot.interfaces.CompetitorMatchesStatistics;

/**
 *
 * @author java
 */
public class CompetitorMatchesEntryStatisticsImpl implements CompetitorMatchesEntryStatistics, Serializable {

    private final String competitionName;
    
    private final List<CompetitorMatchesStatistics> matchData;

    public CompetitorMatchesEntryStatisticsImpl(String competitionName, List<CompetitorMatchesStatistics> matchData) {
        this.competitionName = competitionName;
        this.matchData = matchData;
    }
    
    public CompetitorMatchesEntryStatisticsImpl(CompetitorMatchesEntryStatistics copy) {
        this.competitionName = copy.competitionName();
        this.matchData = copy.matchData();
    }
    
    @Override
    public String competitionName() {
        return competitionName;
    }

    @Override
    public List<CompetitorMatchesStatistics> matchData() {
        return matchData;
    }
    
}
