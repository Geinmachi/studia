/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans.mot.team;

import entities.Competitor;
import java.util.List;
import org.primefaces.model.DualListModel;
import web.backingBeans.mot.competition.CompetitionBackingBean;
import web.converters.interfaces.ConverterDataAccessor;
import web.utils.JsfUtils;

/**
 *
 * @author java
 */
public abstract class TeamBackingBean extends CompetitionBackingBean implements ConverterDataAccessor<Competitor> {
    
    protected boolean duplicatedCompetitorFlag;

    protected DualListModel competitors;

    protected List<Competitor> competitorList;
    
    protected void checkDuplicate() {
        Competitor duplicatedCompetitor = controller.validateCompetitorDuplicate((List<Competitor>) competitors.getTarget());
        
        if (duplicatedCompetitor != null) {
            System.out.println("Duplicated competitor");
            JsfUtils.addErrorMessage("Team contains duplicated competitor" , 
                    duplicatedCompetitor.getIdPersonalInfo().getFirstName() + " " 
                            + duplicatedCompetitor.getIdPersonalInfo().getLastName(), null);
            duplicatedCompetitorFlag = true;
            return;
        }
        
        duplicatedCompetitorFlag = false;
    }

    @Override
    public abstract List<Competitor> getFetchedData();

}
