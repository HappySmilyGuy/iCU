package com.example.sam.beseenserver.notificationutils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PATRICK JOVAN on 22/10/2016.
 */
public class NotificationCreator {

    public List<JsonMessage> createChangeStatusMessages(String newState, List<String> destinationTokens) {
        List<JsonMessage> jsonMessages = new ArrayList<>();
        for (String destinationToken : destinationTokens) {
            JsonMessage jsonMessage = new JsonMessage(createChangedStatusNotification(newState), destinationToken);
            jsonMessages.add(jsonMessage);
        }
        return jsonMessages;
    }

    private Notification createChangedStatusNotification(String newState) {
        return new Notification("Status change", "Someone's status has changed to" + newState);
    }

    public List<JsonMessage> createAllySuccessMessages(List<String> destinationTokens) {
        List<JsonMessage> jsonMessages = new ArrayList<>();
        for (String destinationToken : destinationTokens) {
            JsonMessage jsonMessage = new JsonMessage(createAllySuccessNotification(), destinationToken);
            jsonMessages.add(jsonMessage);
        }
        return jsonMessages;
    }

    private Notification createAllySuccessNotification() {
        return new Notification("Ally connection successful", "You are now connected to a new person");
    }
}
