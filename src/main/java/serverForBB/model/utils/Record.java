package serverForBB.model.utils;

import lombok.Data;
import serverForBB.model.Game;
import serverForBB.model.Player;

@Data
public class Record {
    private Double numbers;
    private Player player;
    private Game game;

    public Record(Double numbers, Player player, Game game) {
        this.numbers = numbers;
        this.player = player;
        this.game = game;
    }
}
