/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

import entities.Competitor;

/**
 *
 * @author java
 */
public class InvalidPlaceException extends ApplicationException {
    
    private short place;
    
    private Competitor competitor;

    public short getPlace() {
        return place;
    }

    public Competitor getCompetitor() {
        return competitor;
    }

    public static final String INVALID_PLACE = "EXCEPTION.INVALID_PLACE.INVALID_PLACE";

    public static final String PLACE_ALREADY_OCCUPED = "EXCEPTION.INVALID_PLACE.PLACE_ALREADY_OCCUPED";

    public InvalidPlaceException() {
    }

    public InvalidPlaceException(String message) {
        super(message);
    }

    public InvalidPlaceException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidPlaceException(String message, short place) {
        super(message);
        this.place = place;
    }

    public InvalidPlaceException(String message, short place, Competitor competitor) {
        super(message);
        this.place = place;
        this.competitor = competitor;
    }

    public InvalidPlaceException(String message, Throwable cause, short place) {
        super(message, cause);
        this.place = place;
    }
    
    public static InvalidPlaceException invalidPlace(short score) {
        return new InvalidPlaceException(INVALID_PLACE, score);
    }
    
    public static InvalidPlaceException invalidPlace(Throwable cause, short score) {
        return new InvalidPlaceException(INVALID_PLACE, cause, score);
    }
    
    public static InvalidPlaceException placeAlreadyOccuped(short score, Competitor competitor) {
        return new InvalidPlaceException(PLACE_ALREADY_OCCUPED, score, competitor);
    }
}