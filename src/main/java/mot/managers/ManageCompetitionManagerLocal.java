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
import javax.ejb.Remote;
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

    List<Competition> getLoggedUserCompetition() throws ApplicationException;

    Competition storeCompetition(Competition competition);

    List<CMG> getCompetitionCMGMappings(Competition competition);

    Map<String,CompetitorMatch> saveCompetitorScore(CompetitorMatch cmg, List<CMG> storedCMGmappings)  throws ApplicationException;

    List<CompetitorMatch> findCMGByIdMatch(Integer idMatch);

    MatchMatchType updateMatchType(Matchh match, List<CMG> storedCMGmappings) throws ApplicationException;

    InactivateMatch disableMatch(InactivateMatch inactivateMatch);

    CurrentMatchType assignCurrentMatchType(CurrentMatchType cmt);
    
    CompetitorMatch advanceCompetitor(CompetitorMatch receivedCompetitorMatch) throws ApplicationException;
    
    Competition saveCompetitionGeneralInfo(Competition competition, Competition storedCompetition) throws ApplicationException;
}
