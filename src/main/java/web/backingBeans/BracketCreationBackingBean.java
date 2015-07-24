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

    public BracketCreationBackingBean() {
    }

    @PostConstruct
    private void init() {
    }

    public void createBracket(List<Competitor> competitors) {
        initializeLists(competitors);
        createModel();
    }

    private void initializeLists(List<Competitor> competitors) {
        competitorMatchGroupList = controller.generateEmptyBracket(competitors);

        Set<Groupp> uniqueGroups = new HashSet();
        Set<Matchh> uniqueFirstRoundMatches = new HashSet();
        Set<Matchh> uniqueOtherMatches = new HashSet();

        for (CompetitorMatchGroup cmg : competitorMatchGroupList) {
            if (cmg.getIdCompetitor() != null) {
                competitorsWithGroups.add(cmg.getIdCompetitor());
            }
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

    private void createModel() {
        System.out.println("WYKONALO SIE create model z lica " + competitorsWithGroups.size());
        dashboardModel = new DefaultDashboardModel();

        DashboardColumn firstRoundColumn = createFirstRoundColumn();
        
        dashboardModel.addColumn(firstRoundColumn);

        List<DashboardColumn> otherColumns = creatOtherRoundsColumns();
        
        for (DashboardColumn dc : otherColumns) {
            dashboardModel.addColumn(dc);
        }
        
//        for (int i = 1; i <= rounds; i++) {
//            DashboardColumn column = new DefaultDashboardColumn();
//            for (int j = 0; j < competitorMatchGroupList.size(); j++) {
//                Panel panel = new Panel();
//                panel.setHeader("id" + i + "-" + j);
//                panel.setId("id" + i + "-" + j);
//                if (competitorMatchGroupList.get(j).getIdMatch().getRoundd() == i) {
//                    panel.getChildren().add(panelContent(competitorMatchGroupList.get(j).getIdCompetitor()));
//
//                    column.addWidget("id" + i + "-" + j);
//                    dashboard.getChildren().add(panel);
//                }
//            }
//            dashboardModel.addColumn(column);
//        }
    }

    private List<DashboardColumn> creatOtherRoundsColumns() {
        UIComponent dashboard = FacesContext.getCurrentInstance().getViewRoot().findComponent("createCompetitionForm:dashboard");

        int rounds = BracketUtil.numberOfRounds(competitorsWithGroups.size());

        List<DashboardColumn> columns = new ArrayList<>();

        for (int i = 0; i < rounds; i++) {
            columns.add(new DefaultDashboardColumn());
        }

        for (int i = 0; i < otherMatches.size(); i++) {
            Panel panel = new Panel();
            panel.setHeader("other" + i);
            panel.setId("other" + i);

            columns.get(otherMatches.get(i).getRoundd()-1).addWidget("other" + i);
            dashboard.getChildren().add(panel);
        }
        
        return columns;
    }

    private DashboardColumn createFirstRoundColumn() {
        UIComponent dashboard = FacesContext.getCurrentInstance().getViewRoot().findComponent("createCompetitionForm:dashboard");
        DashboardColumn firstRoundColumn = new DefaultDashboardColumn();

        boolean isFirstCompetitor;

        for (int i = 0; i < firstRoundMatches.size(); i++) {
            isFirstCompetitor = true;
            Panel panel = new Panel();
            panel.setHeader("first" + i);
            panel.setId("first" + i);

            for (int j = 0; j < competitorMatchGroupList.size(); j++) {
                if (firstRoundMatches.get(i).equals(competitorMatchGroupList.get(j).getIdMatch())) {
//                    System.out.println("firstRoundMatch = " + firstRoundMatches.get(i).getUuid() 
//                            + " competitorMatchGroup-match = " + competitorMatchGroupList.get(j).getIdMatch().getUuid());
                    panel.getChildren().add(panelContent(competitorMatchGroupList.get(j).getIdCompetitor(), isFirstCompetitor));
                    isFirstCompetitor = false;
                }
            }

            firstRoundColumn.addWidget("first" + i);
            dashboard.getChildren().add(panel);
        }
        return firstRoundColumn;
    }

    private OutputLabel panelContent(Competitor competitor, boolean newLine) {

        OutputLabel content = new OutputLabel();
        if (competitor != null) {
            content.setValue(competitor.getIdPersonalInfo().getFirstName() + " "
                    + competitor.getIdPersonalInfo().getLastName());
        }
        if (newLine) {
            HtmlOutputText linebreak = new HtmlOutputText();
            linebreak.setValue("<br/>");
            linebreak.setEscape(false);
            content.getChildren().add(linebreak);
        }

        return content;
    }
}
