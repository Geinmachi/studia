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
public interface InboxEvent {
    
    Date getDate();
    
    String getTitle();
    
    String getDescription();
    
    boolean isReadStatus();
    
    String getRedirectPage();
    
    void setAsRead();
}
