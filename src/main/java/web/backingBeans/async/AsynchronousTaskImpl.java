/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans.async;

import java.util.concurrent.Future;

/**
 *
 * @author java
 */
public class AsynchronousTaskImpl<V> implements AsynchronousTask {

    private Future<V> futureTask;
    
    private final String message;
    
    private final String details;
    
    public AsynchronousTaskImpl(Future<V> futureTask, String message, String details) {
        this.futureTask = futureTask;
        this.message = message;
        this.details = details;
    }

    @Override
    public boolean isDone() {
        return futureTask == null ? true : futureTask.isDone();
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public String details() {
        return details;
    }

}
