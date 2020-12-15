package com.codeoftheweb.salvo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

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

    @ElementCollection
    @Column(name="locationIsHit")
    private List<Boolean> locationIsHit;

    //Constructores
    public Ship() {}
    public Ship(String type, List<String> locations, GamePlayer gamePlayer) {
        this.type = type;
        this.locations = locations;
        this.gamePlayer = gamePlayer;
        switch (type){
            case "patrolboat":
                this.locationIsHit = Arrays.asList(false,false);
                break;
            case "carrier":
                this.locationIsHit = Arrays.asList(false,false,false,false,false);
                break;
            case "battleship":
                this.locationIsHit = Arrays.asList(false,false,false,false);
                break;
            case "submarine":
            case "destroyer":
                this.locationIsHit = Arrays.asList(false,false,false);
                break;
            default:
                break;
        }

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
    public List<Boolean> getLocationIsHit() {
        return locationIsHit;
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
    public void setLocationIsHit() {
        List<String> locations= this.getLocations();
        List<Boolean> locationsIsHit = new ArrayList<>();
        int i = 0;
        for (String location: locations) {
            locationsIsHit.add(false);
        }
        this.locationIsHit = locationsIsHit;
    }
}
