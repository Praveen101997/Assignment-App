package com.praveen.atomapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class RegistrationActivity extends AppCompatActivity{

    String uname;
    private EditText person_name;

    private MaterialButton goto_home_btn;
    private ImageButton back_btn;

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
            Intent main_intent = new Intent(getApplicationContext(), MainActivity.class);
            main_intent.putExtra("uname", uname);
            startActivity(main_intent);
            finish();
        }
    }

    private void startLoginActivity(){
        Intent login_intent = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(login_intent);
        finish();
    }
}