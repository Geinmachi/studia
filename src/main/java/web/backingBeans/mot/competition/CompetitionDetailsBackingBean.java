/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans.mot.competition;

import web.utils.BracketCreation;
import entities.Competition;
import entities.GroupCompetitor;
import entities.GroupDetails;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import web.utils.CheckUtils;
import web.utils.DisplayPageEnum;
import web.utils.JsfUtils;

/**
 *
 * @author java
 */
@Named(value = "competitionDetailsBackingBean")
@RequestScoped
public class CompetitionDetailsBackingBean extends CompetitionBackingBean {

    @Inject
    private BracketCreation bracketCreator;

    private Competition competition;

    private int matchCount = 0;

    public CompetitionDetailsBackingBean() {
    }

    public Competition getCompetition() {
        return competition;
    }

    public BracketCreation getBracketCreator() {
        return bracketCreator;
    }

    public int getMatchCount() {
        return matchCount;
    }

    @PostConstruct
    private void init() {

        System.out.println("COMpetitinonDetailsBB#init ");

        String encodedCompetitionId = JsfUtils.getCompetitionUrlParameter();
        
        System.out.println("PARAM " + encodedCompetitionId + " IS NULL " + (encodedCompetitionId == null));
        
        if (encodedCompetitionId == null) {
            competition = controller.getDisplayedCompetition(DisplayPageEnum.DETAILS);
        } else {
            competition = controller.getCompetitionByEncodedId(encodedCompetitionId);
        }

        if (CheckUtils.isCompetitionNull(competition)) {
            return;
        }

        for (GroupDetails gd : competition.getGroupDetailsList()) {
            for (GroupCompetitor gc : gd.getGroupCompetitorList()) {
                matchCount++;
            }
        }
        bracketCreator.recreateBracketToDisplay(competition);
    }
}
