/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author java
 */
public class TeamCreationException extends ApplicationException {

    public static final String GLOBAL_DUPLICATE = "EXCEPTION.TEAM_CREATION.GLOBAL_DUPLICATE";

    public static final String PRIVATE_DUPLICATE = "EXCEPTION.TEAM_CREATION.PRIVATE_DUPLICATE";

    public static final String DUPLICATED_COMPETITORS = "EXCEPTION.TEAM_CREATION.DUPLICATED_COMPETITORS";

    public static final String TEAMFUL_COMPETITOR = "EXCEPTION.TEAM_CREATION.TEAMFUL_COMPETITOR";

    public TeamCreationException() {
    }

    public TeamCreationException(String message) {
        super(message);
    }

    public TeamCreationException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public static TeamCreationException globalDuplicate(Throwable cause) {
        return new TeamCreationException(GLOBAL_DUPLICATE, cause);
    }
    
    public static TeamCreationException privateDuplicate(Throwable cause) {
        return new TeamCreationException(PRIVATE_DUPLICATE, cause);
    }
    
    public static TeamCreationException duplicatedCompetitors() {
        return new TeamCreationException(DUPLICATED_COMPETITORS);
    }
    
    public static TeamCreationException teamfulCompetitor() {
        return new TeamCreationException(TEAMFUL_COMPETITOR);
    }
}