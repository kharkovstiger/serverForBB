package serverForBB.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import serverForBB.model.Game;
import serverForBB.service.BBAPIService;
import serverForBB.service.BBService;
import serverForBB.service.GameService;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping(value = GameController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
public class GameController {
    static final String REST_URL = "/api/game";
    private static final String LOGIN="lnrstgr";
    private static final String CODE="katana";
    
    private final GameService gameService;
    private final BBAPIService bbapiService;
    private final BBService bbService;

    @Autowired
    public GameController(GameService gameService, BBAPIService bbapiService, BBService bbService) {
        this.gameService = gameService;
        this.bbapiService = bbapiService;
        this.bbService = bbService;
    }
    
    @GetMapping(value = "/addGame")
    public ResponseEntity addGame(@PathParam("id") Integer id){
        String response=bbService.getBoxScore(id);
        Game game=gameService.parseBoxScore(response);
        game.setId(String.valueOf(id));
        gameService.save(game);
        return new ResponseEntity(HttpStatus.OK);
    }
}