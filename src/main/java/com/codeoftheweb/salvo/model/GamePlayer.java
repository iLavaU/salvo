package com.codeoftheweb.salvo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Entity
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    @OneToMany(mappedBy="gamePlayer", fetch = FetchType.EAGER)
    @OrderBy
    private Set<Ship> ships; //= new HashSet<Ship>();

    @OneToMany(mappedBy="gamePlayer", fetch = FetchType.EAGER)
    @OrderBy(value = "turn ASC")
    private Set<Salvo> salvos;

    private Date joinDate;

//    public Score getScore(){
//        this.getPlayer().getScores()
//    }

    //Constructores
    public GamePlayer() {
        this.joinDate = new Date();
    }
    public GamePlayer(Game game, Player player) {
        this.game = game;
        this.player = player;
        this.joinDate = new Date();
    }

    //Getters
    public Game getGame() {
        return game;
    }
    public Player getPlayer() {
        return player;
    }
    public long getId() {
        return id;
    }
    public Date getJoinDate() {
            return joinDate;
        }
    public Set<Ship> getShips() {
        return ships;
    }
    public Set<Salvo> getSalvoes() {
        return salvos;
    }


    //Setters
    public void setPlayer(Player player) {
        this.player = player;
    }
    public void setGame(Game game) {
        this.game = game;
    }
    public void setSalvos(Set<Salvo> salvos) {
        this.salvos = salvos;
    }

    public int getLastTurn(){
        Salvo salvo = Collections.max(this.getSalvoes(), Comparator.comparing(salvo1 -> salvo1.getTurn()));
        return salvo.getTurn();
    }
}
