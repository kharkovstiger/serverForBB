package serverForBB.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

public interface BBAPIService {
    String getBoxScore(String id, String login, String code, HttpEntity<String> entity);
    
    ResponseEntity<String> login(String login, String code);
}
