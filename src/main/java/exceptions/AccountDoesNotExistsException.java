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
public class AccountDoesNotExistsException extends ApplicationException {

    public AccountDoesNotExistsException() {
    }

    public AccountDoesNotExistsException(String message) {
        super(message);
    }

    public AccountDoesNotExistsException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
