/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.managers;

import entities.Competition;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import mot.facades.CompetitionFacadeLocal;

/**
 *
 * @author java
 */
@Stateless
public class PresentCompetitionManager implements PresentCompetitionManagerLocal {
    
    @EJB
    private CompetitionFacadeLocal competitionFacade;

    @Override
    public List<Competition> findAllCompetitions() {
        
        return competitionFacade.findAll();
    }
    
}
