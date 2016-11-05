package com.example.sam.beseenserver.dbutils;

/**
 * Created by PATRICK JOVAN on 05/11/2016.
 */
public class NameToken {

    private String allyName;
    private String token;

    public NameToken(String allyName, String token) {
        this.allyName = allyName;
        this.token = token;
    }

    public String getAllyName() {
        return allyName;
    }

    public String getToken() {
        return token;
    }
}
