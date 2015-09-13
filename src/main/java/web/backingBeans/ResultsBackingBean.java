/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans;

import entities.Competition;
import entities.Competitor;
import entities.Score;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import mot.utils.CMG;
import utils.SortUtil;
import web.controllers.CompetitionController;
import web.utils.CheckUtils;
import web.utils.DisplayPageEnum;

/**
 *
 * @author java
 */
@Named(value = "resultsBackingBean")
@RequestScoped
public class ResultsBackingBean {

    @Inject
    CompetitionController controller;
    
    private Competition competition;
    
    private List<Score> scoreList;
    
    private Map<Competitor,Integer> competitorPositionMap = new HashMap<>();
    
    private Map<Short,Integer> positionScoreMap = new HashMap<>();
            
    public ResultsBackingBean() {
    }

    public Competition getCompetition() {
        return competition;
    }

    public List<Score> getScoreList() {
        return scoreList;
    }

    public Map<Competitor, Integer> getCompetitorPositionMap() {
        return competitorPositionMap;
    }
    
    @PostConstruct
    private void init() {
        competition = controller.getDisplayedCompetition(DisplayPageEnum.RESULTS);
        
        if(CheckUtils.isCompetitionNull(competition)) {
            return;
        }
        
        competitorPositionMap = controller.getCompetitionResults(competition);
    }
}
