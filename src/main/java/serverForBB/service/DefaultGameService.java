package serverForBB.service;

import jdk.nashorn.internal.runtime.regexp.RegExp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import serverForBB.model.Game;
import serverForBB.repository.GameRepository;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class DefaultGameService implements GameService {
    
    private final GameRepository gameRepository;

    @Autowired
    public DefaultGameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public Game parseBoxScore(String response) {
        Game game=new Game();
        String[] temp=response.split("<title>|<\\/title>")[1].trim().split(" ");
        for (String aTemp : temp) {
            if (aTemp.contains("/")) {
                String[] date = aTemp.split("/");
                LocalDate localDate=LocalDate.of(Integer.parseInt(date[2]),Integer.parseInt(date[0]),Integer.parseInt(date[1]));
                break;
            }
        }
        String temp1="<?xml version = \"1.0\"?><table" + response.split("<table|table>")[15] + "table>";
        String away=temp1.replace("&nbsp;","x");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(away);
            doc.getDocumentElement().normalize();
            NodeList nList =doc.getElementsByTagName("td");
            String name=nList.item(0).getTextContent();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        temp = '<table' + response.data.split(/<table|table>/g)[17] + 'table>';
//        var home = $.parseXML(temp.replace(new RegExp('&nbsp;', 'g'), 'x'));
//        var type = response.data.split(/<table|table>/g)[18].split('span')[3].split(/>|</g)[1].substring(14);
        return game;
    }

    @Override
    public Game save(Game game) {
        return gameRepository.save(game);
    }
}
