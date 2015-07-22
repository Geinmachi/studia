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
import entities.GroupCompetition;
import entities.Groupp;
import entities.Organizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import mot.facades.AccountFacadeLocal;
import mot.facades.CompetitionFacadeLocal;
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
    public void createCompetition(Competition competition, List<Competitor> competitors) {
        Account loggedUser = accountFacade.findByLogin(sessionContext.getCallerPrincipal().getName());
        AccessLevel organizer = ConvertUtil.getSpecAccessLevelFromAccount(loggedUser, Organizer.class);

        competition.setIdOrganizer(organizer);
        competition.setCreationDate(new Date());
        createGroups(competitors);
        competitionFacade.create(competition);
    }

    private GroupCompetition createGroups(List<Competitor> competitors) {
        List<Character> letters = new ArrayList<>();
        int asciiValue = 65;
        double numberOfGroups = Math.sqrt((double) competitors.size());
        
        for (int i = 0; i < numberOfGroups; i++) {
            letters.add((char) asciiValue++);
        }
        Collections.shuffle(competitors);
        
        Map<Integer,Character> assignedCompetitorsToGroups = assignCompetitorsToGroups(competitors, letters);
        List<Groupp> groups = new ArrayList<>();
        
        for (int i=0; i< letters.size(); i++) {
            Groupp group = new Groupp();
            group.setStartDate(new Date());
            group.setEndDate(new Date());
            group.setGroupName(letters.get(i));
        }
        return null;
    }
    
    private Map<Integer,Character> assignCompetitorsToGroups(List<Competitor> competitors, List<Character> letters) {
        Map<Integer,Character> assignedCompetitors = new HashMap<>();
        int groupCounter = 0;
        
        for (int i=0; i<competitors.size(); i++) {
            if (i != 0 && i % letters.size() == 0) {
                groupCounter++;
            }
            System.out.println("i = " + i + " letters.get = " + letters.get(groupCounter));
            assignedCompetitors.put(competitors.get(i).getIdCompetitor(), letters.get(groupCounter));
        }
        for (Entry<Integer, Character> entry : assignedCompetitors.entrySet()) {
            System.out.println("Competitor o id " + entry.getKey() + " jest w grupie " + entry.getValue());
        }
        return assignedCompetitors;
    }
}
