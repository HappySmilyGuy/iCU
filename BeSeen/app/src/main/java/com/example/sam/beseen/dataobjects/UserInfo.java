package com.example.sam.beseen.dataobjects;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;


/**
 * The UserInfo class is for accessing data about the current user.
 *
 * @author Eddie
 * @version 1.0
 * @since 04-11-16
 */

public final class UserInfo {
    private static final String LOCAL_DATA = "LocalDataStore";

    /**
     * Saves the given username to the SharedPreferences.
     * @param context the current context.
     * @param userName the username to save.
     */
    public static void saveUsername(final Context context, final String userName) {
        SharedPreferences.Editor editor = context.getSharedPreferences(LOCAL_DATA, MODE_PRIVATE).edit();
        editor.putString("username", userName);
        editor.apply();
    }

    /**
     * Saves the state of the user to the SharedPreferences.
     * @param context the current context.
     * @param state the state to save.
     */
    public static void saveState(final Context context, final TLState state) {
        SharedPreferences.Editor editor = context.getSharedPreferences(LOCAL_DATA, MODE_PRIVATE).edit();
        editor.putString("state", state.toString());
        editor.apply();
    }

    /**
     * Gets the current user's username from the SharedPreferences.
     * @param context the current context.
     * @return the current user's username.
     */
    public static String getUsername(final Context context) {
        SharedPreferences preferences = context.getSharedPreferences(LOCAL_DATA, MODE_PRIVATE);
        return preferences.getString("username", null);
    }

    /**
     * Gets the current user's state from the SharedPreferences.
     * @param context the current context.
     * @return the current user's state.
     */
    public static TLState getState(final Context context) {
        SharedPreferences preferences = context.getSharedPreferences(LOCAL_DATA, MODE_PRIVATE);
        return TLState.valueOf(preferences.getString("state", "UNSET"));
    }

    /**
     * Saves the mapping between ID and the user's given name.
     * @param context the current context.
     * @param id the database ID for the ally.
     * @param name the given username of the ally.
     */
    public static void saveIDNamePair(final Context context, final String id, final String name) {
        SharedPreferences.Editor editor = context.getSharedPreferences(LOCAL_DATA, MODE_PRIVATE).edit();
        editor.putString(id, name);
        editor.apply();
    }

    /**
     * Gets the mapping given username that the user has associated with the given database ID.
     * @param context the current context.
     * @param id the databse ID for the ally.
     * @return the user's given name for the ally.
     */
    public static String getNameFromID(final Context context, final String id) {
        SharedPreferences preferences = context.getSharedPreferences(LOCAL_DATA, MODE_PRIVATE);
        return preferences.getString(id, "Unnamed");
    }
}
