package com.example.sam.beseenserver;

import com.example.sam.beseenserver.dbutils.MongoDb;
import com.example.sam.beseenserver.dbutils.Person;
import com.example.sam.beseenserver.notificationutils.JsonMessage;
import com.example.sam.beseenserver.notificationutils.Notification;
import com.example.sam.beseenserver.notificationutils.NotificationCreator;
import com.example.sam.beseenserver.notificationutils.NotificationService;
import org.hibernate.validator.constraints.Email;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PATRICK JOVAN on 22/10/2016.
 */
@RestController
public class UserController {

    private MongoDb mongoDb = new MongoDb();
    private NotificationService notificationService = new NotificationService();
    private NotificationCreator notificationCreator = new NotificationCreator();

    @RequestMapping("/register")
    public ResponseEntity<String> register(@Email String email, String passwordHash, String phoneNumber, String token) {
        if (mongoDb.createUser(email, passwordHash, phoneNumber, token)) {
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping("/changeState")
    public ResponseEntity<String> changeState(@Email String email, String state) {
        if (mongoDb.changeState(email, state)) {
            notificationService.sendNotifications(
                    notificationCreator.createChangeStatusMessages(state, mongoDb.getAllyTokens(email)));
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping("/addAlly")
    public ResponseEntity<String> addAlly(@Email String requestEmail, String myCode, String theirCode) {
        List<String> emailPair = mongoDb.addAlly(requestEmail, myCode, theirCode);
        if (!emailPair.isEmpty()) {
            List<String> tokens = new ArrayList<>();
            for (String email : emailPair) {
                String token = mongoDb.getToken(email);
                tokens.add(token);
            }
            notificationService.sendNotifications(notificationCreator.createAllySuccessMessages(tokens));
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping("/getAllyList")
    public ResponseEntity<List<Person>> getAllyList(@Email String email) {
        List<Person> allyList = mongoDb.getAllyList(email);
        if (!allyList.isEmpty()) {
            return new ResponseEntity<>(allyList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping("/updateToken")
    public ResponseEntity<String> updateToken(@Email String email, String token) {
        if(mongoDb.updateToken(email, token)) {
            return new ResponseEntity<>("Token updated", HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
