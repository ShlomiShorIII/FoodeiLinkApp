package com.example.foodielink;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Message {
    private  String text; // Message content
    private String time; // Date and time of the message


    public  Message(String text) {
        this.text = text;
        this.time = generateCurrentTimestamp();
    }

    public String getText() {
        return text;
    }

    public String getTime() {
        return time;
    }

    private String generateCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }
}
