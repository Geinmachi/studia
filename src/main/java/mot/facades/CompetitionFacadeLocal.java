/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.facades;

import entities.Competition;
import exceptions.ApplicationException;
import exceptions.CompetitorCreationException;
import java.util.List;
import javax.ejb.Local; import javax.ejb.Remote;

/**
 *
 * @author java
 */
@Remote
public interface CompetitionFacadeLocal {

    void create(Competition competition) throws ApplicationException;

    void edit(Competition competition) throws ApplicationException;

    void remove(Competition competition);

    Competition find(Object id);

    List<Competition> findAll();

    List<Competition> findRange(int[] range);

    int count();

    public List<Competition> findUserCompetitionsByIdAccessLevel(Object id);
        
    Competition createWithReturn(Competition entity) throws ApplicationException;

    public Competition findAndInitializeGD(Integer idCompetition);

    public List<Competition> findUserCompetitions(Integer idAccessLevel);

    public List<Competition> findGlobalCompetitions();

    void competitionContraints(Competition competition) throws CompetitorCreationException;

    void competitionContraintsNotCommited(Competition competition) throws CompetitorCreationException;
}
