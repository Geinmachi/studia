/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.facades;

import entities.GroupName;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;

/**
 *
 * @author java
 */
@Remote
public interface GroupNameFacadeLocal {

    void create(GroupName groupName);

    void edit(GroupName groupName);

    void remove(GroupName groupName);

    GroupName find(Object id);

    List<GroupName> findAll();

    List<GroupName> findRange(int[] range);

    int count();

    GroupName createWithReturn(GroupName entity);

}
