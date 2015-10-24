/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.utils;

import entities.Competition;
import entities.Competitor;
import entities.CompetitorMatch;
import entities.GroupCompetitor;
import entities.GroupDetails;
import entities.GroupName;
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
import mot.interfaces.CMG;
import mot.interfaces.CurrentMatchType;
import mot.interfaces.InactivateMatch;
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
import web.qualifiers.Logging;

/**
 *
 * @author java
 */
//@Named(value = "bracketCreationBackingBean")
//@ViewScoped
//@Logging
@Dependent
public class BracketCreation implements Serializable {

    private static final long serialVersionUID = 234324L;

    @Inject
    private CompetitionController controller;

//    @Inject
//    private BracketDashboardModel bracketModel;
    private DashboardModel dashboardModel;

    private List<CMG> competitorMatchGroupList = new ArrayList<>();

    private List<GroupDetails> groups = new ArrayList<>();

    private List<Matchh> otherMatches = new ArrayList<>();

    private List<Matchh> firstRoundMatches = new ArrayList<>();

    private List<Competitor> competitorsWithGroups = new ArrayList<>();

    private List<DashboardPanel> panelList = new ArrayList<>();

    private List<MatchType> matchTypeList = new ArrayList<>();

    private int rounds;

    public DashboardModel getDashboardModel() {
        return dashboardModel;
    }

