package com.example.sam.beseen;

import android.content.Intent;
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

    ImageButton.OnClickListener redLightButtonListener =
            new ImageButton.OnClickListener(){
                @Override
                public void onClick(View v) {
                    serverCaller.changeState("default@email.com", TLState.RED);
                    // TODO change all images to off and yellow to on.

                    yellowLightButton = (ImageButton) findViewById(R.id.yellowLightButton);
                    greenLightButton = (ImageButton) findViewById(R.id.greenLightButton);
                    redLightButton = (ImageButton) findViewById(R.id.redLightButton);

                    yellowLightButton.setImageResource(R.drawable.amber_off_1hdpi);
                    greenLightButton.setImageResource(R.drawable.green_off_1hdpi);
                    redLightButton.setImageResource(R.drawable.red_on_1hdpi);
                }
            };
    ImageButton.OnClickListener yellowLightButtonListener =
            new ImageButton.OnClickListener() {
                @Override
                public void onClick(View v) {
                    serverCaller.changeState("default@email.com", TLState.YELLOW);
                    // TODO change all images to off and yellow to on.

                    yellowLightButton = (ImageButton) findViewById(R.id.yellowLightButton);
                    greenLightButton = (ImageButton) findViewById(R.id.greenLightButton);
                    redLightButton = (ImageButton) findViewById(R.id.redLightButton);

                    yellowLightButton.setImageResource(R.drawable.amber_on_1hdpi);
                    greenLightButton.setImageResource(R.drawable.green_off_1hdpi);
                    redLightButton.setImageResource(R.drawable.red_off_1hdpi);
                }
            };

    ImageButton.OnClickListener greenLightButtonListener =
            new ImageButton.OnClickListener() {
                @Override
                public void onClick(View v) {
                    serverCaller.changeState("default@email.com", TLState.GREEN);
                    // TODO change all images to off and yellow to on.

                    yellowLightButton = (ImageButton) findViewById(R.id.yellowLightButton);
                    greenLightButton = (ImageButton) findViewById(R.id.greenLightButton);
                    redLightButton = (ImageButton) findViewById(R.id.redLightButton);

                    yellowLightButton.setImageResource(R.drawable.amber_off_1hdpi);
                    greenLightButton.setImageResource(R.drawable.green_on_1hdpi);
                    redLightButton.setImageResource(R.drawable.red_off_1hdpi);
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

    private final ServerCaller serverCaller = ServerCaller.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
    }
}
