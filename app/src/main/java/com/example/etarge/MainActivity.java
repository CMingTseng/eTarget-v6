package com.example.etarge;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void signin(View view) {
        intent = new Intent(this,SignIn.class);
        startActivity(intent);
    }

    public void setting(View view) {
        intent = new Intent(this,Setting.class);
        startActivity(intent);
    }
}
