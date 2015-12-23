/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.models;

import java.io.Serializable;
import mot.interfaces.CompetitorMatchesStatistics;

/**
 *
 * @author java
 */
public class CompetitorMatchesStatisticsImpl implements CompetitorMatchesStatistics, Serializable {

    private final short score;
    
    private final String competitorFirstName;
    
    private final String competitorLastName;

    public CompetitorMatchesStatisticsImpl(short score, String competitorFirstName, String competitorLastName) {
        this.score = score;
        this.competitorFirstName = competitorFirstName;
        this.competitorLastName = competitorLastName;
    }
    
    public CompetitorMatchesStatisticsImpl(CompetitorMatchesStatistics copy) {
        this.score = copy.score();
        this.competitorFirstName = copy.competitiorFirstName();
        this.competitorLastName = copy.competitiorLastName();
    }
    
    @Override
    public short score() {
        return score;
    }

    @Override
    public String competitiorFirstName() {
        return competitorFirstName;
    }

    @Override
    public String competitiorLastName() {
        return competitorLastName;
    }
    
}
