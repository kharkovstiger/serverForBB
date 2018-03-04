package serverForBB.service;

import com.sun.org.apache.xerces.internal.dom.DeferredElementImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import serverForBB.model.*;
import serverForBB.repository.GameRepository;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DefaultGameService implements GameService {

    private static final String LOGIN = "lnrstgr";
    private static final String CODE = "katana";
    private final GameRepository gameRepository;
    private final BBAPIService bbapiService;
    private final BBService bbService;
    private final PlayerService playerService;
    private final DateTimeFormatter FORMATTER=DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    public DefaultGameService(GameRepository gameRepository, BBAPIService bbapiService, BBService bbService, PlayerService playerService) {
        this.gameRepository = gameRepository;
        this.bbapiService = bbapiService;
        this.bbService = bbService;
        this.playerService = playerService;
    }

    @Override
    public Game parseBoxScore(String response) {
        Game game=new Game();
        String[] temp=response.split("<title>|</title>")[1].trim().split(" ");
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
        ArrayList<Double> score=new ArrayList<>();
        score.add(awayTeam.getStats().get("points"));
        score.add(homeTeam.getStats().get("points"));
        if (type.contains("Tournament"))
            type="Consolation Tournament";
        else if (type.contains("Robin") || type.contains("inal")){
            if (game.getSeason()%2==(game.getAwayTeam().getName().matches(".*\\d+.*")?0:1))
                type="Euro Champs";
            else
                type="World Champs";
        }
        game.setType(type.trim());
        game.setScore(score);
        return game;
    }

    @Override
    public int getSeason(LocalDate localDate) {
        String xml=bbapiService.getSeasons(LOGIN, CODE);
        Document doc=getDocument(xml);
        NodeList nodeList=doc.getElementsByTagName("season");
        for (int i = 0; i <nodeList.getLength()-1 ; i++) {
            String s=((DeferredElementImpl) nodeList.item(i)).getElementsByTagName("finish").item(0)
                    .getTextContent().split("T")[0];
            LocalDate date=LocalDate.parse(s, FORMATTER);
            if (!localDate.isAfter(date)){
                return Integer.parseInt(((DeferredElementImpl) nodeList.item(i)).getAttribute("id"));
            }
        }
        return Integer.parseInt(nodeList.item(nodeList.getLength()-1).getAttributes().getNamedItem("id").getTextContent());
    }

    private void setPlayersStats(Team team, String xml) {
        Document doc=getDocument(xml);
        NodeList nodeList=doc.getElementsByTagName("tr");
        team.setPlayers(new ArrayList<>());
        for (int i = 1; i <nodeList.getLength()-1 ; i++) {
            String id=((DeferredElementImpl) nodeList.item(i)).getElementsByTagName("a").item(0)
                    .getAttributes().getNamedItem("href").getTextContent().split("/")[2];
            boolean u21=team.getName().split(" ")[team.getName().split(" ").length-1].equals("U21");
            Player player=new Player(id+(u21?"u21":""), team.getName());
            NodeList nList=((DeferredElementImpl) nodeList.item(i)).getElementsByTagName("td");
            if (nList.getLength()>3){
                int q=nList.getLength()>15?1:0;
                Map<String, Double> stats=new HashMap<>();
                Stats.initialize(stats, Player.class.getName());
                stats.replace("games",1.);
                stats.replace("minutes",Double.parseDouble(nList.item(2).getTextContent().trim()));
                stats.replace("fieldGoals",Double.parseDouble(nList.item(3).getTextContent().trim().split("-")[0].trim()));
                stats.replace("fieldGoalsAttempts",Double.parseDouble(nList.item(3).getTextContent().trim().split("-")[1].trim()));
                stats.replace("threePoints",Double.parseDouble(nList.item(4).getTextContent().trim().split("-")[0].trim()));
                stats.replace("threePointsAttempts",Double.parseDouble(nList.item(4).getTextContent().trim().split("-")[1].trim()));
                stats.replace("freeThrows",Double.parseDouble(nList.item(5).getTextContent().trim().split("-")[0].trim()));
                stats.replace("freeThrowsAttempts",Double.parseDouble(nList.item(5).getTextContent().trim().split("-")[1].trim()));
                stats.replace("plusMinus",q==1?Double.parseDouble(nList.item(6).getTextContent().trim()):0);
                stats.replace("offensiveRebounds",Double.parseDouble(nList.item(6+q).getTextContent().trim()));
                stats.replace("rebounds",Double.parseDouble(nList.item(7+q).getTextContent().trim()));
                stats.replace("assists",Double.parseDouble(nList.item(8+q).getTextContent().trim()));
                stats.replace("turnovers",Double.parseDouble(nList.item(9+q).getTextContent().trim()));
                stats.replace("steals",Double.parseDouble(nList.item(10+q).getTextContent().trim()));
                stats.replace("blocks",Double.parseDouble(nList.item(11+q).getTextContent().trim()));
                stats.replace("fouls",Double.parseDouble(nList.item(12+q).getTextContent().trim()));
                stats.replace("points",Double.parseDouble(nList.item(13+q).getTextContent().trim()));
                stats.replace("doubleDouble",isDD(stats)==2?1.:0.);
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

    private int isDD(Map<String, Double> stats) {
        final int[] c = {0};
        stats.forEach((s, aDouble) -> {
            if (s.equals("points") || s.equals("rebounds") || s.equals("assists") || s.equals("steals") || s.equals("blocks"))
                c[0] +=(aDouble >= 10 ? 1 : 0);
        });
        return c[0];
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
        Map<String, Double> stats=new HashMap<>();
        Stats.initialize(stats,Team.class.getName());
        int q=nList.getLength()>15?1:0;
        stats.replace("fieldGoals",Double.parseDouble(nList.item(2).getTextContent().trim().split("-")[0].trim()));
        stats.replace("fieldGoalsAttempts",Double.parseDouble(nList.item(2).getTextContent().trim().split("-")[1].trim()));
        stats.replace("threePoints",Double.parseDouble(nList.item(3).getTextContent().trim().split("-")[0].trim()));
        stats.replace("threePointsAttempts",Double.parseDouble(nList.item(3).getTextContent().trim().split("-")[1].trim()));
        stats.replace("freeThrows",Double.parseDouble(nList.item(4).getTextContent().trim().split("-")[0].trim()));
        stats.replace("freeThrowsAttempts",Double.parseDouble(nList.item(4).getTextContent().trim().split("-")[1].trim()));
        stats.replace("offensiveRebounds",Double.parseDouble(nList.item(5+q).getTextContent().trim()));
        stats.replace("rebounds",Double.parseDouble(nList.item(6+q).getTextContent().trim()));
        stats.replace("assists",Double.parseDouble(nList.item(7+q).getTextContent().trim()));
        stats.replace("turnovers",Double.parseDouble(nList.item(8+q).getTextContent().trim()));
        stats.replace("steals",Double.parseDouble(nList.item(9+q).getTextContent().trim()));
        stats.replace("blocks",Double.parseDouble(nList.item(10+q).getTextContent().trim()));
        stats.replace("fouls",Double.parseDouble(nList.item(11+q).getTextContent().trim()));
        stats.replace("points",Double.parseDouble(nList.item(12+q).getTextContent().trim()));
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

    @Override
    public List<Game> getAllGamesForCountry(String country, boolean official) {
        return gameRepository.getAllGamesForCountry(country, official);
    }

    @Override
    public List<Game> getAllGamesForCountryForSeason(String country, boolean official, Integer season) {
        return gameRepository.getAllGamesForCountryForSeason(country,official,season);
    }

    @Override
    public List<Game> getAllGamesForCountryAgainstCountry(String s, String s1, boolean official) {
        return gameRepository.getAllGamesForCountryAgainstCountry(s, s1, official);
    }

    @Override
    public List<Game> getGamesForList(List<String> ids) {
        return gameRepository.getGamesForList(ids);
    }

    @Override
    public String getMaxId(int season) {
        Game game=gameRepository.getMaxId(season);
        return game==null?"0":game.getId();
    }

    @Override
    public void addGame(Integer id) {
        String response=bbService.getBoxScore(id);
        final boolean[] flag = {false};
        Countries.countries.forEach((s, integer) -> {
            if (response.contains(s))
                flag[0] =true;
        });
        if (!flag[0])
            return;
        Game game=parseBoxScore(response);
        game.setId(String.valueOf(id));
//        if (Countries.countries.contains(game.getAwayTeam().getName()) || Countries.countries.contains(game.getHomeTeam().getName()))
            save(game);
    }

    @Override
    public Map<String, Double> getSeasonStatisticsForCountry(String country, Integer season) {
        List<Game> games=gameRepository.getAllGamesForCountryForSeason(country, true, season);
        return getAveragedStatistics(games, country);
    }

    @Override
    public Map<String, Double> getAveragedStatistics(List<Game> games, String country) {
        Map<String, Double> stats=new HashMap<>();
        Stats.initialize(stats, Team.class.getName());
        stats.put("pointsAgainst",0.);
        stats.put("winRate",0.);
        games.forEach(game -> {
            if (game.getAwayTeam().getName().equals(country)) {
                addStat(game.getAwayTeam(), stats);
                stats.replace("pointsAgainst",stats.get("pointsAgainst")+ game.getScore().get(1));
                stats.replace("winRate",stats.get("winRate")+ (game.getScore().get(0)>game.getScore().get(1)?1.:0.));
            } else {
                addStat(game.getHomeTeam(), stats);
                stats.replace("pointsAgainst",stats.get("pointsAgainst")+ game.getScore().get(0));
                stats.replace("winRate",stats.get("winRate")+ (game.getScore().get(1)>game.getScore().get(0)?1.:0.));
            }
        });
        stats.forEach((s, aDouble) -> stats.replace(s,aDouble/games.size()));
        stats.put("wins", stats.get("winRate")*games.size());
        stats.put("games", (double) games.size());
        return stats;
    }

    @Override
    public List<Game> getAllGamesForSeason(int season) {
        return gameRepository.getAllGamesForSeason(season);
    }

    @Override
    public Results getResultsFromGameList(List<Game> games, String country) {
        Results results=new Results();
        games.forEach(game -> {
            if (game.getAwayTeam().getName().equals(country)) {
                int pos=game.getScore().get(0)>game.getScore().get(1)?0:1;
                addResult(results, pos, game.getType());
            } else {
                int pos=game.getScore().get(1)>game.getScore().get(0)?0:1;
                addResult(results, pos, game.getType());
            }
        });
        return results;
    }

    private void addResult(Results results, int pos, String type){
        results.add(results.getAll(), pos);
        switch (type){
            case "Euro Champs":
                results.add(results.getContinental(), pos);
                break;
            case "Scrimmage":
                results.add(results.getScrimmage(), pos);
                break;
            case "World Champs":
                results.add(results.getWorld(), pos);
                break;
            case "Consolation Tournament":
                results.add(results.getCt(), pos);
                break;
        }
    }
    
    private void addStat(Team team, Map<String, Double> stats){
        team.getStats().forEach((s, aDouble) -> stats.replace(s, stats.get(s)+aDouble));
    }
}
