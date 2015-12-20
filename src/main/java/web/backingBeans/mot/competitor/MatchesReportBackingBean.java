/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans.mot.competitor;

import entities.Competitor;
import entities.CompetitorMatch;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import web.backingBeans.async.AsynchronousTask;
import web.backingBeans.async.AsynchronousTaskImpl;
import web.backingBeans.mot.competition.CompetitionAsyncBackingBean;
import web.backingBeans.mot.competition.CompetitionBackingBean;
import web.utils.JsfUtils;

/**
 *
 * @author java
 */
@Named(value = "matchesReportBackingBean")
@SessionScoped
public class MatchesReportBackingBean extends CompetitionAsyncBackingBean implements Serializable {

    private static final Logger logger = Logger.getLogger(MatchesReportBackingBean.class.getName());

    private Future<List<CompetitorMatch>> futureCompetitorMatchList;

    private List<CompetitorMatch> competitorMatchList;

    private Competitor selectedCompetitor;

    public Future<List<CompetitorMatch>> getFutureCompetitorMatchList() {
        return futureCompetitorMatchList;
    }

    public void setFutureCompetitorMatchList(Future<List<CompetitorMatch>> futureCompetitorMatchList) {
        this.futureCompetitorMatchList = futureCompetitorMatchList;
    }

    public List<CompetitorMatch> getCompetitorMatchList() {
        return competitorMatchList;
    }

    /**
     * Creates a new instance of MatchesReportBackingBean
     */
    public MatchesReportBackingBean() {
    }

//    @PostConstruct
//    private void init() {
//        competitorMatchList = controller.generateCompetitorMatchesStatistics();
//    }
    public boolean initValues(Competitor competitor) {
        try {
//            if (selectedCompetitor != null && selectedCompetitor.equals(competitor)) {
//                competitorMatchList = futureCompetitorMatchList.get();
//                futureCompetitorMatchList = null;
//
//                return true;
//            }
            
            if (competitor.equals(selectedCompetitor)) { // if selected competitor is previous one
                System.out.println("competitor.equals(selectedCompetitor)");
                if (checkCompleteStatus()) {
                    return true;
                }
            }

            if (selectedCompetitor != null) {
                JsfUtils.addSuccessMessage("Wybrano nowego zawodnika", "pobiera dane an nowo", "globalContainer");
            } else {
                JsfUtils.addSuccessMessage("Dane zostaja pobierane asynchronicznie", "gdy beda gotowe wyskoczy powiadomienie", "globalContainer");
            }
            
            logger.log(Level.INFO, "Pobiera dane asyncrnoicznie111111");
            futureCompetitorMatchList = controller.generateCompetitorMatchesStatistics(competitor);
            async.addTask(new AsynchronousTaskImpl<>(futureCompetitorMatchList, "Pobralo statystyki", "meczow zawodnika"));

//            if (selectedCompetitor == null) { // if first click
//                logger.log(Level.INFO, "Pobiera dane asyncrnoicznie111111");
//                futureCompetitorMatchList = controller.generateCompetitorMatchesStatistics(competitor);
//                async.addTask(new AsynchronousTaskImpl(futureCompetitorMatchList, "Pobralo statystyki", "meczow zawodnika"));
//            } else { // clicked different competitor, should fetch new data
////                if (futureCompetitorMatchList == null) {
////                    logger.log(Level.INFO, "Pobiera dane asyncrnoicznie222222");
////                    futureCompetitorMatchList = controller.generateCompetitorMatchesStatistics(competitor);
////                    async.addTask(new AsynchronousTaskImpl(futureCompetitorMatchList, "Pobralo statystyki", "meczow zawodnika"));
////                } else 
//                if (futureCompetitorMatchList.isDone()) {
//                    logger.log(Level.INFO, "Pobralo, pokazuje strone2222");
//                    competitorMatchList = futureCompetitorMatchList.get();
////                    futureCompetitorMatchList = null;
////                selectedCompetitor = competitor;
//
//                    return true;
//                }
//            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
            throw new IllegalStateException("Cannot init a bean");
        }

        selectedCompetitor = competitor;
        return false;
    }

    private boolean checkCompleteStatus() throws InterruptedException, ExecutionException {
        if (futureCompetitorMatchList != null && !futureCompetitorMatchList.isDone()) {
            logger.log(Level.INFO, "W trakcie pobierania");
            JsfUtils.addSuccessMessage("Trwa pobieranie", "prosze czekac", "competitorListForm");
            return false;
        }
        if (futureCompetitorMatchList != null && futureCompetitorMatchList.isDone()) {
            logger.log(Level.INFO, "Pobralo, pokazuje strone1111");
            competitorMatchList = futureCompetitorMatchList.get();
//            futureCompetitorMatchList = null;

            return true;
        }

        return false;
    }
}
