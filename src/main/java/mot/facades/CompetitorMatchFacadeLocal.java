/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.facades;

import entities.CompetitorMatch;
import entities.Matchh;
import exceptions.ApplicationException;
import java.util.List;
import java.util.concurrent.Future;
import javax.ejb.Remote;
import javax.ejb.Remote;

/**
 *
 * @author java
 */
@Remote
public interface CompetitorMatchFacadeLocal {

    void create(CompetitorMatch competitorMatchGroup) throws ApplicationException;

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

    public CompetitorMatch editWithReturn(CompetitorMatch entity) throws ApplicationException;

    public Matchh editWithReturnAdvancing(Matchh storedMtch) throws ApplicationException;

    public List<CompetitorMatch> findCompetitorMatchStatistics(int idCompetitor);

    /**
     *
     * @param idCompetitor competitor ID
     * @param limit how many records to return
     * @param offset from which record start returning values
     * @return List of objects: object[0] - score, object[1] - Competitor, obejct[2] - competitionName, object[3] - idMatch
     */
    public List<Object[]> findPartialCompetitorMatchStatistics(int idCompetitor, int limit, int offset);

    public int findCompetitorMatchStatisticsCount(int idCompetitor);
}
