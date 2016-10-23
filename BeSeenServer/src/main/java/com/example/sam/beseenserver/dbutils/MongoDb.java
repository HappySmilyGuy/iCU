package com.example.sam.beseenserver.dbutils;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.result.UpdateResult;
import org.bson.BsonArray;
import org.bson.Document;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.mongodb.client.model.Updates.*;
import static com.mongodb.client.model.Filters.*;

/**
 * Created by PATRICK JOVAN on 22/10/2016.
 */
public class MongoDb {

    private static final String EMAIL_FIELD_NAME = "email";
    private static final String STATE_FIELD_NAME = "state";
    private static final String ALLIES_FIELD_NAME = "allies";
    private static final String PHONE_NUMBER_FIELD_NAME = "phoneNumber";
    private static final String PASSWORD_HASH_FIELD_NAME = "passwordHash";
    private static final String CODE_FIELD_NAME = "code";
    private static final String ALLY_CODE_FIELD_NAME = "allyCode";
    private static final String CODE_TIMESTAMP_FIELD_NAME = "codeTimestamp";
    private static final String APP_TOKEN_FIELD_NAME = "appToken";
    private MongoClient client;
    private MongoDatabase mongoDb;
    private MongoCollection<Document> userCollection;

    public MongoDb() {
        client = new MongoClient(new MongoClientURI("mongodb://server:c4tkbeseen@ds063856.mlab.com:63856/users"));
        mongoDb = client.getDatabase("users");
        if(!doesCollectionExist("users")) {
            mongoDb.createCollection("users");
        }
        userCollection = mongoDb.getCollection("users");
    }

    private boolean doesCollectionExist(String collectionName) {
        MongoIterable<String> collectionNames = mongoDb.listCollectionNames();
        for (String dbCollectionName : collectionNames) {
            if (dbCollectionName.equalsIgnoreCase(collectionName)) {
                return true;
            }
        }
        return false;
    }

    public boolean createUser(String email, String hash, String phoneNumber, String token) {
        Document newUser = new Document(EMAIL_FIELD_NAME, email).append(STATE_FIELD_NAME, StateEnum.UNSET.toString())
                .append(ALLIES_FIELD_NAME, new BsonArray())
                .append(PHONE_NUMBER_FIELD_NAME, phoneNumber)
                .append(PASSWORD_HASH_FIELD_NAME, hash)
                .append(APP_TOKEN_FIELD_NAME, token)
                .append(CODE_FIELD_NAME, "")
                .append(ALLY_CODE_FIELD_NAME, "")
                .append(CODE_TIMESTAMP_FIELD_NAME, 0);
        userCollection.insertOne(newUser);
        return (userCollection.getWriteConcern().isAcknowledged());
    }

    public boolean changeState(String email, String state) {
        UpdateResult result = userCollection.updateOne(eq(EMAIL_FIELD_NAME, email), set(STATE_FIELD_NAME, state));
        return result.wasAcknowledged();
    }

    public List<String> addAlly(String email, String code, String allyCode) {
        Document userDoc = userCollection.findOneAndUpdate(
                eq(EMAIL_FIELD_NAME, email),
                combine(set(CODE_FIELD_NAME, code), set(ALLY_CODE_FIELD_NAME, allyCode),
                        currentTimestamp(CODE_TIMESTAMP_FIELD_NAME)));
        boolean result = userCollection.getWriteConcern().isAcknowledged();
        Document userConnect = userCollection.find(and(eq(CODE_FIELD_NAME, allyCode), eq(ALLY_CODE_FIELD_NAME, code))).first();
        if (userConnect != null) {
            Long timestamp = (Long) userConnect.get(CODE_TIMESTAMP_FIELD_NAME);
            Calendar codeTimestamp = Calendar.getInstance();
            codeTimestamp.setTimeInMillis(timestamp);
            Instant codeTimestampInstant = codeTimestamp.toInstant();
            if (ChronoUnit.MINUTES.between(codeTimestampInstant, Instant.now()) < 30) {
                result = userCollection.updateOne(eq(EMAIL_FIELD_NAME, userDoc.get(EMAIL_FIELD_NAME)),
                        addToSet(ALLIES_FIELD_NAME, userConnect.get(EMAIL_FIELD_NAME))).wasAcknowledged() && result;
                result = userCollection.updateOne(eq(EMAIL_FIELD_NAME, userConnect.get(EMAIL_FIELD_NAME)),
                        addToSet(ALLIES_FIELD_NAME, userDoc.get(EMAIL_FIELD_NAME))).wasAcknowledged() && result;
                result = userCollection.updateOne(eq(EMAIL_FIELD_NAME, userDoc.get(EMAIL_FIELD_NAME)),
                        combine(unset(CODE_FIELD_NAME), unset(ALLY_CODE_FIELD_NAME), unset(CODE_TIMESTAMP_FIELD_NAME)))
                        .wasAcknowledged() && result;
                result = userCollection.updateOne(eq(EMAIL_FIELD_NAME, userConnect.get(EMAIL_FIELD_NAME)),
                        combine(unset(CODE_FIELD_NAME), unset(ALLY_CODE_FIELD_NAME), unset(CODE_TIMESTAMP_FIELD_NAME)))
                        .wasAcknowledged() && result;
                if (result) {
                    List<String> emailPair = new ArrayList<>();
                    emailPair.add(email);
                    emailPair.add((String) userConnect.get(EMAIL_FIELD_NAME));
                    return emailPair;
                }
                return new ArrayList<>();
            }
        }
        return new ArrayList<>();
    }

    public List<String> getAllyTokens(String email) {
        List<String> allyTokens = new ArrayList<>();
        List<String> alliesEmails = getAllyEmails(email);
        for (String allyEmail : alliesEmails) {
            allyTokens.add(getToken(allyEmail));
        }
        return allyTokens;
    }

    private List<String> getAllyEmails(String email) {
        Document userDoc = getUserDocument(email);
        return (List<String>) userDoc.get(ALLIES_FIELD_NAME);
    }

    private Document getUserDocument(String email) {
        return userCollection.find(eq(EMAIL_FIELD_NAME, email)).first();
    }

    public String getToken(String email) {
        Document userDoc = getUserDocument(email);
        return (String) userDoc.get(APP_TOKEN_FIELD_NAME);
    }

    public List<Person> getAllyList(String email) {
        List<Person> persons = new ArrayList<>();
        List<String> allyEmailList = getAllyEmails(email);
        for (String allyEmail : allyEmailList) {
            Document allyDoc = getUserDocument(allyEmail);
            Person ally = new Person(allyEmail, StateEnum.valueOf((String)allyDoc.get(STATE_FIELD_NAME)));
            persons.add(ally);
        }
        return persons;
    }
}
