package serverForBB.service;

import com.sun.org.apache.xerces.internal.dom.DeferredElementImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import serverForBB.model.Game;
import serverForBB.model.Player;
import serverForBB.model.Stats;
import serverForBB.model.Team;
import serverForBB.repository.GameRepository;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Service
public class DefaultGameService implements GameService {

    private static final String LOGIN = "lnrstgr";
    private static final String CODE = "katana";
    private final GameRepository gameRepository;
    private final BBAPIService bbapiService;
    private final PlayerService playerService;

    @Autowired
    public DefaultGameService(GameRepository gameRepository, BBAPIService bbapiService, PlayerService playerService) {
        this.gameRepository = gameRepository;
        this.bbapiService = bbapiService;
        this.playerService = playerService;
    }

    @Override
    public Game parseBoxScore(String response) {
        Game game=new Game();
        String[] temp=response.split("<title>|<\\/title>")[1].trim().split(" ");
        int flag = 0;
        for (int i = 0; i <temp.length ; i++) {
            if (temp[i].contains("/")) {
                String[] date = temp[i].split("/");
                LocalDate localDate=LocalDate.of(Integer.parseInt(date[2]),Integer.parseInt(date[0]),Integer.parseInt(date[1]));
                game.setDate(localDate);
                int season=getSeason(localDate);
                game.setSeason(season);
                flag=i;
                break;
            }
        }
        String type="";//temp[flag+1].substring(14).trim();
        for (int i = flag+3; i <temp.length ; i++) {
            type+=temp[i]+" ";
        }
        game.setType(type.trim());
        String temp1="<?xml version=\"1.0\" encoding=\"utf-8\"?><table" + response.split("<table|table>")[15] + "table>";
        String away=temp1.replace("&nbsp;","x");
        Team awayTeam=new Team();
        setTeamStats(awayTeam, away);
        setPlayersStats(awayTeam, away);
        game.setAwayTeam(awayTeam);

        temp1="<?xml version=\"1.0\" encoding=\"utf-8\"?><table" + response.split("<table|table>")[17] + "table>";
        String home=temp1.replace("&nbsp;","x");
        Team homeTeam=new Team();
        setTeamStats(homeTeam, home);
        setPlayersStats(homeTeam, home);
        game.setHomeTeam(homeTeam);
        ArrayList<Integer> score=new ArrayList<>();
        score.add(awayTeam.getStats().getPoints());
        score.add(homeTeam.getStats().getPoints());
        game.setScore(score);
        return game;
    }

    private int getSeason(LocalDate localDate) {
        String xml=bbapiService.getSeasons(LOGIN, CODE);
        Document doc=getDocument(xml);
        NodeList nodeList=doc.getElementsByTagName("season");
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (int i = 0; i <nodeList.getLength()-1 ; i++) {
            String s=((DeferredElementImpl) nodeList.item(i)).getElementsByTagName("finish").item(0)
                    .getTextContent().split("T")[0];
            LocalDate date=LocalDate.parse(s, formatter);
            if (localDate.isBefore(date)){
                return Integer.parseInt(((DeferredElementImpl) nodeList.item(i)).getAttribute("id"));
            }
        }
        return Integer.parseInt(nodeList.item(nodeList.getLength()).getAttributes().getNamedItem("id").getTextContent());
    }

