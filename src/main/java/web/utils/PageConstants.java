/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.utils;

/**
 *
 * @author java
 */
public class PageConstants {
    
    public static final String REDIRECT = "?faces-redirect=true";
    
    // general
    
    public static final String ROOT_COMPETITION_DETAILS = "/competitionDetails.xhtml";
    
    public static final String ROOT_COMPETITION_LIST = "/competitionList.xhtml";
    
    public static final String ROOT_COMPETITION_RESULTS = "/competitionResults.xhtml";
    
    public static final String ROOT_COMPETITOR_REPORTS = "/competitorReports.xhtml";
    
    public static final String ROOT_PLACEMENT_REPORT = "/placementReport.xhtml";
    
    public static final String ROOT_MATCHES_REPORT = "/matchesReport.xhtml";
    
    public static final String ROOT_DONE = "/done.xhtml";
    
    public static final String ROOT_INDEX = "/index.xhtml";
    
    public static final String ROOT_LOGIN_ERROR = "/loginError.xhtml";
    
    public static final String ROOT_REGISTER = "/register.xhtml";
    
    // edit folder
    
    public static final String EDIT_FOLDER = "/edit";
    
    public static final String EDIT_CHOOSE_COMPETITION = EDIT_FOLDER + "/chooseCompetition.xhtml";
    
    public static final String EDIT_COMPETITOR_LIST = EDIT_FOLDER + "/competitorList.xhtml";
    
    public static final String EDIT_EDIT_COMPETITOR = EDIT_FOLDER + "/editCompetitor.xhtml";
    
    public static final String EDIT_EDIT_TEAM = EDIT_FOLDER + "/editTeam.xhtml";
    
    public static final String EDIT_MANAGE_COMPETITION = EDIT_FOLDER + "/manageCompetition.xhtml";
    
    public static final String EDIT_TEAM_LIST = EDIT_FOLDER + "/teamList.xhtml";
    
    // organizer folder

    public static final String ORGANIZER_FOLDER = "/organizer";
        
    public static final String ORGANIZER_ADD_COMPETITOR = ORGANIZER_FOLDER + "/addCompetitor.xhtml";
    
    public static final String ORGANIZER_CREATE_COMPETITION = ORGANIZER_FOLDER + "/createCompetition.xhtml";
    
    public static final String ORGANIZER_CREATE_TEAM = ORGANIZER_FOLDER + "/createTeam.xhtml";
    
    // admin folder
    
    public static final String ADMIN_FOLDER = "/admin";
    
    public static final String ADMIN_EDIT_ACCESS_LEVEL = ADMIN_FOLDER + "/editAccessLevel.xhtml";
    
    public static final String ADMIN_EDIT_USER = ADMIN_FOLDER + "/editUser.xhtml";
    
    public static final String ADMIN_USERS_LIST = ADMIN_FOLDER + "/usersList.xhtml";
    
    
    public static String getPage(String pageConstant, boolean redirect) {
        return redirect ? pageConstant + REDIRECT : pageConstant;
    }
}
