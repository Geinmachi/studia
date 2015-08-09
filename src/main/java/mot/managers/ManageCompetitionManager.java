/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.managers;

import entities.AccessLevel;
import entities.Account;
import entities.Competition;
import entities.Competitor;
import entities.CompetitorMatchGroup;
import entities.MatchMatchType;
import entities.Matchh;
import entities.Organizer;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import mot.facades.AccountFacadeLocal;
import mot.facades.CompetitionFacadeLocal;
import mot.facades.CompetitorMatchGroupFacadeLocal;
import mot.facades.MatchhFacadeLocal;
import utils.ConvertUtil;

/**
 *
 * @author java
 */
@Stateless
public class ManageCompetitionManager implements ManageCompetitionManagerLocal {

    @Resource
    private SessionContext sessionContext;

    @EJB
    private AccountFacadeLocal accountFacade;

    @EJB
    private CompetitionFacadeLocal competitionFacade;

    @EJB
    private CompetitorMatchGroupFacadeLocal cmgFacade;

    @EJB
    private MatchhFacadeLocal matchFacade;

    private final String BEST_OF_PREFIX = "BO";

    @Override
    public List<Competition> getLoggedUserCompetition() {
        Account loggedUser = accountFacade.findByLogin(sessionContext.getCallerPrincipal().getName());
        AccessLevel organizer = ConvertUtil.getSpecAccessLevelFromAccount(loggedUser, Organizer.class);

        return competitionFacade.findUserCompetitionsByIdAccessLevel(organizer.getIdAccessLevel());
    }

    @Override
    public Competition storeCompetition(Competition competition) {
        Competition fetchedCompetition = competitionFacade.find(competition.getIdCompetition());

        if (!fetchedCompetition.equals(competition)) { // after page laod and before button click competition was modified
            throw new IllegalStateException("In the meantime competition was modyfied");
        }

        return fetchedCompetition;
    }

    @Override
    public List<CompetitorMatchGroup> getCompetitionCMGMappings(Competition competition) {
        System.out.println("PRZED getCompetitionCMGMappings");
        return cmgFacade.getCompetitionCMGMappingsByCompetitionId(competition.getIdCompetition());
    }

    /**
     *
     * @param editingCompetition
     * @param receivedCMG
     * @return CompetitorMatchGroup if competitor advanced to next round,
     * oterwise returns null
     */
    @Override
    public CompetitorMatchGroup saveCompetitorScore(Competition editingCompetition, CompetitorMatchGroup receivedCMG) {
        CompetitorMatchGroup fetchedCMG = cmgFacade.find(receivedCMG.getIdCompetitorMatchGroup());
        Matchh fetchedMatch = matchFacade.findAndInitializeTypes(fetchedCMG.getIdMatch().getIdMatch());

//        System.out.println("MATCH TPYYYYYY");
//        for (MatchMatchType mt : fetchedMatch.getMatchMatchTypeList()) {
//            System.out.println("TYYP ::: " + mt.getIdMatchType().getMatchTypeName());
//        }
        validateScore(fetchedMatch, receivedCMG);

        fetchedCMG.setCompetitorMatchScore(receivedCMG.getCompetitorMatchScore());

        cmgFacade.edit(fetchedCMG);

        CompetitorMatchGroup advancedCompetitoCMG = advanceCompetitor(editingCompetition, fetchedMatch, receivedCMG, receivedCMG.getIdCompetitor());

        return advancedCompetitoCMG;
    }

    private void validateScore(Matchh match, CompetitorMatchGroup receivedCMG) {
        for (MatchMatchType mmt : match.getMatchMatchTypeList()) {
            if (mmt.getIdMatchType().getMatchTypeName().startsWith(BEST_OF_PREFIX)) {
                int bestOfDigit = Integer.valueOf(mmt.getIdMatchType().getMatchTypeName().substring(2));
                if (receivedCMG.getCompetitorMatchScore() > ((bestOfDigit + 1) / 2)) {
                    throw new IllegalStateException("Too big number");
                } else if (receivedCMG.getCompetitorMatchScore() < 0) {
                    throw new IllegalStateException("Score can't be lower than 0");
                }

                break;
            }
        }
    }

