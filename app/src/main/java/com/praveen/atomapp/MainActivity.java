package com.praveen.atomapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity{

    FirebaseAuth mAuth;

    private MaterialButton logout_btn;
    private DatabaseReference mDatabase;
    String uid;
    String user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialiseFields();
        clickListeners();

    }

    // Initialize the Fields
    private void initialiseFields(){
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        logout_btn = findViewById(R.id.logout);
    }

    // Implements Click Listener
    private void clickListeners(){
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startLoginActivity();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser  = mAuth.getCurrentUser();

        if(currentUser==null){
            startLoginActivity();
        }else{
            updateActivity(currentUser);
            if (user_name==null||user_name.equals("")||user_name.equals("null")){
                startRegisterActivity();
            }
        }
    }


    private void updateActivity(FirebaseUser user) {
        uid = user.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(uid);
        databaseReference.child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_name = String.valueOf(dataSnapshot.getValue());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if (user_name==null||user_name.equals("")||user_name.equals("null")){
            Intent intent = getIntent();
            user_name = intent.getStringExtra("uname");
        }
        Log.d("TT2","Username : " + user_name);
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mDatabase.child(uid).child("name").setValue(user_name);

    }

    public void startLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void startRegisterActivity(){
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
        finish();
    }


}