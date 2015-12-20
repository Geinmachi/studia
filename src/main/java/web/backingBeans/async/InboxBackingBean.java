/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans.async;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import org.primefaces.context.RequestContext;
import web.models.InboxEvent;
import web.models.InboxEventImpl;
import web.utils.PageConstants;

/**
 *
 * @author java
 */
@Named(value = "inboxBackingBean")
@SessionScoped
public class InboxBackingBean implements Serializable {

    private List<InboxEventImpl> inboxEventList;

    private static final String INBOX_EVENT_LIST_ID = "inboxForm";

    private static final String INBOX_ICON_GROUP_ID = "inboxIconGroup";
    /**
     * For putting events use method addInboxEvent
     * @return new list
     */
    public List<InboxEvent> getInboxEventList() {
        return new ArrayList<>(inboxEventList);
    }
    
    /**
     * Creates a new instance of InboxBackingBean
     */
    public InboxBackingBean() {
    }
    
    @PostConstruct
    private void init() {
        inboxEventList = new ArrayList<>();
        
        for (int i = 0; i < 1; i++) {
            InboxEventImpl a = new InboxEventImpl(new Date(), "aaa" + i, "bbbb" + i, "competitorReports.xhtml");
            if (i % 3 == 0) {
                a.setReadStatus(true);
            }
            inboxEventList.add(a);
        }
    }
    
    public int unreadMessages() {
        int counter = 0;
        for (InboxEvent ie : inboxEventList) {
            counter += ie.isReadStatus() ? 0 : 1;
        }

        return counter;
    }
    
    public void addInboxEvent(InboxEvent inboxEvent) {
        inboxEventList.add(0, (InboxEventImpl)inboxEvent);
        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.update(INBOX_ICON_GROUP_ID);
        requestContext.update(INBOX_EVENT_LIST_ID);
    }
    
    public void removeInboxEvent(InboxEvent inboxEvent) {
        inboxEventList.remove((InboxEventImpl)inboxEvent);
    }
    
    public String markAsRead(InboxEvent inboxEvent) {
        InboxEventImpl event = (InboxEventImpl)inboxEvent;
        event.setReadStatus(true);
        
//        return null;
        return PageConstants.getPage(inboxEvent.getRedirectPage(), true);
    }
}
