package com.example.sam.beseenserver.notificationutils;

/**
 * Created by PATRICK JOVAN on 22/10/2016.
 */
public class Notification {

    private String title;
    private String body;

    public Notification(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

}
