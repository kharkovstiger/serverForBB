package serverForBB.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = Controller.REST_URL, produces = MediaType.APPLICATION_XML_VALUE)
@CrossOrigin
public class Controller {

    static final String REST_URL = "/api";
    private static final String BASE_URL = "http://bbapi.buzzerbeater.com";
    private RestTemplate restTemplate=new RestTemplate();


    @GetMapping(value = "/login")
    public ResponseEntity<String> login(@PathParam("login") String login, @PathParam("code") String code){
        ResponseEntity<String> responseJson=
                restTemplate.getForEntity(BASE_URL + "/login.aspx?login="+login+"&code="+code, String.class);
        HttpHeaders headers=responseJson.getHeaders();
        System.out.println(headers);
        ArrayList<String> cookies=new ArrayList<>();
        cookies.add(headers.get("Set-Cookie").get(0).split(";")[0]);
        cookies.add(headers.get("Set-Cookie").get(1).split(";")[0]);

        HttpHeaders newHeaders=new HttpHeaders();
        newHeaders.put("Set-Cookie",cookies);
        System.out.println(newHeaders);
        return new ResponseEntity<String>(responseJson.getBody(), newHeaders, HttpStatus.OK);
    }

    @GetMapping(value = "/player")
    public ResponseEntity<String> player(@PathParam("id") String id, @PathParam("login") String login, @PathParam("code") String code){

        ResponseEntity<String> q=
                restTemplate.getForEntity(BASE_URL + "/login.aspx?login="+login+"&code="+code, String.class);
        HttpHeaders headers=q.getHeaders();
        ArrayList<String> cookies=new ArrayList<>();
        cookies.add(headers.get("Set-Cookie").get(0).split(";")[0]);
        cookies.add(headers.get("Set-Cookie").get(1).split(";")[0]);

        HttpHeaders headersToSend=new HttpHeaders();
        headersToSend.put("Cookie",cookies);
        HttpEntity<String> entity = new HttpEntity<>(headersToSend);

        ResponseEntity<String> responseJson =
                    restTemplate.exchange(BASE_URL + "/player.aspx?playerid=" + id, HttpMethod.GET, entity, String.class);

        return new ResponseEntity<>(responseJson.getBody(),HttpStatus.OK);
    }
}
