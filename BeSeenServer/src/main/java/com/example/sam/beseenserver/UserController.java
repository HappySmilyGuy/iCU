package com.example.sam.beseenserver;

import com.example.sam.beseenserver.utils.MongoDb;
import com.example.sam.beseenserver.utils.StateEnum;
import org.hibernate.validator.constraints.Email;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by PATRICK JOVAN on 22/10/2016.
 */
@RestController
public class UserController {

    private MongoDb mongoDb = new MongoDb();

    @RequestMapping("/register")
    public ResponseEntity<String> register(@Email String email, String passwordHash, String phoneNumber) {
        if (mongoDb.createUser(email, passwordHash, phoneNumber)) {
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping("/changeState")
    public ResponseEntity<String> changeState(@Email String email, String state) {
        if (mongoDb.changeState(email, state)) {
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping("/addFriend")
    public ResponseEntity<String> addFriend(@Email String email, String myCode, String theirCode) {
        if (mongoDb.addAlly(email, myCode, theirCode)) {
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
