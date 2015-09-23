/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.models;

import entities.Competitor;
import entities.CompetitorMatch;
import entities.GroupCompetitor;
import entities.GroupDetails;
import entities.GroupName;
import entities.Matchh;
import java.util.Objects;
import mot.interfaces.CMG;

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
        if (groupCompetitor != null && groupCompetitor.getIdGroupDetails() != null) {
            return groupCompetitor.getIdGroupDetails().getIdGroupName();
        } else {
            return null;
        }
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
        if (groupCompetitor != null) {
            return groupCompetitor.getIdGroupDetails();
        } else {
            return null;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.competitorMatch);
        hash = 89 * hash + Objects.hashCode(this.groupCompetitor);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CompetitorMatchGroup other = (CompetitorMatchGroup) obj;
        if (!Objects.equals(this.competitorMatch, other.competitorMatch)) {
            return false;
        }
        if (!Objects.equals(this.groupCompetitor, other.groupCompetitor)) {
            return false;
        }
        return true;
    }
    
}
