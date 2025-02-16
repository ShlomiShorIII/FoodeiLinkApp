package com.example.foodielink;

import java.io.Serializable;

public class Friend implements Serializable {

    private int Avatar;
    private String Name;
    private String Location;
    private String LastSeenTime;

    public Friend(String name, int avatar, String location, String lastSeenTime) {
        Name = name;
        Avatar = avatar;
        Location = location;
        LastSeenTime = lastSeenTime;
    }

    public int getAvatar() {
        return Avatar;
    }

    public String getName() {
        return Name;
    }

    public String getLocation() {
        return Location;
    }

    public String getLastSeenTime() {
        return LastSeenTime;
    }

}
