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
import entities.MatchMatchType;
import entities.MatchType;
import entities.Matchh;
import entities.Score;
import entities.Team;
import exceptions.ApplicationException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import mot.interfaces.CMG;
import mot.interfaces.CompetitionPodiumData;
import mot.interfaces.CurrentMatchType;
import mot.interfaces.InactivateMatch;
import mot.interfaces.ReportPlacementData;
import mot.models.CompetitorMatchesStatisticsMarkerEvent;

/**
 *
 * @author java
 */
@Remote
public interface CompetitionServiceLocal {

    public List<Team> findUserAllowedTeams() throws ApplicationException;

    public void addCompetitor(Competitor competitor, boolean global) throws ApplicationException;

    public List<Competitor> getAllCompetitors();

    public List<CompetitionType> getAllCompetitionTypes();

    public Competitor findCopetitorById(Integer id);

    public boolean validateCompetitorsAmount(int amount);

    public void createCompetition(Competition competition, List<CMG> competitorMatchGroupList) throws ApplicationException;

    public CompetitionType findCompetitionTypeById(int id);

    public List<CMG> generateEmptyBracket(List<Competitor> competitors);

    public List<MatchType> getEndUserMatchTypes();

    public List<Competition> getLoggedUserCompetitions() throws ApplicationException;

    public Competition storeCompetition(Competition competition);

    public List<CMG> getCompetitionCMGMappings(Competition competition);

    public Map<String, CompetitorMatch> saveCompetitorScore(CompetitorMatch cmg) throws ApplicationException;

    public List<CompetitorMatch> findCompeitorMatchByIdMatch(Integer idMatch);

    public Competition getInitializedCompetition(int idCompetition);

    public List<Score> findCompetitionScores(int idCompetition);

    public Map<Competitor, Integer> getCompetitionResults(Integer idCompetition);

    public MatchMatchType updateMatchType(Matchh match) throws ApplicationException;

    public InactivateMatch disableMatch(InactivateMatch inactivateMatch);

    public CurrentMatchType assignCurrentMatchType(CurrentMatchType cmt);

    public CompetitorMatch advanceCompetitor(CompetitorMatch competitorMatch) throws ApplicationException;

    public Competition saveCompetitionGeneralInfo(Competition competition) throws ApplicationException;

    public void createTeam(Team team, boolean global) throws ApplicationException;

    public List<Competitor> getAllAllowedTeamlessCompetitors() throws ApplicationException;

    public Competitor validateCompetitorDuplicate(List<Competitor> competitorList);

    public List<Competitor> getCompetitorsToEdit() throws ApplicationException;

    public Competitor storeCompetitor(Competitor competitor);

    public void editCompetitor(Competitor competitor) throws ApplicationException;

    public List<Team> getTeamsToEdit() throws ApplicationException;

    public Team storeTeam(Team team);

    public void editTeam(Team team) throws ApplicationException;

    public List<Competition> findGlobalCompetition();

    public List<Competition> findAllowedCompetitions() throws ApplicationException;

    public void checkCompetitionConstraints(Competition competition) throws ApplicationException;

    public List<Competition> findCompetitionsToDisplay() throws ApplicationException;

    public Competition getCompetitionByEncodedId(String encodedId);

    public String encodeCompetitionId(int competitionId);

    public List<Competitor> getGlobalCompetitors() throws ApplicationException;

    public ReportPlacementData getReportPlacements(Competitor competitor) throws ApplicationException;

    public List<? extends CompetitionPodiumData> generateCompetitionPodiumStatistics() throws ApplicationException;

    public Future<List<CompetitorMatch>> generateCompetitorMatchesStatistics(Competitor competitor);

    public boolean isCompetitorMatchesStatisticsFetched();

    public Future<String> asyncTest();

}
