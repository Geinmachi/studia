/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.managers;

import entities.Competition;
import javax.ejb.Local;

/**
 *
 * @author java
 */
@Local
public interface CreateCompetitionManagerLocal {
    
    public boolean validateCompetitorsAmount(int amount);
    
    public void createCompetition(Competition competition);
}
