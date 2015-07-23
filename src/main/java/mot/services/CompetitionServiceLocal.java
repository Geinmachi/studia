/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.services;

import entities.Competition;
import entities.CompetitionType;
import entities.Competitor;
import entities.CompetitorMatchGroup;
import entities.Team;
import java.util.List;
import javax.ejb.Local; import javax.ejb.Remote;

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
    
    public void createCompetition(Competition competition, List<Competitor> competitors);
    
    public CompetitionType findCompetitionTypeById(int id);
    
    public List<CompetitorMatchGroup> generateEmptyBracket(List<Competitor> competitors);
}
