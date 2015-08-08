/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.facades;

import entities.Groupp;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;

/**
 *
 * @author java
 */
@Remote
public interface GrouppFacadeLocal {

    void create(Groupp groupp);

    void edit(Groupp groupp);

    void remove(Groupp groupp);

    Groupp find(Object id);

    List<Groupp> findAll();

    List<Groupp> findRange(int[] range);

    int count();
    
    Groupp createWithReturn(Groupp entity);
    
}
