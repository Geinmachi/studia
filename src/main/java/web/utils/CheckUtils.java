/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.utils;

import entities.Competition;

/**
 *
 * @author java
 */
public class CheckUtils {
    
    public static boolean isCompetitionNull(Competition competition) {
        if(competition == null) {
            System.out.println("Competition jest nullem");
            JsfUtils.addErrorMessage("Error", "Competition is not loaded, please come here from appropriate button", "msg");
            
            return true;
        }
        
        return false;
    }
}
