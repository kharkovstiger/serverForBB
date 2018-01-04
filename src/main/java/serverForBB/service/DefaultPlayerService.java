package serverForBB.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import serverForBB.model.Game;
import serverForBB.model.Player;
import serverForBB.model.Team;
import serverForBB.repository.PlayerRepository;

import java.util.ArrayList;
import java.util.List;

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
            Double divider=type.equals("game")?player.getStats().get("games"):player.getStats().get("minutes")*48;
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
        games.forEach(game -> {
            if (game.getAwayTeam().getName().equals(country)) {
                addStat(game.getAwayTeam(), players);
            } else {
                addStat(game.getHomeTeam(), players);
            }
        });
        return players;
    }

    private void addStat(Team team, List<Player> players) {
        team.getPlayers().forEach(player -> {
            Player existPlayer=players.stream().filter(player1 -> player1.equals(player)).findFirst().orElse(null);
            if (existPlayer != null) {
                existPlayer.getStats().forEach((s, aDouble) -> existPlayer.getStats().
                        replace(s,existPlayer.getStats().get(s)+player.getStats().get(s)));
            }
            else 
                players.add(player);
        });
    }
}
