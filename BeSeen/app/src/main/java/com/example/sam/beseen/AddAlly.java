package com.example.sam.beseen;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.math.BigInteger;
import java.security.SecureRandom;

public class AddAlly extends AppCompatActivity {

    private SecureRandom random = new SecureRandom();
    String code = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ally);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        code = generateCode();
        TextView t=(TextView)findViewById(R.id.userCode);
        t.setText(code);
    }

    public String generateCode() {
        return new BigInteger(32, random).toString(32).toUpperCase();
    }

}
