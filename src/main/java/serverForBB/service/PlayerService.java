package serverForBB.service;

import serverForBB.model.Game;
import serverForBB.model.Player;
import serverForBB.model.utils.OffensiveTactic;
import serverForBB.model.utils.PlayerResponse;

import java.util.List;
import java.util.Map;

public interface PlayerService {
    
    Player addStats(Player player);

    List<Player> getAllFromCountry(String country);

    List<Player> getAllFromCountryForGame(String country);

    List<Player> getAllFromCountryForMinutes(String country);

    List<Player> getAllForGame(boolean u21);

    List<Player> getAllForMinutes(boolean u21);

    PlayerResponse getPlayersStatForGameList(List<Game> games, String country);

    List<Player> getAverages(List<Player> players, String game);

    Map<OffensiveTactic,Map<String,Double>> getPlayerStatsForOffensiveTactics(List<Game> games, String country, String playerId);
}
