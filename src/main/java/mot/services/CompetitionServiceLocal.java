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
import javax.ejb.Local; import javax.ejb.Remote;
import mot.interfaces.CMG;
import mot.interfaces.CurrentMatchType;
import mot.interfaces.InactivateMatch;

/**
 *
 * @author java
 */
@Remote
public interface CompetitionServiceLocal {
    
    public List<Team> findAllTeams();
    
    public void addCompetitor(Competitor competitor);
    
    public List<Competitor> getAllCompetitors();
    
    public List<CompetitionType> getAllCompetitionTypes();
    
    public Competitor findCopetitorById(Integer id);
    
    public boolean validateCompetitorsAmount(int amount);
    
    public void createCompetition(Competition competition, List<CMG> competitorMatchGroupList);
    
    public CompetitionType findCompetitionTypeById(int id);
    
    public List<CMG> generateEmptyBracket(List<Competitor> competitors);

    public List<MatchType> getEndUserMatchTypes();

    public List<Competition> getLoggedUserCompetitions();

    public Competition storeCompetition(Competition competition);

    public List<CMG> getCompetitionCMGMappings(Competition competition);

    public Map<String, CompetitorMatch> saveCompetitorScore(CompetitorMatch cmg)  throws ApplicationException;

    public List<CompetitorMatch> findCompeitorMatchByIdMatch(Integer idMatch);
    
    public List<Competition> findAllCompetitions();

    public Competition getInitializedCompetition(int idCompetition);

    public List<Score> findCompetitionScores(int idCompetition);

    public Map<Competitor, Integer> getCompetitionResults(Integer idCompetition);

    public MatchMatchType updateMatchType(Matchh match);

    public InactivateMatch disableMatch(InactivateMatch inactivateMatch);

    public CurrentMatchType assignCurrentMatchType(CurrentMatchType cmt);
    
    public CompetitorMatch advanceCompetitor(CompetitorMatch competitorMatch);

    public Competition saveCompetitionGeneralInfo(Competition competition);

    public void createTeam(Team team);
}
