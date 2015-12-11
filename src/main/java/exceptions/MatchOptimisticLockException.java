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
public class MatchOptimisticLockException extends ApplicationException {

    public static final String OPTIMISTIC_LOCK = "EXCEPTION.MATCH_OPTIMISTIC_LOCK_EXCEPTION.OPTIMISTIC_LOCK";
    
    public MatchOptimisticLockException() {
    }

    public MatchOptimisticLockException(String message) {
        super(message);
    }

    public MatchOptimisticLockException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public static MatchOptimisticLockException optimisticLock(Throwable cause) {
        return new MatchOptimisticLockException(OPTIMISTIC_LOCK, cause);
    }

}
