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
import entities.Groupp;
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
import mot.facades.CompetitorMatchGroupFacadeLocal;
import mot.facades.GrouppFacadeLocal;
import mot.facades.MatchMatchTypeFacadeLocal;
import mot.facades.MatchhFacadeLocal;
import utils.BracketUtil;
import utils.ConvertUtil;

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
    private GrouppFacadeLocal groupFacade;

    @EJB
    private MatchhFacadeLocal matchFacade;

    @EJB
    private MatchMatchTypeFacadeLocal mmtFacade;

    @EJB
    private CompetitorMatchGroupFacadeLocal cmgFacade;

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
    public void createCompetition(Competition competition, List<CompetitorMatchGroup> competitorMatchGroupList) {
        Account loggedUser = accountFacade.findByLogin(sessionContext.getCallerPrincipal().getName());
        AccessLevel organizer = ConvertUtil.getSpecAccessLevelFromAccount(loggedUser, Organizer.class);

        competition.setIdOrganizer(organizer);
        competition.setCreationDate(new Date());

        List<Groupp> groupList = getUniqueGroups(competitorMatchGroupList);
        List<Matchh> matchList = getUniqueMatches(competitorMatchGroupList);

        List<Groupp> groupWithIdentityList = new ArrayList<>();

        for (Groupp g : groupList) {
            Groupp managedGroup = groupFacade.createWithReturn(g);
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
            m.setCompetition(competition);
            for (CompetitorMatchGroup cmg : m.getCompetitorMatchGroupList()) {
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
//        for (CompetitorMatchGroup cmg : competitorMatchGroupList) {
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

    private List<Groupp> getUniqueGroups(List<CompetitorMatchGroup> competitorMatchGroupList) {
        Set<Groupp> groups = new HashSet<>();

        int competitorCount = 0;

        for (CompetitorMatchGroup cmg : competitorMatchGroupList) {
            groups.add(cmg.getIdGroup());
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

    private List<Matchh> getUniqueMatches(List<CompetitorMatchGroup> competitorMatchGroupList) {
        Set<Matchh> matches = new HashSet<>();

        for (CompetitorMatchGroup cmg : competitorMatchGroupList) {
            matches.add(cmg.getIdMatch());
        }

        matches.remove(null);

        return new ArrayList<>(matches);
    }

    private void assignSameMatchesToCompetitors(List<CompetitorMatchGroup> competitorMatchGroupList, List<Matchh> uniqueMatchList) {
        for (CompetitorMatchGroup cmg : competitorMatchGroupList) {
            for (Matchh m : uniqueMatchList) {
                if (cmg.getIdMatch().equals(m)) {
                    System.out.println("ASSIGNING " + m.getMatchNumber());
                    cmg.setIdMatch(m);
                    break;
                }
            }
        }
    }

    private void assignSameGroupsToCompetitors(List<CompetitorMatchGroup> competitorMatchGroupList, List<Groupp> uniqueGroupList) {
        for (CompetitorMatchGroup cmg : competitorMatchGroupList) {
            if (cmg.getIdCompetitor() != null) {
                for (Groupp g : uniqueGroupList) {
                    if (cmg.getIdGroup().equals(g)) {
                        cmg.setIdGroup(g);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public List<CompetitorMatchGroup> generateEmptyBracket(List<Competitor> competitors) {
        Collections.shuffle(competitors);

        List<Groupp> groups = createGroups(competitors.size());

        groups.forEach(p -> System.out.println("Grupa: " + p.getGroupName()));

        Map<Competitor, Groupp> assignedCompetitorsToGroups = assignCompetitorsToGroups(competitors, groups);

        return createMatches(assignedCompetitorsToGroups);

    }
//    private GroupCompetition createBracket(List<Competitor> competitors) {
//        
//    }

    private List<Groupp> createGroups(int competitorsAmount) {
        int asciiValue = 65;
//        double numberOfGroups = Math.sqrt((double) competitorsAmount);
        int numberOfGroups = competitorsAmount / GROUP_SIZE;

        List<Groupp> groups = new ArrayList<>();

        for (int i = 0; i < numberOfGroups; i++) {
            Groupp group = new Groupp(UUID.randomUUID());
            group.setGroupName((char) (asciiValue + i));
            groups.add(group);
        }

        return groups;
    }

    private Map<Competitor, Groupp> assignCompetitorsToGroups(List<Competitor> competitors, List<Groupp> groups) {
        Map<Competitor, Groupp> assignedCompetitors = new HashMap<>();
        int groupCounter = 0;

        for (int i = 0; i < competitors.size(); i++) {
            if (i != 0 && i % GROUP_SIZE == 0) {
                groupCounter++;
            }
//            System.out.println("i = " + i + " letters.get = " + groups.get(groupCounter).getGroupName());
            assignedCompetitors.put(competitors.get(i), groups.get(groupCounter));
        }
        for (Entry<Competitor, Groupp> entry : assignedCompetitors.entrySet()) {
//            System.out.println("Competitor o id " + entry.getKey() + " jest w grupie " + entry.getValue().getGroupName());
        }
        return assignedCompetitors;
    }

    private List<CompetitorMatchGroup> createMatches(Map<Competitor, Groupp> assignedCompetitors) {
        Map<Groupp, List<Competitor>> groups = transformAssignedCompetitorsToLists(assignedCompetitors);

//        System.out.println("ile grup " + groups.size());
        for (List<Competitor> list : groups.values()) {
            Collections.shuffle(list);
        }

        List<CompetitorMatchGroup> competitorMatchGroupList = generateFirstRoundMatches(groups);

        generateOtherRounds(competitorMatchGroupList, assignedCompetitors.keySet().size());

//        for (int i = 0; i < competitorMatchGroupList.size(); i++) {
//            System.out.println("iiiiiiiiiiiiii = " + i);
//            System.out.println("Kto = " + competitorMatchGroupList.get(i).getIdCompetitor());
//            if (competitorMatchGroupList.get(i).getIdGroup() != null) {
//                System.out.println("Grupa = " + competitorMatchGroupList.get(i).getIdGroup().getGroupName());
//            }
//            System.out.println("Match = " + competitorMatchGroupList.get(i).getIdMatch().getRoundd());
//
//        }
        return competitorMatchGroupList;
    }

    private Map<Groupp, List<Competitor>> transformAssignedCompetitorsToLists(Map<Competitor, Groupp> assignedCompetitors) {
        Map<Groupp, List<Competitor>> groups = new TreeMap<>();

        for (Groupp g : assignedCompetitors.values()) { // init values
            groups.put(g, new ArrayList<>());
        }
        for (Entry<Competitor, Groupp> entry : assignedCompetitors.entrySet()) {
            List<Competitor> competitorsInGroup = groups.get(entry.getValue());
            competitorsInGroup.add(entry.getKey());
            groups.put(entry.getValue(), competitorsInGroup);
        }

        return groups;
    }

    private List<CompetitorMatchGroup> generateFirstRoundMatches(Map<Groupp, List<Competitor>> groups) {
        List<CompetitorMatchGroup> competitorMatchGroupList = new ArrayList<>();

        short matchCounter = 1;
        for (Entry<Groupp, List<Competitor>> entry : groups.entrySet()) {
//            System.err.println("Rozmiar competitorow w grupie " + entry.getKey().getGroupName()
//                    + "to " + entry.getValue().size());
            for (int i = 0; i < entry.getValue().size(); i = i + 2) {
//                System.out.println("Competitor" + entry.getValue().get(i) + " w matchu, i = " + i);
                Matchh match = new Matchh(UUID.randomUUID());
                match.setRoundd((short) 1);
                match.setMatchNumber(matchCounter++);

                CompetitorMatchGroup cmg1 = new CompetitorMatchGroup(UUID.randomUUID());
                match.getCompetitorMatchGroupList().add(cmg1);

                cmg1.setIdCompetitor(entry.getValue().get(i));
                cmg1.setIdGroup(entry.getKey());
                cmg1.setIdMatch(match);
                cmg1.setPlacer((short)1);
//                match.getCompetitorMatchGroupList().add(cmg1);
//                entry.getKey().getCompetitorMatchGroupList().add(cmg1);

                CompetitorMatchGroup cmg2 = new CompetitorMatchGroup(UUID.randomUUID());
                match.getCompetitorMatchGroupList().add(cmg2);

//                System.out.println("2222Competitor" + entry.getValue().get(i + 1) + " w matchu, i + 1 = " + (i + 1));
                cmg2.setIdCompetitor(entry.getValue().get(i + 1));
                cmg2.setIdGroup(entry.getKey());
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

    private void generateOtherRounds(List<CompetitorMatchGroup> competitorMatchGroupList, int competitorsAmount) {
        int numberOfRounds = BracketUtil.numberOfRounds(competitorsAmount);
        int matchesInRound = 0;
        short matchCounter = (short) (competitorsAmount / 2);

        for (int i = 0; i < numberOfRounds - 1; i++) {
            matchesInRound = Double.valueOf(Math.pow(2, numberOfRounds - i - 2)).intValue();
            for (int j = 0; j < matchesInRound; j++) {

                Matchh match = new Matchh(UUID.randomUUID());
                match.setRoundd((short) (i + 2));
                match.setMatchNumber(++matchCounter);

                CompetitorMatchGroup cmg1 = new CompetitorMatchGroup(UUID.randomUUID());
                match.getCompetitorMatchGroupList().add(cmg1);
                
                CompetitorMatchGroup cmg2 = new CompetitorMatchGroup(UUID.randomUUID());
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
