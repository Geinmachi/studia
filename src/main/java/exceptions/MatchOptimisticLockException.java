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

    public MatchOptimisticLockException() {
    }

    public MatchOptimisticLockException(String message) {
        super(message);
    }

    public MatchOptimisticLockException(String message, Throwable cause) {
        super(message, cause);
    }

}
