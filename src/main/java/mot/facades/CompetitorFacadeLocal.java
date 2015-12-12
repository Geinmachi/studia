/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.facades;

import entities.AccessLevel;
import entities.Competitor;
import exceptions.ApplicationException;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;

/**
 *
 * @author java
 */
@Remote
public interface CompetitorFacadeLocal {

    void create(Competitor competitor) throws ApplicationException;

    void edit(Competitor competitor) throws ApplicationException;

    void remove(Competitor competitor);

    Competitor find(Object id);

    List<Competitor> findAll();

    List<Competitor> findRange(int[] range);

    int count();

    public Competitor findAndInitializeGroups(Integer idCompetitor);

    public Competitor editWithReturn(Competitor competitor);

    public List<Competitor> findAllTeamless();

    public void competitorConstraints(Competitor competitor) throws ApplicationException;

    public List<Competitor> findUserCompetitors(AccessLevel accessLevel);

    public Competitor findCompetitorById(int idCompetitor);

    public List<Competitor> findAllAllowedTeamless(int idAccessLevel);

    public List<Competitor> findGlobalCompetitors();

}
