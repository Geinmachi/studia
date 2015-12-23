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
public interface AsynchronousTask<V> {
    
    Future<V> asyncTask();
    
    String message();
    
    String details();
    
    String resultPage();
    
    Date date();
}
