package serverForBB.sheduled;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import serverForBB.model.Game;
import serverForBB.service.GameService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Component
public class ScheduledTasks {
    
    private final GameService gameService;
    private final int MIN_MAX_ID=373;

    @Autowired
    public ScheduledTasks(GameService gameService) {
        this.gameService = gameService;
    }

    @Scheduled(cron = "0 0 4 ? * 3")
    public void addGames(){
        int season=gameService.getSeason(LocalDate.now());
        final Integer maxId= Integer.valueOf(gameService.getMaxId(season));
        Integer id=maxId;
        System.err.println("Begin to add new games");
        while (id-maxId<1500){
            try {
                gameService.addGame(++id);
                System.err.println("Added game with ID: "+id);
            }
            catch (ArrayIndexOutOfBoundsException e){
                System.err.println(Arrays.toString(e.getStackTrace()));
            }
        }
    }
}
