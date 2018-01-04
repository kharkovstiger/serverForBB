package serverForBB.sheduled;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import serverForBB.service.GameService;

@Component
public class ScheduledTasks {
    
    private final GameService gameService;

    @Autowired
    public ScheduledTasks(GameService gameService) {
        this.gameService = gameService;
    }

//    @Scheduled(cron = "0 0 2 ? * 2")
    @Scheduled(cron = "0 0 18 ? * *")
    public void addGames(){
        Integer maxId= Integer.valueOf(gameService.getMaxId());
        boolean flag=true;
        while (flag){
            try {
                gameService.addGame(++maxId);    
            }
            catch (ArrayIndexOutOfBoundsException e){
                flag=false;
            }
        }
    }
}
