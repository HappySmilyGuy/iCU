package com.example.sam.beseen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.sam.beseen.server.ServerCaller;

import java.math.BigInteger;
import java.security.SecureRandom;

public class AddAlly extends AppCompatActivity {

    private SecureRandom random = new SecureRandom();
    String userCode;
    String allyCode;
    String allyName;
    private static final String LOCAL_DATA = "LocalDataStore";
    private final ServerCaller serverCaller = ServerCaller.getInstance();


    Button.OnClickListener buttonListener =
            new ImageButton.OnClickListener() {
                @Override
                public void onClick(View v) {
                    allyCode = ((EditText)findViewById(R.id.allyCode)).getText().toString();
                    allyName = ((EditText)findViewById(R.id.enterAllyName)).getText().toString();
                    if(userCode != null && allyCode != null) {
                        String username = getSharedPreferences(LOCAL_DATA, MODE_PRIVATE).getString("username", null);
                        if (username != null) {
                            serverCaller.addAlly(username, userCode, allyCode);
                            Intent goToAlliesScreenPage = new Intent(getApplicationContext(), AlliesScreen.class);
                            startActivity(goToAlliesScreenPage);
                        }
                    } else {

                    }
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ally);
        userCode = generateCode();
        TextView t = (TextView)findViewById(R.id.userCode);
        t.setText(userCode);

        final Button button = (Button) findViewById(R.id.registerButton);
        button.setOnClickListener(buttonListener);
    }

    public String generateCode() {
        return new BigInteger(32, random).toString(32).toUpperCase();
    }

    //callback () {
    // saveEmailNamePair(email, allyName);
    //}

    public void saveEmailNamePair(String email, String name) {
        SharedPreferences.Editor editor = getSharedPreferences(LOCAL_DATA, MODE_PRIVATE).edit();
        editor.putString(email, name);
        editor.apply();
    }

}
