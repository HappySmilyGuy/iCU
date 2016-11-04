package com.example.sam.beseen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.sam.beseen.dataobjects.UserInfo;
import com.example.sam.beseen.server.HasherFunction;
import com.example.sam.beseen.server.ServerCaller;

/**
 * The Login class implements the login activity.
 * It checks the input credentials and if correct sends the user to the Status activity.
 *
 * @author Eddie
 * @version 1.0
 * @since 03-11-16
 *
 * */
public class Login extends AppCompatActivity {


    private final ServerCaller serverCaller = ServerCaller.getInstance();

    /**
     * A button listener for logging in.
     * Checks credentials are correctly formatted and match a login in the database.
     * */
    private Button.OnClickListener loginButtonListener =
            new ImageButton.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    try {
                        String email = ((EditText) findViewById(R.id.emailAddress)).getText().toString();
                        String password = ((EditText) findViewById(R.id.enterPassword)).getText().toString();
                        TextView errorMessage = (TextView) findViewById(R.id.errorMessage);

                        StringBuilder errorBuilder = new StringBuilder();
                        errorBuilder.append(getResources().getString(R.string.error_prefix));
                        if (!inputsOk(email, password, errorBuilder)) {
                            errorMessage.setText(errorBuilder.toString());
                            return;
                        }
                        serverCaller.checkLogin(email, HasherFunction.hash(password));
                        if (!serverCaller.getResult() /*TODO change login check failed*/) {
                            errorBuilder.append(getResources().getString(R.string.error_incorrect_login));
                            errorMessage.setText(errorBuilder.toString());
                            // display the Register button
                            Button registerButton = (Button) findViewById(R.id.goToRegistrationButton);
                            registerButton.setVisibility(View.VISIBLE);
                            return;
                        }

                        UserInfo.saveUsername(getApplicationContext(), email);
                        Intent goToStatusPage = new Intent(getApplicationContext(), Status.class);
                        startActivity(goToStatusPage);
                    } catch (NullPointerException e) {
                        Log.d("loginButtonListener", "nullPointerException: " + e.toString());
                    }
                }
            };


    /** A button listener to send users to the Register activity. */
    private Button.OnClickListener goToRegisterButtonListener =
            new ImageButton.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    Intent goToRegisterPage = new Intent(getApplicationContext(), Registration.class);
                    startActivity(goToRegisterPage);
                }
            };

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(loginButtonListener);

        final Button registerButton = (Button) findViewById(R.id.goToRegistrationButton);
        registerButton.setOnClickListener(goToRegisterButtonListener);
    }

    /**
     * Checks the format of the email and password (does not check them against the database).
     * @param email the  email to check
     * @param password the password to check
     * @param errorMessage contains a description of the errors detected (will be blank if no errors).
     * @return false if email or password are incorrectly formatted, true otherwise
     * */
    // TODO check if the StringBuilder works
    private Boolean inputsOk(final String email, final String password, final StringBuilder errorMessage) {

        if ("".equals(email)) {
            errorMessage.append(getResources().getString(R.string.error_no_email));
            return false;
        }
        if ("".equals(password)) {
            errorMessage.append(getResources().getString(R.string.error_no_password));
            return false;
        }
        return true;
    }
}
