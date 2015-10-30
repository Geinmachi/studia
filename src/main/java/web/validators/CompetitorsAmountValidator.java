/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.validators;

import entities.Competitor;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.DualListModel;
import utils.ResourceBundleUtil;
import web.backingBeans.mot.competition.CreateCompetitionBackingBean;
import web.backingBeans.mot.team.CreateTeamBackingBean;
import web.controllers.CompetitionController;
import web.converters.interfaces.CompetitorConverterData;
import web.qualifiers.CompetitorsDataSource;
import web.qualifiers.CompetitorsDualList;

/**
 *
 * @author java
 */
@Named
@RequestScoped
public class CompetitorsAmountValidator implements Validator {

    @Inject
    @ViewScoped
    @CompetitorsDualList
    private CompetitorsDualListSetter createBean;

    @Inject
    private CompetitionController controller;

    @Produces
    @CompetitorsDualList
    public CompetitorsDualListSetter getDataSource(
            @Any CreateCompetitionBackingBean competition,
            @Any CreateTeamBackingBean createTeam) {

        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();

        if (viewId.contains("createCompetition")) {
            return competition;
        } else if (viewId.contains("createTeam")) {
            return createTeam;
        } else {
            System.out.println("CompetitorConverter#getDataSource no injection point");
            throw new IllegalStateException("No injection point found in @Produces");
        }
    }

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        DualListModel competitors = (DualListModel) value;
        createBean.setCompetitors(competitors);

        boolean invalid = controller.validateCompetitorsAmount(competitors.getTarget().size());
        System.out.println("VALL SIZE " + competitors.getTarget().size());
        if (!invalid) {
            System.out.println("VALIDATOR EXCEPTION ");

            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundleUtil.getResourceBundleProperty("assignedCompetitorsMustBePowerOfTwo"),
                    "(" + competitors.getTarget().size() + ")");

            throw new ValidatorException(msg);
        }
    }
}
