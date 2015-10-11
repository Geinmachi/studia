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

    public TeamCreationException() {
    }

    public TeamCreationException(String message) {
        super(message);
    }

    public TeamCreationException(String message, Throwable cause) {
        super(message, cause);
    }
    
}