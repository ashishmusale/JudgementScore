package com.amusale.judgementscore.model;

import java.util.List;

/**
 * Created by amusale on 4/6/16.
 */
public class Game {

    private List<User> user;
    private Score score;
    private String gameAction;
    private Integer gameId;

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }

    public String getGameAction() {
        return gameAction;
    }

    public void setGameAction(String gameAction) {
        this.gameAction = gameAction;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }
}
