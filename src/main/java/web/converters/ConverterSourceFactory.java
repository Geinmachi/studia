/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.converters;

import web.converters.interfaces.ConverterDataAccessor;
import web.converters.interfaces.MatchTypeConverterAccessor;
import java.io.Serializable;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.util.AnnotationLiteral;
import javax.enterprise.util.TypeLiteral;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import web.backingBeans.mot.competition.CompetitionBackingBean;
import web.backingBeans.mot.competition.CreateCompetitionBackingBean;
import web.backingBeans.mot.competition.ManageCompetitionBackingBean;
import web.backingBeans.mot.competitor.AddCompetitorBackingBean;
import web.backingBeans.mot.competitor.EditCompetitorBackingBean;
import web.backingBeans.mot.team.CreateTeamBackingBean;
import web.backingBeans.mot.team.EditTeamBackingBean;
import web.qualifiers.BaseConverterInjectionPoint;
import web.qualifiers.MatchTypeConverterSource;
import web.utils.PageConstants;

/**
 *
 * @author java
 */
@ApplicationScoped
public class ConverterSourceFactory implements Serializable {

    private static final Logger logger = Logger.getLogger(ConverterSourceFactory.class.getName());

    @PostConstruct
    private void init() {
        logger.log(Level.INFO, " utworyl sie ConverterSourcesFactory {0}", this.hashCode());
    }

//    @Produces
//    @BaseConverterInjectionPoint
//    @ViewScoped
    private ConverterDataAccessor lols(@Any Instance<ConverterDataAccessor> instance) {
        System.out.println("Wykonalo sie los @Produces");
//        System.out.println("isAmbigius " + addCompetitor.isAmbiguous());
//        System.out.println("isUnsatisfied " + addCompetitor.isUnsatisfied());
        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        ConverterHelperQualifier annotation = new ConverterHelperQualifier(PageConstants.ORGANIZER_ADD_COMPETITOR);
//        annotation.;
        System.out.println("222222Wykonalo sie los @Produces");
//        Instance<AddCompetitorBackingBean> b = instance.select(new TypeLiteral<AddCompetitorBackingBean>(){}, annotation);
        for (ConverterDataAccessor a : instance) {
            System.out.println("Cos istnieje " + a.getClass());
        }

        for (ConverterDataAccessor o : CDI.current().select(ConverterDataAccessor.class, annotation)) {
            System.out.println("CDI::: " + o.getClass());
        }
//        ProgrammaticBeanLookup
//        System.out.println("Klasa selecta " + b.get().getClass());
        return CDI.current().select(ConverterDataAccessor.class, annotation).get();
//        return instance.select(annotation).get();
//        return addCompetitor.get();
    }
//    @Produces
//    @BaseConverterInjectionPoint
//    @ViewScoped

    private ConverterDataAccessor getBaseConverterSourceBean(
            @Any AddCompetitorBackingBean addCompetitor,
            @Any EditCompetitorBackingBean editCompetitor,
            @Any CreateTeamBackingBean createTeam,
            @Any EditTeamBackingBean editTeam,
            @Any CreateCompetitionBackingBean createCompetition) {
        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();

        if (PageConstants.ORGANIZER_ADD_COMPETITOR.contains(viewId)) {
            return addCompetitor;
        } else if (PageConstants.EDIT_EDIT_COMPETITOR.contains(viewId)) {
            return editCompetitor;
        } else if (PageConstants.ORGANIZER_CREATE_TEAM.contains(viewId)) {
            return createTeam;
        } else if (PageConstants.EDIT_EDIT_TEAM.contains(viewId)) {
            return editTeam;
        } else if (PageConstants.ORGANIZER_CREATE_COMPETITION.contains(viewId)) {
            return createCompetition;
        }

        return null;
    }

    @Produces
    @MatchTypeConverterSource
    @ViewScoped
    private MatchTypeConverterAccessor getMatchTypeConverterSourceBean(
            @Any CreateCompetitionBackingBean createCompetition,
            @Any ManageCompetitionBackingBean editCompetition) {
        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        System.out.println("PRODUCERRR match typow getMatchTypeConverterSourceBean");
        if (PageConstants.ORGANIZER_CREATE_COMPETITION.contains(viewId)) {
            return createCompetition;
        } else if (PageConstants.EDIT_MANAGE_COMPETITION.contains(viewId)) {
            return editCompetition;
        }

        return null;
    }
//    @Inject

//    @Produces
//    @BaseConverterInjectionPoint
    private ConverterDataAccessor initServices(@Any Instance<ConverterDataAccessor> services) {
        System.out.println("Wyonal sie inject metody");
        for (ConverterDataAccessor service : services) {
            System.out.println("Service class " + service.getClass());
        }

        return services.select(new AnnotationLiteral<Any>() {
        }).get();
    }
    
}
