/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans.mot;

import web.utils.BracketCreation;
import entities.Competition;
import entities.Competitor;
import entities.CompetitorMatch;
import entities.GroupDetails;
import entities.MatchMatchType;
import entities.Matchh;
import exceptions.ApplicationException;
import exceptions.InvalidScoreException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import mot.interfaces.CMG;
import mot.interfaces.InactivateMatch;
import org.primefaces.component.panel.Panel;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.context.RequestContext;
import utils.BracketUtil;
import ejbCommon.TrackerInterceptor;
import web.controllers.CompetitionController;
import web.models.DashboardPanel;
import web.utils.JsfUtils;

/**
 *
 * @author java
 */
@Named(value = "manageCompetitionBackingBean")
@ViewScoped
public class ManageCompetitionBackingBean extends CompetitionBackingBean implements Serializable {

    private static final long serialVersionUID = 7526472295622776147L;

    @Inject
    private BracketCreation bracketCreator;

    private List<GroupDetails> groupDetailsList = new ArrayList<>();

//    private List<CMG> cmgList = new ArrayList<>();
    private Competition competition;

    private short competitorOldScore;

//    private List<CMG> storedCMGmappings;
    /**
     * Creates a new instance of ManageCompetitionBacking
     */
    public ManageCompetitionBackingBean() {
    }

    public BracketCreation getBracketCreator() {
        return bracketCreator;
    }

    public Competition getCompetition() {
        return competition;
    }

    public List<GroupDetails> getGroupDetailsList() {
        return groupDetailsList;
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

        groupDetailsList = new ArrayList<>(competition.getGroupDetailsList());

        Collections.sort(groupDetailsList);

        bracketCreator.recreateBracket(competition);
    }

    public void saveScore(CompetitorMatch cmg, DashboardPanel dp) {
        System.out.println("Wykonal sie save z inpalce " + cmg.getCompetitorMatchScore());
        System.out.println("Zawodnika " + cmg.getIdCompetitor());

        try {
            Map<String, CompetitorMatch> savedMap = controller.saveCompetitorScore(cmg);
            CompetitorMatch savedCompetitorMatch = savedMap.get("saved");

            bracketCreator.updateScores(savedCompetitorMatch);

//            CompetitorMatch advancedCompetitorMatchVersioned = savedMap.get("advancedVersioned");
//
//            if (advancedCompetitorMatchVersioned != null) {
//                bracketCreator.addAdvancedCompetitor(advancedCompetitorMatchVersioned);
//            }

            CompetitorMatch advancedCompetitorMatch = savedMap.get("advanced");

            if (advancedCompetitorMatch != null) {
                //        cmgList.add(advancedCompetitorMatch);
                bracketCreator.addAdvancedCompetitor(advancedCompetitorMatch);

                BracketUtil.makeSerializablePanel(dp);
                bracketCreator.disableMatch(dp);

                JsfUtils.addSuccessMessage("Competitor advanced", "Name: "
                        + advancedCompetitorMatch.getIdCompetitor().getIdPersonalInfo().getFirstName()
                        + " "
                        + advancedCompetitorMatch.getIdCompetitor().getIdPersonalInfo().getLastName(), "manageCompetitionForm");
            }

            System.out.println("Przeszlo all, advanced id = " + advancedCompetitorMatch);
            RequestContext.getCurrentInstance().update(":manageCompetitionForm:dashboard");
            System.out.println("Po odswiezeniu");

        } catch (InvalidScoreException ise) {
            cmg.setCompetitorMatchScore(competitorOldScore);
            bracketCreator.updateScores(cmg);
            JsfUtils.addErrorMessage(ise.getLocalizedMessage(), "", "manageCompetitionForm");
        } catch (ApplicationException ae) {
            System.out.println("ManageCompetitonBackingBean#saceScore exception" + ae.getMessage());
            ae.printStackTrace();
        }

        //       init();
        //    refreshPage();
    }

    public void updateMatchType(DashboardPanel dp) {

        Matchh match = dp.getMatch();

//        System.out.println("PANEL SERAILZIABLE BLAAAADDD" + dp.getPanel());
//        dp.setEditable(inactivateMatch.getEditable());
//        dp.setInplaceEditable(inactivateMatch.isInplaceEditable());
//        
//        System.out.println("DP: " + dp);
//        System.out.println("MATCH IDIDID " + dp.getMatch());
        for (MatchMatchType mmt : match.getMatchMatchTypeList()) {
            if (mmt.getIdMatchType().getMatchTypeName().startsWith("BO")) {
                mmt.setIdMatchType(dp.getMatchType());

                break;
            }
        }

        System.out.println("BEFORE matchType updated BB ");
        match.getMatchMatchTypeList().stream().forEach(p -> System.out.println(" id " + p.getIdMatchMatchType() + " type " + p.getIdMatchType()));
        MatchMatchType updatedMMT = controller.updateMatchType(match);
        System.out.println("AFTER matchType updated BB id " + updatedMMT.getIdMatchMatchType() + " typ " + updatedMMT.getIdMatchType());

        for (MatchMatchType mmt : match.getMatchMatchTypeList()) {
            if (mmt.getIdMatchType().getMatchTypeName().startsWith("BO")) {
                int mmtIndex = match.getMatchMatchTypeList().indexOf(mmt);
                match.getMatchMatchTypeList().set(mmtIndex, updatedMMT);

                break;
            }
        }

        BracketUtil.makeSerializablePanel(dp);

        InactivateMatch inactiveMatch = controller.disableMatch(dp);
//        addAdvancedCompetitor(advancedMatchNumber);
        if (!inactiveMatch.getEditable()) {

            dp.setEditable(inactiveMatch.getEditable());
            dp.setInplaceEditable(inactiveMatch.isInplaceEditable());

            CompetitorMatch advancedCompetitorMatch = controller.advanceCompetitor(BracketUtil.getMatchWinner(dp.getMatch()));

            bracketCreator.addAdvancedCompetitor(advancedCompetitorMatch);
        }

        //       init();
    }

    public void saveOldScore(ValueChangeEvent event) {
        System.out.println("OLD VALUE " + event.getOldValue());
        competitorOldScore = (short) event.getOldValue();
    }

    public void saveGeneralInfo() {
        try {
            controller.saveCompetitionGeneralInfo(competition);
            JsfUtils.addSuccessMessage("Data was successfully saved", null, "manageCompetitionForm");
        } catch (Exception e) {
            System.out.println("ManageCompetitionBackingBean#saveGeneralInfo excpetion " + e.getLocalizedMessage());
            e.printStackTrace();
            JsfUtils.addErrorMessage("ERROR", null, "manageCompetitionForm");
        }
    }
}