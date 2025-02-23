package com.example.foodielink;

import java.io.Serializable;

// Represents a friend in the chat
public class Friend implements Serializable {

    private int Avatar;
    private String Name;
    private String Location;
    private String LastSeenTime;

    // Constructor to initialize Friend object
    public Friend(String name, int avatar, String location, String lastSeenTime) {
        Name = name;
        Avatar = avatar;
        Location = location;
        LastSeenTime = lastSeenTime;
    }

    // Getter for avatar image
    public int getAvatar() {
        return Avatar;
    }

    // Getter for friend's name
    public String getName() {
        return Name;
    }

    // Getter for friend's location
    public String getLocation() {
        return Location;
    }

    // Getter for last seen time
    public String getLastSeenTime() {
        return LastSeenTime;
    }

}
