/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author java
 */
public class BracketUtil {
    
    public static int numberOfRounds(int competitorsAmount) {
        byte result = 1;

        while (Math.pow(2, (double) result) != competitorsAmount) {
            if (result == Byte.MAX_VALUE) {
                throw new IllegalStateException("Nie jest potega 2");
            }
            result++;
        }

        return result;
    }
}
