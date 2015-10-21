/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.managers;

import entities.Competition;
import entities.Competitor;
import entities.Score;
import exceptions.ApplicationException;
import java.util.List;
import java.util.Map;
import javax.ejb.Local;
import javax.ejb.Remote;

/**
 *
 * @author java
 */
@Remote
public interface PresentCompetitionManagerLocal {
    
    List<Competition> findAllowedCompetitions() throws ApplicationException;

    public Competition getInitializedCompetition(int idCompetition);

    public List<Score> findCompetitionScore(int idCompetition);

    public Map<Competitor, Integer> getCompetitionResults(int idCompetition);

    public List<Competition> findGlobalCompetitions();

    public List<Competition> findCompetitionsToDisplay() throws ApplicationException;

}
