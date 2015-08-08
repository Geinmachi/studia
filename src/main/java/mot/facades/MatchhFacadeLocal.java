/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.facades;

import entities.Matchh;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;

/**
 *
 * @author java
 */
@Remote
public interface MatchhFacadeLocal {

    void create(Matchh matchh);

    void edit(Matchh matchh);

    void remove(Matchh matchh);

    Matchh find(Object id);

    List<Matchh> findAll();

    List<Matchh> findRange(int[] range);

    int count();
 
    Matchh createWithReturn(Matchh entity);
    
    Matchh findAndInitializeTypes(Object id);

    public Matchh findByMatchNumberAndIdCompetition(short matchNumber, int idCompetition);
}
