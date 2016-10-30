package com.example.sam.beseen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import android.widget.ImageButton;

import com.example.sam.beseen.dataobjects.TLState;
import com.example.sam.beseen.server.ServerCaller;

public class Status extends AppCompatActivity {
    private static final String LOCAL_DATA = "LocalDataStore";
    private final ServerCaller serverCaller = ServerCaller.getInstance();

    ImageButton.OnClickListener redLightButtonListener =
            new ImageButton.OnClickListener(){
                @Override
                public void onClick(View v) {
                    setLights(TLState.RED);
                    SharedPreferences preferences = getSharedPreferences(LOCAL_DATA, MODE_PRIVATE);
                    String username = preferences.getString("username", null);
                    serverCaller.changeState(username, TLState.RED);
                    saveState(TLState.RED);
                }
            };
    ImageButton.OnClickListener yellowLightButtonListener =
            new ImageButton.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setLights(TLState.YELLOW);
                    SharedPreferences preferences = getSharedPreferences(LOCAL_DATA, MODE_PRIVATE);
                    String username = preferences.getString("username", null);
                    serverCaller.changeState(username, TLState.YELLOW);
                    saveState(TLState.YELLOW);
                }
            };

    ImageButton.OnClickListener greenLightButtonListener =
            new ImageButton.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setLights(TLState.GREEN);
                    SharedPreferences preferences = getSharedPreferences(LOCAL_DATA, MODE_PRIVATE);
                    String username = preferences.getString("username", null);
                    serverCaller.changeState(username, TLState.GREEN);
                    saveState(TLState.GREEN);
                }
            };

    ImageButton.OnClickListener nextPageButtonListener =
            new ImageButton.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent goToAlliesScreenPage = new Intent(getApplicationContext(), AlliesScreen.class);
                    startActivity(goToAlliesScreenPage);
                }
            };

    public void setLights(TLState state) {
        ImageButton redLightButton = (ImageButton) findViewById(R.id.redLightButton);
        ImageButton yellowLightButton = (ImageButton) findViewById(R.id.yellowLightButton);
        ImageButton greenLightButton = (ImageButton) findViewById(R.id.greenLightButton);

        redLightButton.setImageResource(R.drawable.red_off);
        yellowLightButton.setImageResource(R.drawable.amber_off);
        greenLightButton.setImageResource(R.drawable.green_off);

        switch (state) {
            case GREEN:
                greenLightButton.setImageResource(R.drawable.green_on);
                break;
            case YELLOW:
                yellowLightButton.setImageResource(R.drawable.amber_on);
                break;
            case RED:
                redLightButton.setImageResource(R.drawable.red_on);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences(LOCAL_DATA, MODE_PRIVATE);
        String username = preferences.getString("username", null);

        if(username == null) {
            Intent goToRegistrationPage = new Intent(getApplicationContext(), Registration.class);
            startActivity(goToRegistrationPage);
        }

        setContentView(R.layout.fragment_status);

        try {
            final ImageButton redLightButton = (ImageButton) findViewById(R.id.redLightButton);
            redLightButton.setOnClickListener(redLightButtonListener);

            final ImageButton yellowLightButton = (ImageButton) findViewById(R.id.yellowLightButton);
            yellowLightButton.setOnClickListener(yellowLightButtonListener);

            final ImageButton greenLightButton = (ImageButton) findViewById(R.id.greenLightButton);
            greenLightButton.setOnClickListener(greenLightButtonListener);
        }
        catch(NullPointerException e){
        }

        final ImageButton button = (ImageButton) findViewById(R.id.nextPage);
        button.setOnClickListener(nextPageButtonListener);

        String stateString =  preferences.getString("state", null);
        if(stateString != null) {
            setLights(TLState.valueOf(stateString));
        }
        else {
            setLights(TLState.UNSET);
        }
    }

    public void saveState(TLState state) {
        SharedPreferences.Editor editor = getSharedPreferences(LOCAL_DATA, MODE_PRIVATE).edit();
        editor.putString("state", state.toString());
        editor.apply();
    }
}
