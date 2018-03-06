package serverForBB.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class PlayerResponse {
    private List<Player> players;
    Map<String, List<Record>> doubles;
    Map<String, Record> records;

    public PlayerResponse(List<Player> players, Map<String, List<Record>> doubles, Map<String, Record> records) {
        this.players = players;
        this.doubles = doubles;
        this.records = records;
    }
}
