package com.example.sam.beseen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static com.example.sam.beseen.R.drawable.layout_bg;

public class AlliesScreen extends AppCompatActivity {

    final int sdk = android.os.Build.VERSION.SDK_INT;

    ImageButton.OnClickListener imageButtonListener =
            new ImageButton.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent goToAddAllyPage = new Intent(getApplicationContext(), AddAlly.class);
                    startActivity(goToAddAllyPage);
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allies_screen);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout parent = (LinearLayout) inflater.inflate(R.layout.content_allies_screen,
                null);


        String[] names = {"Tom", "Dick", "Harry"};
        for (int i = 0; i < 3; i++) {
            FrameLayout custom = (FrameLayout) inflater.inflate(R.layout.ally_template, null);
            RelativeLayout rl = (RelativeLayout) custom.findViewById(R.id.AllyTemplate);

            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                if((i%3) == 0)
                    rl.setBackgroundDrawable( getResources().getDrawable(R.drawable.layout_bg) );
                else if ((i%3) == 1)
                    rl.setBackgroundDrawable( getResources().getDrawable(R.drawable.layout_red) );
                else
                    rl.setBackgroundDrawable( getResources().getDrawable(R.drawable.layout_yellow) );
            } else {
                if((i%3) == 0)
                    rl.setBackground( getResources().getDrawable(R.drawable.layout_bg) );
                else if ((i%3) == 1)
                    rl.setBackground( getResources().getDrawable(R.drawable.layout_red) );
                else
                    rl.setBackground( getResources().getDrawable(R.drawable.layout_yellow) );
            }
            TextView tv = (TextView) custom.findViewById(R.id.AllyNameTemplate);
            tv.setText(names[i]);
            parent.addView(custom);
        }

        setContentView(parent);

        final ImageButton button = (ImageButton) findViewById(R.id.registerButton);

        button.setOnClickListener(imageButtonListener);
    }

}
