package serverForBB.service;

import serverForBB.model.Player;

import java.util.List;

public interface PlayerService {
    
    Player addStats(Player player);

    List<Player> getAllFromCountry(String country);

    List<Player> getAllFromCountryForGame(String country);

    List<Player> getAllFromCountryForMinutes(String country);

    List<Player> getAllForGame(boolean u21);

    List<Player> getAllForMinutes(boolean u21);
}
