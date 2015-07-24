/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans;

import entities.CompetitorMatchGroup;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.primefaces.component.dashboard.Dashboard;
import org.primefaces.component.panel.Panel;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;
import utils.BracketUtil;

/**
 *
 * @author java
 */
@Dependent
public class BracketDashboardModel {

    private DashboardModel model;

    public DashboardModel getModel() {
        return model;
    }

    public BracketDashboardModel() {
    }

    @PostConstruct
    private void init() {
//        model = new DefaultDashboardModel();
//        UIComponent dashboard = FacesContext.getCurrentInstance().getViewRoot().findComponent("createCompetitionForm:dashboard");
//
//        int rounds = BracketUtil.numberOfRounds(16);
//
//        for (int i = 0; i < rounds; i++) {
//            DashboardColumn column = new DefaultDashboardColumn();
//            for (int j = 0; j < 3; j++) {
//                Panel panel2 = new Panel();
//                panel2.setHeader("meh" + i + "-" + j);
//                panel2.setId("meh" + i + "-" + j);
//                column.addWidget("meh" + i + "-" + j);
//                dashboard.getChildren().add(panel2);
//            }
//            model.addColumn(column);
//        }
    }

//    public void createModel(List<CompetitorMatchGroup> competitorMatchGroupList) {
//        System.out.println("WYKONALO SIE create model z lica " + competitorsAmount);
//        model = new DefaultDashboardModel();
//        UIComponent dashboard = FacesContext.getCurrentInstance().getViewRoot().findComponent("createCompetitionForm:dashboard");
//
//        int rounds = BracketUtil.numberOfRounds(competitorsAmount);
//
//        for (int i = 0; i < rounds; i++) {
//            DashboardColumn column = new DefaultDashboardColumn();
//            for (int j = 0; j < competitorsAmount; j++) {
//                Panel panel = new Panel();
//                panel.setHeader("id" + i + "-" + j);
//                panel.setId("id" + i + "-" + j);
//                column.addWidget("id" + i + "-" + j);
//                dashboard.getChildren().add(panel);
//            }
//            model.addColumn(column);
//        }
//    }
}
