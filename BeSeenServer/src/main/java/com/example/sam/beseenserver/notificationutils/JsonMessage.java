package com.example.sam.beseenserver.notificationutils;

/**
 * Created by PATRICK JOVAN on 22/10/2016.
 */
public class JsonMessage {

    private Notification notification;
    private String to;

    public JsonMessage(Notification notification, String to) {
        this.notification = notification;
        this.to = to;
    }

    public Notification getNotification() {
        return notification;
    }

    public String getTo() {
        return to;
    }
}
