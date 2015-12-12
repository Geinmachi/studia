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
import entities.GroupDetails;
import entities.GroupName;
import entities.MatchMatchType;
import entities.MatchType;
import entities.Matchh;
import entities.Organizer;
import entities.Score;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import mot.facades.AccountFacadeLocal;
import mot.facades.CompetitionFacadeLocal;
import mot.facades.CompetitorFacadeLocal;
import mot.facades.CompetitorMatchFacadeLocal;
import mot.facades.GroupCompetitorFacadeLocal;
import mot.facades.GroupDetailsFacadeLocal;
import mot.facades.GroupNameFacadeLocal;
import mot.facades.MatchMatchTypeFacadeLocal;
import mot.facades.MatchTypeFacadeLocal;
import mot.facades.MatchhFacadeLocal;
import mot.facades.ScoreFacadeLocal;
import utils.BracketUtil;
import utils.ConvertUtil;
import mot.interfaces.CMG;
import mot.models.CompetitorMatchGroup;
import ejbCommon.TrackerInterceptor;
import exceptions.ApplicationException;
import java.util.Iterator;

/**
 *
 * @author java
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors({TrackerInterceptor.class})
public class CreateCompetitionManager implements CreateCompetitionManagerLocal {

    @Resource
    private SessionContext sessionContext;

    @EJB
    private CompetitionFacadeLocal competitionFacade;

    @EJB
    private AccountFacadeLocal accountFacade;

//    @EJB
//    private GroupCompetitionFacadeLocal groupCompetitionFacade;
    @EJB
    private GroupNameFacadeLocal groupNameFacade;

    @EJB
    private MatchhFacadeLocal matchFacade;

    @EJB
    private MatchMatchTypeFacadeLocal mmtFacade;

    @EJB
    private CompetitorMatchFacadeLocal cmgFacade;

    @EJB
    private CompetitorFacadeLocal competitorFacade;

    @EJB
    private GroupCompetitorFacadeLocal groupCompetitorFacade;

    @EJB
    private GroupDetailsFacadeLocal groupDetailsFacade;

    @EJB
    private ScoreFacadeLocal scoreFacade;

    @EJB
    private MatchTypeFacadeLocal matchTypeFacade;

    @EJB
    private ManageCompetitionManagerLocal manageCompetitionManager;

    final static int GROUP_SIZE = 4;

    @Override
    public boolean validateCompetitorsAmount(int amount) {
        if (amount <= 0) {
            return false;
        }
        return BracketUtil.isPowerOfTwo(amount);
    }

    @Override
    public void createCompetition(Competition competition, List<CMG> competitorMatchGroupList) throws ApplicationException {
        Account loggedUser = accountFacade.findByLogin(sessionContext.getCallerPrincipal().getName());
        AccessLevel organizer = ConvertUtil.getSpecAccessLevelFromAccount(loggedUser, Organizer.class);

        competition.setIdOrganizer(organizer);
        competition.setCreationDate(new Date());

        competition = competitionFacade.createWithReturn(competition);

        List<GroupDetails> groupDetailsList = getUniqueGroupDetails(competitorMatchGroupList);
        List<Matchh> matchList = getUniqueMatches(competitorMatchGroupList);

        List<GroupDetails> groupDetailsWithIdentityList = new ArrayList<>();

        for (GroupDetails gd : groupDetailsList) {
            gd.setCompetition(competition);
            groupDetailsWithIdentityList.add(groupDetailsFacade.createWithReturn(gd));
            competition.getGroupDetailsList().add(gd);
        }

        System.out.println("SIZE GROUPSS " + groupDetailsWithIdentityList.size());

        for (CMG cmg : competitorMatchGroupList) {
            if (cmg.getGroupDetails() != null) {
                GroupCompetitor gc = cmg.getGroupCompetitor();
                System.out.println("CZY NULL " + cmg.getGroupDetails());
                System.out.println("GROUPDETAILS " + cmg.getGroupDetails().getIdGroupDetails() + " UUID " + cmg.getGroupDetails().getUuid());
                System.out.println("INDXOF " + groupDetailsWithIdentityList.indexOf(cmg.getGroupDetails()));
                System.out.println("GROUPS with identity " + groupDetailsWithIdentityList.get(groupDetailsWithIdentityList.indexOf(cmg.getGroupDetails())).getIdGroupDetails());
                gc.setIdGroupDetails(groupDetailsWithIdentityList.get(groupDetailsWithIdentityList.indexOf(cmg.getGroupDetails())));
                groupCompetitorFacade.create(cmg.getGroupCompetitor());

                if (cmg.getIdCompetitor() != null) {
                    Score score = new Score();
                    score.setIdCompetition(competition);
                    score.setIdCompetitor(cmg.getIdCompetitor());

                    short scoreScore = 0;
                    CompetitorMatch autoAdvancedCompeitorMatch = null;

                    if (cmg.getIdMatch().getRoundd() == 1 // if auto-advance from round 1 then got score 1
                            && (autoAdvancedCompeitorMatch = checkMatchNullCompetitor(cmg.getIdMatch())) != null // if there is auto-advanced competitor in match
                            && autoAdvancedCompeitorMatch.getIdCompetitor().equals(cmg.getIdCompetitor())) { // if current object in iteration is auto-advanced competitor (could be null if object is missing opponent)
                        scoreScore = 1;
                    }

                    score.setScore(scoreScore);

                    scoreFacade.create(score);
                }
            }

        }
        //        for (GroupName g : groupList) {
        //            GroupName managedGroup = groupNameFacade.createWithReturn(g);
        //            groupDetailsWithIdentityList.add(managedGroup);
        //
        ////            GroupCompetition groupCompetition = new GroupCompetition();
        ////            groupCompetition.setIdCompetition(competition);
        ////            groupCompetition.setIdGroup(managedGroup);
        ////
        ////            competition.getGroupCompetitionList().add(groupCompetition);
        //        }

        List<Matchh> matchWithIdentityList = new ArrayList<>();

        //      assignSameGroupsToCompetitors(competitorMatchList, groupDetailsWithIdentityList);
        for (Matchh m : matchList) {
//            m.setCompetition(competition);
            for (CompetitorMatch cmg : m.getCompetitorMatchList()) {
                if (cmg.getIdMatch().getRoundd() == 1 && cmg.getIdCompetitor() != null) {
                    cmg.setCompetitorMatchScore((short) 0);
                } else {
                    System.out.println("RUNDA TOTOOOOOOOOOOOOO " + cmg.getIdMatch().getRoundd());
                }
            }
            if (m.getCompetitorMatchList().isEmpty()) {
                System.out.println("CMG JEsT PUSTEEEE dla meczu nr " + m.getMatchNumber());
            }

            m.setCompetition(competition);
            matchWithIdentityList.add(matchFacade.createWithReturn(m));
//            System.out.println("MMAMAMAMAMAMAM " + m + "  number " + m.getMatchNumber());
        }

//        for (Matchh m : matchWithIdentityList) {
//            System.out.println("Runda " + m.getRoundd() + " MAtch nr " + m.getMatchNumber());
//            m.getCompetitorMatchList().forEach(p -> System.out.println("CompetitorMatch: " + p.getIdCompetitor()));
//
//            if (m.getRoundd() == 1) {
//                CompetitorMatch competitorMatch = checkMatchNullCompetitor(m);
//                if (competitorMatch != null) {
//                    System.out.println("Advancing " + competitorMatch.getIdCompetitor().getIdPersonalInfo());
//                    manageCompetitionManager.advanceCompetitor(competitorMatch, true);
//                }
//            }
//        }
        System.out.println("competitorMatchGroupList sizeeee " + competitorMatchGroupList.size());
//        assignSameMatchesToCompetitors(competitorMatchList, matchWithIdentityList);
//
//        for (CompetitorMatch cmg : competitorMatchList) {
//            if (cmg.getIdMatch() != null) {
////                System.out.println("MAAAAAAAAAAAAAAAATCHHHHHHH " + cmg.getIdMatch());
////                System.out.println("MAtch UUID " + cmg.getIdMatch().getUuid() + " number " + cmg.getIdMatch().getMatchNumber());
//                if (cmg.getIdMatch().getRoundd() == 1) {
//                    cmg.setCompetitorMatchScore((short) 0);
//                }
//                cmgFacade.create(cmg);
//            } else {
////                System.out.println("MATTTTTTTTTTTTTTTTTTTTTCH NULLLLLLLLLLLLL");
//            }
//        }
//
//        competitionFacade.create(competition);
        System.out.println("PRZESZLO SZYSTKO");
//        throw new NullPointerException();
    }

    private List<GroupDetails> getUniqueGroupDetails(List<CMG> competitorMatchGroupList) {
        Set<GroupDetails> groups = new HashSet<>();

        int competitorCount = 0;

        for (CMG cmg : competitorMatchGroupList) {
            groups.add(cmg.getGroupDetails());
            if (cmg.getIdCompetitor() != null) {
                competitorCount++;
            }
        }

        groups.remove(null);

//        if (competitorCount / GROUP_SIZE != groups.size()) {
//            System.out.println("Number Of Rounds = " + BracketUtil.numberOfRounds(competitorCount) + " group size " + groups.size());
//            throw new IllegalArgumentException("Liczba grup nie odpowiada liczbie uczesnitkow");
//        }
        return new ArrayList<>(groups);
    }

    private List<Matchh> getUniqueMatches(List<CMG> competitorMatchGroupList) {
        Set<Matchh> matches = new HashSet<>();

        for (CMG cmg : competitorMatchGroupList) {
            matches.add(cmg.getIdMatch());
        }

        matches.remove(null);

        return new ArrayList<>(matches);
    }

    private void assignSameMatchesToCompetitors(List<CompetitorMatch> competitorMatchGroupList, List<Matchh> uniqueMatchList) {
        for (CompetitorMatch cmg : competitorMatchGroupList) {
            for (Matchh m : uniqueMatchList) {
                if (cmg.getIdMatch().equals(m)) {
                    System.out.println("ASSIGNING " + m.getMatchNumber());
                    cmg.setIdMatch(m);
                    break;
                }
            }
        }
    }

    private void assignSameGroupsToCompetitors(List<CMG> competitorMatchGroupList, List<GroupName> uniqueGroupList) {
        for (CMG cmg : competitorMatchGroupList) {
            if (cmg.getIdCompetitor() != null) {
                for (GroupName g : uniqueGroupList) {
                    if (cmg.getIdGroupName().equals(g)) {
                        cmg.setIdGroupName(g);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public List<CMG> generateEmptyBracket(List<Competitor> competitors) {
        List<Competitor> fetchedCompetitors = new ArrayList<>();

        for (Competitor c : competitors) {
            fetchedCompetitors.add(competitorFacade.findAndInitializeGroups(c.getIdCompetitor()));
        }

        Collections.shuffle(fetchedCompetitors);

        List<GroupName> groups = createGroups(fetchedCompetitors.size());

        //    groups.forEach(p -> System.out.println("Grupa: " + p.getGroupName()));
        Map<Competitor, GroupName> assignedCompetitorsToGroups = assignCompetitorsToGroups(fetchedCompetitors, groups);

        return createMatches(assignedCompetitorsToGroups);

    }
//    private GroupCompetition createBracket(List<Competitor> competitors) {
//        
//    }

    private List<GroupName> createGroups(int competitorsAmount) {
        int asciiValue = 65;
//        double numberOfGroups = Math.sqrt((double) competitorsAmount);
        int numberOfGroups = computeGroupNumber(competitorsAmount);
        List<GroupName> existingGroups = groupNameFacade.findAll();
        Collections.sort(existingGroups);

        int groupsInDatabase = existingGroups.size();

        List<GroupName> groups = new ArrayList<>();

//        if (numberOfGroups > groupsInDatabase) {
//            for (int i = groupsInDatabase; i <= numberOfGroups; i++) {
//                GroupName group = new GroupName();
//                group.setGroupName((char)(asciiValue + i));
//                groups.add(groupNameFacade.createWithReturn(group));
//            }
//        }
        for (int i = 0; i < numberOfGroups; i++) {
            if (i < groupsInDatabase) {
                groups.add(existingGroups.get(i));
                System.out.println("MNIEJSZE NIZ w bazie " + i + " size " + groupsInDatabase + " dodaje " + existingGroups.get(i).getGroupName());
            } else {
                GroupName group = new GroupName();
                group.setGroupName((char) (asciiValue + i));
                groups.add(groupNameFacade.createWithReturn(group));
            }
        }
//        for (int i = 0; i < numberOfGroups; i++) {
//            GroupName group = new GroupName(UUID.randomUUID());
//            group.setGroupName((char) (asciiValue + i));
//            groups.add(group);
//        }

        return groups;
    }

    private int computeGroupNumber(int competitors) {
        int numberOfGroups = competitors / GROUP_SIZE;
        numberOfGroups += (competitors % GROUP_SIZE) == 0 ? 0 : 1; // if not power of 2  add one extra group

        while (!BracketUtil.isPowerOfTwo(numberOfGroups)) {
            numberOfGroups++;
        }

        return numberOfGroups;
    }

    private Map<Competitor, GroupName> assignCompetitorsToGroups(List<Competitor> competitors, List<GroupName> groups) {
        Map<Competitor, GroupName> assignedCompetitors = new HashMap<>();

        System.out.println("competitoros " + competitors.size() + " groups " + groups.size());

        groups.forEach(p -> System.out.println("NAME " + p.getGroupName()));

        int assignedGroupIndex = 0; // 5 competitors 2 groups

        for (int i = 0; i < competitors.size(); i++) {
            assignedGroupIndex = (i % groups.size() == 0) ? 0 : ++assignedGroupIndex;
//            if (i % groups.size() == 0) {
//                assignedGroupIndex = 0;
//            } else {
//                assignedGroupIndex++;
//            }
            System.out.println("i = " + i + " assingedIndex " + assignedGroupIndex + " letters.get = " + groups.get(assignedGroupIndex).getGroupName() + " czy true " + (i % groups.size() == 0));
            assignedCompetitors.put(competitors.get(i), groups.get(assignedGroupIndex));
        }
        for (Entry<Competitor, GroupName> entry : assignedCompetitors.entrySet()) {
//            System.out.println("Competitor o id " + entry.getKey() + " jest w grupie " + entry.getValue().getGroupName());
        }
        return assignedCompetitors;
    }

    private List<CMG> createMatches(Map<Competitor, GroupName> assignedCompetitors) {
        Map<GroupDetails, List<Competitor>> groups = transformAssignedCompetitorsToLists(assignedCompetitors);

//        System.out.println("ile grup " + groups.size());
        for (List<Competitor> list : groups.values()) {
            Collections.shuffle(list);
        }

        List<GroupCompetitor> groupCompetitorList = createGroupCompetitorMappings(groups);

        List<CompetitorMatch> competitorMatchList = generateFirstRoundMatches(groups);

        generateOtherRounds(competitorMatchList, assignedCompetitors.keySet().size());

        List<CMG> cmgMappings = new ArrayList<>();

        System.out.println("Competitormatch size " + competitorMatchList.size());

        for (CompetitorMatch cm : competitorMatchList) {
//            if (Short.compare(cm.getIdMatch().getRoundd(), Short.parseShort("1")) == 0) {
            for (GroupCompetitor gc : groupCompetitorList) {
                if (gc.getIdCompetitor().equals(cm.getIdCompetitor())) {
                    cmgMappings.add(new CompetitorMatchGroup(gc, cm));

                    break;
                }
            }
//            }
        }

        Set<Matchh> firstRoundMatches = new HashSet<>();
        for (CompetitorMatch cm : competitorMatchList) {
            if (cm.getIdCompetitor() == null) {
                cmgMappings.add(new CompetitorMatchGroup(null, cm));
            }
            if (cm.getIdMatch().getRoundd() == 1) {
                firstRoundMatches.add(cm.getIdMatch());
            }
        }

        System.out.println("HashSetowe matches " + firstRoundMatches.size());

        for (Matchh match : firstRoundMatches) {
            CompetitorMatch cmToAdvance = null;
            if ((cmToAdvance = checkMatchNullCompetitor(match)) != null) {
                System.out.println("UP matchNo " + match.getMatchNumber() + " runda " + match.getRoundd());
//                System.out.println("Wynik z up " + cm.getCompetitorMatchScore());
                autoAdvanceCompetitor(cmToAdvance, competitorMatchList);
            }
        }

//        for (int i = 0; i < competitorMatchList.size(); i++) {
//            System.out.println("iiiiiiiiiiiiii = " + i);
//            System.out.println("Kto = " + competitorMatchList.get(i).getIdCompetitor());
//            if (competitorMatchList.get(i).getIdGroup() != null) {
//                System.out.println("Grupa = " + competitorMatchList.get(i).getIdGroup().getGroupName());
//            }
//            System.out.println("Match = " + competitorMatchList.get(i).getIdMatch().getRoundd());
//
//        }
        return cmgMappings;
    }

    private CompetitorMatch checkMatchNullCompetitor(Matchh match) {
        CompetitorMatch competitorMatch = null;
        boolean hasNull = false;

        for (CompetitorMatch cm : match.getCompetitorMatchList()) {
            if (cm.getIdCompetitor() == null) {
                hasNull = true;
            } else {
                competitorMatch = cm;
            }
        }

        return hasNull ? competitorMatch : null;
    }

    private void autoAdvanceCompetitor(CompetitorMatch cm, List<CompetitorMatch> competitorMatchList) {
        Map<String, Double> matchData = getAdvancedMatchNumber(competitorMatchList.size() / 2, cm.getIdMatch());
        short advancedMatchNumber = matchData.get("matchNumber").shortValue();
        double advancedMatchCounter = matchData.get("matchCounter");
        Matchh foundMatchToAdvance = findMatchByMatchNumber(competitorMatchList, advancedMatchNumber);

        System.out.println("AUto advance advancedMatchNumber" + advancedMatchNumber + " foundMatchToAdvance " + foundMatchToAdvance.getMatchNumber());

        for (CompetitorMatch advancedCm : foundMatchToAdvance.getCompetitorMatchList()) {
            if (advancedCm.getIdCompetitor() == null) {
                System.out.println("Jest w pustym CM meczu " + foundMatchToAdvance.getMatchNumber());

                advancedCm.setIdCompetitor(cm.getIdCompetitor());
                advancedCm.setCompetitorMatchScore((short) 0);
                advancedCm.setPlacer(calculatePlacer(advancedMatchCounter));

                break;
            }
        }
    }

    private short calculatePlacer(double matchCounter) {
        return Math.ceil(matchCounter) == matchCounter ? (short) 2 : (short) 1;
    }

    private Map<String, Double> getAdvancedMatchNumber(int competitorCount, Matchh match) {
        int matchesInRound = BracketUtil.matchesInRound(competitorCount / 2, match.getRoundd());
        System.out.println("MatcchesInRound: " + matchesInRound);

        short firstMatchIndexInRound = BracketUtil.firstMatchIndexInRound(competitorCount / 2, match.getRoundd());
        System.out.println("FirstMatchIndexInRound: " + firstMatchIndexInRound);

        Map<String, Double> valueMap = new HashMap<>();
        double matchCounter = 0.0;

        for (short i = 0; i <= matchesInRound; i++) {
            matchCounter += 0.5;
            if ((i + firstMatchIndexInRound) == match.getMatchNumber()) {
                valueMap.put("matchCounter", matchCounter);
                valueMap.put("matchNumber", (firstMatchIndexInRound + matchesInRound + Math.ceil(matchCounter)));

                return valueMap;
            }
        }

        return valueMap;
    }

    private Matchh findMatchByMatchNumber(List<CompetitorMatch> competitorMatchList, short matchNumber) {
        for (CompetitorMatch cm : competitorMatchList) {
            if (cm.getIdMatch().getMatchNumber() == matchNumber) {
                return cm.getIdMatch();
            }
        }

        return null;
    }

    private Map<GroupDetails, List<Competitor>> transformAssignedCompetitorsToLists(Map<Competitor, GroupName> assignedCompetitors) {
        Map<GroupDetails, List<Competitor>> groups = new TreeMap<>();

        for (GroupName g : assignedCompetitors.values()) { // init values
            GroupDetails gd = new GroupDetails(UUID.randomUUID());
            gd.setIdGroupName(g);

            groups.put(gd, new ArrayList<>());
        }
        for (Entry<Competitor, GroupName> entry : assignedCompetitors.entrySet()) {
            for (GroupDetails gd : groups.keySet()) {
                if (gd.getIdGroupName().equals(entry.getValue())) {

                    List<Competitor> competitorsInGroup = groups.get(gd);
                    competitorsInGroup.add(entry.getKey());
                    groups.put(gd, competitorsInGroup);
                }
            }
        }

        return groups;
    }

    private List<GroupCompetitor> createGroupCompetitorMappings(Map<GroupDetails, List<Competitor>> competitorsAndGroups) {
        List<GroupCompetitor> groupCompetitorList = new ArrayList<>();

        for (Entry<GroupDetails, List<Competitor>> entry : competitorsAndGroups.entrySet()) {
            for (Competitor c : entry.getValue()) {
                GroupCompetitor gc = createGroupCompetitor(entry.getKey(), c);

                groupCompetitorList.add(gc);

//                System.out.println("CCCCCC " + c + " scroelist " + c.getScoreList().size());
                c.getGroupCompetitorList().add(gc);
            }
        }

        return groupCompetitorList;
    }

    private List<CompetitorMatch> generateFirstRoundMatches(Map<GroupDetails, List<Competitor>> groups) {
        List<CompetitorMatch> competitorMatchGroupList = new ArrayList<>();

        short matchCounter = 1;
        for (Entry<GroupDetails, List<Competitor>> entry : groups.entrySet()) {
//            System.err.println("Rozmiar competitorow w grupie " + entry.getKey().getGroupName()
//                    + "to " + entry.getValue().size());
            if (entry.getValue().size() < 2) {
                throw new IllegalStateException("Group cannot have less than 2 competitors");
            }
            for (int i = 0; i < entry.getValue().size(); i = i + 2) {
//                System.out.println("Competitor" + entry.getValue().get(i) + " w matchu, i = " + i);
                Matchh match = new Matchh(UUID.randomUUID());
                match.setRoundd((short) 1);
                match.setMatchNumber(matchCounter++);

                CompetitorMatch cmg1 = new CompetitorMatch(UUID.randomUUID());
                match.getCompetitorMatchList().add(cmg1);

                cmg1.setIdCompetitor(entry.getValue().get(i));
                //               cmg1.setIdGroup(entry.getKey());
                cmg1.setIdMatch(match);
                cmg1.setPlacer((short) 1);
//                match.getCompetitorMatchGroupList().add(cmg1);
//                entry.getKey().getCompetitorMatchGroupList().add(cmg1);

                CompetitorMatch cmg2 = new CompetitorMatch(UUID.randomUUID());
                match.getCompetitorMatchList().add(cmg2);

//                System.out.println("2222Competitor" + entry.getValue().get(i + 1) + " w matchu, i + 1 = " + (i + 1));
                Competitor secondCompetitor = null;
                if ((i + 1) < entry.getValue().size()) { // adds second competitor if there is one, otherwise first competitor auto-win 
                    secondCompetitor = entry.getValue().get(i + 1);
                }
                if (entry.getValue().size() == 2) { // if there are 2 competitors in group separate them in different firstRoundMatches
                    secondCompetitor = null;
                    i--;
                }
                cmg2.setIdCompetitor(secondCompetitor);
                //           cmg2.setIdGroup(entry.getKey());
                cmg2.setIdMatch(match);
                cmg2.setPlacer((short) 2);
//                match.getCompetitorMatchGroupList().add(cmg2);
//                entry.getKey().getCompetitorMatchGroupList().add(cmg2);

                competitorMatchGroupList.add(cmg1);
                competitorMatchGroupList.add(cmg2);

//                System.out.println("Hashe: ");
//                System.out.println("cmg1.match = " + cmg1.getIdMatch().hashCode());
//                System.out.println("cmg2.match = " + cmg2.getIdMatch().hashCode());
//                System.out.println("Czy sa rowne? " + cmg1.getIdMatch().equals(cmg2.getIdMatch()));
            }
        }

        return competitorMatchGroupList;
    }

    private GroupCompetitor createGroupCompetitor(GroupDetails groupDetails, Competitor competitor) {
        GroupCompetitor gc = new GroupCompetitor();

        gc.setIdGroupDetails(groupDetails);
        gc.setIdCompetitor(competitor);

        return gc;
    }

    private void generateOtherRounds(List<CompetitorMatch> competitorMatchGroupList, int competitorsAmount) {
        int numberOfRounds = BracketUtil.numberOfRounds(competitorsAmount);
        int matchesInRound = 0;
        short matchCounter = (short) (competitorMatchGroupList.size() / 2);
        System.out.println("numberOfRounds " + numberOfRounds + " matchCounter " + matchCounter);
        for (int i = 0; i < numberOfRounds - 1; i++) {
            System.out.println("RUUUUUUUUUUUNDA " + i);
            matchesInRound = Double.valueOf(Math.pow(2, numberOfRounds - i - 2)).intValue();
            for (int j = 0; j < matchesInRound; j++) {

                Matchh match = new Matchh(UUID.randomUUID());
                match.setRoundd((short) (i + 2));
                match.setMatchNumber(++matchCounter);

                if (matchesInRound == 1) {  // finals
                    MatchType finalMatchType = matchTypeFacade.findByMatchTypeName("final");

                    MatchMatchType mmt = new MatchMatchType();
                    mmt.setIdMatch(match);
                    mmt.setIdMatchType(finalMatchType);

                    match.getMatchMatchTypeList().add(mmt);
                }

                CompetitorMatch cmg1 = new CompetitorMatch(UUID.randomUUID());
                match.getCompetitorMatchList().add(cmg1);

                CompetitorMatch cmg2 = new CompetitorMatch(UUID.randomUUID());
                match.getCompetitorMatchList().add(cmg2);

                cmg1.setIdMatch(match);
                cmg2.setIdMatch(match);
//                match.getCompetitorMatchGroupList().add(cmg);

                competitorMatchGroupList.add(cmg1);
                competitorMatchGroupList.add(cmg2);
            }
        }
    }

    @Override
    public void checkCompetitionConstraints(Competition competition) throws ApplicationException {

        if (competition.getIdOrganizer() == null) {
            Account loggedUser = accountFacade.findByLogin(sessionContext.getCallerPrincipal().getName());
            AccessLevel organizer = ConvertUtil.getSpecAccessLevelFromAccount(loggedUser, Organizer.class);

            competition.setIdOrganizer(organizer);
        }
        competitionFacade.competitionContraintsNotCommited(competition);
    }
}
