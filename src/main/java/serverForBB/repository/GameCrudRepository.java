package serverForBB.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import serverForBB.model.Game;

import java.util.List;

public interface GameCrudRepository extends MongoRepository<Game, String> {

    @Override
    Game save(Game game);

    @Override
    List<Game> findAll();

    @Override
    Game findOne(String s);

    @Override
    boolean exists(String s);
}
