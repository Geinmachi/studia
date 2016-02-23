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
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
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
import web.backingBeans.mot.team.EditTeamBackingBean;
import web.controllers.CompetitionController;
import web.qualifiers.DuplicatedCompetitorsData;

/**
 *
 * @author java
 */
@Named
@RequestScoped
public class DuplicatedCompetitorsValidator implements Validator {

    @Inject
    @ViewScoped
    @DuplicatedCompetitorsData
    private DuplicatedCompetitors createBean;

    @Inject
    private CompetitionController controller;

    @Produces
    @DuplicatedCompetitorsData
    public DuplicatedCompetitors getDataSource(
            @Any CreateCompetitionBackingBean competition,
            @Any CreateTeamBackingBean createTeam,
            @Any EditTeamBackingBean editTeam) {

        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();

        if (viewId.contains("createCompetition")) {
            return competition;
        } else if (viewId.contains("createTeam")) {
            return createTeam;
        } else if (viewId.contains("editTeam")) {
            return editTeam;
        } else {
            System.out.println("CompetitorConverter#getDataSource no injection point");
            throw new IllegalStateException("No injection point found in @Produces");
        }
    }

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        DualListModel competitors = (DualListModel) value;
        createBean.setCompetitors(competitors);

        Competitor duplicatedCompetitor = controller.validateCompetitorDuplicate(competitors.getTarget());
        if (duplicatedCompetitor != null) {
            System.out.println("VALIDATOR EXCEPTION ");

            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundleUtil.getResourceBundleProperty("duplicatedCompetitor"),
                    duplicatedCompetitor.getIdPersonalInfo().getFirstName() + " "
                    + duplicatedCompetitor.getIdPersonalInfo().getLastName());

            createBean.setDuplicatedCompetitorFlag(true);
            ((UIInput)component).resetValue();
            throw new ValidatorException(msg);
        }
        
        createBean.setDuplicatedCompetitorFlag(false);
    }
}
