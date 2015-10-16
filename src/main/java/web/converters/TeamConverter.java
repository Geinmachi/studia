/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.converters;

import web.converters.interfaces.CompetitorConverterData;
import entities.Team;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import web.backingBeans.mot.competitor.AddCompetitorBackingBean;
import web.backingBeans.mot.competition.CreateCompetitionBackingBean;
import web.backingBeans.mot.team.CreateTeamBackingBean;
import web.backingBeans.mot.competitor.EditCompetitorBackingBean;
import web.converters.interfaces.TeamConverterData;
import web.qualifiers.TeamDataSource;

/**
 *
 * @author java
 */
@Named(value = "teamConverter")
@ApplicationScoped
public class TeamConverter implements Converter, Serializable  {

    @Inject
    @TeamDataSource
    @ViewScoped
    private TeamConverterData fetchedData;

    @Produces
    @TeamDataSource
    @ViewScoped
    public TeamConverterData getDataSource(@Any AddCompetitorBackingBean add, @Any EditCompetitorBackingBean edit) {
        System.out.println("PRODUUUUUUUUUUUUUUUUCES");
        
        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();

        if (viewId.contains("add")) {
            return add;
        } else if (viewId.contains("edit")) {
            return edit;
        } else {
            System.out.println("CompetitorConverter#getDataSource no injection point");
            throw new IllegalStateException("No injection point found in @Produces");
        }

    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
//            return fetchedData.findCompetitionTypeById(Integer.parseInt(value));
            List<Team> teamList = fetchedData.getTeamList();
            for (Team t : teamList) {
                if (Integer.compare(t.getIdTeam(), Integer.valueOf(value)) == 0) {
                    return t;
                }
            }

            throw new IllegalArgumentException("Nie ma takiego team");
        } catch (Exception e) {
            System.out.println("TeamConverter#getAsObject WYjatekgetAsObject " + e.getMessage());
            e.printStackTrace();
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Conversion Error"));
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {
//            System.out.println("ID z konwertera " + String.valueOf(((Competitor) value).getIdCompetitor()));
            if (value != null) {
                return String.valueOf(((Team) value).getIdTeam());
            }
            
            return "";
        } catch (Exception e) {
            System.out.println("WYjatekgetAsString " + e.getLocalizedMessage());
            e.printStackTrace();
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Conversion Error"));
        }
    }
}
