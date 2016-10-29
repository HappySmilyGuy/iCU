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

public class Registration extends AppCompatActivity {

    private static final String LOCAL_DATA = "LocalDataStore";
    private final ServerCaller serverCaller = ServerCaller.getInstance();

    Button.OnClickListener registerButtonListener =
            new ImageButton.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = ((EditText)findViewById(R.id.enterAllyName)).getText().toString();
                    String password = ((EditText)findViewById(R.id.enterPassword)).getText().toString();
                    String password2 = ((EditText)findViewById(R.id.reenterPassword)).getText().toString();
                    String phone = ((EditText)findViewById(R.id.phoneNumber)).getText().toString();

                    if(!email.equals("") && password.equals(password2) && !phone.equals("")
                            && password.length() >= 5) {
                        serverCaller.register(email, HasherFunction.hash(password), phone);
                        saveUsername(email);
                        Intent goToStatusPage = new Intent(getApplicationContext(), Status.class);
                        startActivity(goToStatusPage);
                    } else {
                        // TODO react to failed register
                    }
                }
            };

    Button.OnClickListener toLoginButtonListener =
            new ImageButton.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent goToLoginPage = new Intent(getApplicationContext(), Login.class);
                    startActivity(goToLoginPage);
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        final Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(registerButtonListener);

        final Button toLoginButton = (Button) findViewById(R.id.goToLoginButton);
        toLoginButton.setOnClickListener(toLoginButtonListener);
    }

    public void saveUsername(String userName) {
        SharedPreferences.Editor editor = getSharedPreferences(LOCAL_DATA, MODE_PRIVATE).edit();
        editor.putString("username", userName);
        editor.apply();
    }

}
