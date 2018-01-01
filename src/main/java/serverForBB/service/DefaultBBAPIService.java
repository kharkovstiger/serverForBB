package serverForBB.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import serverForBB.controller.BBAPIController;

import java.util.ArrayList;

@Service
public class DefaultBBAPIService implements BBAPIService {
    private RestTemplate restTemplate=new RestTemplate();

    @Override
    public String getBoxScore(String id, String login, String code, HttpEntity<String> entity) {
        ResponseEntity<String> responseJson =
                restTemplate.exchange(BBAPIController.BASE_URL + "/boxscore.aspx?matchid="+id, HttpMethod.GET, entity, String.class);
        return responseJson.getBody();
    }

    @Override
    public ResponseEntity<String> login(String login, String code) {
        ResponseEntity<String> responseJson=
                restTemplate.getForEntity(BBAPIController.BASE_URL + "/login.aspx?login="+login+"&code="+code, String.class);
        HttpHeaders headers=responseJson.getHeaders();
        ArrayList<String> cookies=new ArrayList<>();
        cookies.add(headers.get("Set-Cookie").get(0).split(";")[0]);
        cookies.add(headers.get("Set-Cookie").get(1).split(";")[0]);

        HttpHeaders newHeaders=new HttpHeaders();
        newHeaders.put("Cookie",cookies);
        return new ResponseEntity<>(responseJson.getBody(), newHeaders, HttpStatus.OK);
    }
}
