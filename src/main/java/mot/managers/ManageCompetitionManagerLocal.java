/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.managers;

import entities.Competition;
import entities.CompetitorMatch;
import entities.MatchMatchType;
import entities.Matchh;
import exceptions.ApplicationException;
import java.util.List;
import java.util.Map;
import javax.ejb.Local;
import javax.ejb.Remote;
import mot.interfaces.CMG;
import mot.interfaces.CurrentMatchType;
import mot.interfaces.InactivateMatch;

/**
 *
 * @author java
 */
@Remote
public interface ManageCompetitionManagerLocal {

    public List<Competition> getLoggedUserCompetition() throws ApplicationException;

    public Competition storeCompetition(Competition competition);

    public List<CMG> getCompetitionCMGMappings(Competition competition);

    public Map<String,CompetitorMatch> saveCompetitorScore(CompetitorMatch cmg, List<CMG> storedCMGmappings)  throws ApplicationException;

    public List<CompetitorMatch> findCMGByIdMatch(Integer idMatch);

    public MatchMatchType updateMatchType(Matchh match, List<CMG> storedCMGmappings) throws ApplicationException;

    public InactivateMatch disableMatch(InactivateMatch inactivateMatch);

    public CurrentMatchType assignCurrentMatchType(CurrentMatchType cmt);
    
    CompetitorMatch advanceCompetitor(CompetitorMatch receivedCompetitorMatch) throws ApplicationException;

    public Competition saveCompetitionGeneralInfo(Competition competition, Competition storedCompetition) throws ApplicationException;
}
