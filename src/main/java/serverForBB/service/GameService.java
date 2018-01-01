package serverForBB.service;

import serverForBB.model.Game;

public interface GameService {
    Game parseBoxScore(String response);

    Game save(Game game);
}
