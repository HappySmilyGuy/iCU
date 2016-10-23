package com.example.sam.beseen.dataobjects;

/**
 * Created by PATRICK JOVAN on 23/10/2016.
 */
public class Ally {

    private String email;
    private TLState state;

    public Ally(String email, TLState state) {
        this.email = email;
        this.state = state;
    }

    public String getEmail() {
        return email;
    }

    public TLState getState() {
        return state;
    }
}
