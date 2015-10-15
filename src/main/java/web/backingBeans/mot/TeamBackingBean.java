/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans.mot;

import entities.Competitor;
import java.util.List;
import org.primefaces.model.DualListModel;
import web.utils.JsfUtils;

/**
 *
 * @author java
 */
public abstract class TeamBackingBean extends CompetitionBackingBean {
    
    protected boolean duplicatedCompetitorFlag;

    protected DualListModel competitors;

    protected List<Competitor> competitorList;
    
    protected void checkDuplicate() {
        Competitor duplicatedCompetitor = controller.vlidateCompetitorDuplicate((List<Competitor>) competitors.getTarget());
        
        if (duplicatedCompetitor != null) {
            System.out.println("Duplicated competitor");
            JsfUtils.addErrorMessage("Team contains duplicated competitor" , 
                    duplicatedCompetitor.getIdPersonalInfo().getFirstName() + " " 
                            + duplicatedCompetitor.getIdPersonalInfo().getLastName(), "msg");
            duplicatedCompetitorFlag = true;
            return;
        }
        
        duplicatedCompetitorFlag = false;
    }
}
