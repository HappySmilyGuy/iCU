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
import com.example.sam.beseen.server.ServerCaller;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * The AddAlly class implements the add ally activity.
 *
 * @author Sam
 * @version 1.0
 * @since 04-10-16
 */
public class AddAlly extends AppCompatActivity {

    private static SecureRandom random = new SecureRandom();
    private static String userCode;
    private static String allyName;
    private final ServerCaller serverCaller = ServerCaller.getInstance();
    private static final int RANDOM_INT_SIZE = 32;

    /**
     * A button listener to receive when the user has selected to try and add a new ally.
     */
    private Button.OnClickListener buttonListener =
            new ImageButton.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    String allyCode = ((EditText) findViewById(R.id.allyCode)).getText().toString();
                    allyName = ((EditText) findViewById(R.id.enterAllyName)).getText().toString();
                    String username = UserInfo.getUsername(getApplicationContext());

                    StringBuilder errorBuilder = new StringBuilder();
                    if (!checksOk(allyName, allyCode, username, errorBuilder)) {
                        TextView errorMessage = (TextView) findViewById(R.id.errorMessage);
                        errorMessage.setText(errorBuilder.toString());
                        errorMessage.setVisibility(View.VISIBLE);
                        return;
                    }

                    serverCaller.addAlly(username, allyName, userCode, allyCode);
                    Intent goToAlliesScreenPage = new Intent(getApplicationContext(), AlliesScreen.class);
                    startActivity(goToAlliesScreenPage);
                    // TODO maybe should go to a new page that processes the new user, until the message is received, then moves to AlliesScreen
                }
            };

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ally);
        userCode = generateCode();
        TextView t = (TextView) findViewById(R.id.userCode);
        t.setText(userCode);

        final Button button = (Button) findViewById(R.id.addAllyButton);
        button.setOnClickListener(buttonListener);
    }

    /**
     * Generates a random code for the adding ally handshake.
     *
     * @return a random code.
     */
    public final String generateCode() {
        return new BigInteger(RANDOM_INT_SIZE, random).toString(RANDOM_INT_SIZE).toUpperCase();
    }

    /**
     * A receiver function to retrieve the new ally's ID.
     *
     * @param newAllyID the ID returned from the database
     */
    public final void successfullyAddedAlly(final String newAllyID) {
        // TODO, allyName could change between sending the message and receiving the result.
        UserInfo.saveIDNamePair(getApplicationContext(), newAllyID, allyName);
    }

    /**
     * Checks if userCode and allyCode are set and username exists.
     *
     * @param givenName the name that the user gives for their new ally.
     * @param allyCode the allyCode to check
     * @param username the username to check.
     * @param errorBuilder the description of the error.
     * @return if the checks were all passed.
     */
    public final Boolean checksOk(final String givenName, final String allyCode, final String username, final StringBuilder errorBuilder) {
        errorBuilder.append(getResources().getString(R.string.error_prefix));
        if (givenName == null || "".equals(givenName)) {
            errorBuilder.append(getResources().getString(R.string.error_no_name));
            return false;
        }
        if (userCode == null || "".equals(userCode)) {
            errorBuilder.append(getResources().getString(R.string.error_no_usercode));
            return false;
        }
        if (allyCode == null || "".equals(allyCode)) {
            errorBuilder.append(getResources().getString(R.string.error_no_allycode));
            return false;
        }
        if (username == null || "".equals(username)) {
            errorBuilder.append(getResources().getString(R.string.error_no_username_set));
            return false;
        }
        return true;
    }
}
