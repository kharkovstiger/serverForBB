package serverForBB.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import serverForBB.model.Game;
import serverForBB.service.BBAPIService;
import serverForBB.service.BBService;
import serverForBB.service.GameService;

import javax.websocket.server.PathParam;
import java.util.List;

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
    
    @PostMapping(value = "/allGamesForCountry/{official}")
    public List<Game> getAllGamesForCountry(@RequestBody String country, @PathVariable("official") boolean official){
        return gameService.getAllGamesForCountry(country, official);
    }

    @PostMapping(value = "/allGamesForCountryForSeason/{official}/{season}")
    public List<Game> getAllGamesForCountryForSeason(@RequestBody String country, @PathVariable("official") boolean official, 
                                                     @PathVariable("season") Integer season){
        return gameService.getAllGamesForCountryForSeason(country, official, season);
    }

    @PostMapping(value = "/allGamesForCountryAgainstCountry/{official}",consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<Game> getAllGamesForCountryAgainstCountry(@RequestBody List<String> countries, @PathVariable("official") boolean official){
        return gameService.getAllGamesForCountryAgainstCountry(countries.get(0), countries.get(1), official);
    }
}
