/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans.async;

import entities.CompetitorMatch;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Future;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import web.backingBeans.mot.competitor.MatchesReportBackingBean;
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

    private static final int INTERVAL = 1;

    public int getINTERVAL() {
        return INTERVAL;
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
        System.out.println("Poll sprawdza dane");
        for (Iterator<AsynchronousTask> it = taskList.iterator(); it.hasNext();) {
            AsynchronousTask asyncTask = it.next();

            if (asyncTask.isDone()) {
                JsfUtils.addSuccessMessage(asyncTask.message(), asyncTask.details(), POLL_GROWL_ID);
                RequestContext.getCurrentInstance().update(POLL_FORM_ID);
                addTaskToInbox(asyncTask);
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
        taskList.add(asyncTask);
        RequestContext.getCurrentInstance().update(POLL_FORM_ID);
    }

    public boolean isTaskListEmpty() {
        return taskList.isEmpty();
    }
}
