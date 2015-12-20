/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans.mot.competition;

import javax.inject.Inject;
import web.backingBeans.async.PollListener;
import web.controllers.CompetitionController;

/**
 *
 * @author java
 */
public abstract class CompetitionAsyncBackingBean extends CompetitionBackingBean {
    
    @Inject
    protected PollListener async;
}
