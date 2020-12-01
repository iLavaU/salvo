package com.codeoftheweb.salvo.dto;

import com.codeoftheweb.salvo.model.Score;

import java.util.LinkedHashMap;
import java.util.Map;

public class ScoreDTO {
    private Score score;

    public ScoreDTO(Score score) {
        this.score = score;
    }

    public Score getScore() {
        return score;
    }
    public void setScore(Score score) {
        this.score = score;
    }

    public Map<String, Object> makeScoreDTO() {
        Map<String, Object> dto = new LinkedHashMap<>();

        dto.put("player", this.score.getPlayer().getId());
        dto.put("score", this.score.getScore());
        dto.put("finishDate", this.score.getFinishDate());

        return dto;
    }
}
