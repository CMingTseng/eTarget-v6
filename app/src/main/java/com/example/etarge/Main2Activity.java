package com.example.etarge;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Main2Activity extends AppCompatActivity {
    Intent intent;
    Bundle bundle;
    TextView tv;
    String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        tv = (TextView) findViewById(R.id.user);
        intent = this.getIntent();
        user = intent.getStringExtra("user");
        tv.setText("歡迎"+user);


    }

    public void signin(View view) {
        intent = new Intent(this,SignIn.class);
        startActivity(intent);
    }

    public void setting(View view) {
        intent = new Intent(this,Setting2.class);
        intent.putExtra("user",user);
        startActivity(intent);
    }
}
