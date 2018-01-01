package serverForBB.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import serverForBB.controller.BBController;

@Service
public class DefaultBBService implements BBService{

    private RestTemplate restTemplate=new RestTemplate();
    
    @Override
    public String getBoxScore(Integer id) {
        ResponseEntity<String> responseJson=
                restTemplate.getForEntity(BBController.BASE_URL + "/match/"+id+"/boxscore.aspx", String.class);
        return responseJson.getBody();
    }
}
