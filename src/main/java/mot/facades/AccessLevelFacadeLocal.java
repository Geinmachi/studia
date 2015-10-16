/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.facades;

import entities.AccessLevel;
import exceptions.ApplicationException;
import java.util.List;
import javax.ejb.Local; import javax.ejb.Remote;

/**
 *
 * @author java
 */
@Remote
public interface AccessLevelFacadeLocal {

    void create(AccessLevel accessLevel) throws ApplicationException;

    void edit(AccessLevel accessLevel) throws ApplicationException;

    void remove(AccessLevel accessLevel);

    AccessLevel find(Object id);

    List<AccessLevel> findAll();

    List<AccessLevel> findRange(int[] range);

    int count();
    
}
