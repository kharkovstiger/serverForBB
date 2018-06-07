package serverForBB.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import serverForBB.model.utils.Stats;

import java.util.HashMap;
import java.util.Map;

@Data
@Document
@EqualsAndHashCode(of = "id")
public class Player {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private Map<String, Double> stats;
    private String country;

    public Player(String id, String country) {
        this.id = id;
        this.country = country;
        stats=new HashMap<>();
        Stats.initialize(stats, Player.class.getName());
    }

    public Player() {

    }

    public void addStat(Map<String, Double> stats) {
        stats.forEach((s, integer) -> this.stats.replace(s,this.stats.get(s)+integer));
    }
}
