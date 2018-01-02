package serverForBB.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import serverForBB.model.Player;

import java.util.List;

@Repository
public class DefaultPlayerRepository implements PlayerRepository {
    
    private final PlayerCrudRepository playerCrudRepository;

    @Autowired
    public DefaultPlayerRepository(PlayerCrudRepository playerCrudRepository) {
        this.playerCrudRepository = playerCrudRepository;
    }

    @Override
    public Player save(Player player) {
        return playerCrudRepository.save(player);
    }

    @Override
    public Player findOne(String id) {
        return playerCrudRepository.findOne(id);
    }

    @Override
    public List<Player> getAllFromCountry(String country) {
        return playerCrudRepository.getAllFromCountry(country);
    }
}