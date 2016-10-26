package com.example.sam.beseen.server;

import android.os.AsyncTask;

import android.util.Log;
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
 * Created by Eddie on 22-Oct-16.
 */

public class ServerCaller{
    private final static String SERVER = "http://ec2-35-161-96-38.us-west-2.compute.amazonaws.com/";
    private final static String PORT = "80";
    private final static String RPC_CHANGE_STATE = "changeState";
    private final static String PARAM_EMAIL = "email";
    private final static String PARAM_STATE = "state";
    private final static String RPC_REGISTER = "register";
    private final static String PARAM_PASSWORD = "passwordHash";
    private final static String PARAM_PHONE_NUMBER = "phoneNumber";
    private final static String PARAM_APP_TOKEN = "token";
    private final static String RPC_ADD_ALLY = "addAlly";
    private final static String PARAM_MY_CODE = "myCode";
    private final static String PARAM_FRIEND_CODE = "allyCode";
    private final static String RPC_UPDATE_TOKEN = "updateToken";
    private final static String RPC_ALLY_LIST = "getAllyList";

    private static ServerCaller instance = null;
    private AlliesScreen alliesScreen = null;

    public void changeState(String email, TLState state){
        new messageServer().execute(createURL(RPC_CHANGE_STATE, "?" + PARAM_EMAIL + "=" + email
                                    + "&" + PARAM_STATE + "=" + state));
    }
    public void register(String email, String password, String phone){
        String tokenId = FirebaseInstanceId.getInstance().getId();
        new messageServer().execute(createURL(RPC_REGISTER,
                "?" + PARAM_EMAIL + "=" + email
                + "&" + PARAM_PASSWORD + "=" + password
                + "&" + PARAM_PHONE_NUMBER + "=" + phone
                + "&" + PARAM_APP_TOKEN + "=" + tokenId));
    }
    public void addAlly(String email, String myCode, String theirCode){
        new messageServer().execute(createURL(RPC_ADD_ALLY,
                "?" + PARAM_EMAIL + "=" + email
                + "&" + PARAM_MY_CODE + "=" + myCode.toUpperCase()
                + "&" + PARAM_FRIEND_CODE + "=" + theirCode.toUpperCase()));
    }

    public void updateToken(String email, String token) {
        new messageServer().execute(createURL(RPC_UPDATE_TOKEN,
                "?" + PARAM_EMAIL + "=" + email
                + "&" + PARAM_APP_TOKEN + "=" + token));
    }

    public void getAllies(String email, AlliesScreen alliesScreen) {
        this.alliesScreen = alliesScreen;
        new objectRetriever().execute(createURL(RPC_ALLY_LIST,
                "?" + PARAM_EMAIL + "=" + email));
    }

    private class messageServer extends AsyncTask<URL, Integer, Boolean>
    {
        @Override
        protected Boolean doInBackground(URL... urls) {
            URL url = urls[0];
            try
            {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int rc = connection.getResponseCode();
                return (rc >= 200 && rc < 300);
            }
            catch (MalformedURLException e){
                return false;
            }
            catch (IOException e){
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result) {

            }
        }
    }

    private class objectRetriever extends AsyncTask<URL, Integer, JSONArray>
    {
        @Override
        protected JSONArray doInBackground(URL... urls) {
            URL url = urls[0];
            String result;
            try
            {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                result = readFromStream(inputStream);
                return new JSONArray(result);
            }
            catch (MalformedURLException e){
                return null;
            }
            catch (IOException e){
                return null;
            }
            catch (JSONException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONArray result) {
            List<Ally> allies = new ArrayList<>();
            if(result != null) {
                try {
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject jsonAlly = result.getJSONObject(i);
                        Ally ally = new Ally(jsonAlly.getString("email"), TLState.valueOf(jsonAlly.getString("state")));
                        allies.add(ally);
                    }
                    alliesScreen.receiveAllyList(allies);
                }
                catch (JSONException e) {
                    Log.d("Ahhhhhhhhhhhhh", e.getStackTrace().toString());
                }
            }
        }

        private String readFromStream(InputStream in) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();
        }
    }

    private URL createURL(String rpc_name, String variables)
    {
        try{
            URL output = new URL("http://" + SERVER + ":" + PORT + "/" + rpc_name + variables);
            return output;
        }
        catch (MalformedURLException e)
        {
            return null;
        }
    }

    public static ServerCaller getInstance() {
        if (instance != null) {
            return instance;
        }
        return new ServerCaller();
    }
}
