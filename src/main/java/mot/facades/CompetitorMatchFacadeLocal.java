/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.facades;

import entities.CompetitorMatch;
import entities.Matchh;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;

/**
 *
 * @author java
 */
@Remote
public interface CompetitorMatchFacadeLocal {

    void create(CompetitorMatch competitorMatchGroup);

    void edit(CompetitorMatch competitorMatchGroup);

    void remove(CompetitorMatch competitorMatchGroup);

    CompetitorMatch find(Object id);

    List<CompetitorMatch> findAll();

    List<CompetitorMatch> findRange(int[] range);

    int count();
    
    CompetitorMatch createWithReturn(CompetitorMatch entity);

    public List<CompetitorMatch> getCompetitionCMGMappingsByCompetitionId(Integer idCompetition);

    public List<CompetitorMatch> findByMatchNumberAndIdCompetition(short matchNumber, int idCompetition);

    public List<CompetitorMatch> findCMGByIdMatch(Integer idMatch);

    public List<CompetitorMatch> findByCompetitionId(Integer idCompetition);

    public List<CompetitorMatch> findByIdMatch(Integer idMatch);
    
    public CompetitorMatch editWithReturn(CompetitorMatch entity);
    
    public Matchh editWithReturnAdvancing(Matchh storedMtch);
}
