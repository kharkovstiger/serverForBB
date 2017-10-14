package serverForBB.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.websocket.server.PathParam;
import java.util.ArrayList;

@RestController
@RequestMapping(value = BBAPIController.REST_URL, produces = MediaType.APPLICATION_XML_VALUE)
@CrossOrigin
public class BBAPIController {

    static final String REST_URL = "/api/bbapi";
    private static final String BASE_URL = "http://bbapi.buzzerbeater.com";
    private RestTemplate restTemplate=new RestTemplate();

    @GetMapping(value = "/login")
    public ResponseEntity<String> login(@PathParam("login") String login, @PathParam("code") String code){
        ResponseEntity<String> responseJson=
                restTemplate.getForEntity(BASE_URL + "/login.aspx?login="+login+"&code="+code, String.class);
        HttpHeaders headers=responseJson.getHeaders();
        ArrayList<String> cookies=new ArrayList<>();
        cookies.add(headers.get("Set-Cookie").get(0).split(";")[0]);
        cookies.add(headers.get("Set-Cookie").get(1).split(";")[0]);

        HttpHeaders newHeaders=new HttpHeaders();
        newHeaders.put("Cookie",cookies);
        return new ResponseEntity<>(responseJson.getBody(), newHeaders, HttpStatus.OK);
    }

    @GetMapping(value = "/player")
    public ResponseEntity<String> player(@PathParam("id") String id, @PathParam("login") String login, @PathParam("code") String code){
        HttpEntity<String> entity = new HttpEntity<>(login(login,code).getHeaders());

        ResponseEntity<String> responseJson =
                    restTemplate.exchange(BASE_URL + "/player.aspx?playerid=" + id, HttpMethod.GET, entity, String.class);

        return new ResponseEntity<>(responseJson.getBody(),HttpStatus.OK);
    }

    @GetMapping(value = "/country")
    public ResponseEntity<String> country(@PathParam("login") String login, @PathParam("code") String code){
        HttpEntity<String> entity = new HttpEntity<>(login(login,code).getHeaders());

        ResponseEntity<String> responseJson =
                restTemplate.exchange(BASE_URL + "/countries.aspx", HttpMethod.GET, entity, String.class);

        return new ResponseEntity<>(responseJson.getBody(),HttpStatus.OK);
    }

    @GetMapping(value = "/league")
    public ResponseEntity<String> league(@PathParam("level") Integer level, @PathParam("login") String login, @PathParam("code") String code){
        HttpEntity<String> entity = new HttpEntity<>(login(login,code).getHeaders());

        ResponseEntity<String> responseJson =
                restTemplate.exchange(BASE_URL + "/leagues.aspx?countryid=33&level="+level, HttpMethod.GET, entity, String.class);

        return new ResponseEntity<>(responseJson.getBody(),HttpStatus.OK);
    }

    @GetMapping(value = "/standings")
    public ResponseEntity<String> standings(@PathParam("leagueid") Integer leagueid, @PathParam("login") String login, @PathParam("code") String code){
        HttpEntity<String> entity = new HttpEntity<>(login(login,code).getHeaders());

        ResponseEntity<String> responseJson =
                restTemplate.exchange(BASE_URL + "/standings.aspx?leagueid="+leagueid, HttpMethod.GET, entity, String.class);

        return new ResponseEntity<>(responseJson.getBody(),HttpStatus.OK);
    }

    @GetMapping(value = "/team")
    public ResponseEntity<String> team(@PathParam("id") String id, @PathParam("login") String login, @PathParam("code") String code){
        HttpEntity<String> entity = new HttpEntity<>(login(login,code).getHeaders());

        ResponseEntity<String> responseJson =
                restTemplate.exchange(BASE_URL + "/teaminfo.aspx?teamid=" + id, HttpMethod.GET, entity, String.class);

        return new ResponseEntity<>(responseJson.getBody(),HttpStatus.OK);
    }

    @GetMapping(value = "/season")
    public ResponseEntity<String> team(@PathParam("login") String login, @PathParam("code") String code){
        HttpEntity<String> entity = new HttpEntity<>(login(login,code).getHeaders());

        ResponseEntity<String> responseJson =
                restTemplate.exchange(BASE_URL + "/seasons.aspx", HttpMethod.GET, entity, String.class);

        return new ResponseEntity<>(responseJson.getBody(),HttpStatus.OK);
    }
}
