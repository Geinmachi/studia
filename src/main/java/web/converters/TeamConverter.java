/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.converters;

import entities.MatchType;
import entities.Team;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import web.backingBeans.AddCompetitorBackingBean;
import web.backingBeans.CreateCompetitionBackingBean;

/**
 *
 * @author java
 */
@FacesConverter("teamConverter")
public class TeamConverter implements Converter {

    @Inject
    private AddCompetitorBackingBean fetchedData;

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
