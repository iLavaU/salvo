package com.codeoftheweb.salvo.dto;

import com.codeoftheweb.salvo.model.Game;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class GameDTO {

    private Game game;

    public GameDTO(Game game) {
        this.game = game;
    }

    //Setter
    public void setGame(Game game) {
        this.game = game;
    }

    //Getter
    public Game getGame() {
        return game;
    }

    public static Map<String, Object> makeGameDTO(Game game) {
        Map<String, Object> dto = new LinkedHashMap<>();

        dto.put("id", game.getId());
        dto.put("created", game.getCreated());
        dto.put("gamePlayers", game.getGamePlayers()
                .stream()
                .map(gamePlayer -> GamePlayerDTO.makeGamePlayerDTO(gamePlayer))
                .collect(Collectors.toList()));
        return dto;
    }
}
