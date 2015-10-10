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

/**
 *
 * @author java
 */
@Remote
public interface CompetitionComponentsManagerLocal {
    
    public void createTeam(Team team);

    public List<Competitor> getAllTeamlessCompetitors();

    public void addCompetitor(Competitor competitor, boolean global) throws ApplicationException;
}
