/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.managers;

import entities.Competition;
import entities.CompetitorMatchGroup;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;

/**
 *
 * @author java
 */
@Remote
public interface ManageCompetitionManagerLocal {

    public List<Competition> getLoggedUserCompetition();

    public Competition storeCompetition(Competition competition);

    public List<CompetitorMatchGroup> getCompetitionCMGMappings(Competition competition);

    public CompetitorMatchGroup saveCompetitorScore(Competition editingCompetition, CompetitorMatchGroup cmg);

    public List<CompetitorMatchGroup> findCMGByIdMatch(Integer idMatch);
    
}
