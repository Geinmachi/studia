/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans.mok;

import java.util.logging.Logger;
import javax.inject.Inject;
import web.controllers.AccountController;

/**
 *
 * @author java
 */
public abstract class AccountBackingBean {
    
    @Inject
    protected AccountController controller;
    
    @Inject
    protected transient Logger logger;
}