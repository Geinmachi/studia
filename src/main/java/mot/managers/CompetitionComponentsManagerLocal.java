/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.managers;

import entities.Competitor;
import entities.Team;
import exceptions.ApplicationException;
import java.util.List;
import javax.ejb.Remote;
import javax.ejb.SessionContext;

/**
 *
 * @author java
 */
@Remote
public interface CompetitionComponentsManagerLocal {
    
    public void createTeam(Team team, boolean global) throws ApplicationException;

    public List<Competitor> getAllTeamlessCompetitors();

    public void addCompetitor(Competitor competitor, boolean global) throws ApplicationException;

//    public boolean checkCompetitorDuplicate(Competitor competitor, List<Competitor> competitorList);
    
    public Competitor vlidateCompetitorDuplicate(List<Competitor> competitorList);

    public List<Competitor> getCompetitorsToEdit();

    public Competitor findCompetitorById(int idCompetitor);

    public void editCompetitor(Competitor editingCompetitor, Competitor competitor);

    public List<Team> getTeamsToEdit();

    public Team findTeamById(Integer idTeam);

    public void editTeam(final Team editingTeam, Team team);
}
