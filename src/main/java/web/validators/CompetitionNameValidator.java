/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.validators;

import exceptions.ApplicationException;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import web.backingBeans.mot.competition.CreateCompetitionBackingBean;

/**
 *
 * @author java
 */
@Named
@RequestScoped
public class CompetitionNameValidator implements Validator {

    @Inject
    private CreateCompetitionBackingBean createBean;

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        try {
            createBean.getCompetition().setCompetitionName(value.toString());
            createBean.checkCompetitionConstraints();
        } catch (ApplicationException e) {
            System.out.println("VALIDATOR EXCEPTION ");

            FacesMessage msg = new FacesMessage(e.getLocalizedMessage(), " ");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }

}
