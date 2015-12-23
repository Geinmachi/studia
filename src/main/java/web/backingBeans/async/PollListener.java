/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans.async;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import web.controllers.CompetitionController;
import web.models.InboxEvent;
import web.models.InboxEventImpl;
import web.utils.JsfUtils;

/**
 *
 * @author java
 */
@Named(value = "pollListener")
@SessionScoped
public class PollListener implements Serializable {

    @Inject
    private InboxBackingBean inbox;

    private List<AsynchronousTask> taskList;

    private static final String POLL_GROWL_ID = "pollGrowl";

    private static final String POLL_FORM_ID = "pollForm";

    private static final String POLL_DOWNLOAD_ID = "downloadBlock";

    private static final String POLL_DOWNLOAD_ICON_GROUP_ID = "downloadingIconGroup";

    private static final int INTERVAL = 1;

    public int getINTERVAL() {
        return INTERVAL;
    }

    public List<AsynchronousTask> getTaskList() {
        return new ArrayList<>(taskList);
    }

    /**
     * Creates a new instance of PollListener
     */
    public PollListener() {
    }

    @PostConstruct
    private void init() {
        taskList = new ArrayList<>();
    }

    public void checkData() {
        System.out.println("Poll sprawdza dane, rozmiar " + taskList.size());
        for (Iterator<AsynchronousTask> it = taskList.iterator(); it.hasNext();) {
            AsynchronousTask asyncTask = it.next();

            if (asyncTask.asyncTask().isDone()) {
                System.out.println("POlllistener IF DONEEEE czy bylo anulowane " + asyncTask.asyncTask().isCancelled());
                try {
                    if (asyncTask.asyncTask().get() == null) { // canceled
                        System.out.println("POlllistener IF canceled");
                        JsfUtils.addSuccessMessage("Zadanie anulowane", "", POLL_GROWL_ID);
//                        updateSections();
//                        it.remove();

//                        return;
                    } else {
                        JsfUtils.addSuccessMessage(asyncTask.message(), asyncTask.details(), POLL_GROWL_ID);
                        addTaskToInbox(asyncTask);
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    Logger.getLogger(PollListener.class.getName()).log(Level.SEVERE, null, ex);
                }
//                }
                updateSections();
                it.remove();
            } else {
                System.out.println("Jescze pobiera");
            }
        }
    }

    private void addTaskToInbox(AsynchronousTask asyncTask) {
        InboxEvent inboxEvent = new InboxEventImpl(new Date(), asyncTask.message(), asyncTask.details(), asyncTask.resultPage());
        inbox.addInboxEvent(inboxEvent);
    }

    public void addTask(AsynchronousTask asyncTask) {
        taskList.add(0, asyncTask);
        updateSections();
    }

    public void cancelTask(AsynchronousTask asyncTask) {
        asyncTask.asyncTask().cancel(true);
        JsfUtils.addSuccessMessage("Zadanie zgłoszone do anulowania", "", POLL_FORM_ID);
        updateSections();
    }

    public boolean isTaskListEmpty() {
        return taskList.isEmpty();
    }

    private void updateSections() {
        RequestContext.getCurrentInstance().update(POLL_FORM_ID);
        RequestContext.getCurrentInstance().update(POLL_DOWNLOAD_ID);
        RequestContext.getCurrentInstance().update(POLL_DOWNLOAD_ICON_GROUP_ID);
    }
}
