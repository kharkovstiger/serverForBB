package serverForBB.model;

import lombok.Data;
import serverForBB.model.utils.DefensiveTactic;
import serverForBB.model.utils.OffensiveTactic;

import java.util.List;
import java.util.Map;

@Data
public class Team {
    private String name;
    private List<Player> players;
    private Map<String, Double> stats;
    private OffensiveTactic offensiveTactic;
    private DefensiveTactic defensiveTactic;

    public Team() {

    }

    public void addPlayer(Player player){
        players.add(player);
    }
}
