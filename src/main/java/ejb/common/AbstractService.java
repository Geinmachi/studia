/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.common;

import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.SessionContext;

/**
 *
 * @author java
 */
public abstract class AbstractService {

    @Resource
    private SessionContext sctx;

    /**
     * Pole logger klasy Logger
     */
    protected static final Logger logger = Logger.getGlobal();

    /**
     * Pole logLevel klasy Level ustawiające poziom logowania informacji do
     * dziennika zdarzeń na poziomie SEVERE
     */
    protected static final Level logLevel = Level.SEVERE;

    private String transactionId;

    /**
     * Metoda zapisująca transakcję do dziennika zdarzeń po jej rozpoczęciu
     */
    public void afterBegin() {
        transactionId = Long.toString(System.currentTimeMillis()) + ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);
        StringBuilder msg = new StringBuilder("Transakcja id = ");
        msg.append(transactionId);
        msg.append(" rozpoczeta; użytkownik: ");
        msg.append(sctx.getCallerPrincipal().getName());

        logger.log(logLevel, msg.toString());
    }

    /**
     * Metoda zapisująca transakcję do dziennika zdarzeń przed zatwierdzeniem
     */
    public void beforeCompletion() {
        StringBuilder msg = new StringBuilder("Transakcja id = ");
        msg.append(transactionId);
        msg.append(" przed zatwierdzeniem; użytkownik: ");
        msg.append(sctx.getCallerPrincipal().getName());

        logger.log(logLevel, msg.toString());
    }

    /**
     * Metoda zapisująca transakcję do dziennika zdarzeń po jej zatwierdzeniu
     * lub odwołaniu
     *
     * @param committed
     */
    public void afterCompletion(boolean committed) {
        StringBuilder msg = new StringBuilder("Transakcja id = ");
        msg.append(transactionId);

        if (committed) {
            msg.append(" zatwierdzona;");
        } else {
            msg.append(" odwołana;");
        }

        msg.append("użytkownik: ");
        msg.append(sctx.getCallerPrincipal().getName());

        logger.log(logLevel, msg.toString());
    }
}

