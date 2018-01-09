package serverForBB.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import serverForBB.model.Game;
import serverForBB.model.Results;
import serverForBB.model.StatRequest;
import serverForBB.service.BBAPIService;
import serverForBB.service.BBService;
import serverForBB.service.GameService;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Map;

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
        gameService.addGame(id);
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

    @PostMapping(value = "/allGamesForCountryAgainstCountry/{official}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<Game> getAllGamesForCountryAgainstCountry(@RequestBody List<String> countries, @PathVariable("official") boolean official){
        return gameService.getAllGamesForCountryAgainstCountry(countries.get(0), countries.get(1), official);
    }

    @PostMapping(value = "/gamesForList", consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<Game> getGamesForList(@RequestBody List<String> ids){
        return gameService.getGamesForList(ids);
    }

    @PostMapping(value = "/getSeasonsStatisticsForCountry/{season}")
    public Map<String, Double> getSeasonsStatisticsForCountry(@RequestBody String country, @PathVariable("season") Integer season){
        return gameService.getSeasonStatisticsForCountry(country, season);
    }
    
    @PostMapping(value = "/getAveragedStatistics")
    public Map<String, Double> getAveragedStatistics(@RequestBody StatRequest request){
        return gameService.getAveragedStatistics(request.getGames(), request.getCountry());
    }

    @PostMapping(value = "/resultsForCountryAgainstCountry", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Results getResultsForCountryAgainstCountry(@RequestBody List<String> countries){
        List<Game> games=gameService.getAllGamesForCountryAgainstCountry(countries.get(0), countries.get(1), false);
        return gameService.getResultsFromGameList(games, countries.get(0));
    }
}
