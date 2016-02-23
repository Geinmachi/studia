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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import mot.interfaces.CurrentMatchType;
import mot.interfaces.InactivateMatch;
import org.primefaces.component.panel.Panel;

/**
 *
 * @author java
 */
public class DashboardPanel implements InactivateMatch, CurrentMatchType, Serializable {

    private Panel panel;

    private Matchh match;

    boolean filler = false;

    private MatchType matchType;

    private List<Competitor> competitorList = new ArrayList<>(2);

    private boolean editable = true;

    private boolean inplaceEditable = true;

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

    @Override
    public Matchh getMatch() {
        return match;
    }

    @Override
    public void setMatch(Matchh match) {
        this.match = match;
    }

    public boolean isFiller() {
        return filler;
    }

    public void setFiller(boolean filler) {
        this.filler = filler;
    }

    @Override
    public MatchType getMatchType() {
        return matchType;
    }

    @Override
    public void setMatchType(MatchType matchType) {
        this.matchType = matchType;
    }

    @Override
    public boolean getEditable() {
        return this.editable;
    }

    @Override
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    @Override
    public boolean isInplaceEditable() {
        return inplaceEditable;
    }

    @Override
    public void setInplaceEditable(boolean inplaceEditable) {
        this.inplaceEditable = inplaceEditable;
    }

    /**
     *
     * @return New List with competitors. To add competitors use method
     * updateCMGwithAdvanced
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
    public void updateCMGwithAdvanced(CompetitorMatch cm) {
        List<CompetitorMatch> cmgList = this.match.getCompetitorMatchList();

//        MatchType mt = new MatchType();
//        mt.setEndUser(true);
//        mt.setMatchTypeName("BO3");
//
//        this.matchType = mt;

        for (CompetitorMatch c : cmgList) {
            if (cm.equals(c)) {

            }
            System.out.println("Przed insertem: " + c.getIdCompetitor());
        }
//        competitorList.add(competitor);

        for (CompetitorMatch c : cmgList) {
            if (cm.equals(c)) {
                c.setIdCompetitor(cm.getIdCompetitor());
                c.setCompetitorMatchScore(cm.getCompetitorMatchScore());
                c.setPlacer(cm.getPlacer());
                System.err.println("JJJAKI PLACER " + cm.getPlacer());
                System.out.println("Compedtitori  " + c.getIdCompetitor());
                System.out.println("CMMM ID " + c);
                System.out.println("MATCH ID " + c.getIdMatch());
            }
        }

        System.out.println("MECZ nr " + this.match.getMatchNumber());
        for (int i = 0; i < cmgList.size(); i++) {
            System.out.println("Competuitor " + cmgList.get(i).getIdCompetitor() + " index " + i);
        }
        
        if (cmgList.size() > 1) {
            Collections.sort(cmgList);
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
    @Override
    public String toString() {
        return "DashboardPanel{" + "panel=" + panel + ", match=" + match + ", filler=" + filler + ", matchType=" + matchType + ", competitorList=" + competitorList + ", editable=" + editable + ", inplaceEditable=" + inplaceEditable + '}';
    }

}
