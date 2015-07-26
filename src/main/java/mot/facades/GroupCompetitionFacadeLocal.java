/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.facades;

import entities.GroupCompetition;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;

/**
 *
 * @author java
 */
@Remote
public interface GroupCompetitionFacadeLocal {

    void create(GroupCompetition groupCompetition);

    void edit(GroupCompetition groupCompetition);

    void remove(GroupCompetition groupCompetition);

    GroupCompetition find(Object id);

    List<GroupCompetition> findAll();

    List<GroupCompetition> findRange(int[] range);

    int count();
    
}
