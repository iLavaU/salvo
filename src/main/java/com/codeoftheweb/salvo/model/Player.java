package com.codeoftheweb.salvo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String email;
    private String name;

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    private Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    private Set<Score> scores;

    @JsonIgnore
    public List<Game> getGames() {
        return gamePlayers.stream().map(sub -> sub.getGame()).collect(toList());
    }

    //Constructores
    public Player() { }
    public Player(String email, String name) {
        this.email = email;
        this.name = name;
    }

    //Getters
    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }
    public long getId() {
        return id;
    }
    public String getEmail() {
        return email;
    }
    public String getName() {
        return name;
    }
    public Set<Score> getScores() {
        return scores;
    }

    //Setters
    public void setGamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }

    //toString
    public String toString() {
        return id+" "+name + " " + email;
    }

    public double getTotalScore(){
        double sum = this.getScores().stream().mapToDouble(Score -> Score.getScore()).sum();
        return sum;
    }

    public long getWins(){
        return this.getScores().stream()
                .filter(score -> score.getScore() == 1.0D)
                .count();
    }
    public long getLoses(){
        return this.getScores().stream()
                .filter(score -> score.getScore() == 0.0D)
                .count();
    }
    public long getTies(){
        return this.getScores().stream()
                .filter(score -> score.getScore() == 0.5D)
                .count();
    }



//    public double getWin(Player player){
//        double wins = 0;
//        List<Score> scores = player.getScores();
//        Iterator<Score> iterator = scores.iterator();
//        while (iterator.hasNext()){
//            Score score = iterator.next();
//            if (score.getScore() == 1){
//                wins += 1;
//            }
//        }
//
//        return wins;
//    }

    /*public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayer.setPlayer(this);
        gamePlayers.add(gamePlayer);
    }*/
}

