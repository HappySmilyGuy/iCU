package com.example.sam.beseen.server;

import android.os.AsyncTask;

import com.example.sam.beseen.dataobjects.TLState;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Eddie on 22-Oct-16.
 */

public class ServerCaller{
    private final static String SERVER = "25.82.18.242";
    private final static String PORT = "8080";
    private final static String RPC_CHANGE_STATE = "changeState";
    private final static String PARAM_EMAIL = "email";
    private final static String PARAM_STATE = "state";
    private final static String RPC_REGISTER = "register";
    private final static String PARAM_PASSWORD = "passwordHash";
    private final static String PARAM_PHONE_NUMBER = "phoneNumber";
    private final static String PARAM_APP_TOKEN = "token";
    private final static String RPC_ADD_ALLY = "addAlly";
    private final static String PARAM_MY_CODE = "my_code";
    private final static String PARAM_FRIEND_CODE = "friend_code";
    private final static String RPC_UPDATE_TOKEN = "updateToken";

    private static ServerCaller instance = null;

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
                + "&" + PARAM_MY_CODE + "=" + myCode
                + "&" + PARAM_FRIEND_CODE + "=" + theirCode));
    }

    public void updateToken(String email, String token) {
        new messageServer().execute(createURL(RPC_UPDATE_TOKEN,
                "?" + PARAM_EMAIL + "=" + email
                + "&" + PARAM_APP_TOKEN + "=" + token));
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
            }
            catch (MalformedURLException e){
                return false;
            }
            catch (IOException e){
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result) {

            }
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
