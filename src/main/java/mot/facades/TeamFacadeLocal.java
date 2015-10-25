/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.facades;

import entities.AccessLevel;
import entities.Team;
import exceptions.ApplicationException;
import exceptions.TeamCreationException;
import java.util.List;
import javax.ejb.Local; import javax.ejb.Remote;

/**
 *
 * @author java
 */
@Remote
public interface TeamFacadeLocal {

    void create(Team team) throws ApplicationException;

    void edit(Team team) throws ApplicationException;

    void remove(Team team);

    Team find(Object id);

    List<Team> findAll();

    List<Team> findRange(int[] range);

    int count();

    public Team createWithReturn(Team team) throws ApplicationException;

    public List<Team> findUserTeams(AccessLevel accessLevel);

    public Team findAndInitializeCompetitors(Integer idTeam);
    
    void teamContraints(Team team) throws TeamCreationException;

    public List<Team> findAllAllowed(int idAccessLevel);
    
}
