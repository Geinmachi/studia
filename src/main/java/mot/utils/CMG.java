/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.utils;

import entities.Competitor;
import entities.GroupCompetitor;
import entities.GroupDetails;
import entities.GroupName;
import entities.Matchh;

/**
 *
 * @author java
 */
public interface CMG {
    
    Competitor getIdCompetitor();
    
    Matchh getIdMatch();
    
    GroupName getIdGroupName();
    
    GroupCompetitor getGroupCompetitor();
    
    GroupDetails getGroupDetails();
    
    void setIdCompetitor(Competitor competitor);
    
    void setIdMatch(Matchh match);
    
    void setIdGroupName(GroupName groupName);
}
