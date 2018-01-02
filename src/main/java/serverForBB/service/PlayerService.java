package serverForBB.service;

import serverForBB.model.Player;

import java.util.List;

public interface PlayerService {
    
    Player addStats(Player player);

    List<Player> getAllFromCountry(String country);
}
