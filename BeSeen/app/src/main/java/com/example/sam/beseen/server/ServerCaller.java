package com.example.sam.beseen.server;

import android.content.Context;

import com.example.sam.beseen.R;
import com.example.sam.beseen.dataobjects.TLState;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Eddie on 22-Oct-16.
 */

public class ServerCaller {
    Context context;

    public void changeState(String email, TLState state){
        messageServer(context.getResources().getString(R.string.rpc_change_state),
                "?" + context.getResources().getString(R.string.email_param) + "=" + email
                + "&" + context.getResources().getString(R.string.state_param) + "=" + state);
    }
    public void register(String email, String password, String phone){
        messageServer(context.getResources().getString(R.string.rpc_register),
                "?" + context.getResources().getString(R.string.email_param) + "=" + email
                + "&" + context.getResources().getString(R.string.password_param) + "=" + password
                + "&" + context.getResources().getString(R.string.phone_param) + "=" + phone);
    }
    public void addFriend(String my_code, String their_code){
        messageServer(context.getResources().getString(R.string.rpc_add_friend),
                "?" + context.getResources().getString(R.string.my_code) + "=" + my_code
                + "&" + context.getResources().getString(R.string.friend_code) + "=" + their_code);
    }

    private void messageServer(String rpc_name, String variables)
    {
        try {
            URL serverURL = new URL("http://" + context.getResources().getString(R.string.server)
                        + ":" + context.getResources().getString(R.string.port) + "/" + rpc_name
                        + variables);
            HttpURLConnection httpURLConnection = (HttpURLConnection)serverURL.openConnection();
            httpURLConnection.connect();
        }
        catch (MalformedURLException e){
            // new URL() failed
        }
        catch (IOException e){
            // openConnection() failed
        }

    }
}
