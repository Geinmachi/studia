/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.converters;

import web.converters.interfaces.ConverterDataAccessor;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Inject;
import javax.inject.Named;
import web.qualifiers.BaseConverterInjectionPoint;
import web.utils.JsfUtils;

/**
 *
 * @author java
 */
@Named(value = "genericConverter")
@ApplicationScoped
public class BaseConverter implements Converter, Serializable {

    private static final String MAIN_GROWL_ID = "globalContainer";
    
    public Logger getLogger() {
        return Logger.getLogger(BaseConverter.class.getName());
    }
    
    @Inject
    @BaseConverterInjectionPoint
    protected ConverterDataAccessor fetchedData;

    protected List getFetchedData() {
        return fetchedData.getFetchedData();
    }
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            List dataList = getFetchedData();

            return dataList.get(Integer.valueOf(value));
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "exception during converting objects getAsObject");
            JsfUtils.addErrorMessage(e, MAIN_GROWL_ID);
            e.printStackTrace();
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Conversion Error"));
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
//        getLogger().log(Level.INFO, " getAsString {0} w {1} injected string {2}", new Object[]{value, this.hashCode(), "hhhh"});
        try {
            if (value == null) {
                return "";
            }

            try {
                return String.valueOf(getFetchedData().indexOf((value)));
            } catch (ClassCastException e) {
                getLogger().log(Level.SEVERE, "cannot cast {0} in converter ", value.getClass());
            }

            return "";
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "exception during converting objects getAsString");
            JsfUtils.addErrorMessage(e, MAIN_GROWL_ID);
            e.printStackTrace();
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Conversion Error"));
        }
    }
}
