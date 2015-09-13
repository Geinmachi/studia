/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.facades;

import entities.Score;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;

/**
 *
 * @author java
 */
@Remote
public interface ScoreFacadeLocal {

    void create(Score score);

    void edit(Score score);

    void remove(Score score);

    Score find(Object id);

    List<Score> findAll();

    List<Score> findRange(int[] range);

    int count();

    public Score findByIdCompetitorAndIdCompetition(Integer idCompetition, Integer idCompetitor);

    public List<Score> findScoreByIdCompetition(int idCompetition);
    
}
