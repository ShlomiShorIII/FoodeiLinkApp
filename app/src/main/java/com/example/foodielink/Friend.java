package com.example.foodielink;

import java.io.Serializable;

// Represents a friend in the chat
public class Friend implements Serializable {

    private int avatar;
    private String name;
    private String location;
    private String lastSeenTime;

    // Constructor to initialize Friend object
    public Friend(String name, int avatar, String location, String lastSeenTime) {
        this.name = name;
        this.avatar = avatar;
        this.location = location;
        this.lastSeenTime = lastSeenTime;
    }

    // Getter for avatar image
    public int getAvatar() {
        return avatar;
    }

    // Getter for friend's name
    public String getName() {
        return name;
    }

    // Getter for friend's location
    public String getLocation() {
        return location;
    }

    // Getter for last seen time
    public String getLastSeenTime() {
        return lastSeenTime;
    }

}
