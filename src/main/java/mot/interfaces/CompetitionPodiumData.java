/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.interfaces;

import entities.Competitor;
import java.util.List;

/**
 *
 * @author java
 */
public interface CompetitionPodiumData {
    
    String getCompetitionName();
    
    Competitor getFirstPlaceCompetitor();
    
    Competitor getSecondPlaceCompetitor();
    
    List<Competitor> getThirdPlaceCompetitor();
    
    int getCompetitorsCount();
}
