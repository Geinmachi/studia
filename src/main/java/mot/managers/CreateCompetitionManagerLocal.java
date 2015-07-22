/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.managers;

import entities.Competition;
import javax.ejb.Local; import javax.ejb.Remote;
import javax.ejb.Remote;

/**
 *
 * @author java
 */
@Remote
public interface CreateCompetitionManagerLocal {
    
    public boolean validateCompetitorsAmount(int amount);
    
    public void createCompetition(Competition competition);
}
