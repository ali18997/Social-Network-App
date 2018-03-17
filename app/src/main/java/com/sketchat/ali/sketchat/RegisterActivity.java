package com.sketchat.ali.sketchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

/**
 * Created by ALI on 11/26/2017.
 */

public class RegisterActivity extends AppCompatActivity {

    private EditText name, email, password;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private Toolbar mToolbar;
    private ProgressDialog mRegProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mToolbar = (Toolbar) findViewById(R.id.register_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRegProgress = new ProgressDialog(this);


        name = (EditText) findViewById(R.id.editUsername);
        email = (EditText) findViewById(R.id.editEmail);
        password = (EditText) findViewById(R.id.editPassword);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    public void signupButtonClicked(View view){
        final String display_name, password_content, email_content;
        display_name = name.getText().toString().trim();
        password_content = password.getText().toString().trim();
        email_content=email.getText().toString().trim();
        if(!TextUtils.isEmpty(email_content) && !TextUtils.isEmpty(display_name) && !TextUtils.isEmpty(password_content)){
            mRegProgress.setTitle("Sign up in progress");
            mRegProgress.setMessage("This should be quick!");
            mRegProgress.show();
            mAuth.createUserWithEmailAndPassword(email_content, password_content).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();

                        String uid = current_user.getUid();
                        String current_user_id = mAuth.getCurrentUser().getUid();

                        String deviceToken = FirebaseInstanceId.getInstance().getToken();

                        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                        HashMap<String, String> userMap = new HashMap<>();
                        userMap.put("name", display_name);
                        userMap.put("status", "Hi there, I'm using Sketchat.");
                        userMap.put("image", "default");
                        userMap.put("thumb_image", "default");
                        userMap.put("device_token", deviceToken);

                        mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    mRegProgress.dismiss();
                                    Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(mainIntent);
                                    finish();
                                }
                            }
                        });
                    }
                    else {
                        mRegProgress.hide();
                        Toast.makeText(RegisterActivity.this, "Error while Signing Up!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

}
