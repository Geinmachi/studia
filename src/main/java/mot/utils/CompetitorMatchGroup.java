/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.utils;

import entities.Competitor;
import entities.CompetitorMatch;
import entities.GroupCompetitor;
import entities.GroupDetails;
import entities.GroupName;
import entities.Matchh;

/**
 *
 * @author java
 */
public class CompetitorMatchGroup implements CMG {
    
    private CompetitorMatch competitorMatch;
    
    private GroupCompetitor groupCompetitor;

    public CompetitorMatchGroup(GroupCompetitor gc, CompetitorMatch cm) {
        this.groupCompetitor = gc;
        this.competitorMatch = cm;
    }

    public void setCompetitorMatch(CompetitorMatch competitorMatch) {
        this.competitorMatch = competitorMatch;
    }

    public void setGroupCompetitor(GroupCompetitor groupCompetitor) {
        this.groupCompetitor = groupCompetitor;
    }
 
    @Override
    public Competitor getIdCompetitor() {
        return competitorMatch.getIdCompetitor();
    }

    @Override
    public Matchh getIdMatch() {
        return competitorMatch.getIdMatch();
    }

    @Override
    public GroupName getIdGroupName() {
        return groupCompetitor.getIdGroupDetails().getIdGroupName();
    }

    @Override
    public void setIdCompetitor(Competitor competitor) {
        competitorMatch.setIdCompetitor(competitor);
        groupCompetitor.setIdCompetitor(competitor);
    }

    @Override
    public void setIdMatch(Matchh match) {
        competitorMatch.setIdMatch(match);
    }

    @Override
    public void setIdGroupName(GroupName groupName) {
        groupCompetitor.getIdGroupDetails().setIdGroupName(groupName);
    }

    @Override
    public GroupCompetitor getGroupCompetitor() {
        return groupCompetitor;
    }

    @Override
    public GroupDetails getGroupDetails() {
        return groupCompetitor.getIdGroupDetails();
    }
    
}
