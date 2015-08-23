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
import entities.Matchh;
import entities.Organizer;
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
import mot.facades.AccountFacadeLocal;
import mot.facades.CompetitionFacadeLocal;
import mot.facades.CompetitorFacadeLocal;
import mot.facades.CompetitorMatchFacadeLocal;
import mot.facades.GroupNameFacadeLocal;
import mot.facades.MatchMatchTypeFacadeLocal;
import mot.facades.MatchhFacadeLocal;
import utils.BracketUtil;
import utils.ConvertUtil;
import mot.utils.CMG;
import mot.utils.CompetitorMatchGroup;

/**
 *
 * @author java
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
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
    public void createCompetition(Competition competition, List<CMG> competitorMatchGroupList) {
        Account loggedUser = accountFacade.findByLogin(sessionContext.getCallerPrincipal().getName());
        AccessLevel organizer = ConvertUtil.getSpecAccessLevelFromAccount(loggedUser, Organizer.class);

        competition.setIdOrganizer(organizer);
        competition.setCreationDate(new Date());

        List<GroupName> groupList = getUniqueGroups(competitorMatchGroupList);
        List<Matchh> matchList = getUniqueMatches(competitorMatchGroupList);

        List<GroupName> groupWithIdentityList = new ArrayList<>();

        for (GroupName g : groupList) {
            GroupName managedGroup = groupNameFacade.createWithReturn(g);
            groupWithIdentityList.add(managedGroup);

//            GroupCompetition groupCompetition = new GroupCompetition();
//            groupCompetition.setIdCompetition(competition);
//            groupCompetition.setIdGroup(managedGroup);
//
//            competition.getGroupCompetitionList().add(groupCompetition);
        }

        competition = competitionFacade.createWithReturn(competition);

        List<Matchh> matchWithIdentityList = new ArrayList<>();
        
        assignSameGroupsToCompetitors(competitorMatchGroupList, groupWithIdentityList);

        for (Matchh m : matchList) {
//            m.setCompetition(competition);
            for (CompetitorMatch cmg : m.getCompetitorMatchGroupList()) {
                if (cmg.getIdMatch().getRoundd() == 1) {
                    cmg.setCompetitorMatchScore((short) 0);
                } else {
                    System.out.println("RUNDA TOTOOOOOOOOOOOOO " + cmg.getIdMatch().getRoundd());
                }
            }
            if (m.getCompetitorMatchGroupList().isEmpty()) {
                System.out.println("CMG JEsT PUSTEEEE dla meczu nr " + m.getMatchNumber());
            }
            matchWithIdentityList.add(matchFacade.createWithReturn(m));
//            System.out.println("MMAMAMAMAMAMAM " + m + "  number " + m.getMatchNumber());
        }

        System.out.println("competitorMatchGroupList sizeeee " + competitorMatchGroupList.size());
//        assignSameMatchesToCompetitors(competitorMatchGroupList, matchWithIdentityList);
//
//        for (CompetitorMatch cmg : competitorMatchGroupList) {
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
        System.out.println(
                "PRZESZLO SZYSTKO");
//        throw new NullPointerException();
    }

    private List<GroupName> getUniqueGroups(List<CMG> competitorMatchGroupList) {
        Set<GroupName> groups = new HashSet<>();

        int competitorCount = 0;

        for (CMG cmg : competitorMatchGroupList) {
            groups.add(cmg.getIdGroupName());
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
        
        for(Competitor c : competitors) {
            fetchedCompetitors.add(competitorFacade.findAndInitializeGroups(c.getIdCompetitor()));
        }
                
        Collections.shuffle(fetchedCompetitors);

        List<GroupName> groups = createGroups(fetchedCompetitors.size());

        groups.forEach(p -> System.out.println("Grupa: " + p.getGroupName()));

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
        
        for (int i=0; i< numberOfGroups; i++) {
            if (i < groupsInDatabase) {
                groups.add(existingGroups.get(i));
            } else {
                GroupName group = new GroupName();
                group.setGroupName((char)(asciiValue + i));
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
        
        System.out.print("competitoros " + competitors.size() + " groups " + groups.size());
        
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
        Map<GroupName, List<Competitor>> groups = transformAssignedCompetitorsToLists(assignedCompetitors);

//        System.out.println("ile grup " + groups.size());
        for (List<Competitor> list : groups.values()) {
            Collections.shuffle(list);
        }

        List<GroupCompetitor> groupCompetitorList = createGroupCompetitorMappings(groups);
                
        List<CompetitorMatch> competitorMatchGroupList = generateFirstRoundMatches(groups);

        generateOtherRounds(competitorMatchGroupList, assignedCompetitors.keySet().size());

        List<CMG> cmgMappings = new ArrayList<>();
        
        for (CompetitorMatch cm : competitorMatchGroupList) {
            if (Short.compare(cm.getIdMatch().getRoundd(), Short.parseShort("1")) == 0) {
                for (GroupCompetitor gc : groupCompetitorList) {
                    if (gc.getIdCompetitor().equals(cm.getIdCompetitor())) {
                        cmgMappings.add(new CompetitorMatchGroup(gc, cm));
                        
                        break;
                    }
                }
            }
        }
//        for (int i = 0; i < competitorMatchGroupList.size(); i++) {
//            System.out.println("iiiiiiiiiiiiii = " + i);
//            System.out.println("Kto = " + competitorMatchGroupList.get(i).getIdCompetitor());
//            if (competitorMatchGroupList.get(i).getIdGroup() != null) {
//                System.out.println("Grupa = " + competitorMatchGroupList.get(i).getIdGroup().getGroupName());
//            }
//            System.out.println("Match = " + competitorMatchGroupList.get(i).getIdMatch().getRoundd());
//
//        }
        return cmgMappings;
    }

    private Map<GroupName, List<Competitor>> transformAssignedCompetitorsToLists(Map<Competitor, GroupName> assignedCompetitors) {
        Map<GroupName, List<Competitor>> groups = new TreeMap<>();

        for (GroupName g : assignedCompetitors.values()) { // init values
            groups.put(g, new ArrayList<>());
        }
        for (Entry<Competitor, GroupName> entry : assignedCompetitors.entrySet()) {
            List<Competitor> competitorsInGroup = groups.get(entry.getValue());
            competitorsInGroup.add(entry.getKey());
            groups.put(entry.getValue(), competitorsInGroup);
        }

        return groups;
    }


    private List<GroupCompetitor> createGroupCompetitorMappings(Map<GroupName, List<Competitor>> competitorsAndGroups) {
        List<GroupCompetitor> groupCompetitorList = new ArrayList<>();
        
        for(Entry<GroupName, List<Competitor>> entry : competitorsAndGroups.entrySet()) {
            for (Competitor c : entry.getValue()) {
                GroupCompetitor gc = createGroupCompetitor(entry.getKey(), c);
                
                groupCompetitorList.add(gc);
                
//                System.out.println("CCCCCC " + c + " scroelist " + c.getScoreList().size());
                c.getGroupCompetitorList().add(gc);
            }
        }
        
        return groupCompetitorList;
    }
    
    
    private List<CompetitorMatch> generateFirstRoundMatches(Map<GroupName, List<Competitor>> groups) {
        List<CompetitorMatch> competitorMatchGroupList = new ArrayList<>();

        short matchCounter = 1;
        for (Entry<GroupName, List<Competitor>> entry : groups.entrySet()) {
//            System.err.println("Rozmiar competitorow w grupie " + entry.getKey().getGroupName()
//                    + "to " + entry.getValue().size());
            for (int i = 0; i < entry.getValue().size(); i = i + 2) {
//                System.out.println("Competitor" + entry.getValue().get(i) + " w matchu, i = " + i);
                Matchh match = new Matchh(UUID.randomUUID());
                match.setRoundd((short) 1);
                match.setMatchNumber(matchCounter++);

                CompetitorMatch cmg1 = new CompetitorMatch(UUID.randomUUID());
                match.getCompetitorMatchGroupList().add(cmg1);

                cmg1.setIdCompetitor(entry.getValue().get(i));
 //               cmg1.setIdGroup(entry.getKey());
                cmg1.setIdMatch(match);
                cmg1.setPlacer((short)1);
//                match.getCompetitorMatchGroupList().add(cmg1);
//                entry.getKey().getCompetitorMatchGroupList().add(cmg1);

                CompetitorMatch cmg2 = new CompetitorMatch(UUID.randomUUID());
                match.getCompetitorMatchGroupList().add(cmg2);

//                System.out.println("2222Competitor" + entry.getValue().get(i + 1) + " w matchu, i + 1 = " + (i + 1));
                cmg2.setIdCompetitor(entry.getValue().get(i + 1));
     //           cmg2.setIdGroup(entry.getKey());
                cmg2.setIdMatch(match);
                cmg2.setPlacer((short)2);
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

    private GroupCompetitor createGroupCompetitor(GroupName groupName, Competitor competitor) {
        GroupCompetitor gc = new GroupCompetitor();
        GroupDetails gd = new GroupDetails();
        
        gd.setIdGroupName(groupName);
        
        gc.setIdGroupDetails(gd);
        gc.setIdCompetitor(competitor);
        
        return gc;
    }
    
    private void generateOtherRounds(List<CompetitorMatch> competitorMatchGroupList, int competitorsAmount) {
        int numberOfRounds = BracketUtil.numberOfRounds(competitorsAmount);
        int matchesInRound = 0;
        short matchCounter = (short) (competitorsAmount / 2);

        for (int i = 0; i < numberOfRounds - 1; i++) {
            matchesInRound = Double.valueOf(Math.pow(2, numberOfRounds - i - 2)).intValue();
            for (int j = 0; j < matchesInRound; j++) {

                Matchh match = new Matchh(UUID.randomUUID());
                match.setRoundd((short) (i + 2));
                match.setMatchNumber(++matchCounter);

                CompetitorMatch cmg1 = new CompetitorMatch(UUID.randomUUID());
                match.getCompetitorMatchGroupList().add(cmg1);
                
                CompetitorMatch cmg2 = new CompetitorMatch(UUID.randomUUID());
                match.getCompetitorMatchGroupList().add(cmg2);

                cmg1.setIdMatch(match);
                cmg2.setIdMatch(match);
//                match.getCompetitorMatchGroupList().add(cmg);

                competitorMatchGroupList.add(cmg1);
                competitorMatchGroupList.add(cmg2);
            }
        }
    }
}
