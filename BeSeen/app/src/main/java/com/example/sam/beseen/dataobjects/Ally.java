package com.example.sam.beseen.dataobjects;

/**
 * The Ally class contains all the information associated with each ally.
 *
 * @author Patrick
 * @version 1.0
 * @since 23/10/2016
 */
public final class Ally {

    /** the database ID for the ally's noSQL page. */
    private String id;
    private TLState state;
    private String givenName;

    /**
     * Constructor.
     *
     * @param idIn the database ID for the ally's noSQL page.
     * @param stateIn the state of the ally.
     * @param name the name given by the user.
     */
    public Ally(final String idIn, final TLState stateIn, final String name) {
        this.id = idIn;
        this.state = stateIn;
        this.givenName = name;
    }

    /**
     * Getter.
     * @return the databaseID for the ally's noSQL page
     */
    public String getid() {
        return id;
    }

    /**
     * Getter.
     * @return the state of the ally.
     */
    public TLState getState() {
        return state;
    }

    /**
     * Getter.
     * @return the name given by the user to this ally.
     */
    public String getGivenName() {
        return givenName;
    }
}
