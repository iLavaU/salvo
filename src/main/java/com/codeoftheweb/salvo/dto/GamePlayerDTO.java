package com.codeoftheweb.salvo.dto;

import com.codeoftheweb.salvo.model.GamePlayer;
import com.codeoftheweb.salvo.util.Util;

import java.util.ArrayList;
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

    public static Map<String, Object> makeGameViewDTO(GamePlayer gamePlayer) throws Exception {
        Map<String, Object> dto = new LinkedHashMap<>();
        HitsDTO hitsDTO = new HitsDTO(gamePlayer);

        dto.put("id", gamePlayer.getGame().getId());
        dto.put("created", gamePlayer.getGame().getCreated());
        dto.put("gamePlayers", gamePlayer.getGame().getGamePlayers()
                .stream()
                .map(gamePlay ->makeGamePlayerDTO(gamePlay))
                .collect(Collectors.toList()));
        dto.put("ships", gamePlayer.getShips()
                .stream()
                .map(ship -> ShipDTO.makeShipsDTO(ship))
                .collect(Collectors.toList()));
        dto.put("salvoes",gamePlayer.getGame().getGamePlayers()
                .stream()
                .flatMap(gamePlayer1 -> gamePlayer1.getSalvoes()
                    .stream()
                    .map(salvo -> {SalvoDTO salvoDTO = new SalvoDTO(salvo);
                                    return salvoDTO.makeSalvoDTO();}))
                    .collect(Collectors.toList()));

        dto.put("hits", hitsDTO.makeHitsDTO());
        dto.put("gameState", Util.getGameState(gamePlayer));
        return dto;
    }

    public static Map<String, Object> makeGamePlayerDTO(GamePlayer gamePlayer){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", gamePlayer.getId());
        dto.put("player", PlayerDTO.makePlayerDTO(gamePlayer.getPlayer()));
        return dto;
    }
}
