/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbCommon;

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
    private static final Level logLevel = Level.SEVERE;

    /**
     * *
     * Metoda przechwytująca wywołanie metody i zapis tego faktu do dziennika
     * zdarzeń
     *
     * @param ictx
     * @return Object
     * @throws Exception
     */
    @AroundInvoke
    public Object traceInvoke(InvocationContext ictx) throws Exception {

        Object result;
        StringBuilder message = new StringBuilder("wywołanie metody: ");

        message.append(ictx.getMethod());

        message.append("; użytkownik: ");
        message.append(sctx.getCallerPrincipal().getName());

        message.append("; wartości parametrów: ");
        message.append(getParametrsValues(ictx));

        try {
            result = ictx.proceed();
        } catch (Exception e) {
            message.append("; zakończone wyjątkiem: ");
            message.append(e.toString());
            logger.log(logLevel, message.toString());
            throw e;
        }

        message.append("; wartość zwrócona: ");
        message.append(getResultValue(result));

    //    logger.log(logLevel, message.toString());

        return result;
    }

    /**
     * Metoda zwraca parametry wywołania metody
     *
     * @param ictx
     * @return String
     */
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

    /**
     * *
     * Metoda zwraca rezultat wywołania metody
     *
     * @param result
     * @return String
     */
    private String getResultValue(Object result) {
        String outputMsg;

        if (null == result) {
            outputMsg = "null";
        } else {
            outputMsg = result.toString();
        }

        return outputMsg;
    }
}
