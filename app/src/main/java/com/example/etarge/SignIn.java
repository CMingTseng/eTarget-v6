package com.example.etarge;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SignIn extends AppCompatActivity {
    Intent intent;
    EditText et1,et2;
    String url="http://twairsoft.org/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        et1 = (EditText) findViewById(R.id.et1);
        et2 = (EditText) findViewById(R.id.et2);


    }
    //返回首頁
    public void close(View view) {
        intent = new Intent(this,Main3Activity.class);
        startActivity(intent);
    }
    //進入註冊頁
    public void regist(View view) {
        intent = new Intent(this,Register.class);
        startActivity(intent);
    }
    //登入判斷
    public void check(View view) {
        String acc = et1.getText().toString();
        String pass = et2.getText().toString();

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build());

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());

        try {
            String [] data = new String[]{acc,pass};
            String result = DBConnet.executeQuery(data,url);
            String response = DBConnet.response(data,url);
            if(response.equals("1")){
                intent = new Intent();
                intent.setClass(this,Main2Activity.class);
                intent .putExtra("user",acc);
                startActivity(intent);
            }
            else{
                AlertDialog.Builder Builder = new AlertDialog.Builder(this);
                Builder.setMessage("帳號或密碼不符!");
                Builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                 .show();

            }

        }catch (Exception e){

        }
    }
}
