/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.interfaces;

import entities.MatchType;
import entities.Matchh;

/**
 *
 * @author java
 */
public interface CurrentMatchType {
    
//    void setMatch(Matchh match);
    
    Matchh getMatch();
    
    void setMatchType(MatchType matchType);
    
    MatchType getMatchType();
}
