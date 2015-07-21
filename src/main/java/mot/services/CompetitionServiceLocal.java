/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.services;

import entities.CompetitionType;
import entities.Competitor;
import entities.Team;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author java
 */
@Local
public interface CompetitionServiceLocal {
    
    public List<Team> findAllTeams();
    
    public void addCompetitor(Competitor competitor);
    
    public List<Competitor> getAllCompetitors();
    
    public List<CompetitionType> getAllCompetitionTypes();
    
    public Competitor findCopetitorById(Integer id);
}
