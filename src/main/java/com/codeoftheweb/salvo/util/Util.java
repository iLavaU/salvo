package com.codeoftheweb.salvo.util;

import com.codeoftheweb.salvo.model.*;
import com.codeoftheweb.salvo.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Util {

    public Util() {
    }

    @Autowired
    private ScoreRepository scoreRepository;

    public static Map<String, Object> makeMap(String key, Object object){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put(key,object);
        return dto;
    }
    public static boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    public static GamePlayer getOpponent(GamePlayer gamePlayer) {
        Set<GamePlayer> gamePlayers = gamePlayer.getGame().getGamePlayers();
        Iterator<GamePlayer> iterator = gamePlayers.iterator();
        if (!iterator.hasNext()) {
            throw new RuntimeException("Collection is empty");
        }
        GamePlayer opponent = iterator.next();
        if (opponent.getId() != gamePlayer.getId()){
            return opponent;
        }
        while (iterator.hasNext()) {
            opponent = iterator.next();
            if (opponent.getId() != gamePlayer.getId()){
                return opponent;
            }
        }
        throw new RuntimeException("No such opponent.");
    }

    //Ships related methods.
    public static Optional<Ship> getShipByType(Set<Ship> ships, String shipType){
        Optional<Ship> ship = ships.stream().filter(ship1 -> ship1.getType().equals(shipType)).findFirst();
        return ship;
    }
    public static int getHistoricShipHits(Optional<Ship> ship){
        int hits;
        if (ship.isPresent()){
            Ship newShip = ship.get();
            hits = newShip.getLocationIsHit()
                    .stream()
                    .filter(aBoolean -> aBoolean == true)
                    .collect(Collectors.toList()).size();
            return hits;
        }
        return 0;
    }


    //Hit related methods.
    public static List<String> getAllHits(Salvo salvo) {
        GamePlayer gamePlayer = Util.getOpponent(salvo.getGamePlayer());
        Set<Ship> ships = gamePlayer.getShips();

        List<String> salvoLocations = new ArrayList<>();
        salvoLocations.addAll(salvo.getLocations());
        List<String> hits = new ArrayList<>();

        for (Ship ship: ships) {
            List<Boolean> isHit = ship.getLocationIsHit();
            List<String> locations = ship.getLocations();
            int i =0;
            for (String location: locations) {
                if (salvoLocations.contains(location) && !(isHit.get(i))){
                    hits.add(location);
                }
            }
            i++;
        }
        return hits;
    }
    public static int getShipHits(Salvo salvo, Ship ship){
        List<String> salvoLocations = salvo.getLocations();
        List<String> shipLocations = ship.getLocations();
        salvoLocations.retainAll(shipLocations);

        int hitsAmount = salvoLocations.size();
        return hitsAmount;
    }
    public static int getShipHits(Salvo salvo, Optional<Ship> ship){
        List<String> salvoLocations = new ArrayList<>();
        salvoLocations.addAll(salvo.getLocations());
        int hitsAmount=0;
        if (ship.isPresent()){
            Ship newShip = ship.get();
            List<String> shipLocations = newShip.getLocations();
            List<Boolean> locationIsHit = newShip.getLocationIsHit();
            int i = 0;
            for (String location: shipLocations) {
                if (salvoLocations.contains(location)  &&  !(locationIsHit.get(i))){
                    hitsAmount++;
                    locationIsHit.set(i,true);
                }
                i++;
            }
            return hitsAmount;
        }
        return hitsAmount;
    }
    public static Score createScores(GamePlayer gamePlayer, String result){
        Score myScore = new Score();
        Score enemyScore = new Score();
        myScore.setPlayer(gamePlayer.getPlayer());
        myScore.setGame(gamePlayer.getGame());
        enemyScore.setPlayer(Util.getOpponent(gamePlayer).getPlayer());
        enemyScore.setGame(gamePlayer.getGame());
        myScore.setFinishDate(LocalDateTime.now());
        enemyScore.setFinishDate(LocalDateTime.now());
        switch (result){
            case "WON":
                myScore.setScore(1.0D);
                enemyScore.setScore(0.0D);
                break;
            case "LOST":
                myScore.setScore(0.0D);
                enemyScore.setScore(1.0D);
                break;
            case "TIED":
                myScore.setScore(0.5D);
                enemyScore.setScore(0.5D);
                break;
        }
        return myScore;
    }

    public static String getGameState(GamePlayer gamePlayer) {
        Set<Ship> ships = gamePlayer.getShips();
        Set<GamePlayer> gamePlayers= gamePlayer.getGame().getGamePlayers();
        Util util = new Util();
        if ((gamePlayers.size()==1 && !(ships.size()==5)) || (gamePlayers.size()==2) && !(ships.size()==5)){
            return "PLACESHIPS";
        }
        if ((ships.size() == 5 && gamePlayers.size()==1)){
            return "WAITINGFOROPP";
        }
        GamePlayer opponent = Util.getOpponent(gamePlayer);
        if (!(opponent.getShips().size()==5)){
            return "WAITINGFOROPP";
        }

        if (gamePlayer.getSalvoes().size()==0){

            return "PLAY";
        }

        if(opponent.getSalvoes().size()==0){
            return "WAITINGFOROPP";
        }
        int myLastTurn = gamePlayer.getLastTurn();
        int oppenentLastTurn = opponent.getLastTurn();
        if (myLastTurn>oppenentLastTurn){
            return "WAIT";
        }
        int allEnemyHits = gamePlayer.getShips()
                .stream()
                .flatMap(ship -> ship.getLocationIsHit().stream())
                .filter(aBoolean -> aBoolean==true)
                .collect(Collectors.toList())
                .size();
        int allMyHits = opponent.getShips()
                .stream()
                .flatMap(ship -> ship.getLocationIsHit().stream())
                .filter(aBoolean -> aBoolean==true)
                .collect(Collectors.toList())
                .size();
        if ((allMyHits==17) && (allEnemyHits<17) && (oppenentLastTurn>=myLastTurn)){
            return "WON";
        }
        if (allEnemyHits == 17 && allMyHits<17 && myLastTurn>=oppenentLastTurn){
            return "LOST";
        }
        if (allEnemyHits == 17 && allMyHits==17 && myLastTurn == oppenentLastTurn){
            return "TIE";
        }
        return"PLAY";
    }
}
