/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;
import java.util.List;
/**
 *
 * @author java
 */
public class InvalidScoreException extends ApplicationException {

    public static final String TOO_BIG = "EXCEPTION.INVALID_SCORE.TOO_BIG";
    
    public static final String BELOW_ZERO = "EXCEPTION.INVALID_SCORE.BELOW_ZERO";
    
    public static final String EMPTY = "EXCEPTION.INVALID_SCORE.EMPTY";
    
    public InvalidScoreException() {
        super();
    }

    public InvalidScoreException(String message) {
        super(message);
    }

    public InvalidScoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public static InvalidScoreException tooBigNumber() {
        return new InvalidScoreException(TOO_BIG);
    }

    public static InvalidScoreException belowZero() {
        return new InvalidScoreException(BELOW_ZERO);
    }

    public static InvalidScoreException empty() {
        return new InvalidScoreException(EMPTY);
    }
    
}
