/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.converters;

import web.converters.interfaces.CompetitorConverterData;
import entities.CompetitionType;
import entities.Competitor;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.New;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import web.backingBeans.mot.competition.CreateCompetitionBackingBean;
import web.backingBeans.mot.team.CreateTeamBackingBean;
import web.backingBeans.mot.team.EditTeamBackingBean;
import web.controllers.CompetitionController;
import web.qualifiers.CompetitorsDataSource;

/**
 *
 * @author java
 */
@Named(value = "competitorConverter")
@ApplicationScoped
//@FacesConverter("competitorConverter")
public class CompetitorConverter implements Converter, Serializable {

    @Inject
    @CompetitorsDataSource
    @ViewScoped
    private CompetitorConverterData fetchedData;

    @Produces
    @CompetitorsDataSource
    @ViewScoped
    public CompetitorConverterData getDataSource(
            @Any CreateCompetitionBackingBean competition, 
            @Any CreateTeamBackingBean createTeam,
            @Any EditTeamBackingBean editTeam) {
        
        System.out.println("PRODUUUUUUUUUUUUUUUUCES");
        
        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();

        if (viewId.contains("createCompetition")) {
            return competition;
        } else if (viewId.contains("createTeam")) {
            return createTeam;
        } else if (viewId.contains("editTeam")) {
            return editTeam;
        }
        else {
            System.out.println("CompetitorConverter#getDataSource no injection point");
            throw new IllegalStateException("No injection point found in @Produces");
        }

    }

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        try {
            System.out.println("CompetitorConverter#getAsObject value " + value);
//            System.out.println("ID wybranego z convertera " + controller.findCompetitorById(Integer.parseInt(value)).getIdCompetitor());
//            return controller.findCompetitorById(Integer.parseInt(value));
            List<Competitor> competitorList = fetchedData.getCompetitorList();
            for (Competitor c : competitorList) {
                if (Integer.compare(c.getIdCompetitor(), Integer.valueOf(value)) == 0) {
                    return c;
                }
            }

            throw new IllegalArgumentException("Nie ma takiego competitora " + value);
        } catch (Exception e) {
            System.out.println("CompetitorConverter#getAsObject WYjatekgetAsObject " + e.getMessage());
            e.printStackTrace();
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Conversion Error"));
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {
            System.out.println("CompetitorConverter#getAsString value " + value);
//            System.out.println("ID z konwertera " + String.valueOf(((Competitor) value).getIdCompetitor()));
            return String.valueOf(((Competitor) value).getIdCompetitor());
        } catch (Exception e) {
            System.out.println("WYjatekgetAsString " + e.getMessage());
            e.printStackTrace();
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Conversion Error"));
        }
    }
}
