package com.example.sam.beseen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.ImageButton;

import com.example.sam.beseen.dataobjects.TLState;
import com.example.sam.beseen.dataobjects.UserInfo;
import com.example.sam.beseen.server.ServerCaller;

public class Status extends AppCompatActivity {
    private final ServerCaller serverCaller = ServerCaller.getInstance();

    /** A button listener for the red light button. */
    private ImageButton.OnClickListener redLightButtonListener =
            new ImageButton.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    setLights(TLState.RED);
                    serverCaller.changeState(UserInfo.getUsername(getApplicationContext()), TLState.RED);
                    UserInfo.saveState(getApplicationContext(), TLState.RED);
                }
            };
    /** A button listener for the yellow light button. */
    private ImageButton.OnClickListener yellowLightButtonListener =
            new ImageButton.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    setLights(TLState.YELLOW);
                    serverCaller.changeState(UserInfo.getUsername(getApplicationContext()), TLState.YELLOW);
                    UserInfo.saveState(getApplicationContext(), TLState.YELLOW);
                }
            };
    /** A button listener for the green light button. */
    private ImageButton.OnClickListener greenLightButtonListener =
            new ImageButton.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    setLights(TLState.GREEN);
                    serverCaller.changeState(UserInfo.getUsername(getApplicationContext()), TLState.GREEN);
                    UserInfo.saveState(getApplicationContext(), TLState.GREEN);
                }
            };
    /** A button listener for the next page button. */
    private ImageButton.OnClickListener nextPageButtonListener =
            new ImageButton.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    Intent goToAlliesScreenPage = new Intent(getApplicationContext(), AlliesScreen.class);
                    startActivity(goToAlliesScreenPage);
                }
            };

    /**
     * Sets the light images to all off except for the @state image, which is set to on.
     *
     * @param state the state to set the lights to display.
     * */
    public final void setLights(final TLState state) {
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
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (UserInfo.getUsername(getApplicationContext()) == null) {
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
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        final ImageButton button = (ImageButton) findViewById(R.id.nextPage);
        button.setOnClickListener(nextPageButtonListener);

        setLights(UserInfo.getState(getApplicationContext()));
    }
}
