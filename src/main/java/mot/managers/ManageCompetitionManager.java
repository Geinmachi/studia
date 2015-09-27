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
import entities.Score;
import exceptions.ApplicationException;
import exceptions.InvalidScoreException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import mot.facades.AccountFacadeLocal;
import mot.facades.CompetitionFacadeLocal;
import mot.facades.CompetitorMatchFacadeLocal;
import mot.facades.GroupCompetitorFacadeLocal;
import mot.facades.MatchMatchTypeFacadeLocal;
import mot.facades.MatchTypeFacadeLocal;
import mot.facades.MatchhFacadeLocal;
import mot.facades.ScoreFacadeLocal;
import mot.interfaces.CMG;
import mot.models.CompetitorMatchGroup;
import mot.interfaces.CurrentMatchType;
import mot.interfaces.InactivateMatch;
import utils.BracketUtil;
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
    private MatchhFacadeLocal matchFacade;

    @EJB
    private GroupCompetitorFacadeLocal groupCompetitorFacade;

    @EJB
    private CompetitorMatchFacadeLocal competitorMatchFacade;

    @EJB
    private ScoreFacadeLocal scoreFacade;

    @EJB
    private MatchMatchTypeFacadeLocal mmtFacade;

    private List<CMG> storedCMGmappings;

    private final String BEST_OF_PREFIX = "BO";

    @Override
    public List<Competition> getLoggedUserCompetition() {
        Account loggedUser = accountFacade.findByLogin(sessionContext.getCallerPrincipal().getName());
        AccessLevel organizer = ConvertUtil.getSpecAccessLevelFromAccount(loggedUser, Organizer.class);

        return competitionFacade.findUserCompetitionsByIdAccessLevel(organizer.getIdAccessLevel());
    }

    @Override
    public Competition storeCompetition(Competition competition) {
        Competition fetchedCompetition = competitionFacade.findAndInitializeGD(competition.getIdCompetition());

        if (!fetchedCompetition.equals(competition)) { // after page laod and before button click competition was modified
            throw new IllegalStateException("In the meantime competition was modified");
        }

        return fetchedCompetition;
    }

    @Override
    public List<CMG> getCompetitionCMGMappings(Competition competition) {
        System.out.println("PRZED getCompetitionCMGMappings");
        List<GroupCompetitor> groupCompetitorList = groupCompetitorFacade.findByCompetitionId(competition.getIdCompetition());

        List<CompetitorMatch> competitorMatchList = competitorMatchFacade.findByCompetitionId(competition.getIdCompetition());

        Set<CMG> cmgSet = new HashSet<>();
        System.out.println("GROUPCOMPETITOR SSS " + groupCompetitorList.size());
        System.out.println("COMPETITORMATCH SSS " + competitorMatchList.size());
        for (CompetitorMatch cm : competitorMatchList) {
            //    if (Short.compare(cm.getIdMatch().getRoundd(), Short.parseShort("1")) == 0) {
            System.out.println("RUNDA 11111111111111");
            for (GroupCompetitor gc : groupCompetitorList) {
                if (gc.getIdCompetitor() != null && cm.getIdCompetitor() != null) {
                    if (gc.getIdCompetitor().equals(cm.getIdCompetitor())) {
                        cmgSet.add(new CompetitorMatchGroup(gc, cm));

                        break;
                    }
                } else {
                    cmgSet.add(new CompetitorMatchGroup(null, cm));
                }
            }
            //    } 
        }

        List<CMG> liststoredCMGmappings = new ArrayList<>(cmgSet);

        return liststoredCMGmappings;
    }

    private CompetitorMatch getStoredCompetitorMatch(int idCompetitorMatch) {

        for (CMG cmg : storedCMGmappings) {
            for (CompetitorMatch cm : cmg.getIdMatch().getCompetitorMatchList()) {
                if (cm.getIdCompetitorMatch() == idCompetitorMatch) {
                    return cm;
                }
            }
        }

        throw new IllegalStateException("Given competitorMatch doesnt exist in stored competition");
    }

    /**
     *
     * @param editingCompetition
     * @param receivedCompetitorMatch
     * @return CompetitorMatch if competitor advanced to next round, oterwise
     * returns null
     */
    @Override
    public Map<String, CompetitorMatch> saveCompetitorScore(CompetitorMatch receivedCompetitorMatch, List<CMG> storedCMGmappings) throws ApplicationException {
        this.storedCMGmappings = storedCMGmappings;
        
        CompetitorMatch storedCompetitorMatch = getStoredCompetitorMatch(receivedCompetitorMatch.getIdCompetitorMatch());

        CompetitorMatch fetchedCompetitorMatch = storedCompetitorMatch;
        Matchh fetchedMatch = matchFacade.findAndInitializeTypes(fetchedCompetitorMatch.getIdMatch().getIdMatch());

//        System.out.println("MATCH TPYYYYYY");
//        for (MatchMatchType mt : fetchedMatch.getMatchMatchTypeList()) {
//            System.out.println("TYYP ::: " + mt.getIdMatchType().getMatchTypeName());
//        }
        validateScore(fetchedMatch, receivedCompetitorMatch);

        fetchedCompetitorMatch.setCompetitorMatchScore(receivedCompetitorMatch.getCompetitorMatchScore());

        System.out.println("VERSION before edit " + fetchedCompetitorMatch.getVersion());
        fetchedCompetitorMatch = competitorMatchFacade.editWithReturn(fetchedCompetitorMatch);
        System.out.println("VERSION after edit " + fetchedCompetitorMatch.getVersion());

        CompetitorMatch advancedCompetitoCMG = advanceCompetitor(receivedCompetitorMatch);
        
        Map<String, CompetitorMatch> returnObject = new HashMap<>();
        returnObject.put("saved", fetchedCompetitorMatch);
        returnObject.put("advanced", advancedCompetitoCMG);

        return returnObject;
    }

    private void validateScore(Matchh match, CompetitorMatch receivedCompetitorMatch) throws ApplicationException {
        if (receivedCompetitorMatch == null || receivedCompetitorMatch.getCompetitorMatchScore() == null) {
            throw new IllegalArgumentException("Can't be null");
        }

        for (MatchMatchType mmt : match.getMatchMatchTypeList()) {
            if (mmt.getIdMatchType().getMatchTypeName().startsWith(BEST_OF_PREFIX)) {
                int bestOfDigit = Integer.valueOf(mmt.getIdMatchType().getMatchTypeName().substring(2));
                if (receivedCompetitorMatch.getCompetitorMatchScore() > ((bestOfDigit + 1) / 2)) {
                    System.out.println("IIIDDDDDDDDDDD --- match " + receivedCompetitorMatch.getIdMatch().getIdMatch());
                    throw new InvalidScoreException("Too big number");
                } else if (receivedCompetitorMatch.getCompetitorMatchScore() < 0) {
                    throw new InvalidScoreException("Score can't be lower than 0");
                }

                break;
            }
        }
    }

    @Override
    public CompetitorMatch advanceCompetitor(CompetitorMatch receivedCompetitorMatch) {
        if (receivedCompetitorMatch == null || receivedCompetitorMatch.getIdMatch() == null) {
            throw new IllegalArgumentException("ManageComeptitonManager#advanceCompetitor: "
                    + "competitorMatch or match cannot be null competitorMatch: " + receivedCompetitorMatch);
        }
        Matchh fetchedMatch = matchFacade.findAndInitializeTypes(receivedCompetitorMatch.getIdMatch().getIdMatch());

        System.out.println("WESLO DO ADVANCE");
        int competitorCount = fetchedMatch.getCompetition().getGroupDetailsList().size() * CreateCompetitionManager.GROUP_SIZE;
        double matchCounter = 0.0;

        for (MatchMatchType mmt : fetchedMatch.getMatchMatchTypeList()) {
            if (mmt.getIdMatchType().getMatchTypeName().startsWith(BEST_OF_PREFIX)) {
                int bestOfDigit = Integer.valueOf(mmt.getIdMatchType().getMatchTypeName().substring(2));

                if (receivedCompetitorMatch.getCompetitorMatchScore() == ((bestOfDigit + 1) / 2)) {
                    Score score = scoreFacade.findByIdCompetitorAndIdCompetition(fetchedMatch.getCompetition().getIdCompetition(), receivedCompetitorMatch.getIdCompetitor().getIdCompetitor());
                    score.setScore((short) (score.getScore() + 1));
                    scoreFacade.edit(score);

                    CompetitorMatch advancedCompetitorCMG = new CompetitorMatch();

                    int matchesInRound = BracketUtil.matchesInRound(competitorCount / 2, fetchedMatch.getRoundd());
                    System.out.println("MatcchesInRound: " + matchesInRound);

                    short firstMatchIndexInRound = BracketUtil.firstMatchIndexInRound(competitorCount / 2, fetchedMatch.getRoundd());
                    System.out.println("FirstMatchIndexInRound: " + firstMatchIndexInRound);

                    for (short i = 0; i <= matchesInRound; i++) {
                        matchCounter += 0.5;
                        if ((i + firstMatchIndexInRound) == fetchedMatch.getMatchNumber()) {
                            System.out.println("Znalazlo match nr " + fetchedMatch.getMatchNumber());
                            List<CompetitorMatch> foundCMGs = competitorMatchFacade.findByMatchNumberAndIdCompetition(((short) (firstMatchIndexInRound + matchesInRound - 1 + Math.ceil(matchCounter))), fetchedMatch.getCompetition().getIdCompetition());
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

                            for (MatchMatchType mmt2 : fetchedMatch.getMatchMatchTypeList()) {
                                if (mmt2.getIdMatchType().getMatchTypeName().equals("final")) { // if final there is no advancing
                                    fetchedMatch.getCompetition().setIdWinner(receivedCompetitorMatch.getIdCompetitor());
                                    competitionFacade.edit(fetchedMatch.getCompetition());

                                    return null;
                                }
                            }

                            if (foundCMG == null) {
                                System.out.println("not emtpty CMG");
                                return null;
                                //    throw new IllegalStateException("DID not find emtpy CMG in match");
                            }

                            foundCMG.setIdCompetitor(receivedCompetitorMatch.getIdCompetitor());
                            foundCMG.setCompetitorMatchScore((short) 0);
                            foundCMG.setPlacer(calculatePlacer(matchCounter));
                            competitorMatchFacade.edit(foundCMG);
                            System.out.println("EDYOWANY foundCMG " + foundCMG.getIdMatch().getMatchNumber());

                            advancedCompetitorCMG = foundCMG;
//                            } else {
//                                advancedCompetitorCMG.setCompetitorMatchScore((short) 0);
//                                advancedCompetitorCMG.setIdCompetitor(competitor);
//                                advancedCompetitorCMG.setIdMatch(foundMatch);
//                                advancedCompetitorCMG.calculatePlacer(calculatePlacer(matchCounter));
//                                competitorMatchFacade.create(advancedCompetitorCMG);
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
//    private short firstMatchIndexInRound(int firstRoundMatches, short round) {
//        if (round < 1) {
//            throw new IllegalStateException("Round is lower than 1");
//        }
//        if (round == 1) {
//            return 1;
//        }
//
//        return (short) (firstMatchIndexInRound(firstRoundMatches / 2, (short) (round - 1)) + firstRoundMatches);
//    }
//
//    private int matchesInRound(int firstRoundMatches, short round) {
//        if (round < 1) {
//            throw new IllegalStateException("Round is lower than 1");
//        }
//        if (round == 1) {
//            return firstRoundMatches;
//        }
//        return matchesInRound(firstRoundMatches, (short) (round - 1)) / 2;
//    }
    private short calculatePlacer(double matchCounter) {
        if (Math.ceil(matchCounter) == matchCounter) { // second placer
            return (short) 2;
        } else {
            return (short) 1;
        }
    }

    @Override
    public List<CompetitorMatch> findCMGByIdMatch(Integer idMatch) {
        List<CompetitorMatch> found = competitorMatchFacade.findCMGByIdMatch(idMatch);

//        for (CompetitorMatch cmg2 : found){
//            System.out.println("NR MATCHU i ID COMPETITORA " + cmg2.getIdMatch() + " comp: " + cmg2.getIdCompetitor() + " i wynik " + cmg.getCompetitorMatchScore() + " IDDD " + cmg);
//        }
        return found;
    }

    @Override
    public void updateMatchType(Matchh match) {
        List<CompetitorMatch> competitorMatchList = competitorMatchFacade.findByIdMatch(match.getIdMatch());

        for (MatchMatchType mmt : match.getMatchMatchTypeList()) {
            if (mmt.getIdMatchType().getMatchTypeName().startsWith(BEST_OF_PREFIX)) {
                int bestOfDigit = Integer.valueOf(mmt.getIdMatchType().getMatchTypeName().substring(2));

                for (CompetitorMatch cm : competitorMatchList) {
                    if (cm != null && cm.getCompetitorMatchScore() != null) {
                        if (cm.getCompetitorMatchScore() > ((bestOfDigit + 1) / 2)) {
                            throw new IllegalStateException("Can't change match type because score exceeds maximum possible score");
                        } else if (cm.getCompetitorMatchScore().intValue() == ((bestOfDigit + 1) / 2)) {
                            if (Short.compare(match.getCompetitorMatchList().get(0).getCompetitorMatchScore(),
                                    match.getCompetitorMatchList().get(1).getCompetitorMatchScore()) == 0) {
                                throw new IllegalStateException("After lowering the match type two competitor are tied on match point");
                            }
                        }
                    }
                }
            }

            mmtFacade.edit(mmt);
        }
    }

    @Override
    public InactivateMatch disableMatch(InactivateMatch inactivateMatch) {

        try {
            if (inactivateMatch.getMatch() != null) {

                for (CompetitorMatch cm : inactivateMatch.getMatch().getCompetitorMatchList()) {
                    if (cm.getIdCompetitor() == null) { // no competitor in match
                        inactivateMatch.setInplaceEditable(false);
                        System.out.println("PUSTY COMEPTITOR, INPLACE EDITABLE false " + cm);

                        break;
                    } else {
                        inactivateMatch.setInplaceEditable(true);
                    }
                }

                for (MatchMatchType mmt : inactivateMatch.getMatch().getMatchMatchTypeList()) {
                    //    System.out.println("mmt " + mmt.getIdMatchType().getMatchTypeName());
                    if (mmt.getIdMatchType().getMatchTypeName().startsWith("BO")) {
//                    inactivateMatch.setMatchType(mmt.getIdMatchType());

                        for (CompetitorMatch cm : inactivateMatch.getMatch().getCompetitorMatchList()) {
                            if (cm.getCompetitorMatchScore() != null && ((Integer.valueOf(mmt.getIdMatchType().getMatchTypeName().substring(2)) + 1) / 2) == cm.getCompetitorMatchScore()) {
                                System.out.println("WYLACZA " + inactivateMatch.getMatch());

                                System.out.println("ADVANCINS z DISABLING " + inactivateMatch.getMatch());
                                //                            advanceCompetitor(inactivateMatch.getMatch(), cm, cm.getIdCompetitor());
                                System.out.println("idMatch =  " + inactivateMatch.getMatch().getIdMatch() + " COMPETITORRRRRRRRRRRRRRRRx " + cm.getIdCompetitor() + "   ---   idCompetitorMatch " + cm.getIdCompetitorMatch());

                                inactivateMatch.setEditable(false);

                                return inactivateMatch;
                            }
                        }
                    }
                }
                //    System.out.println("TYP : " + inactivateMatch.getMatch().getMatchMatchTypeList());
            }
            //    for (CompetitorMatch cm : inactivateMatch.getMatch().getCompetitorMatchList()) {
            // if (cm.getCompetitorMatchScore() == cm.ge)
            //   }
        } catch (Throwable e) {
            System.out.println("EXCEPTION MANAGECOMPETITON#disableMatch " + e.getMessage());
            e.printStackTrace();
        }
        return inactivateMatch;
    }

    @Override
    public CurrentMatchType assignCurrentMatchType(CurrentMatchType cmt) {
        if (cmt.getMatch() != null) {
            for (MatchMatchType mmt : cmt.getMatch().getMatchMatchTypeList()) {
                //    System.out.println("mmt " + mmt.getIdMatchType().getMatchTypeName());
                if (mmt.getIdMatchType().getMatchTypeName().startsWith("BO")) {
                    cmt.setMatchType(mmt.getIdMatchType());
                }
            }
        }

        return cmt;
    }

    @Override
    public Competition saveCompetitionGeneralInfo(Competition competition, Competition storedCompetition) {
        if (storedCompetition == null) {
            throw new IllegalArgumentException("There is no stored competition to edit");
        } else if (!competition.equals(storedCompetition)) {
            throw new IllegalStateException("Editing and stored comeptition don't match");
        }

        storedCompetition.setCompetitionName(competition.getCompetitionName());
        storedCompetition.setStartDate(competition.getStartDate());
        storedCompetition.setEndDate(competition.getEndDate());

        competitionFacade.edit(storedCompetition);

        return storedCompetition;
    }
}
