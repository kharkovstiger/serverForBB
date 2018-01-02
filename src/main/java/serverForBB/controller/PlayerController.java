package serverForBB.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import serverForBB.model.Player;
import serverForBB.service.PlayerService;

import java.util.List;

@RestController
@RequestMapping(value = PlayerController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
public class PlayerController {
    static final String REST_URL = "api/player";
    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping(value = "/getAllFromCountry", consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<Player> getAllFromCountry(@RequestBody String country){
        return playerService.getAllFromCountry(country); 
    }
}
