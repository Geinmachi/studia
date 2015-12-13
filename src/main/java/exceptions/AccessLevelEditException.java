/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

import entities.AccessLevel;

/**
 *
 * @author java
 */
public class AccessLevelEditException extends ApplicationException {
    
    private AccessLevel accessLevel;

    public static final String MISSING_ACCESS_LEVEL = "EXCEPTION.ACCESS_LEVEL_EDIT.MISSING_ACCESS_LEVEL";

    public static final String NULL_RECEIVED_LIST = "EXCEPTION.ACCESS_LEVEL_EDIT.NULL_RECEIVED_LIST";

    public static final String NULL_STORED_OBJECT = "EXCEPTION.ACCESS_LEVEL_EDIT.NULL_STORED_OBJECT";

    public static final String OPTIMISTIC_LOCK = "EXCEPTION.ACCESS_LEVEL_EDIT.OPTIMISTIC_LOCK";

    public static final String UNKNOWN_5001 = "EXCEPTION.ACCESS_LEVEL_EDIT.UNKNOWN_5001";

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }
    
    public AccessLevelEditException() {
        super();
    }

    public AccessLevelEditException(String message) {
        super(message);
    }

    public AccessLevelEditException(String message, AccessLevel accessLevel) {
        super(message);
        this.accessLevel = accessLevel;
    }

    public AccessLevelEditException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccessLevelEditException(String message, Throwable cause, AccessLevel accessLevel) {
        super(message, cause);
        this.accessLevel = accessLevel;
    }

    public static AccessLevelEditException missingAccessLevel() {
        return new AccessLevelEditException(MISSING_ACCESS_LEVEL);
    }

    public static AccessLevelEditException missingAccessLevel(AccessLevel accessLevel) {
        return new AccessLevelEditException(MISSING_ACCESS_LEVEL, accessLevel);
    }

    public static AccessLevelEditException nullReceivedList() {
        return new AccessLevelEditException(NULL_RECEIVED_LIST);
    }

    public static AccessLevelEditException nullStoredObject() {
        return new AccessLevelEditException(NULL_STORED_OBJECT);
    }

    public static AccessLevelEditException optimisticLock() {
        return new AccessLevelEditException(OPTIMISTIC_LOCK);
    }

    public static AccessLevelEditException unknown5001() {
        return new AccessLevelEditException(UNKNOWN_5001);
    }
}
