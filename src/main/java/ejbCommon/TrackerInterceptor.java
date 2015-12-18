/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbCommon;

import exceptions.ApplicationException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

/**
 * Klasa interceptora generuje dzienniki zdarzeń uwzględniające każde wywołanie
 * (wraz z wartościami parametrów) oraz każde zakończenie (wraz z wartością
 * zwracaną lub zgłoszonym wyjątkiem) metody, z uwzględnieniem tożsamości
 * użytkownika oraz odpowiednim znacznikiem czasowym. Jeżeli parametrem lub
 * wartością zwracaną metody jest obiekt encji (ew. ich kolekcja), wówczas dla
 * każdego obiektu zapisywana jest jej identyfikator wraz z bieżącym numerem
 * wersji.
 *
 * @author ssbd03
 */
public class TrackerInterceptor {

    @Resource
    private SessionContext sctx;

    private static final Logger logger = Logger.getLogger(TrackerInterceptor.class.getName());
    private static final Level logLevel = Level.INFO;

    @AroundInvoke
    public Object traceInvoke(InvocationContext ictx) throws Exception {
        Object result;
        StringBuilder message = new StringBuilder("METHOD: ");

        message.append(ictx.getMethod());

        message.append(" ||  USER: ");
        message.append(sctx.getCallerPrincipal().getName());

        message.append(" || PARAMETERS: ");
        message.append(getParametrsValues(ictx));

        long start = System.currentTimeMillis();
        long end;
        try {
            result = ictx.proceed();
            end = System.currentTimeMillis();
        } catch (Exception e) {
            end = System.currentTimeMillis();
            message.append("|| THREW EXCEPTION: ");
            message.append(e.toString());
            message.append(getExceptionFields(e));
            message.append("|| EXECUTION TIME: ");
            message.append(end - start);
            message.append("ms");
            logger.log(logLevel, message.toString());
            throw e;
        }

        message.append(" ||RESULT: ");
        message.append(getResultValue(result));
        message.append("|| EXECUTION TIME: ");
        message.append(end - start);
        message.append("ms");

        logger.log(logLevel, message.toString());

        return result;
    }

    private String getParametrsValues(InvocationContext ictx) {
        StringBuilder msg = new StringBuilder();

        if (ictx.getParameters() == null) {
            msg.append("null");
        } else {
            for (Object param : ictx.getParameters()) {
                if (param == null) {
                    msg.append("null ");
                } else {
                    msg.append(param.toString());
                    msg.append(" ");
                }
            }
        }

        return msg.toString();
    }

    private String getResultValue(Object result) {
        String outputMsg;

        if (null == result) {
            outputMsg = "null";
        } else {
            outputMsg = result.toString();
        }

        return outputMsg;
    }

    private String getExceptionFields(Exception e) {
        StringBuilder msg = new StringBuilder();

        if (!(e instanceof ApplicationException)) {
            msg.append("|| NOT APPLICATION EXCEPTION");

            return msg.toString();
        }

        msg.append("|| FIELDS: ");
        Method[] allMethods = e.getClass().getDeclaredMethods();
        for (Method m : allMethods) {
            String methodName = m.getName();

            if (methodName.startsWith("get")) {
                msg.append(methodName.substring(3));
                msg.append("= ");
                try {
                    msg.append(m.invoke(e, new Object[]{}));
                    msg.append(", ");
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(TrackerInterceptor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return msg.toString();
    }
}