    private CompetitorMatchGroup advanceCompetitor(Competition competition, Matchh fetchedMatch, CompetitorMatchGroup receivedCMG, Competitor competitor) {
        System.out.println("WESLO DO ADVANCE");
        int competitorCount = competitor.getGroupCompetitorList().size() * CreateCompetitionManager.GROUP_SIZE;
        double matchCounter = 0.0;

        for (MatchMatchType mmt : fetchedMatch.getMatchMatchTypeList()) {
            if (mmt.getIdMatchType().getMatchTypeName().startsWith(BEST_OF_PREFIX)) {
                int bestOfDigit = Integer.valueOf(mmt.getIdMatchType().getMatchTypeName().substring(2));

                if (receivedCMG.getCompetitorMatchScore() == ((bestOfDigit + 1) / 2)) {
                    CompetitorMatchGroup advancedCompetitorCMG = new CompetitorMatchGroup();

                    int matchesInRound = matchesInRound(competitorCount / 2, fetchedMatch.getRoundd());
                    System.out.println("MatcchesInRound: " + matchesInRound);

                    short firstMatchIndexInRound = firstMatchIndexInRound(competitorCount / 2, fetchedMatch.getRoundd());
                    System.out.println("FirstMatchIndexInRound: " + firstMatchIndexInRound);

                    for (short i = 0; i <= matchesInRound; i++) {
                        matchCounter += 0.5;
                        if ((i + firstMatchIndexInRound) == fetchedMatch.getMatchNumber()) {
                            System.out.println("Znalazlo match nr " + fetchedMatch.getMatchNumber());
                            List<CompetitorMatchGroup> foundCMGs = cmgFacade.findByMatchNumberAndIdCompetition(((short) (firstMatchIndexInRound + matchesInRound - 1 + Math.ceil(matchCounter))), fetchedMatch.getCompetition().getIdCompetition());
//                            Matchh foundMatch = matchFacade.findByMatchNumberAndIdCompetition(((short) (firstMatchIndexInRound + matchesInRound - 1 + Math.ceil(matchCounter))), fetchedMatch.getCompetition().getIdCompetition());

//                            if (foundMatch == null) {
//                                    System.out.println("Match number " + (firstMatchIndexInRound + matchesInRound - 1 + Math.ceil(matchCounter)));
//                                    System.out.println("Competition id " + fetchedMatch.getCompetition().getIdCompetition());
//                                    throw new IllegalStateException("Ssss");
//                                } else {
//                                    System.out.println("Match number XXXXXX  " + foundMatch.getMatchNumber() + " id " + foundMatch.getIdMatch());
//                                }
//                            if (foundCMG.getIdCompetitor() == null) {// if this is the first competitor in match
                            
                            CompetitorMatchGroup foundCMG = null;
                            
                            for (CompetitorMatchGroup cmg : foundCMGs) {
                                if (cmg.getPlacer() == null) {
                                    foundCMG = cmg;
                                    
                                    break;
                                }
                            }
                            
                            if (foundCMG == null) {
                                throw new IllegalStateException("DID not find emtpy CMG in match");
                            }
                            
                            foundCMG.setIdCompetitor(competitor);
                            foundCMG.setCompetitorMatchScore((short) 0);
                            foundCMG.setPlacer(calculatePlacer(matchCounter));
                            cmgFacade.edit(foundCMG);
                            System.out.println("EDYOWANY foundCMG " + foundCMG.getIdMatch().getMatchNumber());

                            advancedCompetitorCMG = foundCMG;
//                            } else {
//                                advancedCompetitorCMG.setCompetitorMatchScore((short) 0);
//                                advancedCompetitorCMG.setIdCompetitor(competitor);
//                                advancedCompetitorCMG.setIdMatch(foundMatch);
//                                advancedCompetitorCMG.calculatePlacer(calculatePlacer(matchCounter));
//                                cmgFacade.create(advancedCompetitorCMG);
//                            }

                            break;
                        }
                    }

                    return advancedCompetitorCMG;
                }
            }

        }

        return null;
    }

    // 8 2, 
    private short firstMatchIndexInRound(int firstRoundMatches, short round) {
        if (round < 1) {
            throw new IllegalStateException("Round is lower than 1");
        }
        if (round == 1) {
            return 1;
        }

        return (short) (firstMatchIndexInRound(firstRoundMatches / 2, (short) (round - 1)) + firstRoundMatches);
    }

    private int matchesInRound(int firstRoundMatches, short round) {
        if (round < 1) {
            throw new IllegalStateException("Round is lower than 1");
        }
        if (round == 1) {
            return firstRoundMatches;
        }
        return matchesInRound(firstRoundMatches, (short) (round - 1)) / 2;
    }

    private short calculatePlacer(double matchCounter) {
        if (Math.ceil(matchCounter) == matchCounter) { // second placer
            return (short) 2;
        } else {
            return (short) 1;
        }
    }

    @Override
    public List<CompetitorMatchGroup> findCMGByIdMatch(Integer idMatch) {
        List<CompetitorMatchGroup> found = cmgFacade.findCMGByIdMatch(idMatch);

//        for (CompetitorMatchGroup cmg2 : found){
//            System.out.println("NR MATCHU i ID COMPETITORA " + cmg2.getIdMatch() + " comp: " + cmg2.getIdCompetitor() + " i wynik " + cmg.getCompetitorMatchScore() + " IDDD " + cmg);
//        }
        
        return found;
    }
}
