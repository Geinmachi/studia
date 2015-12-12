/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.models;

import mot.interfaces.ReportPlacementData;

/**
 *
 * @author java
 */
public class ReportPlacements implements ReportPlacementData {

    private int firstPlaces;
    
    private int secondPlaces;
    
    private int thirdPlaces;
    
    private int participateCount;

    @Override
    public int getFirstPlaces() {
        return firstPlaces;
    }

    public void setFirstPlaces(int firstPlaces) {
        this.firstPlaces = firstPlaces;
    }

    @Override
    public int getSecondPlaces() {
        return secondPlaces;
    }

    public void setSecondPlaces(int secondPlaces) {
        this.secondPlaces = secondPlaces;
    }

    @Override
    public int getThirdPlaces() {
        return thirdPlaces;
    }

    public void setThirdPlaces(int thirdPlaces) {
        this.thirdPlaces = thirdPlaces;
    }

    @Override
    public int getParticipateCount() {
        return participateCount;
    }

    public void setParticipateCount(int participantCount) {
        this.participateCount = participantCount;
    }
}
