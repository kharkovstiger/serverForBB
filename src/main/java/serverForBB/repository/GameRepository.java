package serverForBB.repository;

import serverForBB.model.Game;

public interface GameRepository {
    Game save(Game game);

    boolean exists(String id);
}
