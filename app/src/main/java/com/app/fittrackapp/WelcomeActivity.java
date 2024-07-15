package com.app.fittrackapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {



    Button buttonLogin,buttonSigin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        buttonSigin = findViewById(R.id.buttonSigin);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent=new Intent(WelcomeActivity.this , Login2Activity.class);
                startActivity(intent);

            }
        });
        buttonSigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(WelcomeActivity.this , RegisterActivity.class);
                startActivity(intent);

            }
        });
    }
}