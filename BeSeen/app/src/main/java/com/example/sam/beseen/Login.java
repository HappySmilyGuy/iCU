package com.example.sam.beseen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.sam.beseen.server.HasherFunction;
import com.example.sam.beseen.server.ServerCaller;

/**
 * Created by Eddie on 29-Oct-16.
 */

public class Login extends AppCompatActivity {
    private static final String LOCAL_DATA = "LocalDataStore";
    private final ServerCaller serverCaller = ServerCaller.getInstance();

    Button.OnClickListener buttonListener =
            new ImageButton.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = ((EditText)findViewById(R.id.emailAddress)).getText().toString();
                    String password = ((EditText)findViewById(R.id.enterPassword)).getText().toString();

                    serverCaller.check_login(email, HasherFunction.hash(password));
                    // TODO change to actually check if login is correct.
                    saveUsername(email);
                    Intent goToStatusPage = new Intent(getApplicationContext(), Status.class);
                    startActivity(goToStatusPage);
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        final Button button = (Button) findViewById(R.id.registerButton);
        button.setOnClickListener(buttonListener);
    }

    public void saveUsername(String userName) {
        SharedPreferences.Editor editor = getSharedPreferences(LOCAL_DATA, MODE_PRIVATE).edit();
        editor.putString("username", userName);
        editor.apply();
    }

}
