package com.example.sam.beseenserver.dbutils;

/**
 * Created by PATRICK JOVAN on 23/10/2016.
 */

public class Person {

    private String email;
    private String name;
    private StateEnum state;

    public Person(String email, String name, StateEnum state) {
        this.email = email;
        this.name = name;
        this.state = state;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public StateEnum getState() {
        return state;
    }
}
