/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.controllers;

import entities.Competition;
import entities.CompetitionType;
import entities.Competitor;
import entities.CompetitorMatch;
import entities.MatchMatchType;
import entities.MatchType;
import entities.Matchh;
import entities.Score;
import entities.Team;
import exceptions.ApplicationException;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;
import mot.interfaces.CMG;
import mot.interfaces.CompetitionPodiumData;
import mot.services.CompetitionServiceLocal;
import mot.interfaces.CurrentMatchType;
import mot.interfaces.InactivateMatch;
import mot.interfaces.ReportPlacementData;
import mot.models.CompetitorMatchesStatisticsMarkerEvent;
import web.backingBeans.async.PollListener;
import web.models.DashboardPanel;
import web.qualifiers.Logging;
import web.utils.DisplayPageEnum;
import web.utils.JsfUtils;

/**
 *
 * @author java
 */
@SessionScoped
@Logging
public class CompetitionController implements Serializable {

    private static final long serialVersionUID = 645645987823402129L;

    @EJB
    private CompetitionServiceLocal service;

    private Competition editingCompetition;

    private Competitor editingCompetitor;

    private Team editingTeam;

    private Competition displayedCompetition;

    private DisplayPageEnum pageType;
    
    public CompetitionController() {
    }

    public Competition getEditingCompetition() {
//        System.out.println("FD SIZEEEE " + editingCompetition.getGroupDetailsList().size());
        return editingCompetition;
    }

    public Competitor getEditingCompetitor() {
        return editingCompetitor;
    }

    public Team getEditingTeam() {
        return editingTeam;
    }

    public Competition getDisplayedCompetition(DisplayPageEnum type) {
        System.out.println("TYPP pram " + type + " zmienna " + pageType);
        if (type.equals(pageType)) {
            return service.getInitializedCompetition(displayedCompetition.getIdCompetition());
        } else {
            return null;
        }
    }

    public void setDisplayedCompetition(Competition competition, DisplayPageEnum type) {
        this.displayedCompetition = competition;
        this.pageType = type;
    }

    public DisplayPageEnum getPageType() {
        return pageType;
    }

    public void setPageType(DisplayPageEnum pageType) {
        this.pageType = pageType;
    }

    public List<Team> findUserAllowedTeams() throws ApplicationException {
        return service.findUserAllowedTeams();
    }

    public void addCompetitor(Competitor competitor, boolean global) throws ApplicationException {
        service.addCompetitor(competitor, global);
    }

    public List<Competitor> getAllCompetitors() {
        return service.getAllCompetitors();
    }

    public List<CompetitionType> getAllCompetitionTypes() {
        return service.getAllCompetitionTypes();
    }

    public Competitor findCompetitorById(Integer id) {
        return service.findCopetitorById(id);
    }

    public boolean validateCompetitorsAmount(int amount) {
        return service.validateCompetitorsAmount(amount);
    }

    public void createCompetition(Competition competition, List<CMG> competitorMatchGroupList) throws ApplicationException {
        service.createCompetition(competition, competitorMatchGroupList);
    }

    public CompetitionType findCompetitionTypeById(int id) {
        return service.findCompetitionTypeById(id);
    }

    public List<CMG> generateEmptyBracket(List<Competitor> competitors) {
        return service.generateEmptyBracket(competitors);
    }

    public List<MatchType> getEndUserMatchTypes() {
        return service.getEndUserMatchTypes();
    }

    public List<Competition> getLoggedUserCompetitions() throws ApplicationException {
        return service.getLoggedUserCompetitions();
    }

    public void storeCompetition(Competition competition) {
        editingCompetition = service.storeCompetition(competition);
    }

    public List<CMG> getCompetitionCMGMappings(Competition competition) {
        return service.getCompetitionCMGMappings(competition);
    }

    public Map<String, CompetitorMatch> saveCompetitorScore(CompetitorMatch cmg) throws ApplicationException {
        return service.saveCompetitorScore(cmg);
    }

