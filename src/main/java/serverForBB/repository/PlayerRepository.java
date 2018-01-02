package serverForBB.repository;

import serverForBB.model.Player;

import java.util.List;

public interface PlayerRepository {
    
    Player save(Player player);
    
    Player findOne(String id);
    
    List<Player> getAllFromCountry(String country);
}
