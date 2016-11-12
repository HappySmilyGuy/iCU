package com.example.sam.beseenserver.dbutils;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.result.UpdateResult;
import org.bson.BsonArray;
import org.bson.BsonTimestamp;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    private static final String PENDING_ALLY_FIELD_NAME = "pendingAlly";
    private static final String ALLY_NAME_FIELD_NAME = "name";
    private MongoClient client;
    private MongoDatabase mongoDb;
    private MongoCollection<Document> userCollection;

    private BasicDBObjectToAllyConverter converter;

    public MongoDb() {
        client = new MongoClient(new MongoClientURI("mongodb://server:c4tkbeseen@ds063856.mlab.com:63856/users"));
        mongoDb = client.getDatabase("users");
        if(!doesCollectionExist("users")) {
            mongoDb.createCollection("users");
        }
        userCollection = mongoDb.getCollection("users");
        converter = new BasicDBObjectToAllyConverter();
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

    public boolean login(String email, String hash) {
        MongoIterable result = userCollection.find(and(eq(EMAIL_FIELD_NAME, email), eq(PASSWORD_HASH_FIELD_NAME, hash)));
        return (result.first() != null);
    }

    public boolean changeState(String email, String state) {
        UpdateResult result = userCollection.updateOne(eq(EMAIL_FIELD_NAME, email), set(STATE_FIELD_NAME, state));
        return updateWasSuccess(result);
    }

    public List<Ally> addAlly(String email, String name, String code, String allyCode) {
        Document currentUser = userCollection.findOneAndUpdate(
                eq(EMAIL_FIELD_NAME, email),
                combine(set(CODE_FIELD_NAME, code), set(ALLY_CODE_FIELD_NAME, allyCode), set(PENDING_ALLY_FIELD_NAME, name),
                        currentTimestamp(CODE_TIMESTAMP_FIELD_NAME)));
        boolean result = userCollection.getWriteConcern().isAcknowledged();
        Document potentialConnection = userCollection.find(and(eq(CODE_FIELD_NAME, allyCode), eq(ALLY_CODE_FIELD_NAME, code))).first();
        if (potentialConnection != null) {
            BsonTimestamp timestamp = (BsonTimestamp) potentialConnection.get(CODE_TIMESTAMP_FIELD_NAME);
            long longTimestamp = (long) timestamp.getTime() * 1000;
            Calendar codeTimestamp = Calendar.getInstance();
            codeTimestamp.setTimeInMillis(longTimestamp);
            Instant codeTimestampInstant = codeTimestamp.toInstant();
            if (ChronoUnit.MINUTES.between(codeTimestampInstant, Instant.now()) < 30) {
                BasicDBObject newAllyObject = new BasicDBObject();
                newAllyObject.put(ALLY_NAME_FIELD_NAME, name);
                newAllyObject.put(EMAIL_FIELD_NAME, potentialConnection.get(EMAIL_FIELD_NAME));
                result = userCollection.updateOne(eq(EMAIL_FIELD_NAME, currentUser.get(EMAIL_FIELD_NAME)),
                        addToSet(ALLIES_FIELD_NAME, newAllyObject)).wasAcknowledged() && result;
                BasicDBObject currentUserAsAllyObject = new BasicDBObject();
                currentUserAsAllyObject.put(ALLY_NAME_FIELD_NAME, potentialConnection.get(PENDING_ALLY_FIELD_NAME));
                currentUserAsAllyObject.put(EMAIL_FIELD_NAME, currentUser.get(EMAIL_FIELD_NAME));
                result = userCollection.updateOne(eq(EMAIL_FIELD_NAME, potentialConnection.get(EMAIL_FIELD_NAME)),
                        addToSet(ALLIES_FIELD_NAME, currentUserAsAllyObject)).wasAcknowledged() && result;
                result = userCollection.updateOne(eq(EMAIL_FIELD_NAME, currentUser.get(EMAIL_FIELD_NAME)),
                        combine(unset(CODE_FIELD_NAME), unset(ALLY_CODE_FIELD_NAME), unset(CODE_TIMESTAMP_FIELD_NAME)))
                        .wasAcknowledged() && result;
                result = userCollection.updateOne(eq(EMAIL_FIELD_NAME, potentialConnection.get(EMAIL_FIELD_NAME)),
                        combine(unset(CODE_FIELD_NAME), unset(ALLY_CODE_FIELD_NAME), unset(CODE_TIMESTAMP_FIELD_NAME)))
                        .wasAcknowledged() && result;
                if (result) {
                    List<Ally> nameEmailPair = new ArrayList<>();
                    nameEmailPair.add(converter.convertToAlly(newAllyObject));
                    nameEmailPair.add(converter.convertToAlly(currentUserAsAllyObject));
                    return nameEmailPair;
                }
                return new ArrayList<>();
            }
        }
        return new ArrayList<>();
    }

    public List<NameToken> getAllyTokens(String email) {
        List<NameToken> allyNameTokens = new ArrayList<>();
        List<String> alliesEmails = getAllyEmails(email);
        if (alliesEmails != null) {
            for (String allyEmail : alliesEmails) {
                String token = getToken(allyEmail);
                String name = getAllyNameForMe(email, allyEmail);
                allyNameTokens.add(new NameToken(name, token));
            }
            return allyNameTokens;
        }
        return null;
    }

    private String getAllyNameForMe(String email, String allyEmail) {
        Document userDoc = getUserDocument(email);
        List<BasicDBObject> allies = (List<BasicDBObject>) userDoc.get(ALLIES_FIELD_NAME);
        for (BasicDBObject ally : allies) {
            if (ally.getString(EMAIL_FIELD_NAME).equalsIgnoreCase(allyEmail)) {
                return ally.getString(ALLY_NAME_FIELD_NAME);
            }
        }
        return null;
    }

    private List<String> getAllyEmails(String email) {
        List<Document> allyList = getAllyObjects(email);
        List<String> allyEmails = new ArrayList<>();
        if (allyList != null) {
            for (Document ally : allyList) {
                String allyEmail = ally.getString(EMAIL_FIELD_NAME);
                if (allyEmail != null && !allyEmail.isEmpty()) {
                    allyEmails.add(allyEmail);
                }
            }
            return allyEmails;
        }
        return null;
    }

    private List<Document> getAllyObjects(String email) {
        Document userDoc = getUserDocument(email);
        if (userDoc != null) {
            return (List<Document>) userDoc.get(ALLIES_FIELD_NAME);
        }
        return null;
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
        List<Document> allyList = getAllyObjects(email);
        for (Document allyObject : allyList) {
            Document allyDoc = getUserDocument(allyObject.getString(EMAIL_FIELD_NAME));
            Person ally = new Person(allyObject.getString(EMAIL_FIELD_NAME), allyObject.getString(ALLY_NAME_FIELD_NAME),
                    StateEnum.valueOf((String)allyDoc.get(STATE_FIELD_NAME)));
            persons.add(ally);
        }
        return persons;
    }

    public boolean updateToken(String email, String token) {
        UpdateResult result = userCollection.updateOne(eq(EMAIL_FIELD_NAME, email), set(APP_TOKEN_FIELD_NAME, token));
        return updateWasSuccess(result);
    }

    private boolean updateWasSuccess(UpdateResult result) {
        return result.wasAcknowledged() && result.getMatchedCount() > 0 && result.getModifiedCount() > 0;
    }


}
