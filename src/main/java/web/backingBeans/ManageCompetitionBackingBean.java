/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans;

import entities.Competition;
import entities.Competitor;
import entities.CompetitorMatchGroup;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import web.controllers.CompetitionController;
import web.models.DashboardPanel;

/**
 *
 * @author java
 */
@Named(value = "manageCompetitionBackingBean")
@ViewScoped
public class ManageCompetitionBackingBean implements Serializable {

    @Inject
    private CompetitionController controller;

    @Inject
    private BracketCreationBackingBean bracketCreator;

    private List<Competitor> competitorList = new ArrayList<>();

    private List<CompetitorMatchGroup> cmgList = new ArrayList<>();

    private Competition competition;

    /**
     * Creates a new instance of ManageCompetitionBacking
     */
    public ManageCompetitionBackingBean() {
    }

    public BracketCreationBackingBean getBracketCreator() {
        return bracketCreator;
    }

    public Competition getCompetition() {
        return competition;
    }

//    public List<CompetitorMatchGroup> getCmgList() {
//        return cmgList;
//    }
    @PostConstruct
    private void init() {
        System.out.println("INITTTTTTTTTTTTTTTTTTTTT");
        competition = controller.getEditingCompetition();
        if (competition == null) {
            throw new IllegalStateException("There is no competition to edit");
        }
        cmgList = controller.getCompetitionCMGMappings(competition);
        bracketCreator.recreateBracket(cmgList);
    }

    public void saveScore(CompetitorMatchGroup cmg) {
        System.out.println("Wykonal sie save z inpalce " + cmg.getCompetitorMatchScore());
        System.out.println("Zawodnika " + cmg.getIdCompetitor());

        bracketCreator.updateScores(cmg);
        try {
            CompetitorMatchGroup advancedCMG = controller.saveCompetitorScore(cmg);
            if (advancedCMG != null) {
                //        cmgList.add(advancedCMG);
                bracketCreator.addAdvancedCompetitor(advancedCMG);
            }
            System.out.println("Przeszlo all, advanced id = " + advancedCMG);
            RequestContext.getCurrentInstance().update(":manageCompetitionForm:dashboard");
            System.out.println("Po odswiezeniu");

        } catch (IllegalStateException ise) {
            if (ise.getMessage().equals("Too big number")) {
                cmg.setCompetitorMatchScore((short) 0);
                bracketCreator.updateScores(cmg);
            }
        }

        refreshPage();
    }

    public boolean wa() {
        RequestContext.getCurrentInstance().update(":manageCompetitionForm:dashboard");
        System.out.println("Wykonalo sie waAAAAAAAAAAAA");
        return true;
    }

    public void remoteC(CompetitorMatchGroup cmg) {
        System.out.println("WYKONALO remotecOMNAE ");
        RequestContext.getCurrentInstance().update("manageCompetitionForm:dashboard");


        System.out.println("PO ODSWIEZENIEU JS CMG::: " + cmg.getCompetitorMatchScore());
        System.out.println("NR MATCHU i ID COMPETITORA " + cmg.getIdMatch() + " comp: " + cmg.getIdCompetitor() + " iddd " + cmg);

        System.out.println("Z finda all: ");

        for (DashboardPanel dp : bracketCreator.getPanelList()) {
            if (dp != null && dp.getMatch() != null) {
                if (dp.getMatch().equals(cmg.getIdMatch())) {
                    System.out.println("ODSWIEZENIE SA ROWNE");
                    for (CompetitorMatchGroup ccmg : dp.getMatch().getCompetitorMatchGroupList()) {
                        if (ccmg.equals(cmg)) {
                            System.out.println("DP SCOREEE : " + ccmg.getCompetitorMatchScore());
                            System.out.println("ACTUAL ScORE: " + cmg.getCompetitorMatchScore());

                            System.out.println("CZy sa rowne referencyjnie " + (cmg == ccmg));

                            System.out.println("DP SIZE " + bracketCreator.getPanelList().size());
                            System.out.println("CMG SIZE " + dp.getMatch().getCompetitorMatchGroupList().size());
                        }
                    }
                }
            }
        }

        for (CompetitorMatchGroup cmg2 : controller.findCMGByIdMatch(cmg.getIdMatch().getIdMatch())) {
            System.out.println("NR MATCHU i ID COMPETITORA " + cmg2.getIdMatch() + " comp: " + cmg2.getIdCompetitor() + " i wynik " + cmg2.getCompetitorMatchScore() + " IDDD " + cmg2);
        }
    }

    private void refreshPage() {
        
        FacesContext context = FacesContext.getCurrentInstance();
        String viewId = context.getViewRoot().getViewId();
        ViewHandler handler = context.getApplication().getViewHandler();
        UIViewRoot root = handler.createView(context, viewId);
        root.setViewId(viewId);
        context.setViewRoot(root);
        
        System.out.println("ODSEIZYLo STRONE");
    }

    public void saveScore2(CompetitorMatchGroup cmg) {
        System.out.println("Wykonal sie save222 z inputTexta " + cmg.getIdCompetitor());
//        for(Competi)

//        controller.saveCompetitorScore();
    }
//    private void getCMGMappers() { 
//        for (GroupCompetition gc : competition.getGroupCompetitionList()) {
//            for (CompetitorMatchGroup cmg : gc.getIdGroup().getCompetitorMatchGroupList()) {
//                cmgList.add(cmg);
//            }
//        }
//    }
}
