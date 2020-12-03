package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.dto.GameDTO;
import com.codeoftheweb.salvo.dto.GamePlayerDTO;
import com.codeoftheweb.salvo.dto.PlayerDTO;
import com.codeoftheweb.salvo.model.Player;
import com.codeoftheweb.salvo.repository.GameRepository;
import com.codeoftheweb.salvo.model.GamePlayer;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import com.codeoftheweb.salvo.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SalvoController {

    //Conecto los repositorios.
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    GameRepository gameRepository;
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    GamePlayerRepository gamePlayerRepository;
    @Autowired
    ShipRepository shipRepository;

    /*Creo el mapeo para cada url. Estoy utilizando métodos estáticos, por lo que no es
    necesario instanciar cada dto.
    */
    @RequestMapping("/players")
    public List<Map<String, Object>> getPlayerAll(){
        return playerRepository.findAll()
                .stream()
                .map(player -> PlayerDTO.makePlayerDTO(player) )
                .collect(Collectors.toList());
    }
    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            //@RequestParam String userName,
            @RequestParam String email,
            @RequestParam String password) {

        if (email.isEmpty() || /*email.isEmpty() ||*/ password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (playerRepository.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        playerRepository.save(new Player(email, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping("/games")
    public Map<String, Object> getGameAll(Authentication authentication){
        Map<String, Object> dto = new LinkedHashMap<>();
        if (isGuest(authentication)) {
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

    @RequestMapping("/leaderboard")
    public List<Map<String, Object>> getScoreAll(){
        return playerRepository.findAll()
                .stream()
                .map(player -> {
                    PlayerDTO playerDTO = new PlayerDTO(player);
                    return playerDTO.makePlayerScoreDTO();})
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


    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }
}
