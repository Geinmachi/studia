/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.facades;

import entities.Competition;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author java
 */
@Local
public interface CompetitionFacadeLocal {

    void create(Competition competition);

    void edit(Competition competition);

    void remove(Competition competition);

    Competition find(Object id);

    List<Competition> findAll();

    List<Competition> findRange(int[] range);

    int count();
    
}
