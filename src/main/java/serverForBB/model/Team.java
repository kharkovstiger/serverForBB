package serverForBB.model;

import lombok.Data;

import java.util.List;

@Data
public class Team {
    private String name;
    private List<Player> players;
    private Stats stats;

    public void addPlayer(Player player){
        players.add(player);
    }
}
