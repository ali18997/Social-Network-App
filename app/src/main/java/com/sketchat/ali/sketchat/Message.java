package com.sketchat.ali.sketchat;

/**
 * Created by ALI on 11/26/2017.
 */

public class Message {

    private String content, username, time;

    public Message() {

    }

    public Message(String content, String username, String time){
        this.content = content;
        this.username = username;
        this.time = time;
    }

    public String getTimemsg() {return time;}

    public void setTimemsg(String time) {this.time = time; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content){
        this.content = content;
    }
}
