package serverForBB.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document
public class Game implements Serializable{
    @Id
    private String Id;
    private Team homeTeam;
    private Team awayTeam;
    private int[] score=new int[2];
}
