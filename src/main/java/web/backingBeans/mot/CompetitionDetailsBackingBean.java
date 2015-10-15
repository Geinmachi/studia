/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans.mot;

import web.utils.BracketCreation;
import entities.Competition;
import entities.Competitor;
import entities.GroupCompetitor;
import entities.GroupDetails;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import mot.interfaces.CMG;
import web.controllers.CompetitionController;
import web.models.DashboardPanel;
import web.utils.CheckUtils;
import web.utils.DisplayPageEnum;
import web.utils.JsfUtils;

/**
 *
 * @author java
 */
@Named(value = "competitionDetailsBackingBean")
@RequestScoped
public class CompetitionDetailsBackingBean  extends CompetitionBackingBean {
    
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
        competition = controller.getDisplayedCompetition(DisplayPageEnum.DETAILS);
        
        if (CheckUtils.isCompetitionNull(competition)) {
            return;
        }
        
        for (GroupDetails gd : competition.getGroupDetailsList()) {
            for (GroupCompetitor gc : gd.getGroupCompetitorList()) {
                matchCount++;
            }
        }
        bracketCreator.recreateBracket(competition);
    }
    
}
