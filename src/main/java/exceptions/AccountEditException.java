/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

import entities.Account;

/**
 *
 * @author java
 */
public class AccountEditException extends ApplicationException {
    
    private Account receivedAccount;
    
    private Account storedAccoount;

    public Account getReceivedAccount() {
        return receivedAccount;
    }

    public Account getStoredAccoount() {
        return storedAccoount;
    }

    public static final String PASSED_NULL = "EXCEPTION.ACCOUNT_EDIT.PASSED_NULL";
    
    public static final String STORED_NULL = "EXCEPTION.ACCOUNT_EDIT.STORED_NULL";
    
    public static final String OBJECTS_MISMATCH = "EXCEPTION.ACCOUNT_EDIT.OBJECTS_MISMATCH";
    
    public static final String OPTIMISTIC_LOCK = "EXCEPTION.ACCOUNT_EDIT.OPTIMISTIC_LOCK";
    
    public AccountEditException() {
        super();
    }

    public AccountEditException(String message) {
        super(message);
    }

    public AccountEditException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountEditException(String message, Account receivedAccount, Account storedAccount) {
        super(message);
        this.receivedAccount = receivedAccount;
        this.storedAccoount = storedAccount;
    }

    public static AccountEditException passedNull(Account receivedAccount, Account storedAccount) {
        return new AccountEditException(PASSED_NULL, receivedAccount, storedAccount);
    }

    public static AccountEditException storedNull(Account receivedAccount, Account storedAccount) {
        return new AccountEditException(STORED_NULL, receivedAccount, storedAccount);
    }

    public static AccountEditException objectsMismatch(Account receivedAccount, Account storedAccount) {
        return new AccountEditException(OBJECTS_MISMATCH, receivedAccount, storedAccount);
    }

    public static AccountEditException optimisticLock(Account receivedAccount, Account storedAccount) {
        return new AccountEditException(OPTIMISTIC_LOCK, receivedAccount, storedAccount);
    }
    
}
