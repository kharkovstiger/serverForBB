package serverForBB.model;

import lombok.Data;

import java.util.Map;

import static javafx.scene.input.KeyCode.T;

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

    public static void initialize(Map<String, Double> stats, String className){
        try {
            Class clas=Class.forName(className);
            if (clas.newInstance() instanceof Player){
                stats.put("games",0.);
                stats.put("minutes",0.);
                stats.put("plusMinus",0.);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        stats.put("fieldGoals",0.);
        stats.put("fieldGoalsAttempts",0.);
        stats.put("threePoints",0.);
        stats.put("threePointsAttempts",0.);
        stats.put("freeThrows",0.);
        stats.put("freeThrowsAttempts",0.);
        stats.put("offensiveRebounds",0.);
        stats.put("rebounds",0.);
        stats.put("assists",0.);
        stats.put("turnovers",0.);
        stats.put("steals",0.);
        stats.put("blocks",0.);
        stats.put("fouls",0.);
        stats.put("points",0.);
    }
}
