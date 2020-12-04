package com.codeoftheweb.salvo.controller;

import com.codeoftheweb.salvo.dto.GameDTO;
import com.codeoftheweb.salvo.dto.PlayerDTO;
import com.codeoftheweb.salvo.model.Game;
import com.codeoftheweb.salvo.model.GamePlayer;
import com.codeoftheweb.salvo.model.Player;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import com.codeoftheweb.salvo.repository.GameRepository;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import com.codeoftheweb.salvo.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class GamesController {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    GamePlayerRepository gamePlayerRepository;

    @Autowired
    PlayerRepository playerRepository;

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
}
