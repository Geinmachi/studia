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
import java.util.Collection;
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

    final static int GROUP_SIZE = 4;

    @Override
    public boolean validateCompetitorsAmount(int amount) {
        if (amount <= 0) {
            return false;
        }
        return isPowerOfTwo(amount);
    }

    private boolean isPowerOfTwo(int number) {
        int power = 1;
        while (Math.pow(2, power) != number) {
            if (Math.pow(2, power) > Integer.MAX_VALUE) {
                return false;
            }
            power++;
        }
        return true;
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
                    score.setScore((short) 0);

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
                if (cmg.getIdMatch().getRoundd() == 1) {
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

        if (competitorCount / GROUP_SIZE != groups.size()) {
            System.out.println("Number Of Rounds = " + BracketUtil.numberOfRounds(competitorCount) + " group size " + groups.size());
            throw new IllegalArgumentException("Liczba grup nie odpowiada liczbie uczesnitkow");
        }

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
        int numberOfGroups = competitorsAmount / GROUP_SIZE;

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
                System.out.println("MNIEJSZE NIZ w bazie " + i + " size " + groupsInDatabase);
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

    private Map<Competitor, GroupName> assignCompetitorsToGroups(List<Competitor> competitors, List<GroupName> groups) {
        Map<Competitor, GroupName> assignedCompetitors = new HashMap<>();

        System.out.println("competitoros " + competitors.size() + " groups " + groups.size());

        int groupCounter = 0;

        for (int i = 0; i < competitors.size(); i++) {
            if (i != 0 && i % GROUP_SIZE == 0) {
                groupCounter++;
            }
//            System.out.println("i = " + i + " letters.get = " + groups.get(groupCounter).getGroupName());
            assignedCompetitors.put(competitors.get(i), groups.get(groupCounter));
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

        for (CompetitorMatch cm : competitorMatchList) {
            if (cm.getIdCompetitor() == null) {
                cmgMappings.add(new CompetitorMatchGroup(null, cm));
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
                cmg2.setIdCompetitor(entry.getValue().get(i + 1));
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
        short matchCounter = (short) (competitorsAmount / 2);
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
