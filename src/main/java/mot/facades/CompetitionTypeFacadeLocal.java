/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.facades;

import entities.CompetitionType;
import java.util.List;
import javax.ejb.Local; import javax.ejb.Remote;

/**
 *
 * @author java
 */
@Remote
public interface CompetitionTypeFacadeLocal {

    void create(CompetitionType competitionType);

    void edit(CompetitionType competitionType);

    void remove(CompetitionType competitionType);

    CompetitionType find(Object id);

    List<CompetitionType> findAll();

    List<CompetitionType> findRange(int[] range);

    int count();
    
}
