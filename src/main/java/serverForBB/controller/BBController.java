package serverForBB.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping(value = BBController.REST_URL, produces = MediaType.TEXT_HTML_VALUE)
@CrossOrigin
public class BBController {

    static final String REST_URL = "/api/bb";
    private static final String BASE_URL = "http://buzzerbeater.com";
    private RestTemplate restTemplate=new RestTemplate();

    @GetMapping(value = "/country")
    public ResponseEntity<String> country(){

        ResponseEntity<String> responseJson=
                restTemplate.getForEntity(BASE_URL + "/country/33/overview.aspx", String.class);

        return new ResponseEntity<>(responseJson.getBody(), HttpStatus.OK);
    }

    @GetMapping(value = "/cup")
    public ResponseEntity<String> cup(@PathParam("season") String season){

        ResponseEntity<String> responseJson=
                restTemplate.getForEntity(BASE_URL + "country/33/cup.aspx?season="+season, String.class);

        return new ResponseEntity<>(responseJson.getBody(), HttpStatus.OK);
    }
}
