/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans.async;

import java.util.Date;
import java.util.concurrent.Future;

/**
 *
 * @author java
 */
public class AsynchronousTaskImpl<V> implements AsynchronousTask {

    private final Future<V> futureTask;
    
    private final String message;
    
    private final String details;
    
    private final String resultPage;
    
    private final Date date;
    
    public AsynchronousTaskImpl(Future<V> futureTask, String message, String details, String resultPage) {
        this.futureTask = futureTask;
        this.message = message;
        this.details = details;
        this.resultPage = resultPage;
        this.date = new Date();
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public String details() {
        return details;
    }

    @Override
    public String resultPage() {
        return resultPage;
    }

    @Override
    public Date date() {
        return date;
    }

    @Override
    public Future asyncTask() {
        return futureTask;
    }
}
