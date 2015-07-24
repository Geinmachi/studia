/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import entities.AccessLevel;
import entities.Account;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author java
 */
public class ConvertUtil {
    
    public static String generateSHA512HashFromPassword(String password) {
        System.out.println("Wywolany convert, haslo :");
        byte[] input = {};
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.reset();
            input = digest.digest(password.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            Logger.getLogger(ConvertUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(DatatypeConverter.printHexBinary(input));
        return DatatypeConverter.printHexBinary(input);
    }
    
    public static <T extends AccessLevel> T getSpecAccessLevelFromAccount(Account account, Class<T> type) {
        if (account == null) {
            throw new ArrayIndexOutOfBoundsException("warunek 1");
        }
        T specAccessLevel = null;
        System.out.println("Konto : " + account);
        System.out.println("accessLevel rozmiar " + account.getAccessLevelList().size());
        for(AccessLevel al: account.getAccessLevelList()) {
            System.out.println("AccessLevel: " + al + " klasa: " + al.getClass().getName() + " czy aktywny " + al.getIsActive());
            if (type.isAssignableFrom(al.getClass()) && al.getIsActive()) {
                specAccessLevel = type.cast(al);
                break;
            }
        }
        if (specAccessLevel == null) {
            throw new ArrayIndexOutOfBoundsException("warunek 2");
        }
        return specAccessLevel;
    }
}
