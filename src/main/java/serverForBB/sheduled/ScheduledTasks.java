package serverForBB.sheduled;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import serverForBB.service.GameService;

import java.util.Arrays;

@Component
public class ScheduledTasks {
    
    private final GameService gameService;
    private final int MIN_MAX_ID=373;

    @Autowired
    public ScheduledTasks(GameService gameService) {
        this.gameService = gameService;
    }

//    @Scheduled(cron = "0 0 2 ? * 2")
    @Scheduled(cron = "0 53 16 ? * *")
    public void addGames(){
//        Integer maxId= Integer.valueOf(gameService.getMaxId());
        System.err.println("Begin to add new games");
        Integer maxId=373;
        boolean flag=true;
        while (flag){
            try {
                gameService.addGame(++maxId);
                System.err.println("Added game with ID: "+maxId);
            }
            catch (ArrayIndexOutOfBoundsException e){
                System.err.println(Arrays.toString(e.getStackTrace()));
                if (maxId>44500)
                    flag=false;
            }
        }
    }
}
