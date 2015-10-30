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
public class CompetitorCreationException extends ApplicationException{

    public static final String GLOBAL_DUPLICATE = "EXCEPTION.COMPETITOR_CREATION.GLOBAL_DUPLICATE";

    public static final String PRIVATE_DUPLICATE = "EXCEPTION.COMPETITOR_CREATION.PRIVATE_DUPLICATE";
    
    public CompetitorCreationException() {
    }

    public CompetitorCreationException(String message) {
        super(message);
    }

    public CompetitorCreationException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public static CompetitorCreationException globalDuplicate(Throwable cause) {
        return new CompetitorCreationException(GLOBAL_DUPLICATE, cause);
    }
    
    public static CompetitorCreationException privateDuplicate(Throwable cause) {
        return new CompetitorCreationException(PRIVATE_DUPLICATE, cause);
    }
}