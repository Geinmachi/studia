/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

import entities.Matchh;

/**
 *
 * @author java
 */
public class NotAllowedMatchTypeException extends ApplicationException{

    private Matchh match;
    
    public static final String NOT_ALLOWED_SETTABLE = "EXCEPTION.NOT_ALLOWED_MATCH_TYPE.NOT_ALLOWED_SETTABLE";

    public Matchh getMatch() {
        return match;
    }
    
    public NotAllowedMatchTypeException() {
    }

    public NotAllowedMatchTypeException(String message) {
        super(message);
    }

    public NotAllowedMatchTypeException(String message, Matchh match) {
        super(message);
        this.match = match;
    }

    public NotAllowedMatchTypeException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public static NotAllowedMatchTypeException notAllowedSettable(Throwable cause) {
        return new NotAllowedMatchTypeException(NOT_ALLOWED_SETTABLE, cause);
    }
    
    public static NotAllowedMatchTypeException notAllowedSettable(Matchh match) {
        return new NotAllowedMatchTypeException(NOT_ALLOWED_SETTABLE, match);
    }
}