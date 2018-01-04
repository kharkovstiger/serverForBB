package serverForBB.service;

import serverForBB.model.Game;

import java.util.List;

public interface GameService {
    Game parseBoxScore(String response);

    Game save(Game game);

    List<Game> getAllGamesForCountry(String country, boolean official);

    List<Game> getAllGamesForCountryForSeason(String country, boolean official, Integer season);

    List<Game> getAllGamesForCountryAgainstCountry(String s, String s1, boolean official);

    List<Game> getGamesForList(List<String> ids);
}
