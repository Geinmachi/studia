/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.services;

import entities.Competition;
import entities.CompetitionType;
import entities.Competitor;
import entities.CompetitorMatch;
import entities.MatchType;
import entities.Matchh;
import entities.Score;
import entities.Team;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import mot.interfaces.CMG;
import mot.facades.CompetitionTypeFacadeLocal;
import mot.facades.CompetitorFacadeLocal;
import mot.facades.MatchTypeFacadeLocal;
import mot.facades.TeamFacadeLocal;
import mot.managers.CreateCompetitionManagerLocal;
import mot.managers.ManageCompetitionManagerLocal;
import mot.managers.PresentCompetitionManagerLocal;
import mot.interfaces.CurrentMatchType;
import mot.interfaces.InactivateMatch;

/**
 *
 * @author java
 */
@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class CompetitionService implements CompetitionServiceLocal {

    @EJB
    private CreateCompetitionManagerLocal createCompetitionManager;
    
    @EJB
    private ManageCompetitionManagerLocal manageCompetitionManager;
    
    @EJB
    private PresentCompetitionManagerLocal presentCompetitionManager;
    
    @EJB
    private TeamFacadeLocal teamFacade;

    @EJB
    private CompetitorFacadeLocal competitorFacade;
    
    @EJB
    private CompetitionTypeFacadeLocal competitionTypeFacade;
    
    @EJB
    private MatchTypeFacadeLocal matchTypeFacade;
    
    private Competition editingCompetition;

    @Override
    public List<Team> findAllTeams() {
        return teamFacade.findAll();
    }

    @Override
    public void addCompetitor(Competitor competitor) {
        competitorFacade.create(competitor);
    }

    @Override
    public List<Competitor> getAllCompetitors() {
//        return new ArrayList<Competitor>(){{
//           add(new Competitor());     
//        }};
        return competitorFacade.findAll();
    }

    @Override
    public List<CompetitionType> getAllCompetitionTypes() {
        return competitionTypeFacade.findAll();
    }

    @Override
    public Competitor findCopetitorById(Integer id) {
        return competitorFacade.find(id);
    }

    @Override
    public boolean validateCompetitorsAmount(int amount) {
        return createCompetitionManager.validateCompetitorsAmount(amount);
    }

    @Override
    public void createCompetition(Competition competition, List<CMG> competitorMatchGroupList) {
        createCompetitionManager.createCompetition(competition, competitorMatchGroupList);
    }

    @Override
    public CompetitionType findCompetitionTypeById(int id) {
        return competitionTypeFacade.find(id);
    }

    @Override
    public List<CMG> generateEmptyBracket(List<Competitor> competitors) {
        return createCompetitionManager.generateEmptyBracket(competitors);
    }

    @Override
    public List<MatchType> getEndUserMatchTypes() {
        return matchTypeFacade.findEndUserMatchTypes();
    }

    @Override
    public List<Competition> getLoggedUserCompetitions() {
        return manageCompetitionManager.getLoggedUserCompetition();
    }

    @Override
    public Competition storeCompetition(Competition competition) {
        editingCompetition = manageCompetitionManager.storeCompetition(competition);
        return editingCompetition;
    }

    @Override
    public List<CMG> getCompetitionCMGMappings(Competition competition) {
        return manageCompetitionManager.getCompetitionCMGMappings(competition);
    }

    @Override
    public CompetitorMatch saveCompetitorScore(CompetitorMatch cmg) {
        return manageCompetitionManager.saveCompetitorScore(cmg);
    }

    @Override
    public List<CompetitorMatch> findCompeitorMatchByIdMatch(Integer idMatch) {
        return manageCompetitionManager.findCMGByIdMatch(idMatch);
    }

    @Override
    public List<Competition> findAllCompetitions() {
        return presentCompetitionManager.findAllCompetitions();
    }

    @Override
    public Competition getInitializedCompetition(int idCompetition) {
        return presentCompetitionManager.getInitializedCompetition(idCompetition);
    }

    @Override
    public List<Score> findCompetitionScores(int idCompetition) {
        return presentCompetitionManager.findCompetitionScore(idCompetition);
    }

    @Override
    public Map<Competitor, Integer> getCompetitionResults(Integer idCompetition) {
        return presentCompetitionManager.getCompetitionResults(idCompetition);
    }

    @Override
    public void updateMatchType(Matchh match) {
        manageCompetitionManager.updateMatchType(match);
    }

    @Override
    public InactivateMatch disableMatch(InactivateMatch inactivateMatch) {
        return manageCompetitionManager.disableMatch(inactivateMatch);
    }

    @Override
    public CurrentMatchType assignCurrentMatchType(CurrentMatchType cmt) {
        return manageCompetitionManager.assignCurrentMatchType(cmt);
    }
}
