/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.timers;

import java.util.Date;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import mot.services.CompetitionServiceLocal;

/**
 *
 * @author java
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class CompetitorMatchesStatisticsTimer implements CompetitorMatchesStatisticsTimerLocal {

    @EJB
    private CompetitionServiceLocal service;

    @Resource
    SessionContext s;

    @Resource
    TimerService timerService;

    private static final long COMPETITOR_MATCHES_STATISTICS_TIMER_INTERVAL = 1000; // 1 sec

    @Timeout
    private void timeout(Timer timer) {
        System.out.println("Timer event: " + new Date());
//        System.out.println("Service Identity: " + service.getIdentity());

        if (service.isCompetitorMatchesStatisticsFetched()) {
            System.out.println("Juz pobralo, konczy timer");
            timer.cancel();
        } else {
            System.out.println("jeszcze nie pobralo, spradzi za " + COMPETITOR_MATCHES_STATISTICS_TIMER_INTERVAL + "ms");
        }
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public void startTimer() {
        System.out.println("Startuje timer");
        timerService.createIntervalTimer(COMPETITOR_MATCHES_STATISTICS_TIMER_INTERVAL,
                COMPETITOR_MATCHES_STATISTICS_TIMER_INTERVAL, null);
    }
}
