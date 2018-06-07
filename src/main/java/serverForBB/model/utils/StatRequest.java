package serverForBB.model.utils;

import lombok.Data;
import serverForBB.model.Game;

import java.util.List;

@Data
public class StatRequest {
    
    private List<Game> games;
    private String country;
}
