package com.codeoftheweb.salvo.dto;

import com.codeoftheweb.salvo.model.Ship;

import java.util.LinkedHashMap;
import java.util.Map;

public class ShipDTO {

    private Ship ship;

    public ShipDTO(Ship ship) {
        this.ship = ship;
    }

    public static Map<String, Object> makeShipsDTO(Ship ship){
        Map<String, Object> dto = new LinkedHashMap<>();

        dto.put("type", ship.getType());
        dto.put("locations", ship.getLocations());
        dto.put("locationIsHit",ship.getLocationIsHit());

        return dto;
    }

}
