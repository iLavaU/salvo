package com.codeoftheweb.salvo.model;

import com.codeoftheweb.salvo.repository.ScoreRepository;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private Game game;

    private double score;
    private LocalDateTime finishDate;

    //Constructores
    public Score() {
    }
    public Score(Player player, Game game, double score, LocalDateTime finishDate) {
        this.player = player;
        this.game = game;
        this.score = score;
        this.finishDate = finishDate;
    }

    //Getters
    public Player getPlayer() {
        return player;
    }
    public Game getGame() {
        return game;
    }
    public double getScore() {
        return score;
    }
    public LocalDateTime getFinishDate() {
        return finishDate;
    }
    public long getId() {
        return id;
    }

    //Setters
    public void setGame(Game game) {
        this.game = game;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }
    public void setScore(double score) {
        this.score = score;
    }
    public void setFinishDate(LocalDateTime finishDate) {
        this.finishDate = finishDate;
    }
}
