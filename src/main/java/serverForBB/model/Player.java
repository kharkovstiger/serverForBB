package serverForBB.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Player {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private Stats stats;
    private String country;

    public void addStat(Stats stats) {
        this.stats.setGames(this.stats.getGames()+1);
        this.stats.setMinutes(this.stats.getMinutes()+stats.getMinutes());
        this.stats.setFieldGoals(this.stats.getFieldGoals()+stats.getFieldGoals());
        this.stats.setFieldGoalsAttempts(this.stats.getFieldGoalsAttempts()+stats.getFieldGoalsAttempts());
        this.stats.setThreePoints(this.stats.getThreePoints()+stats.getThreePoints());
        this.stats.setThreePointsAttempts(this.stats.getThreePointsAttempts()+stats.getThreePointsAttempts());
        this.stats.setFreeThrows(this.stats.getFreeThrows()+stats.getFreeThrows());
        this.stats.setFreeThrowsAttempts(this.stats.getFreeThrowsAttempts()+stats.getFreeThrowsAttempts());
        this.stats.setPlusMinus(this.stats.getPlusMinus()+stats.getPlusMinus());
        this.stats.setOffensiveRebounds(this.stats.getOffensiveRebounds()+stats.getOffensiveRebounds());
        this.stats.setRebounds(this.stats.getRebounds()+stats.getRebounds());
        this.stats.setAssists(this.stats.getAssists()+stats.getAssists());
        this.stats.setTurnovers(this.stats.getTurnovers()+stats.getTurnovers());
        this.stats.setSteals(this.stats.getSteals()+stats.getSteals());
        this.stats.setBlocks(this.stats.getBlocks()+stats.getBlocks());
        this.stats.setFouls(this.stats.getFouls()+stats.getFouls());
        this.stats.setPoints(this.stats.getPoints()+stats.getPoints());
    }
}
