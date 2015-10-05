/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.facades;

import entities.MatchMatchType;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;

/**
 *
 * @author java
 */
@Remote
public interface MatchMatchTypeFacadeLocal {

    void create(MatchMatchType matchMatchType);

    void edit(MatchMatchType matchMatchType);

    void remove(MatchMatchType matchMatchType);

    MatchMatchType find(Object id);

    List<MatchMatchType> findAll();

    List<MatchMatchType> findRange(int[] range);

    int count();
    
    MatchMatchType editWithReturn(MatchMatchType matchMatchType);
}
