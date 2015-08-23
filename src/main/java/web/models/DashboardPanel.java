/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.models;

import entities.Competitor;
import entities.CompetitorMatch;
import entities.MatchType;
import entities.Matchh;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.primefaces.component.panel.Panel;

/**
 *
 * @author java
 */
public class DashboardPanel {

    private Panel panel;

    private Matchh match;

    boolean filler = false;

    private MatchType matchType;

    private List<Competitor> competitorList = new ArrayList<>(2);

    public DashboardPanel() {
        competitorList.add(null);
        competitorList.add(null);
    }

    public Panel getPanel() {
        return panel;
    }

    public void setPanel(Panel panel) {
        this.panel = panel;
    }

    public Matchh getMatch() {
        return match;
    }

    public void setMatch(Matchh match) {
        this.match = match;
    }

    public boolean isFiller() {
        return filler;
    }

    public void setFiller(boolean filler) {
        this.filler = filler;
    }

    public MatchType getMatchType() {
        return matchType;
    }

    public void setMatchType(MatchType matchType) {
        this.matchType = matchType;
    }

    /**
     *
     * @return New List with competitors. To add competitors use method
 updateCMGwithAdvanced
     */
    public List<Competitor> getCompetitorList() {
        return new ArrayList<>(competitorList);
    }

    public void setCompetitorList(List<Competitor> competitorList) {
        this.competitorList = competitorList;
    }

    /**
     * Used for inserting advanced players (rounds other than 1)
     *
     * @param competitor
     * @param index
     */
    public void updateCMGwithAdvanced(CompetitorMatch cmg) {
        List<CompetitorMatch> cmgList = this.match.getCompetitorMatchGroupList();

        for (CompetitorMatch c : cmgList) {
            if (cmg.equals(c)) {
                
            }
            System.out.println("Przed insertem: " + c.getIdCompetitor());
        }
//        competitorList.add(competitor);

        for (CompetitorMatch c : cmgList) {
            if (cmg.equals(c)) {
                c.setIdCompetitor(cmg.getIdCompetitor());
                System.err.println("JAKI PLACER " + c.getPlacer());
            }
        }
//        cmgList.get(index).setIdCompetitor(competitor);

//        competitorList.remove(null);
//
//        competitorList.add(index, competitor);
        for (CompetitorMatch c : cmgList) {
            System.out.println("Po insercie: " + c.getIdCompetitor());
        }
    }

//    /**
//     * Inserts competitor at unspecific index (rounds 1)
//     *
//     * @param competitor
//     */
//    public void updateCMGwithAdvanced(Competitor competitor) {
//
//        List<CompetitorMatchGroup> cmgList = this.match.getCompetitorMatchGroupList();
//
//        cmgList.remove(null);
//        cmgList.a
////        cmgList.ad
////        competitorList.remove(null);
////        competitorList.add(competitor);
//    }
}