    private void setPlayersStats(Team team, String xml) {
        Document doc=getDocument(xml);
        NodeList nodeList=doc.getElementsByTagName("tr");
        team.setPlayers(new ArrayList<>());
        for (int i = 1; i <nodeList.getLength()-1 ; i++) {
            Player player=new Player();
            String id=((DeferredElementImpl) nodeList.item(i)).getElementsByTagName("a").item(0)
                    .getAttributes().getNamedItem("href").getTextContent().split("/")[2];
            boolean u21=team.getName().split(" ")[team.getName().split(" ").length-1].equals("U21");
            player.setId(id+(u21?"u21":""));                
            player.setCountry(team.getName());
            NodeList nList=((DeferredElementImpl) nodeList.item(i)).getElementsByTagName("td");
            if (nList.getLength()>3){
                int q=nList.getLength()>15?1:0;
                Stats stats=new Stats();
                stats.setGames(1);
                stats.setMinutes(Integer.parseInt(nList.item(2).getTextContent().trim()));
                stats.setFieldGoals(Integer.parseInt(nList.item(3).getTextContent().trim().split("-")[0].trim()));
                stats.setFieldGoalsAttempts(Integer.parseInt(nList.item(3).getTextContent().trim().split("-")[1].trim()));
                stats.setThreePoints(Integer.parseInt(nList.item(4).getTextContent().trim().split("-")[0].trim()));
                stats.setThreePointsAttempts(Integer.parseInt(nList.item(4).getTextContent().trim().split("-")[1].trim()));
                stats.setFreeThrows(Integer.parseInt(nList.item(5).getTextContent().trim().split("-")[0].trim()));
                stats.setFreeThrowsAttempts(Integer.parseInt(nList.item(5).getTextContent().trim().split("-")[1].trim()));
                stats.setPlusMinus(q==1?Integer.parseInt(nList.item(6).getTextContent().trim()):0);
                stats.setOffensiveRebounds(Integer.parseInt(nList.item(6+q).getTextContent().trim()));
                stats.setRebounds(Integer.parseInt(nList.item(7+q).getTextContent().trim()));
                stats.setAssists(Integer.parseInt(nList.item(8+q).getTextContent().trim()));
                stats.setTurnovers(Integer.parseInt(nList.item(9+q).getTextContent().trim()));
                stats.setSteals(Integer.parseInt(nList.item(10+q).getTextContent().trim()));
                stats.setBlocks(Integer.parseInt(nList.item(11+q).getTextContent().trim()));
                stats.setFouls(Integer.parseInt(nList.item(12+q).getTextContent().trim()));
                stats.setPoints(Integer.parseInt(nList.item(13+q).getTextContent().trim()));
                player.setStats(stats);
                String playerString=bbapiService.getPlayer(id, LOGIN, CODE);
                doc=getDocument(playerString);
                String firstName=doc.getElementsByTagName("firstName").item(0).getTextContent().trim();
                String lastName=doc.getElementsByTagName("lastName").item(0).getTextContent().trim();
                player.setFirstName(firstName);
                player.setLastName(lastName);
                team.addPlayer(player);
            }
        }
    }

    private Document getDocument(String s){
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        Document doc = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(new InputSource(new StringReader(s)));
            doc.getDocumentElement().normalize();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return doc;
    }

    private void setTeamStats(Team team, String xml){
        Document doc=getDocument(xml);
        NodeList nodeList =doc.getElementsByTagName("td");
        Node tr=doc.getElementsByTagName("tr").item(doc.getElementsByTagName("tr").getLength()-1);
        NodeList nList=((DeferredElementImpl) tr).getElementsByTagName("td");
        String name=nodeList.item(0).getTextContent().trim();
        team.setName(name);
        int q=nList.getLength()>15?1:0;
        Stats stats=new Stats();
        stats.setFieldGoals(Integer.parseInt(nList.item(2).getTextContent().trim().split("-")[0].trim()));
        stats.setFieldGoalsAttempts(Integer.parseInt(nList.item(2).getTextContent().trim().split("-")[1].trim()));
        stats.setThreePoints(Integer.parseInt(nList.item(3).getTextContent().trim().split("-")[0].trim()));
        stats.setThreePointsAttempts(Integer.parseInt(nList.item(3).getTextContent().trim().split("-")[1].trim()));
        stats.setFreeThrows(Integer.parseInt(nList.item(4).getTextContent().trim().split("-")[0].trim()));
        stats.setFreeThrowsAttempts(Integer.parseInt(nList.item(4).getTextContent().trim().split("-")[1].trim()));
        stats.setOffensiveRebounds(Integer.parseInt(nList.item(5+q).getTextContent().trim()));
        stats.setRebounds(Integer.parseInt(nList.item(6+q).getTextContent().trim()));
        stats.setAssists(Integer.parseInt(nList.item(7+q).getTextContent().trim()));
        stats.setTurnovers(Integer.parseInt(nList.item(8+q).getTextContent().trim()));
        stats.setSteals(Integer.parseInt(nList.item(9+q).getTextContent().trim()));
        stats.setBlocks(Integer.parseInt(nList.item(10+q).getTextContent().trim()));
        stats.setFouls(Integer.parseInt(nList.item(11+q).getTextContent().trim()));
        stats.setPoints(Integer.parseInt(nList.item(12+q).getTextContent().trim()));
        team.setStats(stats);
    }

    @Override
    public Game save(Game game) {
        boolean exist=gameRepository.exists(game.getId());
        if (!exist){
            game.getAwayTeam().getPlayers().forEach(playerService::addStats);
            game.getHomeTeam().getPlayers().forEach(playerService::addStats);
        }
        return gameRepository.save(game);
    }
}
