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

    final String TAG = "Main Activity";

    //Firebase Auth
    FirebaseAuth mAuth;

    // Variable declaration
    private MaterialButton logout_btn;
    private DatabaseReference mDatabase;
    String uid;
    static String user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initilaize variables and fields
        initialiseFields();
        //Set Click Listener On necessary fields
        clickListeners();

    }

    // Initialize the Fields
    private void initialiseFields(){
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        logout_btn = findViewById(R.id.logout);
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
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

    // Start Login Activity
    public void startLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    // Start Register Activity
    public void startRegisterActivity(){
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser  = mAuth.getCurrentUser();

        if(currentUser==null){
            startLoginActivity();
        }else{
            getUserDetail(currentUser);
        }
    }


    // Update the activity detail
    private void getUserDetail(FirebaseUser user) {
        uid = user.getUid();
        DatabaseReference uidRef = mDatabase.child(uid);
        getUserFromDatabase(uidRef);
        Log.d(TAG,"Username : " + user_name);
    }

    // Get Detail from Realtime Database and do necessary Actions
    private void getUserFromDatabase(DatabaseReference ref){

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if((dataSnapshot.exists()) && (dataSnapshot.hasChild("name") )){
                    String name = dataSnapshot.child("name").getValue().toString();
                    Log.d("tt3",name);
                    user_name = name;
                    if (name==null||name.equals("")||name.equals("null")){
                        startRegisterActivity();
                    }
                }else{
                    startRegisterActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}