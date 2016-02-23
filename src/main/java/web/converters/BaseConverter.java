/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.converters;

import entities.Team;
import web.converters.interfaces.ConverterDataAccessor;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.util.AnnotationLiteral;
import javax.enterprise.util.TypeLiteral;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import web.qualifiers.BackingBean;
import web.utils.JsfUtils;

/**
 *
 * @author java
 */
@Named(value = "genericConverter")
@ViewScoped
public class BaseConverter implements Converter, Serializable {

    private static final String MAIN_GROWL_ID = "globalContainer";

    public Logger getLogger() {
        return Logger.getLogger(BaseConverter.class.getName());
    }

    protected ConverterDataAccessor fetchedData;

    public ConverterDataAccessor getFetchedDataVar() {
        return fetchedData;
    }

    @PostConstruct
    private void init() {
        System.out.println("init base convertera");
    }

    protected List getFetchedData() {
        if (fetchedData == null) {
            BeanManager bm = CDI.current().getBeanManager();
            Set<Bean<?>> beans = bm.getBeans(Object.class, new AnnotationLiteral<BackingBean>() {
            });

            for (Bean bean : beans) {
                Context beanScopeContext;
                try {
                    beanScopeContext = bm.getContext(bean.getScope());
                } catch (Exception e) {
                    getLogger().log(Level.INFO, "Nie udalo sie utworzyc kontekstu", e);
                    continue;
                }
                Object beanInstance = beanScopeContext.get(bean);

                if (beanInstance != null) {
                    fetchedData = (ConverterDataAccessor) beanInstance;
                    break;
                }
            }
        }

        return fetchedData.getFetchedData();
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            List dataList = getFetchedData();

            return dataList.get(Integer.valueOf(value));
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "exception during converting objects getAsObject", e);
            JsfUtils.addErrorMessage(e.getMessage(), MAIN_GROWL_ID);
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Conversion Error", "Conversion Error"));
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {
            if (value == null) {
                return "";
            }
            
            return String.valueOf(getFetchedData().indexOf((value)));
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "exception during converting objects getAsString", e);
            JsfUtils.addErrorMessage(e.getMessage(), MAIN_GROWL_ID);
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Conversion Error", "Conversion Error"));
        }
    }
}
