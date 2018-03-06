package serverForBB.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import serverForBB.model.*;
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
    public PlayerResponse getPlayersStatForGameList(List<Game> games, String country) {
        List<Player> players=new ArrayList<>();
        Map<String, List<Record>> doubles=new HashMap<>();
        doubles.put("doubleDouble", new ArrayList<>());
        doubles.put("tripleDouble", new ArrayList<>());
        doubles.put("quadroDouble", new ArrayList<>());
        doubles.put("pentaDouble", new ArrayList<>());
        doubles.put("twenty", new ArrayList<>());
        Map<String, Record> records=new HashMap<>();
//        Stats.initialize(records, Player.class.getName());
        games.forEach(game -> {
            if (game.getAwayTeam().getName().equals(country)) {
                addStat(game, game.getAwayTeam(), players, doubles, records);
            } else {
                addStat(game, game.getHomeTeam(), players, doubles, records);
            }
        });
        return new PlayerResponse(players, doubles, records);
    }

    private void addStat(Game game, Team team, List<Player> players, Map<String, List<Record>> doubles, Map<String, Record> records) {
        team.getPlayers().forEach(player -> {
            Player existPlayer=players.stream().filter(player1 -> player1.equals(player)).findFirst().orElse(null);
            if (existPlayer != null) {
                existPlayer.getStats().forEach((s, aDouble) -> existPlayer.getStats().
                        replace(s,existPlayer.getStats().get(s)+player.getStats().get(s)));
            }
            else 
                players.add(player);
            player.getStats().forEach((s, aDouble) -> {
                if (records.get(s)==null || aDouble>records.get(s).getNumbers())
                    records.replace(s, new Record(aDouble, player, game));
            });
            int[] c=isDoubles(player.getStats());
            List<Record> record;
            switch (c[0]){
                case 2:
                    record=doubles.get("doubleDouble");
                    record.add(new Record(0., player, game));
                    doubles.replace("doubleDouble", record);
                    break;
                case 3:
                    record=doubles.get("tripleDouble");
                    record.add(new Record(0., player, game));
                    doubles.replace("tripleDouble", record);
                    break;
                case 4:
                    record=doubles.get("quadroDouble");
                    record.add(new Record(0., player, game));
                    doubles.replace("quadroDouble", record);
                    break;
                case 5:
                    record=doubles.get("pentaDouble");
                    record.add(new Record(0., player, game));
                    doubles.replace("pentaDouble", record);
                    break;
            }
            if (c[1]>=2){
                record=doubles.get("twenty");
                record.add(new Record(0., player, game));
                doubles.replace("twenty", record);
            }
        });
    }

    private int[] isDoubles(Map<String, Double> stats) {
        final int[] c = {0, 0};
        stats.forEach((s, aDouble) -> {
            if (s.equals("points") || s.equals("rebounds") || s.equals("assists") || s.equals("steals") || s.equals("blocks")) {
                c[0] += (aDouble >= 10 ? 1 : 0);
                c[1] += (aDouble >= 20 ? 1 : 0);
            }
        });
        return c;
    }
}
