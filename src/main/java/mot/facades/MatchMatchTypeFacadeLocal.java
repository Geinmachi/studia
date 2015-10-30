/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.facades;

import entities.MatchMatchType;
import exceptions.ApplicationException;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;

/**
 *
 * @author java
 */
@Remote
public interface MatchMatchTypeFacadeLocal {

    void create(MatchMatchType matchMatchType) throws ApplicationException;

    void edit(MatchMatchType matchMatchType) throws ApplicationException;

    void remove(MatchMatchType matchMatchType);

    MatchMatchType find(Object id);

    List<MatchMatchType> findAll();

    List<MatchMatchType> findRange(int[] range);

    int count();
    
    MatchMatchType editWithReturn(MatchMatchType matchMatchType) throws ApplicationException;
}
