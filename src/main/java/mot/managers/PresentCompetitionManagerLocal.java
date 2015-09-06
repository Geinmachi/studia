/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.managers;

import entities.Competition;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;

/**
 *
 * @author java
 */
@Remote
public interface PresentCompetitionManagerLocal {
    
    List<Competition> findAllCompetitions();

    public Competition getInitializedCompetition(int idCompetition);
}
