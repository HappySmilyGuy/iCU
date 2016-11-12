package com.example.sam.beseen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.sam.beseen.dataobjects.UserInfo;
import com.example.sam.beseen.server.HasherFunction;
import com.example.sam.beseen.server.ServerCaller;

/**
 * The Registration class implements the registration activity.
 * It checks the input credentials and if correctly formatted and not already present
 * in the database, it registers, logs in and sends the user to the Status activity.
 *
 * @author Eddie
 * @version 1.0
 * @since 04-11-16
 *
 * */
public class Registration extends AppCompatActivity {

    private static final String LOCAL_DATA = "LocalDataStore";
    private final ServerCaller serverCaller = ServerCaller.getInstance();
    private static final int PASSWORD_MIN_CHARS = 5;

    /**
     * A button listener for submitting the registration.
     * Checks formatting of inputs and checks if email address is already registered.
     */
    private Button.OnClickListener registerButtonListener =
            new ImageButton.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    String email = ((EditText) findViewById(R.id.enterEmail)).getText().toString();
                    String password = ((EditText) findViewById(R.id.enterPassword)).getText().toString();
                    String password2 = ((EditText) findViewById(R.id.reenterPassword)).getText().toString();
                    String phone = ((EditText) findViewById(R.id.phoneNumber)).getText().toString();

                    StringBuilder errorBuilder = new StringBuilder();
                    if (!checkInputs(email, password, password2, phone, errorBuilder)) {
                        TextView errorMessage = (TextView) findViewById(R.id.errorMessage);
                        errorMessage.setText(errorBuilder.toString());
                        errorMessage.setVisibility(View.VISIBLE);
                        return;
                    }

                    // TODO check if already registered information.
                    serverCaller.register(email, HasherFunction.hash(password), phone);
                    UserInfo.saveUsername(getApplicationContext(), email);
                    Intent goToStatusPage = new Intent(getApplicationContext(), Status.class);
                    startActivity(goToStatusPage);
                }
            };

    /**
     * A button listener to send users to the login screen.
     */
    private Button.OnClickListener toLoginButtonListener =
            new ImageButton.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    Intent goToLoginPage = new Intent(getApplicationContext(), Login.class);
                    startActivity(goToLoginPage);
                }
            };

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        final Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(registerButtonListener);

        final Button toLoginButton = (Button) findViewById(R.id.goToLoginButton);
        toLoginButton.setOnClickListener(toLoginButtonListener);
    }

    /**
     * a helper function to check the inputs are correctly formatted.
     *
     * @param email the desired email
     * @param password the desired password
     * @param password2 the repeat input of that password (checking for user error)
     * @param phoneNo the user's phone number
     * @param errorBuilder a description of the error if returns false.
     * @return whether all checks were passed.
     */
    private Boolean checkInputs(final String email, final String password,
                                final String password2, final String phoneNo,
                                final StringBuilder errorBuilder) {
        errorBuilder.append(getResources().getString(R.string.error_prefix));
        if ("".equals(email)) {
            errorBuilder.append(getResources().getString(R.string.error_no_email));
            return false;
        }
        if (!password.equals(password2)) {
            errorBuilder.append(getResources().getString(R.string.error_password_mismatch));
            return false;
        }
        if ("".equals(password)) {
            errorBuilder.append(getResources().getString(R.string.error_no_password));
            return false;
        }
        if (password.length() < PASSWORD_MIN_CHARS) {
            errorBuilder.append(getResources().getString(R.string.error_password_too_short));
            errorBuilder.append(String.format(getResources().getString(R.string.hint_password_length),
                                PASSWORD_MIN_CHARS));
            return false;
        }
        if ("".equals(phoneNo)) {
            errorBuilder.append(getResources().getString(R.string.error_no_phoneNo));
            return false;
        }
        return true;
    }

}
