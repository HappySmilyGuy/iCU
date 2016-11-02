package com.example.sam.beseen.dataobjects;

/**
 * Created by PATRICK JOVAN on 23/10/2016.
 */
public class Ally {

    private String id;
    private TLState state;
    private String givenName;

    public Ally(String email, TLState state, String givenName) {
        this.id = email;
        this.state = state;
        this.givenName = givenName;
    }

    public String getid() {
        return id;
    }

    public TLState getState() {
        return state;
    }

    public String getGivenName() { return givenName; }
}
