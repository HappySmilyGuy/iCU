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

    private ImageButton yellowLightButton;
    private ImageButton greenLightButton;
    private ImageButton redLightButton;

    private static final String LOCAL_DATA = "LocalDataStore";

    ImageButton.OnClickListener redLightButtonListener =
            new ImageButton.OnClickListener(){
                @Override
                public void onClick(View v) {
                    SharedPreferences preferences = getSharedPreferences(LOCAL_DATA, MODE_PRIVATE);
                    String username = preferences.getString("username", null);
                    serverCaller.changeState(username, TLState.RED);
                    saveState(TLState.RED);
                    // TODO change all images to off and yellow to on.
                    setRed();
                }
            };
    ImageButton.OnClickListener yellowLightButtonListener =
            new ImageButton.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences preferences = getSharedPreferences(LOCAL_DATA, MODE_PRIVATE);
                    String username = preferences.getString("username", null);
                    serverCaller.changeState(username, TLState.YELLOW);
                    saveState(TLState.YELLOW);
                    // TODO change all images to off and yellow to on.

                    setYellow();
                }
            };

    ImageButton.OnClickListener greenLightButtonListener =
            new ImageButton.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences preferences = getSharedPreferences(LOCAL_DATA, MODE_PRIVATE);
                    String username = preferences.getString("username", null);
                    serverCaller.changeState(username, TLState.GREEN);
                    saveState(TLState.GREEN);
                    // TODO change all images to off and yellow to on.

                    setGreen();
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

    public void setRed() {
        yellowLightButton = (ImageButton) findViewById(R.id.yellowLightButton);
        greenLightButton = (ImageButton) findViewById(R.id.greenLightButton);
        redLightButton = (ImageButton) findViewById(R.id.redLightButton);

        yellowLightButton.setImageResource(R.drawable.amber_off_1hdpi);
        greenLightButton.setImageResource(R.drawable.green_off_1hdpi);
        redLightButton.setImageResource(R.drawable.red_on_1hdpi);
    }

    public void setYellow() {
        yellowLightButton = (ImageButton) findViewById(R.id.yellowLightButton);
        greenLightButton = (ImageButton) findViewById(R.id.greenLightButton);
        redLightButton = (ImageButton) findViewById(R.id.redLightButton);

        yellowLightButton.setImageResource(R.drawable.amber_on_1hdpi);
        greenLightButton.setImageResource(R.drawable.green_off_1hdpi);
        redLightButton.setImageResource(R.drawable.red_off_1hdpi);
    }

    public void setGreen() {
        yellowLightButton = (ImageButton) findViewById(R.id.yellowLightButton);
        greenLightButton = (ImageButton) findViewById(R.id.greenLightButton);
        redLightButton = (ImageButton) findViewById(R.id.redLightButton);

        yellowLightButton.setImageResource(R.drawable.amber_off_1hdpi);
        greenLightButton.setImageResource(R.drawable.green_on_1hdpi);
        redLightButton.setImageResource(R.drawable.red_off_1hdpi);
    }

    private final ServerCaller serverCaller = ServerCaller.getInstance();

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
            TLState state = TLState.valueOf(stateString);
            if(state.equals(TLState.RED)) {
                setRed();
            } else if (state.equals(TLState.YELLOW)) {
                setYellow();
            } else if (state.equals(TLState.GREEN)) {
                setGreen();
            }
        }

    }

    public void saveState(TLState state) {
        SharedPreferences.Editor editor = getSharedPreferences(LOCAL_DATA, MODE_PRIVATE).edit();
        editor.putString("state", state.toString());
        editor.apply();
    }
}
