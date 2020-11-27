package com.codeoftheweb.salvo.dto;

import com.codeoftheweb.salvo.model.Player;

import java.util.LinkedHashMap;
import java.util.Map;

public class PlayerDTO {

    private Player player;

    public PlayerDTO(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public static Map<String, Object> makePlayerDTO(Player player){
        Map<String, Object> dto = new LinkedHashMap<>();

        dto.put("id", player.getId());
        dto.put("nombre", player.getName());
        dto.put("email", player.getEmail());

        return dto;
    }
}
