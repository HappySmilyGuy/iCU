package com.example.sam.beseen;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.sam.beseen.dataobjects.Ally;
import com.example.sam.beseen.dataobjects.TLState;
import com.example.sam.beseen.dataobjects.UserInfo;
import com.example.sam.beseen.server.ServerCaller;

import java.util.ArrayList;
import java.util.List;

/**
 * The AlliesScreen class implements the the Allies Screen activity.
 * It displays all the user's allies with their associated states.
 *
 * @author Eddie
 * @version 1.0
 * @since 04-11-16
 *
 * */
public class AlliesScreen extends AppCompatActivity {

    private final ServerCaller serverCaller = ServerCaller.getInstance();
    private static List<Ally> allies = new ArrayList<>();
    private LayoutInflater inflater;
    private RelativeLayout parent;

    /**
     * A button listener to send user to the AddAlly activity.
     */
    private ImageButton.OnClickListener imageButtonListener =
            new ImageButton.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    Intent goToAddAllyPage = new Intent(getApplicationContext(), AddAlly.class);
                    startActivity(goToAddAllyPage);
                }
            };

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allies_screen);

        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        parent = (RelativeLayout) inflater.inflate(R.layout.content_allies_screen,
                null);

        serverCaller.getAllies(UserInfo.getUsername(getApplicationContext()), this);

        setContentView(parent);

        final ImageButton button = (ImageButton) findViewById(R.id.addAllyButton);

        button.setOnClickListener(imageButtonListener);
    }

    /**
     * A receiver function to be called when the server returns the list of allies.
     *
     * @param alliesFromServer the list of allies received from the server.
     * */
    public void receiveAllyList(final List<Ally> alliesFromServer) {
        // do something with allies
        allies = alliesFromServer;
        displayAllies(parent, inflater);
    }

    /**
     * Positions @allyView under the View associated to @previousViewId.
     *
     * @param allyView the view to position.
     * @param previousViewId the ID of the view to position @allyView under.
     * */
    private void positionAllyView(final FrameLayout allyView, final int previousViewId) {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.BELOW, previousViewId);
        allyView.setLayoutParams(lp);
    }

    /**
     * Gives @view a unique ID.
     *
     * @param view the view which to give a unique ID to.
     */
    private void giveUniqueViewId(final FrameLayout view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            view.setId(view.generateViewId());
            return;
        }
        // TODO test if fallback actually works.
        int i = 0;
        while (findViewById(i) != null) {
            ++i;
        }
        view.setId(i);
    }

    /**
     * Sets the colour of @allyView to the colour associated with the @state.
     *
     * @param allyView the view to change colour of.
     * @param state the state of the associated ally.
     * */
    private void setAllyViewColourByState(final FrameLayout allyView, final TLState state) {
        int colour;
        switch (state) {
            case GREEN:
                colour = R.color.allyGreen;
                break;
            case YELLOW:
                colour = R.color.allyRed;
                break;
            case RED:
                colour = R.color.allyYellow;
                break;
            case UNSET:
            default:
                colour = R.color.allyGrey;
                break;
        }

        Drawable d = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ally_background_shape);
        d.setColorFilter(ResourcesCompat.getColor(getResources(), colour, null), PorterDuff.Mode.SRC_ATOP);

        RelativeLayout rl = (RelativeLayout) allyView.findViewById(R.id.AllyTemplate);
        rl.setBackground(d);
    }

    /**
     * Puts ally blocks on the screen.
     *
     * @param inflater the inflater
     * */
    private void displayAllies(final RelativeLayout parent, final LayoutInflater inflater) {
        int previousViewId = R.id.allyTitle;
        for (Ally ally : allies) {
            FrameLayout allyView = (FrameLayout) inflater.inflate(R.layout.ally_template, null);

            positionAllyView(allyView, previousViewId);
            giveUniqueViewId(allyView);
            previousViewId = allyView.getId();

            setAllyViewColourByState(allyView, ally.getState());

            ((TextView) allyView.findViewById(R.id.AllyNameTemplate)).setText(ally.getGivenName());

            parent.addView(allyView);
        }
    }

}
