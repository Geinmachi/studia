/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans;

import entities.Competitor;
import entities.CompetitorMatchGroup;
import entities.Groupp;
import entities.Matchh;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.component.datatable.DataTable;
import web.controllers.CompetitionController;
/**
 *
 * @author java
 */
//@Named(value = "bracketCreationBackingBean")
//@ViewScoped
@Dependent
public class BracketCreationBackingBean implements Serializable {

    @Inject
    private CompetitionController controller;

    private List<CompetitorMatchGroup> competitorMatchGroupList = new ArrayList<>();

    private List<Groupp> groups = new ArrayList<>();

    private List<Matchh> otherMatches = new ArrayList<>();

    private List<Matchh> firstRoundMatches = new ArrayList<>();

    private List<Competitor> competitorsWithGroups = new ArrayList<>();


    public List<Groupp> getGroups() {
        return groups;
    }

    public List<Matchh> getOtherMatches() {
        return otherMatches;
    }

    public List<Matchh> getFirstRoundMatches() {
        return firstRoundMatches;
    }

    public List<Competitor> getCompetitorsWithGroups() {
        return competitorsWithGroups;
    }

    public List<CompetitorMatchGroup> getCompetitorMatchGroupList() {
        return competitorMatchGroupList;
    }

    public BracketCreationBackingBean() {
    }

    @PostConstruct
    private void init() {

    }

    public void createBracket(List<Competitor> competitors) {
        initializeLists(competitors);
    }

    private void initializeLists(List<Competitor> competitors) {
        competitorMatchGroupList = controller.generateEmptyBracket(competitors);

        Set<Groupp> uniqueGroups = new HashSet();
        Set<Matchh> uniqueFirstRoundMatches = new HashSet();
        Set<Matchh> uniqueOtherMatches = new HashSet();

        for (CompetitorMatchGroup cmg : competitorMatchGroupList) {
            competitorsWithGroups.add(cmg.getIdCompetitor());
            uniqueGroups.add(cmg.getIdGroup());
            if (cmg.getIdMatch().getRoundd() == 1) {
                uniqueFirstRoundMatches.add(cmg.getIdMatch());
            } else {
                uniqueOtherMatches.add(cmg.getIdMatch());
            }
        }

        groups.addAll(uniqueGroups);
        groups.remove(null);
        groups.sort(new Comparator() {

            @Override
            public int compare(Object o1, Object o2) {
                return ((Groupp) o1).getGroupName().compareTo(((Groupp) o2).getGroupName());
            }

        });
        firstRoundMatches.addAll(uniqueFirstRoundMatches);
        otherMatches.addAll(uniqueOtherMatches);

        System.out.println("GRoups size is: " + groups.size());
    }

    public void clearLists() {
        groups.removeAll(groups);
        firstRoundMatches.removeAll(firstRoundMatches);
        otherMatches.removeAll(otherMatches);
        competitorsWithGroups.removeAll(competitorsWithGroups);
    }
}
