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
    public String getBoxScore(String id, String login, String code) {
        HttpEntity<String> entity = new HttpEntity<>(login(login, code, 0).getHeaders());
        ResponseEntity<String> responseJson =
                restTemplate.exchange(BBAPIController.BASE_URL + "/boxscore.aspx?matchid="+id, HttpMethod.GET, entity, String.class);
        return responseJson.getBody();
    }

    @Override
    public ResponseEntity<String> login(String login, String code, Integer info) {
        ResponseEntity<String> responseJson=
                restTemplate.getForEntity(BBAPIController.BASE_URL + "/login.aspx?login="+login+"&code="+code+"&quickinfo="+info, String.class);
        HttpHeaders headers=responseJson.getHeaders();
        ArrayList<String> cookies=new ArrayList<>();
        cookies.add(headers.get("Set-Cookie").get(0).split(";")[0]);
        cookies.add(headers.get("Set-Cookie").get(1).split(";")[0]);

        HttpHeaders newHeaders=new HttpHeaders();
        newHeaders.put("Cookie",cookies);
        return new ResponseEntity<>(responseJson.getBody(), newHeaders, HttpStatus.OK);
    }

    @Override
    public String getPlayer(String id, String login, String code) {
        HttpEntity<String> entity = new HttpEntity<>(login(login, code, 0).getHeaders());
        ResponseEntity<String> responseJson =
                restTemplate.exchange(BBAPIController.BASE_URL + "/player.aspx?playerid=" + id, HttpMethod.GET, entity, String.class);
        return responseJson.getBody();
    }

    @Override
    public String getSeasons(String login, String code) {
        HttpEntity<String> entity = new HttpEntity<>(login(login, code, 0).getHeaders());
        ResponseEntity<String> responseJson =
                restTemplate.exchange(BBAPIController.BASE_URL + "/seasons.aspx", HttpMethod.GET, entity, String.class);
        return responseJson.getBody();
    }
}
