/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.converters;

import web.converters.interfaces.MatchTypeConverterAccessor;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.eclipse.persistence.logging.SessionLog;
import web.qualifiers.MatchTypeConverterSource;

/**
 *
 * @author java
 */
@Named(value = "matchTypeConverter")
@ApplicationScoped
public class MatchTypeConverter extends BaseConverter {
    
    @Inject
    @MatchTypeConverterSource
    protected MatchTypeConverterAccessor fetchedData;

    @Override
    protected List getFetchedData() {
        return fetchedData.getMatchTypeList();
    }
}
