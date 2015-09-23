/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import entities.Matchh;
import mot.interfaces.AdvancingMatchData;
import org.primefaces.component.panel.Panel;
import web.models.DashboardPanel;

/**
 *
 * @author java
 */
public class BracketUtil {

    public static int numberOfRounds(int competitorsAmount) {
        byte result = 1;

        while (Math.pow(2, (double) result) != competitorsAmount) {
            if (result == Byte.MAX_VALUE) {
                throw new IllegalStateException("Nie jest potega 2 competitiorsamount = " + competitorsAmount);
            }
            result++;
        }

        return result;
    }

    public static int advancedMatchNumber(AdvancingMatchData matchData, int competitorCount) {

        System.out.println("COMPETITOR COUNT " + competitorCount);
        double matchCounter = 0.0;

        int matchesInRound = matchesInRound(competitorCount / 2, matchData.getRoundd());

        short firstMatchIndexInRound = firstMatchIndexInRound(competitorCount / 2, matchData.getRoundd());

        System.out.println("matchesInRound " + matchesInRound + " -- firstMatchIndexInRound"
                + firstMatchIndexInRound);

        for (short i = 0; i <= matchesInRound; i++) {
            matchCounter += 0.5;
            if ((i + firstMatchIndexInRound) == matchData.getMatchNumber()) {

                break;
            }
        }

        int advancedMatchNumber = ((Double) (firstMatchIndexInRound + matchesInRound - 1 + Math.ceil(matchCounter))).intValue();

        System.out.println("ZNALEIONY MATHC NUMBER " + advancedMatchNumber);
        return advancedMatchNumber;
    }

    public static int matchesInRound(int firstRoundMatches, short round) {
        if (round < 1) {
            throw new IllegalStateException("Round is lower than 1");
        }
        if (round == 1) {
            return firstRoundMatches;
        }
        return matchesInRound(firstRoundMatches, (short) (round - 1)) / 2;
    }

    public static short firstMatchIndexInRound(int firstRoundMatches, short round) {
        if (round < 1) {
            throw new IllegalStateException("Round is lower than 1");
        }
        if (round == 1) {
            return 1;
        }

        return (short) (firstMatchIndexInRound(firstRoundMatches / 2, (short) (round - 1)) + firstRoundMatches);
    }

    public static void makeSerializablePanel(DashboardPanel dp) {
        Panel p = new Panel(); // Panel not serializable excpetion workaround
        p.setId(dp.getPanel().getId());
        dp.setPanel(p);
    }

    public static int matchPositionInRound(int matchNumber, int competitorCount) {

        short roundNumber = 1;
        int roundsCount = 0;

        while (matchNumber >= (roundsCount += matchesInRound(competitorCount / 2, roundNumber))) {
            System.out.println("ROUNDS_COUNT " + roundsCount);
            roundNumber++;
        }

        short firstMatchIndexInRound = firstMatchIndexInRound(competitorCount / 2, roundNumber);

        System.out.println("mach firstMatchIndexInRound " + firstMatchIndexInRound);
//        roun_(count/2 - (nr - 1))
//        System.out.println("WYNIK " + )
        return 0;
    }
}
