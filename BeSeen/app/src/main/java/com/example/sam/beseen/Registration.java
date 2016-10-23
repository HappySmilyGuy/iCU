package com.example.sam.beseen;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import com.example.sam.beseen.server.ServerCaller;

public class Registration extends AppCompatActivity {

    String email;
    String password;
    String password2;
    String phone;
    private static final String LOCAL_DATA = "LocalDataStore";
    private final ServerCaller serverCaller = ServerCaller.getInstance();

    Button.OnClickListener buttonListener =
            new ImageButton.OnClickListener() {
                @Override
                public void onClick(View v) {
                    email = ((EditText)findViewById(R.id.enterAllyName)).getText().toString();
                    password = ((EditText)findViewById(R.id.enterPassword)).getText().toString();
                    password2 = ((EditText)findViewById(R.id.reenterPassword)).getText().toString();
                    phone = ((EditText)findViewById(R.id.phoneNumber)).getText().toString();

                    if(email != null && password.equals(password2) && phone != null) {
                        serverCaller.register(email, password, phone);
                        saveUsername(email);
                    } else {

                    }
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        final Button button = (Button) findViewById(R.id.registerButton);
        button.setOnClickListener(buttonListener);
    }

    //callback () {
    //  saveUsername(username)
    //}

    public void saveUsername(String userName) {
        SharedPreferences.Editor editor = getSharedPreferences(LOCAL_DATA, MODE_PRIVATE).edit();
        editor.putString("username", userName);
        editor.apply();
    }

}
