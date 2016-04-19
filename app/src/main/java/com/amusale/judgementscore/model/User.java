package com.amusale.judgementscore.model;

/**
 * Created by amusale on 4/4/16.
 */
public class User {

    private int userId;
    private String userName;
    private String userGender;
    private int userAge;
    private boolean playing;

    public User(int userId, String userName, String userGender, int userAge, boolean playing) {
        this.userId = userId;
        this.userName = userName;
        this.userGender = userGender;
        this.userAge = userAge;
        this.playing = playing;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public int getUserAge() {
        return userAge;
    }

    public void setUserAge(int userAge) {
        this.userAge = userAge;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userGender='" + userGender + '\'' +
                ", userAge=" + userAge +
                ", playing=" + playing +
                '}';
    }
}
