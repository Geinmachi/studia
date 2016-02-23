/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.facades;

import entities.Matchh;
import exceptions.ApplicationException;
import java.util.List;
import javax.ejb.Remote;
import javax.ejb.Remote;

/**
 *
 * @author java
 */
@Remote
public interface MatchhFacadeLocal {

    void create(Matchh matchh) throws ApplicationException;

    void edit(Matchh matchh) throws ApplicationException;

    void remove(Matchh matchh);

    Matchh find(Object id);

    List<Matchh> findAll();

    List<Matchh> findRange(int[] range);

    int count();
 
    Matchh createWithReturn(Matchh entity);
    
    Matchh findAndInitializeTypes(Object id);

    public Matchh findByMatchNumberAndIdCompetition(short matchNumber, int idCompetition);
}
