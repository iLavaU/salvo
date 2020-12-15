package com.codeoftheweb.salvo.controller;

import com.codeoftheweb.salvo.dto.GamePlayerDTO;
import com.codeoftheweb.salvo.dto.PlayerDTO;
import com.codeoftheweb.salvo.model.Game;
import com.codeoftheweb.salvo.model.Player;
import com.codeoftheweb.salvo.repository.GameRepository;
import com.codeoftheweb.salvo.model.GamePlayer;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import com.codeoftheweb.salvo.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AppController {

    //Conecto los repositorios.
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    /*Creo el mapeo para cada url. Estoy utilizando métodos estáticos, por lo que no es
    necesario instanciar cada dto.
    */
    @RequestMapping(value = "/game/{idGame}/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> joinGame(@PathVariable long idGame, Authentication authentication){
        if (Util.isGuest(authentication)){
            return new ResponseEntity<>(Util.makeMap("error","Is a guest.") ,HttpStatus.UNAUTHORIZED);
        }
        Player player = playerRepository.findByEmail(authentication.getName());
        Game gameToJoin = gameRepository.getOne(idGame);

        if (gameToJoin == null){
            return  new ResponseEntity<>(Util.makeMap("error","Game not found."),HttpStatus.FORBIDDEN);
        }
        List<Player> players = gameToJoin.getPlayers();
        for (Player player1: players) {
            if (player1.getId() == player.getId()){
                return new ResponseEntity<>(Util.makeMap("error","Already in game."),HttpStatus.FORBIDDEN);
            }
        }
//        Con Collection.contains es mas corto
//        if (players.contains(player)){
//            return new ResponseEntity<>(Util.makeMap("error","Already in game."),HttpStatus.FORBIDDEN);
//        }
        long gamePlayersCount = gameToJoin.getGamePlayers().size();
        if (gamePlayersCount == 1){
            GamePlayer gamePlayer = gamePlayerRepository.save(new GamePlayer(gameToJoin, player));
            return new ResponseEntity<>(Util.makeMap("gpid", gamePlayer.getId()), HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(Util.makeMap("error", "Game is full."), HttpStatus.FORBIDDEN);
        }
    }
    @RequestMapping(path = "/gamePlayers", method = RequestMethod.GET)
    public List<Map<String, Object>> getGameplayersAll(){
        return gamePlayerRepository.findAll()
                .stream()
                .map(gamePlayer -> GamePlayerDTO.makeGamePlayerDTO(gamePlayer))
                .collect(Collectors.toList());
    }
    @RequestMapping(path = "/game_view/{idgame_player}", method = RequestMethod.GET)
    private ResponseEntity<Map<String, Object>> getGamePlayerAll(@PathVariable long idgame_player, Authentication authentication) throws Exception {
        if (Util.isGuest(authentication)){
            return new ResponseEntity<>(Util.makeMap("error","Is guest."), HttpStatus.FORBIDDEN);
        }else {
            GamePlayer gameplayer = gamePlayerRepository.findById(idgame_player).get();
            Player player = playerRepository.findByEmail(authentication.getName());
            if (gameplayer.getPlayer().getId() == player.getId()){
                return new ResponseEntity<>(GamePlayerDTO.makeGameViewDTO(gameplayer), HttpStatus.CREATED);
            }
            return new ResponseEntity<>(Util.makeMap("error", "You can't see the enemy's board. You are logged in as " + player.getName() +
                    " and want to see " + gameplayer.getPlayer().getName() + "'s board."), HttpStatus.FORBIDDEN);
        }
    }
    @RequestMapping("/leaderboard")
    public List<Map<String, Object>> getScoreAll(){
        return playerRepository.findAll()
                .stream()
                .map(player -> {
                    PlayerDTO playerDTO = new PlayerDTO(player);
                    return playerDTO.makePlayerScoreDTO();})
                .collect(Collectors.toList());
    }
}
