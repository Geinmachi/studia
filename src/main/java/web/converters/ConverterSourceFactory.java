/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.converters;

import web.converters.interfaces.ConverterDataAccessor;
import web.converters.interfaces.MatchTypeConverterAccessor;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
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

    @Produces
    @BaseConverterInjectionPoint
    @ViewScoped
    private ConverterDataAccessor getBaseConverterSourceBean(
            @Any AddCompetitorBackingBean addCompetitor,
            @Any EditCompetitorBackingBean editCompetitor,
            @Any CreateTeamBackingBean createTeam,
            @Any EditTeamBackingBean editTeam,
            @Any CreateCompetitionBackingBean createCompetition) {
        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();

        logger.log(Level.INFO, " LOLO converterFatoyr produces {0} w View {1}", new Object[]{this.hashCode(), viewId});

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

        if (PageConstants.ORGANIZER_CREATE_COMPETITION.contains(viewId)) {
            return createCompetition;
        } else if (PageConstants.EDIT_MANAGE_COMPETITION.contains(viewId)) {
            return editCompetition;
        }
        
        return null;
    }
}
