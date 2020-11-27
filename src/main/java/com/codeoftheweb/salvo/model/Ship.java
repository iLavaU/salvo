package com.codeoftheweb.salvo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Ship {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String type;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    @ElementCollection
    @Column(name="location")
    private List<String> locations = new ArrayList<>();

    //Constructores
    public Ship() {
    }
    public Ship(String type, List<String> locations, GamePlayer gamePlayer) {
        this.type = type;
        this.locations = locations;
        this.gamePlayer = gamePlayer;
    }

    //Getters
    public String getType() {
        return type;
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
    public void setType(String type) {
        this.type = type;
    }
    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }
    public void setLocations(List<String> locations) {
        this.locations = locations;
    }
}
