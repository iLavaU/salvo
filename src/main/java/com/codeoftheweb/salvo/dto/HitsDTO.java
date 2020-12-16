package com.codeoftheweb.salvo.dto;

import com.codeoftheweb.salvo.model.GamePlayer;
import com.codeoftheweb.salvo.model.Salvo;
import com.codeoftheweb.salvo.util.Util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HitsDTO {

    private GamePlayer gamePlayer;

    public HitsDTO(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public HitsDTO() {

    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }
    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public Map<String, Object> makeHitsDTO() {
        Map<String, Object> dto = new LinkedHashMap<>();

        if(this.gamePlayer.getGame().getGamePlayers().size()==2){
            dto.put("opponent", this.gamePlayer.getSalvoes()
                    .stream()
                    .map(salvo1 -> this.makeHitDTO(salvo1,this.gamePlayer))
                    .collect(Collectors.toList()));

                dto.put("self", Util.getOpponent(this.gamePlayer).getSalvoes()
                        .stream()
                        .map(salvo1 -> this.makeHitDTO(salvo1,Util.getOpponent(this.gamePlayer)))
                        .collect(Collectors.toList()));
                return dto;
        }
        Map<String, Object> opponent = new LinkedHashMap<>();
            opponent.put("opponent", new ArrayList<>());
        Map<String, Object> self = new LinkedHashMap<>();
            opponent.put("self", new ArrayList<>());
        dto.putAll(opponent);
        dto.putAll(self);
        return dto;
    }

    public Map<String, Object> makeHitDTO(Salvo salvo, GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<>();
        Map<String, Object> damages = new LinkedHashMap<>();
        List<String> hits = Util.getAllHits(salvo);
        GamePlayer opponent = Util.getOpponent(gamePlayer);
        dto.put("turn", salvo.getTurn());
        dto.put("hitLocations", hits);

            int missed = salvo.getLocations().size()-hits.size();
            int carrierHits = Util.getShipHits(salvo,Util.getShipByType(opponent.getShips(),"carrier"));
            int submarineHits = Util.getShipHits(salvo,Util.getShipByType(opponent.getShips(),"submarine"));
            int destroyerHits = Util.getShipHits(salvo,Util.getShipByType(opponent.getShips(),"destroyer"));
            int patrolboatHits = Util.getShipHits(salvo,Util.getShipByType(opponent.getShips(),"patrolboat"));
            int battleshipHits = Util.getShipHits(salvo,Util.getShipByType(opponent.getShips(),"battleship"));

            int historicCarrierHits = Util.getHistoricShipHits(Util.getShipByType(opponent.getShips(),"carrier"));
            int historicBattleshipHits = Util.getHistoricShipHits(Util.getShipByType(opponent.getShips(),"battleship"));
            int historicSubmarineHits = Util.getHistoricShipHits(Util.getShipByType(opponent.getShips(),"submarine"));
            int historicDestroyerHits = Util.getHistoricShipHits(Util.getShipByType(opponent.getShips(),"destroyer"));
            int historicPatrolboatHits = Util.getHistoricShipHits(Util.getShipByType(opponent.getShips(),"patrolboat"));

            damages.put("carrierHits", carrierHits);
            damages.put("battleshipHits", battleshipHits);
            damages.put("submarineHits", submarineHits);
            damages.put("destroyerHits", destroyerHits);
            damages.put("patrolboatHits", patrolboatHits);
            damages.put("carrier", historicCarrierHits);
            damages.put("battleship", historicBattleshipHits);
            damages.put("submarine", historicSubmarineHits);
            damages.put("destroyer", historicDestroyerHits);
            damages.put("patrolboat", historicPatrolboatHits);

        dto.put("damages", damages);
        dto.put("missed", missed);
        return dto;
    }
}


