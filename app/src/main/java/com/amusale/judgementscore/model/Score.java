package com.amusale.judgementscore.model;

/**
 * Created by amusale on 4/4/16.
 */
public class Score {

    private String wildcard;
    private int maxNumOfCards;
    private String status;
    private String points;

    public String getWildcard() {
        return wildcard;
    }

    public void setWildcard(String wildcard) {
        this.wildcard = wildcard;
    }

    public int getMaxNumOfCards() {
        return maxNumOfCards;
    }

    public void setMaxNumOfCards(int maxNumOfCards) {
        this.maxNumOfCards = maxNumOfCards;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}
