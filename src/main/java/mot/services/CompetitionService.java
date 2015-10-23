/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.services;

import entities.Competition;
import entities.CompetitionType;
import entities.Competitor;
import entities.CompetitorMatch;
import entities.MatchMatchType;
import entities.MatchType;
import entities.Matchh;
import entities.Score;
import entities.Team;
import exceptions.ApplicationException;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import mot.interfaces.CMG;
import mot.facades.CompetitionTypeFacadeLocal;
import mot.facades.CompetitorFacadeLocal;
import mot.facades.MatchTypeFacadeLocal;
import mot.facades.TeamFacadeLocal;
import mot.managers.CreateCompetitionManagerLocal;
import mot.managers.ManageCompetitionManagerLocal;
import mot.managers.PresentCompetitionManagerLocal;
import mot.interfaces.CurrentMatchType;
import mot.interfaces.InactivateMatch;
import ejbCommon.TrackerInterceptor;
import mot.managers.CompetitionComponentsManagerLocal;

/**
 *
 * @author java
 */
@Stateful
@Interceptors({TrackerInterceptor.class})
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class CompetitionService implements CompetitionServiceLocal {

    @EJB
    private CreateCompetitionManagerLocal createCompetitionManager;

    @EJB
    private ManageCompetitionManagerLocal manageCompetitionManager;

    @EJB
    private PresentCompetitionManagerLocal presentCompetitionManager;
    
    @EJB
    private CompetitionComponentsManagerLocal competitionComponentsManager;

    @EJB
    private TeamFacadeLocal teamFacade;

    @EJB
    private CompetitorFacadeLocal competitorFacade;

    @EJB
    private CompetitionTypeFacadeLocal competitionTypeFacade;

    @EJB
    private MatchTypeFacadeLocal matchTypeFacade;

    private Competition editingCompetition;
    
    private Competitor editingCompetitor;
    
    private Team editingTeam;

    private List<CMG> storedCMGmappings;
    
    public static String ADMIN_PROPERTY_KEY = "role.admin";
    
    public static String ANONYMOUS_USER = "anonymous";

    @Override
    public List<Team> findAllTeams() {
        return teamFacade.findAll();
    }

    @Override
    public void addCompetitor(Competitor competitor, boolean global) throws ApplicationException {
        competitionComponentsManager.addCompetitor(competitor, global);
    }

    @Override
    public List<Competitor> getAllCompetitors() {
//        return new ArrayList<Competitor>(){{
//           add(new Competitor());     
//        }};
        return competitorFacade.findAll();
    }

    @Override
    public List<CompetitionType> getAllCompetitionTypes() {
        return competitionTypeFacade.findAll();
    }

    @Override
    public Competitor findCopetitorById(Integer id) {
        return competitorFacade.find(id);
    }

    @Override
    public boolean validateCompetitorsAmount(int amount) {
        return createCompetitionManager.validateCompetitorsAmount(amount);
    }

    @Override
    public void createCompetition(Competition competition, List<CMG> competitorMatchGroupList) throws ApplicationException {
        createCompetitionManager.createCompetition(competition, competitorMatchGroupList);
    }

    @Override
    public CompetitionType findCompetitionTypeById(int id) {
        return competitionTypeFacade.find(id);
    }

    @Override
    public List<CMG> generateEmptyBracket(List<Competitor> competitors) {
        return createCompetitionManager.generateEmptyBracket(competitors);
    }

    @Override
    public List<MatchType> getEndUserMatchTypes() {
        return matchTypeFacade.findEndUserMatchTypes();
    }

    @Override
    public List<Competition> getLoggedUserCompetitions() throws ApplicationException {
        return manageCompetitionManager.getLoggedUserCompetition();
    }

    @Override
    public Competition storeCompetition(Competition competition) {
        editingCompetition = manageCompetitionManager.storeCompetition(competition);
        return editingCompetition;
    }

    @Override
    public List<CMG> getCompetitionCMGMappings(Competition competition) {
        storedCMGmappings = manageCompetitionManager.getCompetitionCMGMappings(competition);
        return storedCMGmappings;
    }

    @Override
    public Map<String, CompetitorMatch> saveCompetitorScore(CompetitorMatch receivedCompetitorMatch) throws ApplicationException {
        Map<String, CompetitorMatch> returnMap = manageCompetitionManager.saveCompetitorScore(receivedCompetitorMatch, storedCMGmappings);

        for (CompetitorMatch cm : returnMap.values()) {
//            System.out.println("VERSION in service " + cm + " " + cm.getVersion());
            
            if (cm != null) {
                replaceUpdatedMatch(cm.getIdMatch());
            }
        }
//        replaceUpdatedMatch(returnMap.get("saved").getIdMatch());
//
//        if (returnMap.get("advanced") != null) {
//            replaceUpdatedMatch(returnMap.get("advanced").getIdMatch());
//        }

        return returnMap;
    }

//    private void replaceStoredCompetitorMatch(CompetitorMatch competitorMatch) {
//        if (competitorMatch == null) {
//            return;
//        }
//
//        for (CMG cmg : storedCMGmappings) {
//            for (CompetitorMatch cm : cmg.getIdMatch().getCompetitorMatchList()) {
//                if (Integer.compare(cm.getIdCompetitorMatch(), competitorMatch.getIdCompetitorMatch()) == 0) {
//                    int cmIndex = cm.getIdMatch().getCompetitorMatchList().indexOf(cm);
//                    cmg.getIdMatch().getCompetitorMatchList().set(cmIndex, competitorMatch);
//
//                    return;
//                }
//            }
//        }
//
//        throw new IllegalStateException("There was no competitorMatch to replace");
//    }
    @Override
    public List<CompetitorMatch> findCompeitorMatchByIdMatch(Integer idMatch) {
        return manageCompetitionManager.findCMGByIdMatch(idMatch);
    }

    @Override
    public Competition getInitializedCompetition(int idCompetition) {
        return presentCompetitionManager.getInitializedCompetition(idCompetition);
    }

    @Override
    public List<Score> findCompetitionScores(int idCompetition) {
        return presentCompetitionManager.findCompetitionScore(idCompetition);
    }

    @Override
    public Map<Competitor, Integer> getCompetitionResults(Integer idCompetition) {
        return presentCompetitionManager.getCompetitionResults(idCompetition);
    }

    @Override
    public MatchMatchType updateMatchType(Matchh match) {
        MatchMatchType updatedMMT = manageCompetitionManager.updateMatchType(match, storedCMGmappings);
        replaceUpdatedMatch(updatedMMT.getIdMatch());

        return updatedMMT;
    }

//    private void replaceStoredMatchMatchType(MatchMatchType updatedMMT) {
//        if (updatedMMT == null) {
//            return;
//        }
//
//        for (CMG cmg : storedCMGmappings) {
//            if (Integer.compare(cmg.getIdMatch().getIdMatch(), updatedMMT.getIdMatch().getIdMatch()) == 0) {
//                for (MatchMatchType mmt : cmg.getIdMatch().getMatchMatchTypeList()) {
//                    if (Integer.compare(mmt.getIdMatchMatchType(), updatedMMT.getIdMatchMatchType()) == 0) {
//                        int mmtIndex = cmg.getIdMatch().getMatchMatchTypeList().indexOf(mmt);
//                        cmg.getIdMatch().getMatchMatchTypeList().set(mmtIndex, updatedMMT);
//
//                        return;
//                    }
//                }
//            }
//        }
//
//        throw new IllegalStateException("There was no matchMatchType to replace");
//    }
    @Override
    public InactivateMatch disableMatch(InactivateMatch inactivateMatch) {
        return manageCompetitionManager.disableMatch(inactivateMatch);
    }

    @Override
    public CurrentMatchType assignCurrentMatchType(CurrentMatchType cmt) {
        return manageCompetitionManager.assignCurrentMatchType(cmt);
    }

    @Override
    public CompetitorMatch advanceCompetitor(CompetitorMatch competitorMatch) throws ApplicationException {
        CompetitorMatch updatedCompetitorMatch = manageCompetitionManager.advanceCompetitor(competitorMatch);
        replaceUpdatedMatch(updatedCompetitorMatch.getIdMatch());

        return updatedCompetitorMatch;
    }

    @Override
    public Competition saveCompetitionGeneralInfo(Competition competition) throws ApplicationException {
        editingCompetition = manageCompetitionManager.saveCompetitionGeneralInfo(competition, editingCompetition);
        return editingCompetition;
    }

    private void replaceUpdatedMatch(Matchh updatedMatch) {
        if (updatedMatch == null) {
            System.out.println("PARAMETER nuLl in Service#replaceUpdatedMatch");
            return;
        }

        for (CMG cmg : storedCMGmappings) {
            if (Integer.compare(cmg.getIdMatch().getIdMatch(), updatedMatch.getIdMatch()) == 0) {
                cmg.setIdMatch(updatedMatch);

                return;
            }
        }

        throw new IllegalStateException("There was no match to replace");
    }

    @Override
    public void createTeam(Team team, boolean global) throws ApplicationException {
        competitionComponentsManager.createTeam(team, global);
    }

    @Override
    public List<Competitor> getAllTeamlessCompetitors() {
        return competitionComponentsManager.getAllTeamlessCompetitors();
    }

    @Override
    public Competitor vlidateCompetitorDuplicate(List<Competitor> competitorList) {
        return competitionComponentsManager.vlidateCompetitorDuplicate(competitorList);
    }

    @Override
    public List<Competitor> getCompetitorsToEdit() throws ApplicationException {
        return competitionComponentsManager.getCompetitorsToEdit();
    }

    @Override
    public Competitor storeCompetitor(Competitor competitor) {
        editingCompetitor = competitionComponentsManager.findCompetitorById(competitor.getIdCompetitor());
        return editingCompetitor;
    }

    @Override
    public void editCompetitor(Competitor competitor) throws ApplicationException {
        competitionComponentsManager.editCompetitor(editingCompetitor, competitor);
        editingCompetitor = null;
    }

    @Override
    public List<Team> getTeamsToEdit() throws ApplicationException {
        return competitionComponentsManager.getTeamsToEdit();
    }

    @Override
    public Team storeTeam(Team team) {
        editingTeam = competitionComponentsManager.findTeamById(team.getIdTeam());
        return editingTeam;
    }

    @Override
    public void editTeam(Team team) throws ApplicationException {
        competitionComponentsManager.editTeam(editingTeam, team);
        editingTeam = null;
    }

    @Override
    public List<Competition> findGlobalCompetition() {
        return presentCompetitionManager.findGlobalCompetitions();
    }

    @Override
    public List<Competition> findAllowedCompetitions() throws ApplicationException {
        return presentCompetitionManager.findAllowedCompetitions();
    }

    @Override
    public void checkCompetitionConstraints(Competition competition) throws ApplicationException {
        createCompetitionManager.checkCompetitionConstraints(competition);
    }

    @Override
    public List<Competition> findCompetitionsToDisplay() throws ApplicationException {
        return presentCompetitionManager.findCompetitionsToDisplay();
    }
    
}
