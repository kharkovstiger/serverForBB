package serverForBB.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import serverForBB.model.Player;

import java.util.List;

public interface PlayerCrudRepository extends MongoRepository<Player, String> {

    @Override
    Player save(Player player);

    @Override
    List<Player> findAll();

    @Override
    Player findOne(String id);
    
    @Query(value = "{'country':?0}")
    List<Player> getAllFromCountry(String country);

    @Query(value = "{'country':?0, 'stats.games':{$gt:4}}")
    List<Player> getAllFromCountryMinGames(String country);

    @Query(value = "{'country':{$regex:?0}, 'stats.games':{$gt:0}}")
    List<Player> getAllMinGames(String regex);
}
