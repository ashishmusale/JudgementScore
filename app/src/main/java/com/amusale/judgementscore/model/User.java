package com.amusale.judgementscore.model;

/**
 * Created by amusale on 4/4/16.
 */
public class User {

    private String userName;
    private String userGender;
    private int userAge;

    public User(String userName, String userGender, int userAge) {
        this.userName = userName;
        this.userGender = userGender;
        this.userAge = userAge;
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
}
