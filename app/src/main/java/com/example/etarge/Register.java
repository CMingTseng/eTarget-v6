package com.example.etarge;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {
    Context context = this;
    EditText email,user,pass,cpass;
    Button bt;
    WebView webView;
    CookieManager cookieManager;
    String cookieStr,id_text;
    String url="http://twairsoft.org/";
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email = (EditText) findViewById(R.id.email);
        user = (EditText) findViewById(R.id.user);
        pass = (EditText) findViewById(R.id.pass);
        cpass = (EditText) findViewById(R.id.checkpass);
        bt = (Button) findViewById(R.id.insert);
        //解android.os.NetworkOnMainThreadException問題的語法，可以檢測出意外執行的錯誤並顯示
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedClosableObjects().detectLeakedSqlLiteObjects().penaltyDeath().penaltyLog().build());

        wcookie(context);//建立抓取cookie的語法
        //Handler為時間處理器
        Handler myHandler = new Handler();
        myHandler.postDelayed(runTimerStop,15000);//postDelayed用來指定時間到時運行Runnable對象
        if(cookieStr !=null){
            myHandler.removeCallbacks(runTimerStop);//removeCallbacks方法是刪除指定的Runnable對象，使現成對象停止運行
        }

    }

    private void wcookie(Context context) {
        CookieSyncManager.createInstance(context);
        cookieManager = CookieManager.getInstance();
        webView = new WebView(context);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                cookieManager.setAcceptCookie(true);
                cookieStr=cookieManager.getCookie(url);
            }
        });
        webView.loadUrl(url);
        webView.clearHistory();
        webView.clearCache(true);
        cookieManager.removeAllCookie();
        cookieManager.removeSessionCookie();
    }

    private Runnable runTimerStop = new Runnable() {
        @Override
        public void run() {

        }
    };

    //新增資料
    public void insert(View view) {

        String mail = email.getText().toString();
        String acc = user.getText().toString();
        String p = pass.getText().toString();
        String cp = cpass.getText().toString();
        String[] data=new String[]{acc,p,mail};
        try{
            if(p.equals(cp)){
                String[] result = DBInssert.Inphp(data,cookieStr,url);
                Log.e("log_tag_q",result[1]+" ya");
                if(result[1].equals("1")){
                    intent = new Intent (this,Main3Activity.class);
                    startActivity(intent);
                }
                else {
                    new AlertDialog.Builder(Register.this)
                            .setMessage("帳號或密碼已經存在")//設定顯示文字
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })//設定結束的子視窗
                            .show();//呈現對話視窗
                }

            }
            else {
                new AlertDialog.Builder(Register.this)
                        .setTitle("錯誤!")//設定視窗標題
                        .setMessage("密碼不符")//設定顯示文字
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })//設定結束的子視窗
                        .show();//呈現對話視窗
            }
        }catch (Exception e){
            Log.e("log_tag","error");
        }

    }


}
