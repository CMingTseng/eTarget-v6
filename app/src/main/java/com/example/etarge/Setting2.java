package com.example.etarge;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Setting2 extends AppCompatActivity {
    Intent intent;
    Button TurnOn;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting2);
        fragmentManager = getSupportFragmentManager(); //管理fragment
        transaction = fragmentManager.beginTransaction(); //執行切換fragment
        Fragment_1 fragment_1 = new Fragment_1();
        transaction.replace(R.id.fragment_container,fragment_1);
        transaction.commit();
        TurnOn =(Button)findViewById(R.id.TurnOn);
    }



    /*public void TurnOn(View view) {
        startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 1);
    }*/

    public void back(View view) {
        intent = new Intent(this,Main2Activity.class);
        startActivity(intent);
    }

    public void onClick(View view) {
        transaction = fragmentManager.beginTransaction();
        switch (view.getId()){
            case R.id.mainset:
                Fragment_1 fragment_1 = new Fragment_1();
                transaction.replace(R.id.fragment_container,fragment_1);

                break;
            case R.id.target:
                Fragment_2 fragment_2 = new Fragment_2();
                transaction.replace(R.id.fragment_container,fragment_2);

                break;
            case R.id.audio:
                Fragment_3 fragment_3 = new Fragment_3();
                transaction.replace(R.id.fragment_container,fragment_3);

                break;
        }
        transaction.commit();
    }
}
