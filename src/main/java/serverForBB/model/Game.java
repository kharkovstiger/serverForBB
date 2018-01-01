package serverForBB.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Document
public class Game implements Serializable{
    @Id
    private String Id;
    private Team homeTeam;
    private Team awayTeam;
    private int[] score=new int[2];
    private int season;
    private String type;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    private LocalDate date;
}
