/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.logUtils;

import java.io.Serializable;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import web.qualifiers.Logging;

/**
 *
 * @author java
 */
@Logging
@Interceptor
public class LoggerInterceptor implements Serializable {

    @AroundInvoke
    public Object logMethodEntry(InvocationContext invocationContext)
            throws Exception {
        
        System.out.println("Entering method: "
                + invocationContext.getMethod().getName() + " in class "
                + invocationContext.getMethod().getDeclaringClass().getName());

        Object returnObject = null;
        try {
            returnObject = invocationContext.proceed();
        } catch (Exception e) {
            System.out.println("Method " + invocationContext.getMethod().getName() + " was finished by the exception: " + e.getMessage());
            throw e;
        }
        
//        System.out.println("Exiting method: "
//                + invocationContext.getMethod().getName() + " in class "
//                + invocationContext.getMethod().getDeclaringClass().getName());
//        
        return returnObject;
    }

}
