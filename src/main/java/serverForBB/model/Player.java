package serverForBB.model;

import lombok.Data;

@Data
public class Player {
    private String Id;
    private String firstName;
    private String lastName;
    private Stats stats;
}
