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
public class LoginAlreadyExistsException extends ApplicationException {
    
    private String login;
    
    public static final String LOGIN_ALREADY_EXISTS = "EXCEPTION.LOGIN_ALREADY_EXISTS.LOGIN_ALREADY_EXISTS";

    public String getLogin() {
        return login;
    }
    
    public LoginAlreadyExistsException() {
        super();
    }

    public LoginAlreadyExistsException(String message, String login) {
        super(message);
        this.login = login;
    }

    public LoginAlreadyExistsException(String message, String login, Throwable cause) {
        super(message, cause);
        this.login = login;
    }
    
    public static LoginAlreadyExistsException loginAlreadyExists(String login) {
        return new LoginAlreadyExistsException(LOGIN_ALREADY_EXISTS, login);
    }
    
    public static LoginAlreadyExistsException loginAlreadyExists(String login, Throwable cause) {
        return new LoginAlreadyExistsException(LOGIN_ALREADY_EXISTS, login, cause);
    }

}
