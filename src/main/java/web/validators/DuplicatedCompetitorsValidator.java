/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.validators;

import entities.Competitor;
import exceptions.ApplicationException;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.component.picklist.PickList;
import org.primefaces.model.DualListModel;
import web.backingBeans.mot.competition.CreateCompetitionBackingBean;
import web.controllers.CompetitionController;

/**
 *
 * @author java
 */
@Named
@RequestScoped
public class DuplicatedCompetitorsValidator implements Validator {

    @Inject
    private CreateCompetitionBackingBean createBean;
    
    @Inject
    private CompetitionController controller;

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        DualListModel competitors = (DualListModel) value;
        createBean.setCompetitors(competitors);
        
        Competitor duplicatedCompetitor = controller.vlidateCompetitorDuplicate(competitors.getTarget());
        if (duplicatedCompetitor != null) {
            System.out.println("VALIDATOR EXCEPTION ");

            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Competition contains duplicated competitor",
                    duplicatedCompetitor.getIdPersonalInfo().getFirstName() + " "
                    + duplicatedCompetitor.getIdPersonalInfo().getLastName());
            
            throw new ValidatorException(msg);
        }
    }
}
