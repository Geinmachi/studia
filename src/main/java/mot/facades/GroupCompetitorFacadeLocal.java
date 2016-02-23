/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.facades;

import entities.GroupCompetitor;
import exceptions.ApplicationException;
import java.util.List;
import javax.ejb.Remote;
import javax.ejb.Remote;

/**
 *
 * @author java
 */
@Remote
public interface GroupCompetitorFacadeLocal {

    void create(GroupCompetitor groupCompetitor)  throws ApplicationException;

    void edit(GroupCompetitor groupCompetitor) throws ApplicationException;

    void remove(GroupCompetitor groupCompetitor);

    GroupCompetitor find(Object id);

    List<GroupCompetitor> findAll();

    List<GroupCompetitor> findRange(int[] range);

    int count();

    public List<GroupCompetitor> findByCompetitionId(Integer idCompetition);

}
