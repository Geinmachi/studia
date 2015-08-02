/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans;

import entities.Competitor;
import entities.CompetitorMatchGroup;
import entities.Groupp;
import entities.MatchMatchType;
import entities.MatchType;
import entities.Matchh;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIPanel;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.component.dashboard.Dashboard;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.panel.Panel;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;
import utils.BracketUtil;
import web.controllers.CompetitionController;
import web.models.DashboardPanel;

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

//    @Inject
//    private BracketDashboardModel bracketModel;
    private DashboardModel dashboardModel;

    private List<CompetitorMatchGroup> competitorMatchGroupList = new ArrayList<>();

    private List<Groupp> groups = new ArrayList<>();

    private List<Matchh> otherMatches = new ArrayList<>();

    private List<Matchh> firstRoundMatches = new ArrayList<>();

    private List<Competitor> competitorsWithGroups = new ArrayList<>();

    private List<DashboardPanel> panelList = new ArrayList<>();

    private List<MatchType> matchTypeList = new ArrayList<>();

    public DashboardModel getDashboardModel() {
        return dashboardModel;
    }

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

    public List<DashboardPanel> getPanelList() {
        return panelList;
    }

    public List<MatchType> getMatchTypeList() {
        return matchTypeList;
    }

    public BracketCreationBackingBean() {
    }

    @PostConstruct
    private void init() {
        matchTypeList = controller.getEndUserMatchTypes();
    }

    public void createEmptyBracket(List<Competitor> competitors) {
        competitorMatchGroupList = controller.generateEmptyBracket(competitors);
        initializeLists();
        createModel();
    }

    public void recreateBracket(List<CompetitorMatchGroup> competitorMatchGroupList) {
        this.competitorMatchGroupList = competitorMatchGroupList;
        initializeLists();
        createModel();
    }

    public void updateBracket() {
        assignMatchTypes();
        verifyEverything();
    }

    public void verifyEverything() {
        System.out.println("DO persistowania:");

        for (CompetitorMatchGroup cmg : competitorMatchGroupList) {
            System.out.println("Competitor " + cmg.getIdCompetitor());
            if (cmg.getIdGroup() != null) {
                System.out.println("Grupa " + cmg.getIdGroup().getGroupName());
            } else {
                System.out.println("Grupa null");
            }
            if (cmg.getIdMatch() != null) {
                System.out.println("Match " + cmg.getIdMatch().getUuid() + " nr " + cmg.getIdMatch().getMatchNumber());
                System.out.println("Data " + cmg.getIdMatch().getMatchDate());
                System.out.println("typy ");
                for (MatchMatchType mmt : cmg.getIdMatch().getMatchMatchTypeList()) {
                    System.out.println(mmt.getIdMatchType().getMatchTypeName());
                }
            } else {
                System.out.print("Match null");
            }
        }
    }

    public void assignMatchTypes() {
        for (CompetitorMatchGroup cmg : competitorMatchGroupList) {
            for (DashboardPanel dp : panelList) {
                if (cmg.getIdMatch().equals(dp.getMatch())) {
                    MatchMatchType mmt = new MatchMatchType();
                    mmt.setIdMatchType(dp.getMatchType());
                    mmt.setIdMatch(cmg.getIdMatch());
                    if (!cmg.getIdMatch().getMatchMatchTypeList().contains(mmt)) {
                        cmg.getIdMatch().getMatchMatchTypeList().add(mmt);
                    }
                    break;
                }
            }
        }
    }

    private void initializeLists() {
//        Collections.shuffle(competitorMatchGroupList);
        competitorMatchGroupList.sort(new Comparator<CompetitorMatchGroup>() {

            @Override
            public int compare(CompetitorMatchGroup o1, CompetitorMatchGroup o2) {
                return Short.compare(o1.getIdMatch().getMatchNumber(), o2.getIdMatch().getMatchNumber());
            }

        });

        Set<Groupp> uniqueGroups = new HashSet();
        Set<Matchh> uniqueFirstRoundMatches = new HashSet();
        Set<Matchh> uniqueOtherMatches = new HashSet();

        for (CompetitorMatchGroup cmg : competitorMatchGroupList) {
            System.out.println("CMMMMGGG " + cmg.getIdMatch().getMatchNumber() + " round "
                    + cmg.getIdMatch().getRoundd());
            if (cmg.getIdCompetitor() != null && cmg.getIdGroup() != null) {
                competitorsWithGroups.add(cmg.getIdCompetitor());
            }
            uniqueGroups.add(cmg.getIdGroup());
            if (cmg.getIdMatch().getRoundd() == 1) {
                uniqueFirstRoundMatches.add(cmg.getIdMatch());
            } else {
                System.out.println("OTHER MATCH " + cmg.getIdMatch().getMatchNumber());
                uniqueOtherMatches.add(cmg.getIdMatch());
            }
        }

        for (CompetitorMatchGroup cmg : competitorMatchGroupList) {
            if (cmg.getIdGroup() != null) {
                System.out.println("Grupa " + cmg.getIdGroup().getGroupName());
            }
        }
        groups.addAll(uniqueGroups);
        groups.remove(null);
        Collections.sort(groups);

        firstRoundMatches.addAll(uniqueFirstRoundMatches);
        Collections.sort(firstRoundMatches);

        otherMatches.addAll(uniqueOtherMatches);
        Collections.sort(otherMatches);
        System.out.println("GRoups size is: " + groups.size());
    }

    public void clearLists() {
        groups.removeAll(groups);
        firstRoundMatches.removeAll(firstRoundMatches);
        otherMatches.removeAll(otherMatches);
        competitorsWithGroups.removeAll(competitorsWithGroups);
        panelList.removeAll(panelList);
    }

    private void createModel() {
        System.out.println("WYKONALO SIE create model z lica " + competitorsWithGroups.size());
        dashboardModel = new DefaultDashboardModel();

        DashboardColumn firstRoundColumn = createFirstRoundColumn();

        dashboardModel.addColumn(firstRoundColumn);

        List<DashboardColumn> otherColumns = createOtherRoundsColumns();

        for (int i = 0; i < otherColumns.size(); i++) {
            dashboardModel.addColumn(createFillers(i));
            dashboardModel.addColumn(otherColumns.get(i));
        }
    }

    private DashboardColumn createFillers(int columnNumber) {
        DashboardColumn column = new DefaultDashboardColumn();

        for (int i = 0; i < 20; i++) {
            Panel panel = new Panel();
            panel.setId("filler" + columnNumber + i);

            DashboardPanel dashboardPanel = new DashboardPanel();
            dashboardPanel.setFiller(true);
            dashboardPanel.setPanel(panel);
            column.addWidget("filler" + columnNumber + i);
            panelList.add(dashboardPanel);
        }

        return column;
    }

    private List<DashboardColumn> createOtherRoundsColumns() {
        int rounds = BracketUtil.numberOfRounds(competitorsWithGroups.size());

        System.out.println("competitorsWithGroups " + competitorsWithGroups.size());
        System.out.println("otherMatches " + otherMatches.size());

        List<DashboardColumn> columns = new ArrayList<>();

        for (int i = 0; i < rounds - 1; i++) {
            columns.add(new DefaultDashboardColumn());
        }

        for (int i = 0; i < otherMatches.size(); i++) {

            System.out.println("MAAATTTTTTHCHCHCHCCHCHCHHCHC ++++ " + otherMatches.get(i).getRoundd()
                    + " number " + otherMatches.get(i).getMatchNumber());
            Panel panel = new Panel();
            System.out.println("Competitors: ");

            panel.setId("other" + (otherMatches.get(i).getRoundd()) + i);

            columns.get(otherMatches.get(i).getRoundd() - 2)
                    .addWidget("other" + (otherMatches.get(i).getRoundd()) + i);
//            dashboard.getChildren().add(panel);

            DashboardPanel dashboardPanel = new DashboardPanel();

//            for (CompetitorMatchGroup cmg : otherMatches.get(i).getCompetitorMatchGroupList()) {
//                if (cmg.getIdCompetitor() != null) {
//                    System.out.println("CMGGGHHGG : " + cmg.getIdCompetitor());
//                    dashboardPanel.addCompetitorToList(cmg.getIdCompetitor(), cmg.getPlacer() - 1);
//                }
//            }

            dashboardPanel.setPanel(panel);
//            if (otherMatches.get(i).getRoundd() == 3) {
//                dashboardPanel.setMargin(580);
//            }
//            if (otherMatches.get(i).getRoundd() == 2) {
//                dashboardPanel.setMargin(255);
//            }
//            if (otherMatches.get(i).getRoundd() == 4) {
//                dashboardPanel.setMargin(650);
//            }//590
            dashboardPanel.setMatch(otherMatches.get(i));
            dashboardPanel.getMatch().setMatchDate(new Date());
            panelList.add(dashboardPanel);
        }

        return columns;
    }

    private DashboardColumn createFirstRoundColumn() {
        DashboardColumn firstRoundColumn = new DefaultDashboardColumn();

        for (int i = 0; i < firstRoundMatches.size(); i++) {
            Panel panel = new Panel();
            panel.setId("first" + i);

            DashboardPanel dashboardPanel = new DashboardPanel();

            firstRoundColumn.addWidget("first" + i);
//            dashboard.getChildren().add(panel);
//            dashboardPanel.setMargin(50);
            dashboardPanel.setPanel(panel);
            dashboardPanel.setMatch(firstRoundMatches.get(i));
            dashboardPanel.getMatch().setMatchDate(new Date());

//            for (int j = 0; j < competitorMatchGroupList.size(); j++) {
//                if (firstRoundMatches.get(i).equals(competitorMatchGroupList.get(j).getIdMatch())) {
////                    System.out.println("firstRoundMatch = " + firstRoundMatches.get(i).getUuid() 
////                            + " competitorMatchGroup-match = " + competitorMatchGroupList.get(j).getIdMatch().getUuid());
////                    panel.getChildren().add(panelContent(competitorMatchGroupList.get(j).getIdCompetitor(), isFirstCompetitor));
//                    dashboardPanel.addCompetitorToList(competitorMatchGroupList.get(j).getIdCompetitor(), competitorMatchGroupList.get(j).getPlacer() - 1);
//                }
//            }

            panelList.add(dashboardPanel);

        }

        return firstRoundColumn;
    }

    public void addAdvancedCompetitor(CompetitorMatchGroup cmg) {
        for (DashboardPanel dp : panelList) {
//            System.out.println("MATCHH GGGGGGGGGGGGGG UMBER " + dp.getMatch().getMatchNumber());
            if (cmg.getIdMatch() == null) {
                System.out.println("MAAAAAAAATCHHHHHHH NUUUUUULLLLLL ");

                return;
            } else {
                if (Short.compare(dp.getMatch().getMatchNumber(), cmg.getIdMatch().getMatchNumber()) == 0) {
                    System.out.println("JET ROWNY mstch number " + cmg.getIdMatch().getMatchNumber());
                    dp.getMatch().getCompetitorMatchGroupList().add(cmg);
                    System.out.println("ROZMIAR COMPETITOROW PRZED " + dp.getCompetitorList().size());
                    for (Competitor c : dp.getCompetitorList()) {
                        System.out.println("COmpettttt : " + c);
                    }
                    dp.addCompetitorToList(cmg.getIdCompetitor(), cmg.getPlacer() - 1);
                    System.out.println("ROZMIAR COMPETITOROW POO " + dp.getCompetitorList().size());
                    for (Competitor c : dp.getCompetitorList()) {
                        System.out.println("COmpettttt : " + c);
                    }
                    break;
                }
            }
        }
    }
}
