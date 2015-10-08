/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.converters;

import entities.CompetitionType;
import entities.Competitor;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
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
import web.backingBeans.CreateCompetitionBackingBean;
import web.backingBeans.CreateTeamBackingBean;
import web.controllers.CompetitionController;
import web.qualifiers.DataSource;
import web.utils.ConverterData;

/**
 *
 * @author java
 */
@Dependent
@FacesConverter("competitorConverter")
public class CompetitorConverter implements Converter {
//
//    @Inject
//    private CreateCompetitionBackingBean fetchedData;

    @Produces
    @DataSource
    @ApplicationScoped
    private ConverterData getConverterDataImp(CreateTeamBackingBean team, CreateCompetitionBackingBean competition) {
        System.out.println("WYKONANY PRODUCER");
        System.out.println("HASCODE beana " + competition.toString());
//        
        return competition;
    }

    @Inject
    @DataSource
    @ApplicationScoped
    private ConverterData converterData;

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        try {
            System.out.println("CompetitorConverter#getAsObject " + value);
//            System.out.println("ID wybranego z convertera " + controller.findCompetitorById(Integer.parseInt(value)).getIdCompetitor());
//            return controller.findCompetitorById(Integer.parseInt(value));
            List<Competitor> competitorList = converterData.getCompetitorList();
            for (Competitor c : competitorList) {
                if (Integer.compare(c.getIdCompetitor(), Integer.valueOf(value)) == 0) {
                    return c;
                }
            }

            throw new IllegalArgumentException("Nie ma takiego competitionType");
        } catch (Exception e) {
            System.out.println("CompetitorConverter#getAsObject WYjatekgetAsObject " + e.getMessage());
            e.printStackTrace();
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Conversion Error"));
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {
            System.out.println("CompetitorConverter#getAsString " + value);
            System.out.println("CompetitorConverter converterData::  " + converterData.toString());
//            System.out.println("ID z konwertera " + String.valueOf(((Competitor) value).getIdCompetitor()));
            return String.valueOf(((Competitor) value).getIdCompetitor());
        } catch (Exception e) {
            System.out.println("WYjatekgetAsString");
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Conversion Error"));
        }
    }
}
