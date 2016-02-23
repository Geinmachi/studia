/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.logUtils;

import java.io.Serializable;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 *
 * @author java
 */
@Dependent
public class LoggerProducer implements Serializable {

    @Produces
    public Logger produceLogger(InjectionPoint injectionPoint) {
        Logger logger = Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());

        if (logger.getHandlers().length == 0) {
            logger.setUseParentHandlers(false);
            Handler conHdlr = new ConsoleHandler();
            conHdlr.setFormatter(new Formatter() {
                public String format(LogRecord record) {
                    return record.getLevel() + ": "
                            + record.getSourceClassName() + "##"
                            + record.getSourceMethodName() + ": "
                            + record.getMessage() + "\n";
                }
            });

            logger.addHandler(conHdlr);
        }
        
        return logger;
    }
}
