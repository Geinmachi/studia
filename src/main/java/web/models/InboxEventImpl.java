/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.models;

import java.util.Date;

/**
 *
 * @author java
 */
public class InboxEventImpl implements InboxEvent {
    
    private Date date;
    
    private String title;
    
    private String description;
    
    private boolean readStatus;
    
    private String redirectPage;

    public InboxEventImpl(Date date, String title, String description, String redirectPage) {
        this.date = date;
        this.title = title;
        this.description = description;
        this.readStatus = false;
        this.redirectPage = redirectPage;
    }

    @Override
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public boolean isReadStatus() {
        return readStatus;
    }

    public void setReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }

    @Override
    public String getRedirectPage() {
        return redirectPage;
    }

    public void setRedirectPage(String redirectPage) {
        this.redirectPage = redirectPage;
    }

    @Override
    public void setAsRead() {
        this.readStatus = true;
    }
}
