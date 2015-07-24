/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.converters;

import entities.CompetitionType;
import entities.Competitor;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import web.controllers.CompetitionController;

/**
 *
 * @author java
 */
@FacesConverter("competitionTypeConverter")
public class CompetitionTypeConverter implements Converter {

    @Inject
    private CompetitionController controller;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
//            System.out.println("ID wybranego z convertera " + controller.findCompetitorById(Integer.parseInt(value)).getIdCompetitor());
            return controller.findCompetitionTypeById(Integer.parseInt(value));
        } catch (Exception e) {
            System.out.println("WYjatekgetAsObject");
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", ""));
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {
//            System.out.println("ID z konwertera " + String.valueOf(((Competitor) value).getIdCompetitor()));
            return String.valueOf(((CompetitionType) value).getIdCompetitionType());
        } catch (Exception e) {
            System.out.println("WYjatekgetAsString");
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", ""));
        }
    }

}
