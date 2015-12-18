/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.models;

import entities.Competitor;
import exceptions.InvalidPlaceException;
import java.util.ArrayList;
import java.util.List;
import mot.interfaces.CompetitionPodiumData;

/**
 *
 * @author java
 */
public class CompetitionPodium implements CompetitionPodiumData {

    private String competitionName;

    private Competitor firstPlaceCompetitor;

    private Competitor secondPlaceCompetitor;

    private List<Competitor> thirdPlaceCompetitor = new ArrayList<>();

    private int competitorsCount;

    @Override
    public String getCompetitionName() {
        return competitionName;
    }

    public void setCompetitionName(String competitionName) {
        this.competitionName = competitionName;
    }

    @Override
    public Competitor getFirstPlaceCompetitor() {
        return firstPlaceCompetitor;
    }

    public void setFirstPlaceCompetitor(Competitor firstPlaceCompetitor) {
        this.firstPlaceCompetitor = firstPlaceCompetitor;
    }

    @Override
    public Competitor getSecondPlaceCompetitor() {
        return secondPlaceCompetitor;
    }

    public void setSecondPlaceCompetitor(Competitor secondPlaceCompetitor) {
        this.secondPlaceCompetitor = secondPlaceCompetitor;
    }

    @Override
    public List<Competitor> getThirdPlaceCompetitor() {
        return thirdPlaceCompetitor;
    }

    public void setThirdPlaceCompetitor(List<Competitor> thirdPlaceCompetitor) {
        this.thirdPlaceCompetitor = thirdPlaceCompetitor;
    }

    @Override
    public int getCompetitorsCount() {
        return competitorsCount;
    }

    public void setCompetitorsCount(int competitorsCount) {
        this.competitorsCount = competitorsCount;
    }

    public void setCompetitor(short place, Competitor competitor) throws InvalidPlaceException {
        switch (place) {
            case 1:
                if (firstPlaceCompetitor != null) {
                    throw InvalidPlaceException.placeAlreadyOccuped((short) 1, competitor);
                }
                firstPlaceCompetitor = competitor;
                break;
            case 2:
                if (secondPlaceCompetitor != null) {
                    throw InvalidPlaceException.placeAlreadyOccuped((short) 2, competitor);
                }
                secondPlaceCompetitor = competitor;
                break;
            case 3:
                if (thirdPlaceCompetitor.size() > 1) {
                    throw InvalidPlaceException.placeAlreadyOccuped((short) 3, competitor);
                }
                thirdPlaceCompetitor.add(competitor);
                break;
            default:
                throw InvalidPlaceException.invalidPlace(place);
        }
    }
}
