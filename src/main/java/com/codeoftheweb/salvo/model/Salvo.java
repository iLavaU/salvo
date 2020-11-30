package com.codeoftheweb.salvo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Salvo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private int turn;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    @ElementCollection
    @Column(name="location")
    private List<String> locations = new ArrayList<>();

    //Constructores
    public Salvo() {
    }
    public Salvo(int turn, GamePlayer gamePlayer, List<String> locations) {
        this.turn = turn;
        this.gamePlayer = gamePlayer;
        this.locations = locations;
    }

    //Getters
    public int getTurn() {
        return turn;
    }
    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }
    public List<String> getLocations() {
        return locations;
    }
    public long getId() {
        return id;
    }

    //Setters
    public void setTurn(int turn) {
        this.turn = turn;
    }
    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }
    public void setLocations(List<String> locations) {
        this.locations = locations;
    }
}
