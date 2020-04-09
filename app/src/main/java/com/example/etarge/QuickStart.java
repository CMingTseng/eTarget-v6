package com.example.etarge;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class QuickStart extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    Button target,rule;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_start);

        fragmentManager = getSupportFragmentManager(); //管理fragment
        transaction = fragmentManager.beginTransaction(); //執行切換fragment
        TargetInf targetInf = new TargetInf();
        transaction.replace(R.id.fragment_container2,targetInf);
        transaction.commit();
        target = (Button) findViewById(R.id.tif);
        rule = (Button) findViewById(R.id.rule);


    }


    public void signin(View view) {
    }

    public void setting(View view) {
    }

    public void A1(View view) {

    }

    public void onClick(View view){
        transaction = fragmentManager.beginTransaction();
        switch (view.getId()){
            case R.id.tif:
                Log.e("log_tag_q", "ok");
                TargetInf targetInf = new TargetInf();
                transaction.replace(R.id.fragment_container2,targetInf);
                target.setEnabled(false);
                rule.setEnabled(true);
                break;
            case R.id.rule:
                Log.e("log_tag_q", "good");
                RuleSpinner ruleSpinner = new RuleSpinner();
                transaction.replace(R.id.fragment_container2,ruleSpinner);
                target.setEnabled(true);
                rule.setEnabled(false);
                break;
        }
        transaction.commit();
    }


    public void next(View view) {
        intent = new Intent(this,StandBy.class);
        startActivity(intent);
    }

    public void main(View view) {
        intent = new Intent(this,Main3Activity.class);
        startActivity(intent);
    }
}
