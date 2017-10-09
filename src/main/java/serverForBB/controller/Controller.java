package serverForBB.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.websocket.server.PathParam;

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

        return new ResponseEntity<>(responseJson.getBody(),HttpStatus.OK);
    }

    @GetMapping(value = "/player")
    public ResponseEntity<String> player(@PathParam("id") String id){

        ResponseEntity<String> q=
                restTemplate.getForEntity(BASE_URL + "/login.aspx?login=lnrstgr&code=katana", String.class);

        ResponseEntity<String> responseJson = null;
        if (q.getStatusCodeValue()==200) {
            responseJson =
                    restTemplate.getForEntity(BASE_URL + "/player.aspx?playerid=" + id, String.class);
        }
        else {
            return new ResponseEntity<>("shit",HttpStatus.EXPECTATION_FAILED);
        }

        return new ResponseEntity<>(responseJson.getBody(),HttpStatus.OK);
    }
}
