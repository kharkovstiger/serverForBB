package serverForBB.model;

import lombok.Data;

import java.util.List;

@Data
public class StatRequest {
    
    private List<Game> games;
    private String country;
}
