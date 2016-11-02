package com.example.sam.beseen;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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
import com.example.sam.beseen.server.ServerCaller;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.generateViewId;

public class AlliesScreen extends AppCompatActivity {

    final int sdk = android.os.Build.VERSION.SDK_INT;
    private static final String LOCAL_DATA = "LocalDataStore";
    private final ServerCaller serverCaller = ServerCaller.getInstance();
    private static List<Ally> allies = new ArrayList<>();

    ImageButton.OnClickListener imageButtonListener =
            new ImageButton.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent goToAddAllyPage = new Intent(getApplicationContext(), AddAlly.class);
                    startActivity(goToAddAllyPage);
                }
            };

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allies_screen);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout parent = (RelativeLayout) inflater.inflate(R.layout.content_allies_screen,
                null);

        serverCaller.getAllies(getSharedPreferences(LOCAL_DATA, MODE_PRIVATE).getString("username", null), this);

        // TODO to be replaced by information from the server
        allies.add(new Ally("1", TLState.GREEN, "Tom"));
        allies.add(new Ally("2", TLState.YELLOW, "Dick"));
        allies.add(new Ally("3", TLState.RED, "Harry"));
        allies.add(new Ally("4", TLState.UNSET, "Charles"));

        int previousViewId = R.id.allyTitle;
        for (Ally ally : allies) {
            FrameLayout allyView = (FrameLayout) inflater.inflate(R.layout.ally_template, null);

            positionAllyView(allyView, previousViewId);
            setAllyViewId(allyView);
            previousViewId = allyView.getId();

            setAllyViewColourByState(allyView, ally.getState());

            ((TextView) allyView.findViewById(R.id.AllyNameTemplate)).setText(ally.getGivenName());

            parent.addView(allyView);
        }

        setContentView(parent);

        final ImageButton button = (ImageButton) findViewById(R.id.addAllyButton);

        button.setOnClickListener(imageButtonListener);
    }

    public static void receiveAllyList(List<Ally> allies_from_server) {
        // do something with allies
        allies = allies_from_server;
        // TODO refresh screen.
    }

    private void positionAllyView(FrameLayout allyView, int previousViewId){
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.BELOW, previousViewId);
        allyView.setLayoutParams(lp);
    }

    private void setAllyViewId(FrameLayout allyView){
        if (sdk >= 17) {
            allyView.setId(generateViewId());
        } else {
            int j = 0;
            while (findViewById(j) != null) {
                ++j;
            }
            allyView.setId(j);
        }
    }

    private void setAllyViewColourByState(FrameLayout allyView, TLState state)
    {
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

}
