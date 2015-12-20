/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans.async;

/**
 *
 * @author java
 */
public interface AsynchronousTask {
    
    boolean isDone();
    
    String message();
    
    String details();
    
}
