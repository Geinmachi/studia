/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.converters;

import entities.CompetitionType;
import entities.MatchType;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import web.backingBeans.BracketCreationBackingBean;
import web.backingBeans.CreateCompetitionBackingBean;
import web.controllers.CompetitionController;

/**
 *
 * @author java
 */
@FacesConverter("matchTypeConverter")
public class MatchTypeConverter  implements Converter {
    
    @Inject
    private CreateCompetitionBackingBean fetchedData;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            
            System.out.println("ID wybranego z convertera MatchType" );
//            return fetchedData.findCompetitionTypeById(Integer.parseInt(value));
            List<MatchType> matchTypeList = fetchedData.getBracketCreator().getMatchTypeList();
            for (MatchType mt : matchTypeList) {
                if (Integer.compare(mt.getIdMatchType(), Integer.valueOf(value)) == 0) {
                    return mt;
                }
            }
            
            throw new IllegalArgumentException("Nie ma takiego matchType");
        } catch (Exception e) {
            System.out.println("WYjatekgetAsObject");
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Conversion Error"));
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {
//            System.out.println("ID z konwertera " + String.valueOf(((Competitor) value).getIdCompetitor()));
            return String.valueOf(((MatchType) value).getIdMatchType());
        } catch (Exception e) {
            System.out.println("WYjatekgetAsString");
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Conversion Error"));
        }
    }
}
