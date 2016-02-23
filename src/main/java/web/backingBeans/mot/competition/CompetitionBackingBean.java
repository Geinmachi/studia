/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans.mot.competition;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import web.controllers.CompetitionController;
import web.qualifiers.BackingBean;

/**
 *
 * @author java
 */
//@Logging
@BackingBean
public abstract class CompetitionBackingBean {

    @Inject
    protected CompetitionController controller;

    @Inject
    protected transient Logger logger;

}
