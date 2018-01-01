package serverForBB.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import serverForBB.model.Game;
import serverForBB.service.BBAPIService;
import serverForBB.service.GameService;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping(value = GameController.REST_URL, produces = MediaType.APPLICATION_XML_VALUE)
@CrossOrigin
public class GameController {
    static final String REST_URL = "/api/";
    private static final String LOGIN="lnrstgr";
    private static final String CODE="katana";
    
    private final GameService gameService;
    private final BBAPIService bbapiService;

    @Autowired
    public GameController(GameService gameService, BBAPIService bbapiService) {
        this.gameService = gameService;
        this.bbapiService = bbapiService;
    }
    
    @GetMapping(value = "addGame")
    public ResponseEntity addGame(@PathParam("id") String id){
        HttpEntity<String> entity=new HttpEntity<>(bbapiService.login(LOGIN, CODE).getHeaders());
        String response=bbapiService.getBoxScore(id, LOGIN, CODE, entity);
        Game game=gameService.parseBoxScore(response);
        gameService.save(game);
        return new ResponseEntity(HttpStatus.OK);
    }
}
