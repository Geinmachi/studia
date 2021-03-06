/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.facades;

import entities.MatchType;
import exceptions.ApplicationException;
import java.util.List;
import javax.ejb.Remote;
import javax.ejb.Remote;

/**
 *
 * @author java
 */
@Remote
public interface MatchTypeFacadeLocal {

    void create(MatchType matchType) throws ApplicationException;

    void edit(MatchType matchType) throws ApplicationException;

    void remove(MatchType matchType);

    MatchType find(Object id);

    List<MatchType> findAll();

    List<MatchType> findRange(int[] range);

    int count();
    
    List<MatchType> findUserSettableMatchTypes();
    
    MatchType findByMatchTypeName(String name);

    public MatchType findAutoAdvanceType();

    public List<MatchType> findEndUserMatchTypes();
}
