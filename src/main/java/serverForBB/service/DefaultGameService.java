package serverForBB.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import serverForBB.model.Game;
import serverForBB.repository.GameRepository;

@Service
public class DefaultGameService implements GameService {
    
    private final GameRepository gameRepository;

    @Autowired
    public DefaultGameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public Game parseBoxScore(String response) {
        Game game=new Game();
        
        return game;
    }

    @Override
    public Game save(Game game) {
        return gameRepository.save(game);
    }
}
