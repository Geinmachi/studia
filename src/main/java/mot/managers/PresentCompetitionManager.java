/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.managers;

import entities.Competition;
import entities.Competitor;
import entities.Score;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import mot.facades.CompetitionFacadeLocal;
import mot.facades.ScoreFacadeLocal;
import utils.SortUtil;
import entities.AccessLevel;
import entities.Account;
import entities.Organizer;
import exceptions.ApplicationException;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.ejb.SessionContext;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import mot.facades.AccountFacadeLocal;
import mot.interfaces.CompetitionPodiumData;
import mot.models.CompetitionPodium;
import mot.services.CompetitionService;
import utils.ConvertUtil;
import utils.Hashids;
import utils.ResourceBundleUtil;

/**
 *
 * @author java
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class PresentCompetitionManager implements PresentCompetitionManagerLocal {

    @Resource
    private SessionContext sessionContext;

    @EJB
    private AccountFacadeLocal accountFacade;

    @EJB
    private CompetitionFacadeLocal competitionFacade;

    @EJB
    private ScoreFacadeLocal scoreFacade;

    private static final Hashids ENCRYPTION = new Hashids("Competition - salt_");

    @Override
    public List<Competition> findAllowedCompetitions() throws ApplicationException {

        List<Competition> competitionList = null;

        if (sessionContext.isCallerInRole(ResourceBundleUtil.getResourceBundleBusinessProperty(CompetitionService.ADMIN_PROPERTY_KEY))) {
            competitionList = competitionFacade.findAll();
        } else {
            Account loggedUser = accountFacade.findByLogin(sessionContext.getCallerPrincipal().getName());
            AccessLevel organizer = ConvertUtil.getSpecAccessLevelFromAccount(loggedUser, Organizer.class);

            competitionList = competitionFacade.findUserCompetitions(organizer.getIdAccessLevel());
        }

        return competitionList;
    }

    @Override
    public Competition getInitializedCompetition(int idCompetition) {
        return competitionFacade.findAndInitializeGD(idCompetition);
    }

    @Override // niewykorzystawana?
    public List<Score> findCompetitionScore(int idCompetition) {
        return scoreFacade.findScoreByIdCompetition(idCompetition);
    }

    @Override
    public Map<Competitor, Integer> getCompetitionResults(int idCompetition) {
        List<Score> scoreList = new ArrayList<>(scoreFacade.findScoreByIdCompetition(idCompetition));

//        scoreList.sort(new Comparator<Score>() {
//
//            @Override
//            public int compare(Score o1, Score o2) {
//                return Short.compare(o2.getScore(), o1.getScore());
//            }
//
//        });
//
//        Map<Short, Integer> positionScoreMap = new HashMap<>();
//
//        int positionCounter = 1;
//        for (int i = 0; i < scoreList.size() - 1; i++) {
//            if (Short.compare(scoreList.get(i).getScore(), scoreList.get(i + 1).getScore()) != 0) {
//                positionScoreMap.put(scoreList.get(i).getScore(), positionCounter++);
//            }
//        }
//
//        positionScoreMap.put((short) 0, positionCounter);
        Map<Competitor, Integer> competitorPositionMap = new HashMap<>();

        for (Score s : scoreList) {
            competitorPositionMap.put(s.getIdCompetitor(), (int) s.getPlace());
//            competitorPositionMap.put(s.getIdCompetitor(), positionScoreMap.get(s.getScore()));

        }

        return SortUtil.sortByValue(competitorPositionMap, true);

    }

    @Override
    public List<Competition> findGlobalCompetitions() {
        return competitionFacade.findGlobalCompetitions();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Competition> findCompetitionsToDisplay() throws ApplicationException {
        String userLogin = sessionContext.getCallerPrincipal().getName();

        List competitionList;

        if (userLogin.equals(ResourceBundleUtil.getResourceBundleBusinessProperty(CompetitionService.ANONYMOUS_USER))) {
            competitionList = competitionFacade.findGlobalCompetitions();
        } else if (sessionContext.isCallerInRole(ResourceBundleUtil.getResourceBundleBusinessProperty(CompetitionService.ADMIN_PROPERTY_KEY))) {
            competitionList = competitionFacade.findAll();
        } else {
            Account account = accountFacade.findByLogin(userLogin);
            AccessLevel accessLevel = ConvertUtil.getSpecAccessLevelFromAccount(account, Organizer.class);

            competitionList = competitionFacade.findGlobalCompetitions();
            competitionList.addAll(competitionFacade.findUserCompetitionsByIdAccessLevel(accessLevel.getIdAccessLevel()));
        }

        return competitionList;
    }

    @Override
    public Competition getCompetitionByEncodedId(String encodedId) {
        String decodedId = ENCRYPTION.decodeHex(encodedId);

        if (!decodedId.isEmpty()) {
            return competitionFacade.findAndInitializeGD(Integer.parseInt(decodedId));
        }

        return null;
    }

    @Override
    public String encodeCompetitionId(int competitionId) {
        return ENCRYPTION.encodeHex(String.valueOf(competitionId));
    }
    
}
