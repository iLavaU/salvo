package com.codeoftheweb.salvo.controller;

import com.codeoftheweb.salvo.dto.GameDTO;
import com.codeoftheweb.salvo.dto.PlayerDTO;
import com.codeoftheweb.salvo.dto.SalvoDTO;
import com.codeoftheweb.salvo.dto.ShipDTO;
import com.codeoftheweb.salvo.model.*;
import com.codeoftheweb.salvo.repository.*;
import com.codeoftheweb.salvo.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class GameController {

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private ShipRepository shipRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private SalvoRepository salvoRepository;


    @RequestMapping(path = "/games", method = RequestMethod.POST)
    public ResponseEntity<Object> createGame(Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<>();
        if (Util.isGuest(authentication)){
            return new ResponseEntity<>("Please sign in", HttpStatus.FORBIDDEN);
        }
        Player player = playerRepository.findByEmail(authentication.getName());
        Game game = new Game();
        GamePlayer gamePlayer = new GamePlayer(game, player);

        gameRepository.save(game);
        playerRepository.save(player);
        gamePlayerRepository.save(gamePlayer);

        dto.put("gpid", gamePlayer.getId());

        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }
    @RequestMapping(value = "/games", method = RequestMethod.GET)
    public Map<String, Object> getGameAll(Authentication authentication){
        Map<String, Object> dto = new LinkedHashMap<>();
        if (Util.isGuest(authentication)) {
            dto.put("player","Guest");
        } else {
            Player player = playerRepository.findByEmail(authentication.getName());
            dto.put("player", PlayerDTO.makePlayerDTO(player));
        }
        dto.put("games", gameRepository.findAll()
                .stream()
                .map(game -> GameDTO.makeGameDTO(game))
                .collect(Collectors.toList()));
        return dto;
    }

    @RequestMapping(value= "/games/players/{gamePlayerId}/ships", method = RequestMethod.POST)
    public ResponseEntity<Map<String,Object>> postShips(@PathVariable long gamePlayerId, @RequestBody List<Ship> ships, Authentication auth){



        //Chequeo si el usuario está autenticado.
        if (Util.isGuest(auth)){
            return new ResponseEntity<>(Util.makeMap("error","Please log in."), HttpStatus.UNAUTHORIZED);
        }
        Player player = playerRepository.findByEmail(auth.getName());
        GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayerId).orElse(null);

        //Chequeo que el gameplayer no sea null.
        if(gamePlayer == null){
            return new ResponseEntity<>(Util.makeMap("error","Gameplayer not found."),HttpStatus.FORBIDDEN);
        }
        //Chequeo si el usuario quiere ver si propio board, o no.
        if (gamePlayer.getPlayer().getId() != player.getId()) {
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
            ship.setLocationIsHit();
            ship.setGamePlayer(gamePlayer);
            shipRepository.save(ship);
        }
        //Haciendolo con stream debería andar también.
        //ships.stream().map(ship -> {ship.setGamePlayer(gamePlayer);return ship;}).collect(Collectors.toList());
        //shipRepository.saveAll(ships);
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

    @RequestMapping(value="/games/players/{gamePlayerId}/salvoes", method = RequestMethod.POST)
    public ResponseEntity<Map<String,Object>> postSalvoes(@PathVariable long gamePlayerId, @RequestBody Salvo salvo, Authentication auth) throws Exception {

        if (Util.isGuest(auth)){
            return new ResponseEntity<>(Util.makeMap("error","Please log in."),HttpStatus.UNAUTHORIZED);
        }
        Player player = playerRepository.findByEmail(auth.getName());
        GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayerId).orElse(null);

        if (gamePlayer == null){
            return new ResponseEntity<>(Util.makeMap("error","Gameplayer not found."),HttpStatus.FORBIDDEN);
        }

        if (gamePlayer.getPlayer().getId() != player.getId()){
            return new ResponseEntity<>(Util.makeMap("error","This is not your board."), HttpStatus.UNAUTHORIZED);
        }

        GamePlayer opponentGp = Util.getOpponent(gamePlayer);

        int mySalvoAmount = gamePlayer.getSalvoes().size();
        int enemySalvoAmount = opponentGp.getSalvoes().size();

        if (mySalvoAmount > enemySalvoAmount){
            return new ResponseEntity<>(Util.makeMap("error","Not your turn."),HttpStatus.FORBIDDEN);
        }
        if (salvo.getLocations().size()>5){
            return new ResponseEntity<>(Util.makeMap("error","You can't shoot more than five shots."),HttpStatus.FORBIDDEN);
        }

        salvo.setTurn(mySalvoAmount+1);
        salvo.setGamePlayer(gamePlayer);

        salvoRepository.save(salvo);
        return new ResponseEntity<>(Util.makeMap("OK", "Salvo added."), HttpStatus.CREATED);
    }

    @RequestMapping(value="/games/players/{gamePlayerId}/salvoes",method = RequestMethod.GET)
    public ResponseEntity<Map<String,Object>> getSalvoes(@PathVariable long gamePlayerId, Authentication auth){
        if (Util.isGuest(auth)){
            return new ResponseEntity<>(Util.makeMap("error","Please log in."),HttpStatus.UNAUTHORIZED);
        }
        Player player = playerRepository.findByEmail(auth.getName());
        GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayerId).orElse(null);
        if (gamePlayer == null){
            return new ResponseEntity<>(Util.makeMap("error","Gameplayer not found."),HttpStatus.FORBIDDEN);
        }
        if (gamePlayer.getPlayer().getId() != player.getId()){
            return new ResponseEntity<>(Util.makeMap("error","This is not your board."), HttpStatus.UNAUTHORIZED);
        }
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("salvoes",gamePlayer.getSalvoes()
                .stream()
                .map(salvo -> {SalvoDTO salvoDTO = new SalvoDTO(salvo);
                return salvoDTO.makeSalvoDTO();})
                .collect(Collectors.toList()));
        return new ResponseEntity<>(dto,HttpStatus.CREATED);
    }
}



