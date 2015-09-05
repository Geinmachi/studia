/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.facades;

import entities.MatchType;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;

/**
 *
 * @author java
 */
@Remote
public interface MatchTypeFacadeLocal {

    void create(MatchType matchType);

    void edit(MatchType matchType);

    void remove(MatchType matchType);

    MatchType find(Object id);

    List<MatchType> findAll();

    List<MatchType> findRange(int[] range);

    int count();
    
    List<MatchType> findEndUserMatchTypes();
    
    MatchType findByMatchTypeName(String name);
}
