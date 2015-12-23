/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans.mot.competitor;

import entities.Competitor;
import exceptions.ApplicationException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import mot.interfaces.CompetitorMatchesEntryStatistics;
import utils.ResourceBundleUtil;
import web.backingBeans.async.BaseAsyncBackingBean;
import web.utils.PageConstants;

/**
 *
 * @author java
 */
@Named(value = "matchesReportBackingBean")
@SessionScoped
public class MatchesReportBackingBean extends BaseAsyncBackingBean<List<CompetitorMatchesEntryStatistics>, Competitor> implements Serializable {

    private static final Logger logger = Logger.getLogger(MatchesReportBackingBean.class.getName());

    public Future<List<CompetitorMatchesEntryStatistics>> getFutureCompetitorMatchList() {
        return super.getFutureResult();
    }

    public List<CompetitorMatchesEntryStatistics> getCompetitorMatchList() {
        return super.getActualResult();
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

    @Override
    protected void handleApplicationException(ApplicationException e) {
        logger.log(Level.SEVERE, "Exception during initializing values");
        e.printStackTrace();
    }

    @Override
    protected String getResultPageAddress() {
        return PageConstants.ROOT_MATCHES_REPORT;
    }

    @Override
    protected String getNewResourceSelectedMessage() {
       return ResourceBundleUtil.getResourceBundleProperty("competitorMatches.newCompetitorSelected");
    }

    @Override
    protected String getTaskTitle() {
       return ResourceBundleUtil.getResourceBundleProperty("competitorMatches.taskTitle");
    }

    @Override
    protected String getTaskDescription() {
        return selectedResource.getIdPersonalInfo().getFirstName() + " " + selectedResource.getIdPersonalInfo().getLastName();
    }

    @Override
    protected Future<List<CompetitorMatchesEntryStatistics>> getFutureResultControllerDelegate(Competitor resource) throws ApplicationException {
        return controller.generateCompetitorMatchesStatistics(resource);
    }
}
