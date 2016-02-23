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
public class AccountDoesNotExistException extends ApplicationException {

    private String login;

    public String getLogin() {
        return login;
    }
    
    public AccountDoesNotExistException() {
    }

    public AccountDoesNotExistException(String message) {
        super(message);
    }

    public AccountDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountDoesNotExistException(String message, Throwable cause, String login) {
        super(message, cause);
        this.login = login;
    }
    
}
