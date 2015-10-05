/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans;

import entities.Competitor;
import java.io.Serializable;
import java.util.List;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import web.converters.CompetitorConverterData;

/**
 *
 * @author java
 */
@Named(value = "createTeamBackingBean")
@ViewScoped
public class CreateTeamBackingBean implements Serializable, CompetitorConverterData{

    /**
     * Creates a new instance of CreateTeamBackingBean
     */
    public CreateTeamBackingBean() {
    }

    @Override
    public List<Competitor> getCompetitorList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
