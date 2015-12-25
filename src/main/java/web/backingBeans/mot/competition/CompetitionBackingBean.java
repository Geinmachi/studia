/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans.mot.competition;

import java.util.logging.Logger;
import javax.inject.Inject;
import web.controllers.CompetitionController;

/**
 *
 * @author java
 */
//@Logging
public abstract class CompetitionBackingBean {
    
    @Inject
    protected CompetitionController controller;
    
    @Inject
    protected transient Logger logger;

    
}
