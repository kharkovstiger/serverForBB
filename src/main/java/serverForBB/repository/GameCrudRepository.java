package serverForBB.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
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

    @Query(value = "{$or:[{'homeTeam.name':?0}, {'awayTeam.name':?0}]}")
    List<Game> getAllGamesForCountry(String country);

    @Query(value = "{$or:[{'homeTeam.name':?0}, {'awayTeam.name':?0}], 'type':{$ne:'Scrimmage'}}")
    List<Game> getAllOfficialGamesForCountry(String country);

    @Query(value = "{$or:[{'homeTeam.name':?0}, {'awayTeam.name':?0}], 'season':?1, 'type':{$ne:'Scrimmage'}}")
    List<Game> getAllOfficialGamesForCountryForSeason(String country, Integer season);

    @Query(value = "{$or:[{'homeTeam.name':?0}, {'awayTeam.name':?0}], 'season':?1}")
    List<Game> getAllGamesForCountryForSeason(String country, Integer season);

    @Query(value = "{$or:[{'homeTeam.name':?0, 'awayTeam.name':?1}, {'homeTeam.name':?1, 'awayTeam.name':?0}], 'type':{$ne:'Scrimmage'}}")
    List<Game> getAllOfficialGamesForCountryAgainstCountry(String s, String s1);

    @Query(value = "{$or:[{'homeTeam.name':?0, 'awayTeam.name':?1}, {'homeTeam.name':?1, 'awayTeam.name':?0}]}")
    List<Game> getAllGamesForCountryAgainstCountry(String s, String s1);
}
