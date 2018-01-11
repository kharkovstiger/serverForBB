package serverForBB.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import serverForBB.model.Game;
import serverForBB.model.Player;
import serverForBB.model.Stats;
import serverForBB.model.Team;
import serverForBB.repository.PlayerRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DefaultPlayerService implements PlayerService{
    
    private final PlayerRepository playerRepository;

    @Autowired
    public DefaultPlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public Player addStats(Player player) {
        Player existPlayer=playerRepository.findOne(player.getId());
        if (existPlayer==null)
            return playerRepository.save(player);
        existPlayer.addStat(player.getStats());
        return playerRepository.save(existPlayer);
    }

    @Override
    public List<Player> getAllFromCountry(String country) {
        return playerRepository.getAllFromCountry(country);
    }

    @Override
    public List<Player> getAllFromCountryForGame(String country) {
        List<Player> players=playerRepository.getAllFromCountryMinGames(country);
        return getAverages(players, "game");
    }

    @Override
    public List<Player> getAverages(List<Player> players, String type) {
        players.forEach(player -> {
            Double divider=type.equals("game")?player.getStats().get("games"):player.getStats().get("minutes")/48;
            player.getStats().forEach((s, aDouble) -> {
                if (!s.equals("games"))
                    player.getStats().replace(s,aDouble/divider);
            });
        });
        return players;
    }

    @Override
    public List<Player> getAllFromCountryForMinutes(String country) {
        List<Player> players=playerRepository.getAllFromCountryMinGames(country);
        return getAverages(players, "minutes");
    }

    @Override
    public List<Player> getAllForGame(boolean u21) {
        List<Player> players=playerRepository.getAllMinGames(u21);
        return getAverages(players, "game");
    }

    @Override
    public List<Player> getAllForMinutes(boolean u21) {
        List<Player> players=playerRepository.getAllMinGames(u21);
        return getAverages(players, "minutes");
    }

    @Override
    public List<Player> getPlayersStatForGameList(List<Game> games, String country) {
        List<Player> players=new ArrayList<>();
        Map<String, Double> summaryStats=new HashMap<>();
        summaryStats.put("doubleDouble", 0.);
        summaryStats.put("tripleDouble", 0.);
        summaryStats.put("quadroDouble", 0.);
        summaryStats.put("pentaDouble", 0.);
        summaryStats.put("twenty", 0.);
        Map<String, Double> records=new HashMap<>();
        Stats.initialize(records, Player.class.getName());
        games.forEach(game -> {
            if (game.getAwayTeam().getName().equals(country)) {
                addStat(game.getAwayTeam(), players, summaryStats, records);
            } else {
                addStat(game.getHomeTeam(), players, summaryStats, records);
            }
        });
        return players;
    }

    private void addStat(Team team, List<Player> players, Map<String, Double> map, Map<String, Double> records) {
        team.getPlayers().forEach(player -> {
            Player existPlayer=players.stream().filter(player1 -> player1.equals(player)).findFirst().orElse(null);
            if (existPlayer != null) {
                existPlayer.getStats().forEach((s, aDouble) -> existPlayer.getStats().
                        replace(s,existPlayer.getStats().get(s)+player.getStats().get(s)));
            }
            else 
                players.add(player);
            player.getStats().forEach((s, aDouble) -> {
                if (aDouble>records.get(s))
                    records.replace(s,aDouble);
            });
        });
    }
}
