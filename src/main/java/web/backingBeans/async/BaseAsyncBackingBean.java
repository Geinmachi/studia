/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans.async;

import exceptions.ApplicationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import utils.ResourceBundleUtil;
import web.backingBeans.mot.competition.CompetitionBackingBean;
import web.utils.JsfUtils;

/**
 *
 * @author java
 * @param <V> Type in Future<?> returned by service layer
 * @param <T> Type of selected resource taken by service layer as a parameter to
 * fetch data
 */
public abstract class BaseAsyncBackingBean<V, T> extends CompetitionBackingBean {

    @Inject
    private PollListener async;

    private Future<V> futureResult;

    private V actualResult;

    protected abstract Future<V> getFutureResultControllerDelegate(T resource) throws ApplicationException;

    protected abstract void handleApplicationException(ApplicationException e);

    protected abstract String getResultPageAddress();

    protected abstract String getNewResourceSelectedMessage();

    protected abstract String getTaskTitle();

    protected abstract String getTaskDescription();

    protected T selectedResource;

    protected String getFormId() {
        return POLL_FORM_ID;
    }

    protected Logger getLogger() {
        return logger;
    }
    
    private static final String POLL_FORM_ID = "pollForm";

    private static final String DOWNLOAD_IN_PROGRESS_PROPERTY = "downloadInProgress";

    private static final String PLEASE_WAIT_PROPERTY = "pleaseWait";

    private static final String ASYNCHRONOUS_DOWNLOAD_IN_PROGRESS_PROPERTY = "asynchronousDownloadInProgress";

    private static final String DOWNLOAD_FINISH_NOTIFICATION_PROPERTY = "downloadFinishNotification";

    private static final String NEW_DOWNLOAD_PROPERTY = "newDownload";

    public Future<V> getFutureResult() {
        return futureResult;
    }

    public void setFutureResult(Future<V> futureResult) {
        this.futureResult = futureResult;
    }

    public V getActualResult() {
        if (actualResult == null && futureResult != null && futureResult.isDone()) {
            try {
                return futureResult.get();
            } catch (InterruptedException | ExecutionException ex) {
                getLogger().log(Level.SEVERE, null, ex);
            }
        }
        return actualResult;
    }

    public boolean initValues(T resource) {
        try {
            if (resource.equals(selectedResource)) { // if selected resource is previous one
                getLogger().log(Level.INFO, "Selected previous resource");
                if (checkCompleteStatus()) {
                    return true;
                }
            }

            if (selectedResource != null) {
                JsfUtils.addSuccessMessage(getNewResourceSelectedMessage(),
                        ResourceBundleUtil.getResourceBundleProperty(NEW_DOWNLOAD_PROPERTY), POLL_FORM_ID);
                if (futureResult != null && !futureResult.isDone()) {
                    futureResult.cancel(true);
                }
            } else {
                JsfUtils.addSuccessMessage(ResourceBundleUtil.getResourceBundleProperty(ASYNCHRONOUS_DOWNLOAD_IN_PROGRESS_PROPERTY),
                        ResourceBundleUtil.getResourceBundleProperty(DOWNLOAD_FINISH_NOTIFICATION_PROPERTY), POLL_FORM_ID);
            }

            getLogger().log(Level.INFO, "Asynchronous download started");

            selectedResource = resource;
            try {
                futureResult = getFutureResultControllerDelegate(resource);
            } catch (ApplicationException e) {
                handleApplicationException(e);

                return false;
            }
            async.addTask(new AsynchronousTaskImpl<>(
                    futureResult, getTaskTitle(), getTaskDescription(), getResultPageAddress())
            );

        } catch (ExecutionException | InterruptedException e) {
            getLogger().log(Level.SEVERE, null, e);
        }

        return false;
    }

    private boolean checkCompleteStatus() throws InterruptedException, ExecutionException {
        if (futureResult != null && !futureResult.isDone()) {
            getLogger().log(Level.INFO, "During download");
            JsfUtils.addSuccessMessage(ResourceBundleUtil.getResourceBundleProperty(DOWNLOAD_IN_PROGRESS_PROPERTY),
                    ResourceBundleUtil.getResourceBundleProperty(PLEASE_WAIT_PROPERTY), getResultPageAddress());
            return false;
        }
        if (futureResult != null && futureResult.isDone()) {
            if (futureResult.get() == null) {
                getLogger().log(Level.INFO, "Task was cancelled, have to download again");
                return false;
            }
            getLogger().log(Level.INFO, "Downloaded successfuly, redirects to result page");
            actualResult = futureResult.get();

            return true;
        }

        return false;
    }
}
