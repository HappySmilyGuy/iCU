package com.example.sam.beseen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.sam.beseen.server.HasherFunction;
import com.example.sam.beseen.server.ServerCaller;

public class Login extends AppCompatActivity {

    private static final String LOCAL_DATA = "LocalDataStore";
    private final ServerCaller serverCaller = ServerCaller.getInstance();

    Button.OnClickListener loginButtonListener =
            new ImageButton.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String email = ((EditText) findViewById(R.id.emailAddress)).getText().toString();
                        String password = ((EditText) findViewById(R.id.enterPassword)).getText().toString();

                        TextView errorMessage = (TextView) findViewById(R.id.errorMessage);
                        if (email.equals("")) {
                            errorMessage.setText(R.string.error_prefix + R.string.error_no_email);
                            return;
                        }
                        if (password.equals("")) {
                            errorMessage.setText(R.string.error_prefix + R.string.error_no_password);
                            return;
                        }
                        serverCaller.check_login(email, HasherFunction.hash(password));
                        if (!serverCaller.result /*TODO change login check failed*/) {
                            errorMessage.setText(R.string.error_prefix + R.string.error_incorrect_login);
                            Button registerButton = (Button) findViewById(R.id.goToRegistrationButton);
                            registerButton.setVisibility(View.VISIBLE);
                            return;
                        }

                        saveUsername(email);
                        Intent goToStatusPage = new Intent(getApplicationContext(), Status.class);
                        startActivity(goToStatusPage);
                    } catch (NullPointerException e){
                        Log.d("loginButtonListener", "nullPointerException: " + e.toString());
                    }
                }
            };

    Button.OnClickListener goToRegisterButtonListener =
            new ImageButton.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent goToRegisterPage = new Intent(getApplicationContext(), Registration.class);
                    startActivity(goToRegisterPage);
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(loginButtonListener);

        final Button registerButton = (Button) findViewById(R.id.goToRegistrationButton);
        registerButton.setOnClickListener(goToRegisterButtonListener);
    }

    public void saveUsername(String userName) {
        SharedPreferences.Editor editor = getSharedPreferences(LOCAL_DATA, MODE_PRIVATE).edit();
        editor.putString("username", userName);
        editor.apply();
    }

}
