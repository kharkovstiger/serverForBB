package serverForBB.model;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Team {
    private String name;
    private List<Player> players;
    private Map<String, Double> stats;

    public Team() {

    }

    public void addPlayer(Player player){
        players.add(player);
    }
}
