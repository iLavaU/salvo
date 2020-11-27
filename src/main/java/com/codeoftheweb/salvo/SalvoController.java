package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.dto.GameDTO;
import com.codeoftheweb.salvo.dto.GamePlayerDTO;
import com.codeoftheweb.salvo.dto.PlayerDTO;
import com.codeoftheweb.salvo.repository.GameRepository;
import com.codeoftheweb.salvo.model.GamePlayer;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import com.codeoftheweb.salvo.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SalvoController {

    //Conecto los repositorios.

    @Autowired
    GameRepository gameRepository;
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    GamePlayerRepository gamePlayerRepository;
    @Autowired
    ShipRepository shipRepository;

    /*Creo el mapeo para cada url. Estoy utilizando métodos estáticos, por lo que no es necesario instanciar cada dto.
    */
    @RequestMapping("/players")
    public List<Map<String, Object>> getPlayerAll(){
        return playerRepository.findAll()
                .stream()
                .map(player -> PlayerDTO.makePlayerDTO(player) )
                .collect(Collectors.toList());
    }

    @RequestMapping("/games")
    public List<Map<String, Object>> getGameAll(){
        return gameRepository.findAll()
                .stream()
                .map(game -> GameDTO.makeGameDTO(game))
                .collect(Collectors.toList());
    }

    @RequestMapping("/gamePlayers")
    public List<Map<String, Object>> getGameplayersAll(){
        return gamePlayerRepository.findAll()
                .stream()
                .map(gamePlayer -> GamePlayerDTO.makeGamePlayerDTO(gamePlayer))
                .collect(Collectors.toList());
    }

    @RequestMapping("/game_view/{idgame_player}")
    private Map<String, Object> getGamePlayerAll(@PathVariable long idgame_player){
         GamePlayer gameplayer = gamePlayerRepository.findById(idgame_player).get();
        return GamePlayerDTO.makeGameViewDTO(gameplayer);
    }
}
