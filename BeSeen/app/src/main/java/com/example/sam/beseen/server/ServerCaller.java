package com.example.sam.beseen.server;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import com.example.sam.beseen.R;
import com.example.sam.beseen.dataobjects.TLState;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Eddie on 22-Oct-16.
 */

public class ServerCaller{
    Context context;

    public void changeState(String email, TLState state){
        Log.d("Eddie's messages", "changeState.start");
        messageServer(context.getResources().getString(R.string.rpc_change_state),
                "?" + context.getResources().getString(R.string.email_param) + "=" + email
                + "&" + context.getResources().getString(R.string.state_param) + "=" + state);
        Log.d("Eddie's messages", "changeState.end");
    }
    public void register(String email, String password, String phone){
        messageServer(context.getResources().getString(R.string.rpc_register),
                "?" + context.getResources().getString(R.string.email_param) + "=" + email
                + "&" + context.getResources().getString(R.string.password_param) + "=" + password
                + "&" + context.getResources().getString(R.string.phone_no_param) + "=" + phone);
    }
    public void addAlly(String email, String my_code, String their_code){
        messageServer(context.getResources().getString(R.string.rpc_add_ally),
                "?" + context.getResources().getString(R.string.email_param)
                + "&" + context.getResources().getString(R.string.my_code) + "=" + my_code
                + "&" + context.getResources().getString(R.string.friend_code) + "=" + their_code);
    }

    private void messageServer(String rpc_name, String variables)
    {
        try {
            URL url = new URL("http://" + context.getResources().getString(R.string.server)
                        + ":" + context.getResources().getString(R.string.port) + "/" + rpc_name
                        + variables);
            Log.d("Eddie's message", "start");

            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            Log.d("Eddie's message", "middle");
            connection.connect();
            Log.d("Eddie's message", "end");
        }
        catch (MalformedURLException e){
            Log.d("Eddie's exceptions", "messageServer MalfomedURLException");
        }
        catch (IOException e){
            Log.d("Eddie's exceptions", "messageServer IOException");
        }
        catch (Exception e)
        {
            Log.d("Eddie's exceptions", "messageServer Any Exception");
        }
    }

   // public class messageServer extends AsyncTask<URL, Integer, boolean>
   // {

   // }
}
