package com.example.sam.beseenserver.dbutils;

/**
 * Created by PATRICK JOVAN on 23/10/2016.
 */

public class Person {

    private String email;
    private StateEnum state;

    public Person(String email, StateEnum state) {
        this.email = email;
        this.state = state;
    }

    public String getEmail() {
        return email;
    }

    public StateEnum getState() {
        return state;
    }
}
