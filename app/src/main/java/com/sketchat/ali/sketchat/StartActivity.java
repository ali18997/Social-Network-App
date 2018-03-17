package com.sketchat.ali.sketchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by ALI on 11/27/2017.
 */

public class StartActivity extends AppCompatActivity {

    private Button mRegBtn;
    private Button mLogBtn;
    private Button mP2PBtn;
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mRegBtn = (Button) findViewById(R.id.start_reg_button);

        mLogBtn = (Button) findViewById(R.id.start_log_btn);

        mP2PBtn = (Button) findViewById(R.id.start_p2p_btn);

        mRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reg_intent = new Intent(StartActivity.this, RegisterActivity.class);
                startActivity(reg_intent);
            }
        });

        mLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reg_intent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(reg_intent);
            }
        });

        mP2PBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reg_intent = new Intent(StartActivity.this, ClientActivity.class);
                startActivity(reg_intent);
            }
        });

    }
}

