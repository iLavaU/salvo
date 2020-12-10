package com.codeoftheweb.salvo.controller;

import com.codeoftheweb.salvo.dto.ShipDTO;
import com.codeoftheweb.salvo.model.GamePlayer;
import com.codeoftheweb.salvo.model.Player;
import com.codeoftheweb.salvo.model.Ship;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import com.codeoftheweb.salvo.repository.ShipRepository;
import com.codeoftheweb.salvo.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class GameController {

    @Autowired
    GamePlayerRepository gamePlayerRepository;

    @Autowired
    ShipRepository shipRepository;

    @Autowired
    PlayerRepository playerRepository;

    @RequestMapping(value= "/games/players/{gamePlayerId}/ships", method = RequestMethod.POST)
    public ResponseEntity<Map<String,Object>> postShips(@PathVariable long gamePlayerId, @RequestBody List<Ship> ships, Authentication auth){

        Player player = playerRepository.findByEmail(auth.getName());
        GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayerId).orElse(null);

        //Chequeo si el usuario está autenticado.
        if (Util.isGuest(auth)){
            return new ResponseEntity<>(Util.makeMap("error","Please log in."), HttpStatus.UNAUTHORIZED);
        }
        //Chequeo que el gameplayer no sea null.
        if(gamePlayer == null){
            return new ResponseEntity<>(Util.makeMap("error","Gamplayer not found."),HttpStatus.FORBIDDEN);
        }
        //Chequeo si el usuario quiere ver si propio board.
        if (!(gamePlayer.getPlayer().getId() == player.getId())) {
            return new ResponseEntity<>(Util.makeMap("error","This is not your board."), HttpStatus.UNAUTHORIZED);
        }
        //Chequeo si el usuario tiene barcos.
        int sizeOldShips = gamePlayer.getShips().size();
        if (sizeOldShips != 0) {
            return new ResponseEntity<>(Util.makeMap("error","Already have ships."), HttpStatus.FORBIDDEN);
        }
        //Chequeo si mandó 5 barcos.
        int sizeNewShips = ships.size();
        if (sizeNewShips != 5){
            return new ResponseEntity<>(Util.makeMap("error","You need five ships."), HttpStatus.FORBIDDEN);
        }
        for (Ship ship : ships) {
            ship.setGamePlayer(gamePlayer);
            shipRepository.save(ship);
        }
        //Haciendolo con stream debería andar también.
        ships.stream().map(ship -> {ship.setGamePlayer(gamePlayer);return ship;}).collect(Collectors.toList());
        shipRepository.saveAll(ships);
        return new ResponseEntity<>(Util.makeMap("OK","Added ships."), HttpStatus.CREATED);
    }

    @RequestMapping(value= "/games/players/{gamePlayerId}/ships", method = RequestMethod.GET)
    public ResponseEntity<Object> getShips(@PathVariable long gamePlayerId, Authentication auth){
        if (Util.isGuest(auth)){
            return new ResponseEntity<>("Error, please log in.",HttpStatus.FORBIDDEN);
        }
        List<Map<String, Object>> dto;
        Player player = playerRepository.findByEmail(auth.getName());
        GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayerId).get();
        //Chequeo si el usuario quiere ver si propio board o no.
        if (!(gamePlayer.getPlayer().getId() == player.getId())) {
            return new ResponseEntity<>("Error, you can not see the enemy ships.", HttpStatus.FORBIDDEN);
        }
        dto = gamePlayer.getShips()
                .stream()
                .map(ship -> ShipDTO.makeShipsDTO(ship))
                .collect(Collectors.toList());
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }
}
