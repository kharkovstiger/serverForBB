package serverForBB.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import serverForBB.model.Game;

@Repository
public class DefaultGameRepository implements GameRepository {
    
    private final GameCrudRepository gameCrudRepository;

    @Autowired
    public DefaultGameRepository(GameCrudRepository gameCrudRepository) {
        this.gameCrudRepository = gameCrudRepository;
    }

    @Override
    public Game save(Game game) {
        return gameCrudRepository.save(game);
    }
}
