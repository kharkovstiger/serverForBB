package serverForBB.model.utils;

import lombok.Data;
import serverForBB.model.Player;

import java.util.List;
import java.util.Map;

@Data
public class PlayerResponse {
    private List<Player> players;
    Map<String, List<Record>> doubles;
    Map<String, List<Record>> records;

    public PlayerResponse(List<Player> players, Map<String, List<Record>> doubles, Map<String, List<Record>> records) {
        this.players = players;
        this.doubles = doubles;
        this.records = records;
    }
}
