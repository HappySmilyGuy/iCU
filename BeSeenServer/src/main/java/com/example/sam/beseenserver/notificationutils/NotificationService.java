package com.example.sam.beseenserver.notificationutils;

import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

/**
 * Created by PATRICK JOVAN on 22/10/2016.
 */
public class NotificationService {

    private static final String URL = "https://fcm.googleapis.com/fcm/send";
    private static final String API_KEY = "AIzaSyAUntslFhUhukgrNJvYpmkbVU7GK_C1DbI";

    public void sendNotification(JsonMessage jsonMessage) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "key=" + API_KEY);
        headers.add("Content-Type", "application/json");
        HttpEntity<JsonMessage> entity = new HttpEntity<>(jsonMessage, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity response = restTemplate.postForEntity(URL, entity, Object.class);
    }

    public void sendNotifications(List<JsonMessage> jsonMessages) {
        jsonMessages.forEach(this::sendNotification);
    }
}
