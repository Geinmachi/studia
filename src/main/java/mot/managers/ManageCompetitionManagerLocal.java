/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.managers;

import entities.Competition;
import entities.CompetitorMatch;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import mot.utils.CMG;

/**
 *
 * @author java
 */
@Remote
public interface ManageCompetitionManagerLocal {

    public List<Competition> getLoggedUserCompetition();

    public Competition storeCompetition(Competition competition);

    public List<CMG> getCompetitionCMGMappings(Competition competition);

    public CompetitorMatch saveCompetitorScore(CompetitorMatch cmg);

    public List<CompetitorMatch> findCMGByIdMatch(Integer idMatch);
    
}