    public List<GroupDetails> getGroups() {
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

    public List<CMG> getCompetitorMatchGroupList() {
        return competitorMatchGroupList;
    }

    public List<DashboardPanel> getPanelList() {
        return panelList;
    }

    public List<MatchType> getMatchTypeList() {
        return matchTypeList;
    }

    public BracketCreation() {
    }

    public int getRounds() {
        return rounds;
    }

    @PostConstruct
    private void init() {
        Package p = FacesContext.class.getPackage();
        System.out.println("MOJARA " + p.getImplementationTitle() + " " + p.getImplementationVersion());
        matchTypeList = controller.getEndUserMatchTypes();
    }

    public void createEmptyBracket(List<Competitor> competitors) {
        competitorMatchGroupList = controller.generateEmptyBracket(competitors);
        initializeLists();
        createModel();
    }

//    public void recreateBracketToEdit(List<CMG> competitorMatchGroupList) {
//        this.competitorMatchGroupList = competitorMatchGroupList;
//        initializeLists();
//        createModel();
//    }
    public void recreateBracketToEdit(Competition competition) {
        List<CMG> cmgList = controller.getCompetitionCMGMappings(competition);

        this.competitorMatchGroupList = cmgList;
        initializeLists();
        createModel();
        disableFinishedMatches();
        assignCurrentMatchTypes();
    }
    
    public void recreateBracketToDisplay(Competition competition) {
        List<CMG> cmgList = controller.getCompetitionCMGMappings(competition);

        this.competitorMatchGroupList = cmgList;
        initializeLists();
        createModel();
        assignCurrentMatchTypes();
    }

    public void updateBracket() {
        assignMatchTypes();
        verifyEverything();
    }

    public void verifyEverything() {
        System.out.println("DO persistowania:");

        for (CMG cmg : competitorMatchGroupList) {
            System.out.println("Competitor " + cmg.getIdCompetitor());
            if (cmg.getGroupCompetitor() != null) {
                System.out.println("Grupa " + cmg.getIdGroupName());
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
                System.out.println("Match null");
            }
        }
    }

    public void assignMatchTypes() {
        for (CMG cmg : competitorMatchGroupList) {
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
        competitorMatchGroupList.sort(new Comparator<CMG>() {

            @Override
            public int compare(CMG o1, CMG o2) {
                return Short.compare(o1.getIdMatch().getMatchNumber(), o2.getIdMatch().getMatchNumber());
            }

        });

        Set<GroupDetails> uniqueGroups = new HashSet();
        Set<Matchh> uniqueFirstRoundMatches = new HashSet();
        Set<Matchh> uniqueOtherMatches = new HashSet();

        for (CMG cmg : competitorMatchGroupList) {
            System.out.println("CMMMMGGG " + cmg.getIdMatch().getMatchNumber() + " round " + cmg.getIdMatch().getRoundd());
            if (cmg.getIdCompetitor() != null && cmg.getGroupCompetitor() != null && cmg.getIdMatch().getRoundd() == 1) { // bylo cmg.getIdGroup
                competitorsWithGroups.add(cmg.getIdCompetitor());
            } else {
                System.out.println("NUUULLL " + cmg.getIdCompetitor() + " GROIUPCOMPETITOR " + cmg.getGroupCompetitor());
            }
            uniqueGroups.add(cmg.getGroupDetails());
            if (cmg.getIdMatch().getRoundd() == 1) {
                uniqueFirstRoundMatches.add(cmg.getIdMatch());
            } else {
                System.out.println("OTHER MATCH " + cmg.getIdMatch().getMatchNumber());
                uniqueOtherMatches.add(cmg.getIdMatch());
            }
        }

        for (CMG cmg : competitorMatchGroupList) {
            if (cmg.getGroupCompetitor() != null) {
                System.out.println("Grupa " + cmg.getIdGroupName());
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
            dashboardModel.addColumn(createFillers(i, otherColumns.get(i).getWidgetCount()));
            dashboardModel.addColumn(otherColumns.get(i));
        }

    }

    private DashboardColumn createFillers(int columnNumber, int matchesInRoundCount) {
        DashboardColumn column = new DefaultDashboardColumn();

        for (int i = 0; i < ((matchesInRoundCount * 2) + matchesInRoundCount); i++) {
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
        rounds = BracketUtil.numberOfRounds(competitorsWithGroups.size());

        System.out.println("competitorsWithGroups " + competitorsWithGroups.size());
        System.out.println("otherMatches " + otherMatches.size());

        List<DashboardColumn> columns = new ArrayList<>();

        for (int i = 0; i < rounds - 1; i++) {
            columns.add(new DefaultDashboardColumn());
        }

        for (int i = 0; i < otherMatches.size(); i++) {

            System.out.println("MAAATTTTTTHCHCHCHCCHCHCHHCHC ++++ " + otherMatches.get(i).getRoundd() + " number " + otherMatches.get(i).getMatchNumber());
            Panel panel = new Panel();
            System.out.println("Competitors: ");

            panel.setId("other" + (otherMatches.get(i).getRoundd()) + i);

            columns.get(otherMatches.get(i).getRoundd() - 2)
                    .addWidget("other" + (otherMatches.get(i).getRoundd()) + i);
//            dashboard.getChildren().add(panel);

            DashboardPanel dashboardPanel = new DashboardPanel();

//            for (CompetitorMatch cmg : otherMatches.get(i).getCompetitorMatchGroupList()) {
//                if (cmg.getIdCompetitor() != null) {
//                    System.out.println("CMGGGHHGG : " + cmg.getIdCompetitor());
//                    dashboardPanel.updateCMGwithAdvanced(cmg.getIdCompetitor(), cmg.getPlacer() - 1);
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
//            dashboardPanel.getMatch().setMatchDate(new Date());
            System.out.println("SOROWANIE INNYCH RUND");
            sortCompetitorsInMatch(otherMatches.get(i));
            for (CompetitorMatch cm : dashboardPanel.getMatch().getCompetitorMatchList()) {
                System.out.println("CZZZZY psoortowane " + cm.getIdCompetitor() + " placer " + cm.getPlacer());
            }

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
//            dashboardPanel.getMatch().setMatchDate(new Date());
            System.out.println("SORTOWANIE PIERWSZEJ RUNDY");
            sortCompetitorsInMatch(firstRoundMatches.get(i));
            for (CompetitorMatch cm : dashboardPanel.getMatch().getCompetitorMatchList()) {
                System.out.println("CZZZZY psoortowane " + cm.getIdCompetitor() + " placer " + cm.getPlacer());
            }
//            for (int j = 0; j < competitorMatchGroupList.size(); j++) {
//                if (firstRoundMatches.get(i).equals(competitorMatchGroupList.get(j).getIdMatch())) {
////                    System.out.println("firstRoundMatch = " + firstRoundMatches.get(i).getUuid() 
////                            + " competitorMatchGroup-match = " + competitorMatchGroupList.get(j).getIdMatch().getUuid());
////                    panel.getChildren().add(panelContent(competitorMatchGroupList.get(j).getIdCompetitor(), isFirstCompetitor));
//                    dashboardPanel.updateCMGwithAdvanced(competitorMatchGroupList.get(j).getIdCompetitor(), competitorMatchGroupList.get(j).getPlacer() - 1);
//                }
//            }

            panelList.add(dashboardPanel);

        }

        return firstRoundColumn;
    }

    private void sortCompetitorsInMatch(Matchh match) {
        if (match != null && match.getCompetitorMatchList().size() > 1) {
            for (CompetitorMatch cmg : match.getCompetitorMatchList()) {
                System.out.println("Prowownanie cmg.placer = " + cmg.getPlacer());
                System.out.println("cmop " + cmg.getIdCompetitor());
            }
            Collections.sort(match.getCompetitorMatchList(), new Comparator<CompetitorMatch>() {

                @Override
                public int compare(CompetitorMatch o1, CompetitorMatch o2) {
                    System.out.println("ooo1 placer = " + o1.getPlacer() + " 00002 placer" + o2.getPlacer());
                    System.out.println("o1 = " + o1.getIdCompetitor() + " o2 " + o2.getIdCompetitor());
                    if (o1.getPlacer() != null) {
                        System.out.println("o1 placer rozny od null");
                        return placerPosition(o1.getPlacer());
                    }
                    if (o2.getPlacer() != null) {
                        System.out.println("o2 placer rozny od null");
                        return placerPosition(o2.getPlacer());
                    }

                    return 0;
                }

            });
        }
    }

    int placerPosition(short placer) {
        if (Short.compare(placer, (short) 1) == 0) {
            System.out.println(" ocena placer: " + placer + " zwraca -1");
            return -1;
        } else {
            System.out.println(" ocena placer: " + placer + " zwraca 1");
            return 1;
        }
    }

    public void updateScores(CompetitorMatch updatedCompetitorMatch) {
        for (DashboardPanel dp : panelList) {
            if (dp.getMatch().equals(updatedCompetitorMatch.getIdMatch())) {
                System.out.println("DPPPPPPPPPPP : " + dp.getMatch());
                System.out.println("NEIW DPPPPPPPPPPPP : " + updatedCompetitorMatch.getIdMatch());

                for (CompetitorMatch cmg : dp.getMatch().getCompetitorMatchList()) {

                    System.out.println("DP CMGGGGGG  : " + cmg);
                    System.out.println("CMGGGGGG  : " + updatedCompetitorMatch);

                    if (cmg.equals(updatedCompetitorMatch)) {
                        int cmIndex = dp.getMatch().getCompetitorMatchList().indexOf(cmg);
                        
                        System.out.println("VERSION before update " + dp.getMatch().getCompetitorMatchList().get(cmIndex).getVersion());
                        dp.getMatch().getCompetitorMatchList().set(cmIndex, updatedCompetitorMatch);
                        System.out.println("VERSION after update " + dp.getMatch().getCompetitorMatchList().get(cmIndex).getVersion());
//                        cmg.setCompetitorMatchScore(updatedCompetitorMatch.getCompetitorMatchScore());

                        System.out.println("CMGs sa rowne:::: score " + cmg.getCompetitorMatchScore());
                        System.out.println("CZy sa rowne referencyjnie " + (cmg == updatedCompetitorMatch));
                        System.out.println("DP SIZE " + panelList.size());
                        System.out.println("CMG SIZE " + dp.getMatch().getCompetitorMatchList().size());
                        return;
                    }
                }
            }
        }
    }

    public void addAdvancedCompetitor(CompetitorMatch cmg) {
        System.out.println("WYKONALO SIE ADVANCE!!");

        for (DashboardPanel dp : panelList) {
//            System.out.println("MATCHH GGGGGGGGGGGGGG UMBER " + dp.getMatch().getMatchNumber());
            if (cmg.getIdMatch() == null) {
                System.out.println("MAAAAAAAATCHHHHHHH NUUUUUULLLLLL ");

                return;
            } else {
                if (Short.compare(dp.getMatch().getMatchNumber(), cmg.getIdMatch().getMatchNumber()) == 0) {
                    System.out.println("JET ROWNY mstch number " + cmg.getIdMatch().getMatchNumber());

//                    dp.getMatch().getCompetitorMatchGroupList().add(cmg);
                    System.out.println("ROZMIAR COMPETITOROW PRZED " + dp.getMatch().getCompetitorMatchList().size());

                    for (CompetitorMatch c : dp.getMatch().getCompetitorMatchList()) {
                        System.out.println("COmpettttt : " + c.getIdCompetitor());
                    }

                    dp.updateCMGwithAdvanced(cmg);
                    
                    System.out.println("PRZED DISABLE");
//                    BracketUtil.makeSerializablePanel(dp);
                    this.disableMatch(dp);
                    System.out.println("PO DISABLE");
                    
                    System.out.println("ROZMIAR COMPETITOROW POO " + dp.getMatch().getCompetitorMatchList().size());
                    System.out.println("IIIIIIIIIII MATCH SCORE VALUE = " + cmg.getCompetitorMatchScore());

                    System.out.println("CZY jest nULL w CM " + dp.getMatch().getCompetitorMatchList().contains(null));
                    for (CompetitorMatch c : dp.getMatch().getCompetitorMatchList()) {
                        System.out.println("COmpettttt : " + c.getIdCompetitor());
                    }

                    break;
                }
            }
        }
    }

    public void disableFinishedMatches() {
//        for (int i = 0; i < panelList.size(); i++) {
//            Collections.sw
//        }
        for (DashboardPanel dp : panelList) {

            //    System.out.println("PANEL SERAILZIABLE " + dp.getPanel());
            
       //     BracketUtil.makeSerializablePanel(dp);
            InactivateMatch updatedMatch = controller.disableMatch(dp);

            dp.setEditable(updatedMatch.getEditable());
            dp.setInplaceEditable(updatedMatch.isInplaceEditable());
            dp.setMatch(updatedMatch.getMatch());

            /*    dp_block:
             {
             if (dp.getMatch() != null) {
                    
             for (CompetitorMatch cm : dp.getMatch().getCompetitorMatchList()) {
             if(cm.getIdCompetitor() == null) {
             System.out.println("Nie ma jakiegos competitora w metch, wylacza edycje");
             dp.setInplaceEditable(false);
                            
             continue;
             }
             }
                    
             for (MatchMatchType mmt : dp.getMatch().getMatchMatchTypeList()) {
             System.out.println("mmt " + mmt.getIdMatchType().getMatchTypeName());
             if (mmt.getIdMatchType().getMatchTypeName().startsWith("BO")) {
             dp.setMatchType(mmt.getIdMatchType());
                            
             for (CompetitorMatch cm : dp.getMatch().getCompetitorMatchList()) {
             if (cm.getCompetitorMatchScore() != null && ((Integer.valueOf(mmt.getIdMatchType().getMatchTypeName().substring(2)) + 1) / 2) == cm.getCompetitorMatchScore()) {
             System.out.println("WYLACZA " + dp.getMatch());
                                    
             System.out.println("idMatch =  " + dp.getMatch().getIdMatch() + " COMPETITORRRRRRRRRRRRRRRRx " + cm.getIdCompetitor() + "   ---   idCompetitorMatch " + cm.getIdCompetitorMatch());
                                    
             dp.setEditable(false);
             break dp_block;
             }
             }
             }
             }
             System.out.println("TYP : " + dp.getMatch().getMatchMatchTypeList());
             }
             //    for (CompetitorMatch cm : dp.getMatch().getCompetitorMatchList()) {
             // if (cm.getCompetitorMatchScore() == cm.ge)
             //   }
             }
             */
        }
    }

    private void assignCurrentMatchTypes() {
        for (DashboardPanel dp : panelList) {
            
//            CurrentMatchType dpWrapper = new DashboardPanel();
//            
//            dpWrapper.setMatchType(dp.getMatchType());
//            dpWrapper.setMatch(dp.getMatch());

            CurrentMatchType cmt = controller.assignCurrentMatchType(dp);

            dp.setMatchType(cmt.getMatchType());
        }
    }
    
    /**
     *
     * @param match
     * @return true if something was disabled
     */
    public boolean disableMatch(InactivateMatch match) {
        InactivateMatch disabledMatch = controller.disableMatch(match);
        
        match.setEditable(disabledMatch.getEditable());
        match.setInplaceEditable(disabledMatch.isInplaceEditable());
        
        return disabledMatch.getEditable() || disabledMatch.isInplaceEditable();
    }
}
