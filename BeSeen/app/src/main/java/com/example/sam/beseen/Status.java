package com.example.sam.beseen;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import android.widget.ImageButton;

import com.example.sam.beseen.dataobjects.TLState;
import com.example.sam.beseen.server.ServerCaller;

public class Status extends AppCompatActivity {

    ImageButton.OnClickListener redLightButtonListener =
            new ImageButton.OnClickListener(){
                @Override
                public void onClick(View v) {
                    serverCaller.changeState("default@email.com", TLState.RED);
                    // TODO change all images to off and yellow to on.
                }
            };
    ImageButton.OnClickListener yellowLightButtonListener =
            new ImageButton.OnClickListener() {
                @Override
                public void onClick(View v) {
                    serverCaller.changeState("default@email.com", TLState.YELLOW);
                    // TODO change all images to off and yellow to on.
                }
            };

    ImageButton.OnClickListener greenLightButtonListener =
            new ImageButton.OnClickListener() {
                @Override
                public void onClick(View v) {
                    serverCaller.changeState("default@email.com", TLState.GREEN);
                    // TODO change all images to off and yellow to on.
                }
            };

    private final ServerCaller serverCaller = ServerCaller.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_status);

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
    }
}
