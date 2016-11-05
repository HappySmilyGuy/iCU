package com.example.sam.beseenserver.dbutils;

import com.mongodb.BasicDBObject;

/**
 * Created by PATRICK JOVAN on 05/11/2016.
 */
public class BasicDBObjectToAllyConverter {

    public Ally convertToAlly(BasicDBObject dbObject) {
        return new Ally(dbObject.getString("name"), dbObject.getString("email"));
    }
}
