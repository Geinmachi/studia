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
import entities.CompetitorMatch;
import entities.GroupCompetitor;
import entities.MatchMatchType;
import entities.Matchh;
import entities.Organizer;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import mot.facades.AccountFacadeLocal;
import mot.facades.CompetitionFacadeLocal;
import mot.facades.CompetitorMatchFacadeLocal;
import mot.facades.GroupCompetitorFacadeLocal;
import mot.facades.MatchhFacadeLocal;
import mot.utils.CMG;
import mot.utils.CompetitorMatchGroup;
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
    private CompetitorMatchFacadeLocal cmgFacade;

    @EJB
    private MatchhFacadeLocal matchFacade;
    
    @EJB
    private GroupCompetitorFacadeLocal groupCompetitorFacade;
    
    @EJB
    private CompetitorMatchFacadeLocal competitorMatchFacade;

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
    public List<CMG> getCompetitionCMGMappings(Competition competition) {
        System.out.println("PRZED getCompetitionCMGMappings");
        List<GroupCompetitor> groupCompetitorList = groupCompetitorFacade.findByCompetitionId(competition.getIdCompetition());
        
        List<CompetitorMatch> competitorMatchList = competitorMatchFacade.findByCompetitionId(competition.getIdCompetition());
        
        List<CMG> cmgList = new ArrayList<>();
        
        for (CompetitorMatch cm : competitorMatchList) {
            if (Short.compare(cm.getIdMatch().getRoundd(), Short.parseShort("1")) == 0) {
                for (GroupCompetitor gc : groupCompetitorList) {
                    if (gc.getIdCompetitor().equals(cm.getIdCompetitor())) {
                        cmgList.add(new CompetitorMatchGroup(gc, cm));
                        
                        break;
                    }
                }
            }
        }
        return cmgList;
    }

    /**
     *
     * @param editingCompetition
     * @param receivedCMG
     * @return CompetitorMatch if competitor advanced to next round,
 oterwise returns null
     */
    @Override
    public CompetitorMatch saveCompetitorScore(Competition editingCompetition, CompetitorMatch receivedCMG) {
        CompetitorMatch fetchedCMG = cmgFacade.find(receivedCMG.getIdCompetitorMatch());
        Matchh fetchedMatch = matchFacade.findAndInitializeTypes(fetchedCMG.getIdMatch().getIdMatch());

//        System.out.println("MATCH TPYYYYYY");
//        for (MatchMatchType mt : fetchedMatch.getMatchMatchTypeList()) {
//            System.out.println("TYYP ::: " + mt.getIdMatchType().getMatchTypeName());
//        }
        validateScore(fetchedMatch, receivedCMG);

        fetchedCMG.setCompetitorMatchScore(receivedCMG.getCompetitorMatchScore());

        cmgFacade.edit(fetchedCMG);

        CompetitorMatch advancedCompetitoCMG = advanceCompetitor(editingCompetition, fetchedMatch, receivedCMG, receivedCMG.getIdCompetitor());

        return advancedCompetitoCMG;
    }

    private void validateScore(Matchh match, CompetitorMatch receivedCMG) {
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

    private CompetitorMatch advanceCompetitor(Competition competition, Matchh fetchedMatch, CompetitorMatch receivedCMG, Competitor competitor) {
        System.out.println("WESLO DO ADVANCE");
        int competitorCount = competitor.getGroupCompetitorList().size() * CreateCompetitionManager.GROUP_SIZE;
        double matchCounter = 0.0;

        for (MatchMatchType mmt : fetchedMatch.getMatchMatchTypeList()) {
            if (mmt.getIdMatchType().getMatchTypeName().startsWith(BEST_OF_PREFIX)) {
                int bestOfDigit = Integer.valueOf(mmt.getIdMatchType().getMatchTypeName().substring(2));

                if (receivedCMG.getCompetitorMatchScore() == ((bestOfDigit + 1) / 2)) {
                    CompetitorMatch advancedCompetitorCMG = new CompetitorMatch();

                    int matchesInRound = matchesInRound(competitorCount / 2, fetchedMatch.getRoundd());
                    System.out.println("MatcchesInRound: " + matchesInRound);

                    short firstMatchIndexInRound = firstMatchIndexInRound(competitorCount / 2, fetchedMatch.getRoundd());
                    System.out.println("FirstMatchIndexInRound: " + firstMatchIndexInRound);

                    for (short i = 0; i <= matchesInRound; i++) {
                        matchCounter += 0.5;
                        if ((i + firstMatchIndexInRound) == fetchedMatch.getMatchNumber()) {
                            System.out.println("Znalazlo match nr " + fetchedMatch.getMatchNumber());
                            List<CompetitorMatch> foundCMGs = cmgFacade.findByMatchNumberAndIdCompetition(((short) (firstMatchIndexInRound + matchesInRound - 1 + Math.ceil(matchCounter))), fetchedMatch.getCompetition().getIdCompetition());
//                            Matchh foundMatch = matchFacade.findByMatchNumberAndIdCompetition(((short) (firstMatchIndexInRound + matchesInRound - 1 + Math.ceil(matchCounter))), fetchedMatch.getCompetition().getIdCompetition());

//                            if (foundMatch == null) {
//                                    System.out.println("Match number " + (firstMatchIndexInRound + matchesInRound - 1 + Math.ceil(matchCounter)));
//                                    System.out.println("Competition id " + fetchedMatch.getCompetition().getIdCompetition());
//                                    throw new IllegalStateException("Ssss");
//                                } else {
//                                    System.out.println("Match number XXXXXX  " + foundMatch.getMatchNumber() + " id " + foundMatch.getIdMatch());
//                                }
//                            if (foundCMG.getIdCompetitor() == null) {// if this is the first competitor in match
                            
                            CompetitorMatch foundCMG = null;
                            
                            for (CompetitorMatch cmg : foundCMGs) {
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
    public List<CompetitorMatch> findCMGByIdMatch(Integer idMatch) {
        List<CompetitorMatch> found = cmgFacade.findCMGByIdMatch(idMatch);

//        for (CompetitorMatch cmg2 : found){
//            System.out.println("NR MATCHU i ID COMPETITORA " + cmg2.getIdMatch() + " comp: " + cmg2.getIdCompetitor() + " i wynik " + cmg.getCompetitorMatchScore() + " IDDD " + cmg);
//        }
        
        return found;
    }
}
