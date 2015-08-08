/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.facades;

import entities.CompetitorMatchGroup;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;

/**
 *
 * @author java
 */
@Remote
public interface CompetitorMatchGroupFacadeLocal {

    void create(CompetitorMatchGroup competitorMatchGroup);

    void edit(CompetitorMatchGroup competitorMatchGroup);

    void remove(CompetitorMatchGroup competitorMatchGroup);

    CompetitorMatchGroup find(Object id);

    List<CompetitorMatchGroup> findAll();

    List<CompetitorMatchGroup> findRange(int[] range);

    int count();
    
    CompetitorMatchGroup createWithReturn(CompetitorMatchGroup entity);

    public List<CompetitorMatchGroup> getCompetitionCMGMappingsByCompetitionId(Integer idCompetition);

    public List<CompetitorMatchGroup> findByMatchNumberAndIdCompetition(short matchNumber, int idCompetition);

    public List<CompetitorMatchGroup> findCMGByIdMatch(Integer idMatch);
}
