/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.facades;

import entities.GroupDetails;
import exceptions.ApplicationException;
import java.util.List;
import javax.ejb.Remote;
import javax.ejb.Remote;

/**
 *
 * @author java
 */
@Remote
public interface GroupDetailsFacadeLocal {

    void create(GroupDetails groupDetails) throws ApplicationException;

    void edit(GroupDetails groupDetails) throws ApplicationException;

    void remove(GroupDetails groupDetails);

    GroupDetails find(Object id);

    List<GroupDetails> findAll();

    List<GroupDetails> findRange(int[] range);

    int count();

    public GroupDetails createWithReturn(GroupDetails gc);
    
}
