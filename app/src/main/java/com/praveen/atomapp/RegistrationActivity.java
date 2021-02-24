package com.praveen.atomapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegistrationActivity extends AppCompatActivity{

    final String TAG = "Registration Activity :";

    String uname;
    private EditText person_name;
    private MaterialButton goto_home_btn;
    private ImageButton back_btn;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private DatabaseReference mDatabase;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initialiseFields();

        clickListeners();

    }

    // Initialize the Fields
    private void initialiseFields(){
        person_name = findViewById(R.id.edit_txt_person_name);
        goto_home_btn = findViewById(R.id.goto_home_btn);
        back_btn = findViewById(R.id.backbtn);

        mAuth = FirebaseAuth.getInstance();
        currentUser  = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        final FirebaseUser user = currentUser;
        getUserDetail(user);

    }

    // Implements Click Listener
    private void clickListeners(){
        goto_home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMainActivity();

            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLoginActivity();
            }
        });
    }

    // Start Main Activity
    private void startMainActivity(){
        uname = person_name.getText().toString();
        if (uname.trim().equals("")){
            Toast.makeText(getApplicationContext(),"Please Enter User Name",Toast.LENGTH_SHORT).show();
        }else {
            Boolean check = stroreUnameInDatabase();
            if (!check){
                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
            }else {
                Intent main_intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(main_intent);
                finish();
            }
        }
    }

    // Store Username into Database
    private Boolean stroreUnameInDatabase(){
        final Boolean[] complete = {true};
        uid = currentUser.getUid();
        DatabaseReference uidRef = mDatabase.child(uid);
        uidRef.child("name").setValue(uname).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()){
                    complete[0] = false;
                }
            }
        });
        return  complete[0];
    }

    // Start Login Activity
    private void startLoginActivity(){
        Intent login_intent = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(login_intent);
        finish();
    }

    // Update the activity detail
    private void getUserDetail(FirebaseUser user) {
        uid = user.getUid();
        DatabaseReference uidRef = mDatabase.child(uid);
        getUserFromDatabase(uidRef);
        Log.d(TAG,"Username : " + uname);
    }

    private void getUserFromDatabase(DatabaseReference ref){

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if((dataSnapshot.exists()) && (dataSnapshot.hasChild("name") )){
                    String name = dataSnapshot.child("name").getValue().toString();
                    uname = name;
                    person_name.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}