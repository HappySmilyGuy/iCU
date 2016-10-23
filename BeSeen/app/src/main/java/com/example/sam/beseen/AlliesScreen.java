package com.example.sam.beseen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AlliesScreen extends AppCompatActivity {

    ImageButton.OnClickListener imageButtonListener =
            new ImageButton.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent goToAddAllyPage = new Intent(getApplicationContext(), AddAlly.class);
                    startActivity(goToAddAllyPage);
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allies_screen);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout parent = (LinearLayout) inflater.inflate(R.layout.content_allies_screen,
                null);


        for (int i = 0; i < 3; i++) {
            FrameLayout custom = (FrameLayout) inflater.inflate(R.layout.ally_template, null);
            TextView tv = (TextView) custom.findViewById(R.id.AllyNameTemplate);
            tv.setText("Custom View " + i);
            parent.addView(custom);
        }

        setContentView(parent);

        final ImageButton button = (ImageButton) findViewById(R.id.registerButton);

        button.setOnClickListener(imageButtonListener);

    }

}
