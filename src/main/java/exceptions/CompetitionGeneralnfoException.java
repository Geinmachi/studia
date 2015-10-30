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
public class CompetitionGeneralnfoException extends ApplicationException {

    public static final String OPTIMISTIC_LOCK = "EXCEPTION.COMPETITION_GENERAL_INFO.OPTIMISTIC_LOCK";

    public static final String GLOBAL_DUPLICATE = "EXCEPTION.COMPETITION_GENERAL_INFO.GLOBAL_DUPLICATE";

    public static final String PRIVATE_DUPLICATE = "EXCEPTION.COMPETITION_GENERAL_INFO.PRIVATE_DUPLICATE";
    
    public static final String DATABASE_DUPLICATE = "EXCEPTION.COMPETITION_GENERAL_INFO.DATABASE_DUPLICATE";
    
    public CompetitionGeneralnfoException() {
    }

    public CompetitionGeneralnfoException(String message) {
        super(message);
    }

    public CompetitionGeneralnfoException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public static CompetitionGeneralnfoException optimisticLock(Throwable cause) {
        return new CompetitionGeneralnfoException(OPTIMISTIC_LOCK, cause);
    }
    
    public static CompetitionGeneralnfoException globalDuplicate(Throwable cause) {
        return new CompetitionGeneralnfoException(GLOBAL_DUPLICATE, cause);
    }
    
    public static CompetitionGeneralnfoException privateDuplicate(Throwable cause) {
        return new CompetitionGeneralnfoException(PRIVATE_DUPLICATE, cause);
    }
    
    public static CompetitionGeneralnfoException globalDuplicate() {
        return new CompetitionGeneralnfoException(GLOBAL_DUPLICATE);
    }
    
    public static CompetitionGeneralnfoException privateDuplicate() {
        return new CompetitionGeneralnfoException(PRIVATE_DUPLICATE);
    }
    
    public static CompetitionGeneralnfoException databaseDuplicate(Throwable cause) {
        return new CompetitionGeneralnfoException(DATABASE_DUPLICATE, cause);
    }
}
