package com.example.sam.beseen.server;

import android.os.AsyncTask;

import com.example.sam.beseen.AlliesScreen;
import com.example.sam.beseen.dataobjects.Ally;
import com.example.sam.beseen.dataobjects.TLState;
import com.google.firebase.iid.FirebaseInstanceId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * The ServerCaller class is created to contain all the calls to the sever.
 *
 * @author Eddie
 * @version 1.0
 * @since 22-10-16.
 */
public final class ServerCaller {
    private static final String SERVER = "10.0.2.2";
    private static final String PORT = "8080";
    private static final String RPC_CHANGE_STATE = "changeState";
    private static final String PARAM_EMAIL = "email";
    private static final String PARAM_STATE = "state";
    private static final String RPC_REGISTER = "register";
    private static final String PARAM_PASSWORD = "passwordHash";
    private static final String PARAM_PHONE_NUMBER = "phoneNumber";
    private static final String PARAM_APP_TOKEN = "token";
    private static final String RPC_ADD_ALLY = "addAlly";
    private static final String PARAM_MY_CODE = "myCode";
    private static final String PARAM_FRIEND_CODE = "allyCode";
    private static final String PARAM_NAME = "name";
    private static final String RPC_UPDATE_TOKEN = "updateToken";
    private static final String RPC_ALLY_LIST = "getAllyList";
    private static final String RPC_LOGIN = "login";

    private static ServerCaller instance = null;
    private static Boolean result = false;

    public static Boolean getResult() {
        return result;
    }

    /**
     * Private constructor to stop multiple constructors being created.
     */
    private ServerCaller() { }

    /**
     * Gets the instance of the ServerCaller, stops multiple instances.
     * @return the ServerCaller.
     */
    public static ServerCaller getInstance() {
        if (instance != null) {
            return instance;
        }
        return new ServerCaller();
    }

    /**
     * Calls the change state function on the server.
     *
     * @param email the user who's state to change.
     * @param state what to set the new state as.
     */
    public void changeState(final String email, final TLState state) {
        new MessageServer().execute(createURL(RPC_CHANGE_STATE, "?" + PARAM_EMAIL + "=" + email
                                    + "&" + PARAM_STATE + "=" + state));
    }

    /**
     * Calls the register function on the server.
     *
     * @param email the email of the new user.
     * @param passwordHash the hashed password of the new user.
     * @param phone the phone number of the new user.
     */
    public void register(final String email, final String passwordHash, final String phone) {
        String tokenId = FirebaseInstanceId.getInstance().getId();
        new MessageServer().execute(createURL(RPC_REGISTER,
                "?" + PARAM_EMAIL + "=" + email
                + "&" + PARAM_PASSWORD + "=" + passwordHash
                + "&" + PARAM_PHONE_NUMBER + "=" + phone
                + "&" + PARAM_APP_TOKEN + "=" + tokenId));
    }

    /**
     * Calls the login function on the server.
     *
     * @param email the email of the user.
     * @param passwordHash the hashed password of the user.
     */
    // TODO get to return if login is correct.
    public void checkLogin(final String email, final String passwordHash) {
        new MessageServer().execute(createURL(RPC_LOGIN,
                "?" + PARAM_EMAIL + "+" + "=" + email
                + "&" + PARAM_PASSWORD + "=" + passwordHash));
    }

    /**
     * Calls the add ally function on the server.
     *
     * @param email the email of the current user.
     * @param name the name for the new ally.
     * @param myCode the generated code of the current user.
     * @param theirCode the generated code of the user they wish to add.
     */
    public void addAlly(final String email, final String name, final String myCode, final String theirCode) {
        new MessageServer().execute(createURL(RPC_ADD_ALLY,
                "?" + PARAM_EMAIL + "=" + email + "&" + PARAM_NAME + "=" + name
                + "&" + PARAM_MY_CODE + "=" + myCode.toUpperCase()
                + "&" + PARAM_FRIEND_CODE + "=" + theirCode.toUpperCase()));
    }

    /** TODO add the description of this method.
     *
     * @param email
     * @param token
     */
    public void updateToken(final String email, final String token) {
        new MessageServer().execute(createURL(RPC_UPDATE_TOKEN,
                "?" + PARAM_EMAIL + "=" + email
                + "&" + PARAM_APP_TOKEN + "=" + token));
    }

    /**
     * Calls the get allies function on the server.
     * Used to return the list of allies that the user (@email) has associated with themselves.
     *
     * @param email used to identify the current user.
     * @param alliesScreen the screen to be return the list of allies to.
     */
    public void getAllies(final String email, final AlliesScreen alliesScreen) {
        new ObjectRetriever() {
            protected void onPostExecute(final JSONArray received) {
                List<Ally> allies = new ArrayList<>();
                if (received != null) {
                    try {
                        for (int i = 0; i < received.length(); i++) {
                            JSONObject jsonAlly = received.getJSONObject(i);
                            // TODO change to take the _id instead of the email
                            // TODO change to set the name as the name in local store, set when adding ally.
                            Ally ally = new Ally(jsonAlly.getString("email"), TLState.valueOf(jsonAlly.getString("state")), jsonAlly.getString("name"));
                            allies.add(ally);
                        }
                        alliesScreen.receiveAllyList(allies);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.execute(createURL(RPC_ALLY_LIST,
                "?" + PARAM_EMAIL + "=" + email));
    }

    /**
     * The class MessageServer is an asynchronous task that sends a message to the server.
     */
    private class MessageServer extends AsyncTask<URL, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(final URL... urls) {
            URL url = urls[0];
            try {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int rc = connection.getResponseCode();
                // TODO replace these magic numbers
                return (rc >= 200 && rc < 300);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean out) {
            result = out;
        }
    }

    /**
     * The ObjectRetriever class is an asyncronus task that sends a message to the server expecting a reply.
     * To use, the protected void onPostExecute(final JSONArray received) method must be overwritten.
     */
    private class ObjectRetriever extends AsyncTask<URL, Integer, JSONArray> {
        @Override
        protected JSONArray doInBackground(final URL... urls) {
            URL url = urls[0];
            String output;
            try {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                output = readFromStream(inputStream);
                return new JSONArray(output);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        /** TODO description to be written
         *
         * @param in
         * @return
         * @throws IOException
         */
        private String readFromStream(final InputStream in) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();
        }
    }

    /**
     * Creates a formatted URL with which to poke the server.
     *
     * @param rpcName the name of the method you are trying to call.
     * @param variables the list of vairables and their values in the format: "?<1stVariable>=<value1>&<anotherVariable1>=<value2>&<anotherVariable2>=<value3>"
     * @return the formatted URL.
     */
    private URL createURL(final String rpcName, final String variables) {
        try {
            return new URL("http://" + SERVER + ":" + PORT + "/" + rpcName + variables);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
