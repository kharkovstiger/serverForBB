package serverForBB.model;

import lombok.Data;

@Data
public class Stats {
    private int games;
    private int minutes;
    private int fieldGoals;
    private int fieldGoalsAttempts;
    private int threePoints;
    private int threePointsAttempts;
    private int freeThrows;
    private int freeThrowsAttempts;
    private int plusMinus;
    private int offensiveRebounds;
    private int rebounds;
    private int assists;
    private int turnovers;
    private int steals;
    private int blocks;
    private int fouls;
    private int points;
}
