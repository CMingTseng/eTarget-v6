package com.example.etarge;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Setting extends AppCompatActivity {
    TextView tv;
    private BluetoothAdapter mBluetoothAdapter ;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    Button main,target,audio;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //取得img陣列值
        Bundle power = this.getIntent().getExtras();
        //int[] img = power.getIntArray("power");
        /*Log.e("log_tag_q", "setting-img[8]="+img[8]);
        Fragment_1 fg_1 = new Fragment_1();
        Bundle bundle = new Bundle();
        bundle.putIntArray("power",img);//这里的values就是我们要传的值
        fg_1.setArguments(bundle);*/
        //mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        fragmentManager = getSupportFragmentManager(); //管理fragment
        transaction = fragmentManager.beginTransaction(); //執行切換fragment
        Fragment_1 fragment_1 = new Fragment_1();
        transaction.replace(R.id.fragment_container,fragment_1);
        transaction.commit();

        main = (Button)findViewById(R.id.mainset);
        target = (Button)findViewById(R.id.target);
        audio =(Button)findViewById(R.id.audio);





    }


    //關閉設定
    public void done(View view) {
        Intent intent = new Intent(this,Main3Activity.class);
        startActivity(intent);
    }

    public void onClick(View view){
        transaction = fragmentManager.beginTransaction();
        switch (view.getId()){
            case R.id.mainset:
                Fragment_1 fragment_1 = new Fragment_1();
                transaction.replace(R.id.fragment_container,fragment_1);
                main.setEnabled(false);
                target.setEnabled(true);
                audio.setEnabled(true);
                break;
            case R.id.target:
                Fragment_2 fragment_2 = new Fragment_2();
                transaction.replace(R.id.fragment_container,fragment_2);
                main.setEnabled(true);
                target.setEnabled(false);
                audio.setEnabled(true);

                break;
            case R.id.audio:
                Fragment_3 fragment_3 = new Fragment_3();
                transaction.replace(R.id.fragment_container,fragment_3);
                main.setEnabled(true);
                target.setEnabled(true);
                audio.setEnabled(false);

                break;
        }
        transaction.commit();
    }


}
