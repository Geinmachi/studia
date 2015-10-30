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
public class InvalidScoreException extends ApplicationException {

    public static final String optimisticLock = "EXCEPTION.INVALID_SCORE.TOO_BIG";

    public InvalidScoreException() {
        super(optimisticLock);
    }

    public InvalidScoreException(String message) {
        super(message);
    }

    public InvalidScoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidScoreException(Throwable cause) {
        super(optimisticLock, cause);
    }

}