    public List<CompetitorMatch> findCompetitorMatchByIdMatch(Integer idMatch) {
        return service.findCompeitorMatchByIdMatch(idMatch);
    }

    public List<Score> findCompetitionScores(Competition competition) {
        return service.findCompetitionScores(competition.getIdCompetition());
    }

    public Map<Competitor, Integer> getCompetitionResults(Competition competition) {
        return service.getCompetitionResults(competition.getIdCompetition());
    }

    public MatchMatchType updateMatchType(Matchh match) throws ApplicationException {
        return service.updateMatchType(match);
    }

    public InactivateMatch disableMatch(InactivateMatch inactivateMatch) {
        InactivateMatch dto = new DashboardPanel();
        dto.setMatch(inactivateMatch.getMatch());

        return service.disableMatch(dto);
    }

    public CurrentMatchType assignCurrentMatchType(CurrentMatchType cmt) {
        CurrentMatchType dto = new DashboardPanel();
        dto.setMatch(cmt.getMatch());
        dto.setMatchType(cmt.getMatchType());

        return service.assignCurrentMatchType(dto);
    }

    public CompetitorMatch advanceCompetitor(CompetitorMatch competitorMatch) throws ApplicationException {
        return service.advanceCompetitor(competitorMatch);
    }

    public void saveCompetitionGeneralInfo(Competition competition) throws ApplicationException {
        editingCompetition = service.saveCompetitionGeneralInfo(competition);
    }

    public void createTeam(Team team, boolean global) throws ApplicationException {
        service.createTeam(team, global);
    }

    public List<Competitor> getAllAllowedTeamlessCompetitors() throws ApplicationException {
        return service.getAllAllowedTeamlessCompetitors();
    }

    public Competitor validateCompetitorDuplicate(List<Competitor> competitorList) {
        return service.validateCompetitorDuplicate(competitorList);
    }

    public List<Competitor> getCompetitorsToEdit() throws ApplicationException {
        return service.getCompetitorsToEdit();
    }

    public void storeCompetitor(Competitor competitor) {
        editingCompetitor = service.storeCompetitor(competitor);
    }

    public void editCompetitor(Competitor competitor) throws ApplicationException {
        service.editCompetitor(competitor);
        editingCompetitor = null;
    }

    public List<Team> getTeamsToEdit() throws ApplicationException {
        return service.getTeamsToEdit();
    }

    public void storeTeam(Team team) {
        editingTeam = service.storeTeam(team);
    }

    public void editTeam(Team team) throws ApplicationException {
        service.editTeam(team);
        editingTeam = null;
    }

    public List<Competition> findGlobalCompetitions() {
        return service.findGlobalCompetition();
    }

    public List<Competition> findAllowedCompetitions() throws ApplicationException {
        return service.findAllowedCompetitions();
    }

    public void checkCompetitionConstraints(Competition competition) throws ApplicationException {
        service.checkCompetitionConstraints(competition);
    }

    public List<Competition> findCompetitionsToDisplay() throws ApplicationException {
        return service.findCompetitionsToDisplay();
    }

    public Competition getCompetitionByEncodedId(String encodedId) {
        return service.getCompetitionByEncodedId(encodedId);
    }

    public String encodeCompetitionId(int competitionId) {
        return service.encodeCompetitionId(competitionId);
    }

    public List<Competitor> getGlobalCompetitors() throws ApplicationException {
        return service.getGlobalCompetitors();
    }

    public ReportPlacementData getReportPlacements(Competitor competitor) throws ApplicationException {
        return service.getReportPlacements(competitor);
    }

    public List<? extends CompetitionPodiumData> generateCompetitionPodiumStatistics() throws ApplicationException {
        return service.generateCompetitionPodiumStatistics();
    }

    public Future<List<CompetitorMatch>> generateCompetitorMatchesStatistics(Competitor competitor) {
//        Future<List<CompetitorMatch>> competitorMatchesStatistics = ;
//        pollListener.setIncomingData(true);
        
        return service.generateCompetitorMatchesStatistics(competitor);
    }

    public Future<String> asyncTest() {
        return service.asyncTest();
    }
}
