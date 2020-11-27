package com.codeoftheweb.salvo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import static java.util.stream.Collectors.toList;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private Date created;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    @OrderBy
    private Set<GamePlayer> gamePlayers;

    public List<Player> getPlayers() {
        return gamePlayers.stream().map(sub -> sub.getPlayer()).collect(toList());
    }

    //Constructores
    public Game() {
        Date date = new Date();
        this.created = date;
    }
    public Game(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    //Getters
    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }
    public long getId() {
        return id;
    }
    public Date getCreated() {
        return created;
    }

    //Setters
    public void setGamePlayers(LinkedHashSet<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }
    public void setCreated (Date created) {
        this.created = created;
    }

    /*public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayer.setGame(this);
        gamePlayers.add(gamePlayer);
    }*/
}
