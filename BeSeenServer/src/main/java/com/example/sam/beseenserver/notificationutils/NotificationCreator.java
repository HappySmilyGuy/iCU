package com.example.sam.beseenserver.notificationutils;

import com.example.sam.beseenserver.dbutils.NameToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PATRICK JOVAN on 22/10/2016.
 */
public class NotificationCreator {

    public List<JsonMessage> createChangeStatusMessages(String newState, List<NameToken> destinationNameTokens) {
        List<JsonMessage> jsonMessages = new ArrayList<>();
        for (NameToken destinationNameToken : destinationNameTokens) {
            JsonMessage jsonMessage = new JsonMessage(
                    createChangedStatusNotification(newState, destinationNameToken.getAllyName()),
                    destinationNameToken.getToken());
            jsonMessages.add(jsonMessage);
        }
        return jsonMessages;
    }

    private Notification createChangedStatusNotification(String newState, String name) {
        return new Notification("Status change", name + "'s status has changed to " + newState);
    }

    public List<JsonMessage> createAllySuccessMessages(List<NameToken> destinationNameTokens) {
        List<JsonMessage> jsonMessages = new ArrayList<>();
        for (NameToken destinationNameToken : destinationNameTokens) {
            JsonMessage jsonMessage = new JsonMessage(createAllySuccessNotification(destinationNameToken.getAllyName()),
                    destinationNameToken.getToken());
            jsonMessages.add(jsonMessage);
        }
        return jsonMessages;
    }

    private Notification createAllySuccessNotification(String name) {
        return new Notification("Ally connection successful", "You are now connected to " + name);
    }
}
