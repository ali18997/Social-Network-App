package com.sketchat.ali.sketchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SetServerActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText mIP;
    private Button mSavebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_server);

        mToolbar = (Toolbar) findViewById(R.id.setServer_appBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Connection Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        mIP = (EditText) findViewById(R.id.setServer_input);
        mSavebtn = (Button) findViewById(R.id.setServer_save_button);

        mSavebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Client.SERVERIP = mIP.getText().toString();
                Intent startIntent = new Intent(SetServerActivity.this, ClientActivity.class);
                startActivity(startIntent);
                finish();
            }
        });


    }
}
