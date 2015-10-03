/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.managers;

import entities.Competition;
import entities.Competitor;
import entities.Score;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import mot.facades.CompetitionFacadeLocal;
import mot.facades.ScoreFacadeLocal;
import utils.SortUtil;
import ejbCommon.TrackerInterceptor;

/**
 *
 * @author java
 */
@Stateless
@Interceptors({TrackerInterceptor.class})
public class PresentCompetitionManager implements PresentCompetitionManagerLocal {

    @EJB
    private CompetitionFacadeLocal competitionFacade;

    @EJB
    private ScoreFacadeLocal scoreFacade;

    @Override
    public List<Competition> findAllCompetitions() {

        return competitionFacade.findAll();
    }

    @Override
    public Competition getInitializedCompetition(int idCompetition) {
        return competitionFacade.findAndInitializeGD(idCompetition);
    }

    @Override // niewykorzystawana?
    public List<Score> findCompetitionScore(int idCompetition) {
        return scoreFacade.findScoreByIdCompetition(idCompetition);
    }

    @Override
    public Map<Competitor, Integer> getCompetitionResults(int idCompetition) {
        List<Score> scoreList = new ArrayList<>(scoreFacade.findScoreByIdCompetition(idCompetition));

        scoreList.sort(new Comparator<Score>() {

            @Override
            public int compare(Score o1, Score o2) {
                return Short.compare(o2.getScore(), o1.getScore());
            }

        });

        Map<Short, Integer> positionScoreMap = new HashMap<>();

        int positionCounter = 1;
        for (int i = 0; i < scoreList.size() - 1; i++) {
            if (Short.compare(scoreList.get(i).getScore(), scoreList.get(i + 1).getScore()) != 0) {
                positionScoreMap.put(scoreList.get(i).getScore(), positionCounter++);
            }
        }

        positionScoreMap.put((short) 0, positionCounter);

        Map<Competitor, Integer> competitorPositionMap = new HashMap<>();

        for (Score s : scoreList) {
            competitorPositionMap.put(s.getIdCompetitor(), positionScoreMap.get(s.getScore()));
        }

        return SortUtil.sortByValue(competitorPositionMap, true);

    }

}
