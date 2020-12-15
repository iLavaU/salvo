package com.codeoftheweb.salvo.dto;

import com.codeoftheweb.salvo.model.Game;
import com.codeoftheweb.salvo.model.Salvo;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SalvoDTO {

    private Salvo salvo;

    public SalvoDTO(Salvo salvo) {
        this.salvo = salvo;
    }
    public Salvo getSalvo() {
        return salvo;
    }
    public void setSalvo(Salvo salvo) {
        this.salvo = salvo;
    }

    public Map<String, Object> makeSalvoDTO(){
        Map<String, Object> dto = new LinkedHashMap<>();
        List<String> locations = this.salvo.getLocations();
        dto.put("turn", this.salvo.getTurn());
        dto.put("player", this.salvo.getGamePlayer().getPlayer().getId());
        dto.put("locations", locations);

        return dto;
    }
}
