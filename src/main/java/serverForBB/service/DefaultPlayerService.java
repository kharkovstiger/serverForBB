package serverForBB.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import serverForBB.model.Player;
import serverForBB.repository.PlayerRepository;

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
}
