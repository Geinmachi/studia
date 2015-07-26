/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.models;

import entities.Competitor;
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
    
    private int margin;
    
    boolean filler = false;
    
    private MatchType matchType;
    
    private List<Competitor> competitorList = new ArrayList<>();

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

    public int getMargin() {
        return margin;
    }

    public void setMargin(int margin) {
        this.margin = margin;
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

    public List<Competitor> getCompetitorList() {
        return competitorList;
    }

    public void setCompetitorList(List<Competitor> competitorList) {
        this.competitorList = competitorList;
    }
    
    
}
