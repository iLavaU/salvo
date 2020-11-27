package com.codeoftheweb.salvo.dto;

import com.codeoftheweb.salvo.model.GamePlayer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class GamePlayerDTO {

    private GamePlayer gamePlayer;

    public GamePlayerDTO(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public static Map<String, Object> makeGameViewDTO(GamePlayer gamePlayer){
        Map<String, Object> dto = new LinkedHashMap<>();

        dto.put("id", gamePlayer.getGame().getId());
        dto.put("created", gamePlayer.getGame().getId());
        dto.put("gamePlayers", gamePlayer.getGame().getGamePlayers()
                .stream()
                .map(gamePlay ->makeGamePlayerDTO(gamePlay))
                .collect(Collectors.toList()));
        dto.put("ships", gamePlayer.getShips()
                .stream()
                .map(ship -> ShipDTO.makeShipsDTO(ship))
                .collect(Collectors.toList()));
        return dto;
    }

    public static Map<String, Object> makeGamePlayerDTO(GamePlayer gamePlayer){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", gamePlayer.getId());
        dto.put("player", PlayerDTO.makePlayerDTO(gamePlayer.getPlayer()));
        return dto;
    }
}
