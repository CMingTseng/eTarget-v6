package com.example.etarge;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;

public class Main3Activity extends AppCompatActivity {
    Intent intent;
    ImageView logo;
    BluetoothAdapter ble_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        ble_adapter = BluetoothAdapter.getDefaultAdapter();
        logo = (ImageView)findViewById(R.id.imageView5);
        Resources res = getResources();
        //使用BitmapDrawable獲取點陣圖
        //使用BitmapDrawable (InputStream is)構造一個BitmapDrawable；
        //使用BitmapDrawable類的getBitmap()獲取得到點陣圖；
        // 讀取InputStream並得到點陣圖
        @SuppressLint("ResourceType") InputStream is = res.openRawResource(R.drawable.logo);
        BitmapDrawable bmpDraw = new BitmapDrawable(is);
        Bitmap bmp = bmpDraw.getBitmap();
        logo.setImageBitmap(setImgSize(bmp, 1146, 500));
        //Log.e("log_tag_q", setImgSize(bmp, 1146, 448));

    }

    public void signin(View view) {
        intent = new Intent(this, SignIn.class);
        startActivity(intent);
    }

    public void setting(View view) {
        intent = new Intent(this, Setting.class);
        startActivity(intent);
    }

    public Bitmap setImgSize(Bitmap bm, int newWidth, int newHeight) {
        // 獲得圖片的寬高.
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 計算縮放比例.
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要縮放的matrix引數.
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的圖片.
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    public void challenge(View view) {
        intent=getIntent();
        String mac = intent.getStringExtra("MAC");
        AlertDialog.Builder blueNoti;
        if (!ble_adapter.isEnabled()){

            blueNoti = new AlertDialog.Builder(Main3Activity.this);
            blueNoti.setTitle("連接藍芽");
            blueNoti.setMessage("請先到“設定SETTING”開啟藍芽並連接裝置!");
            blueNoti.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            blueNoti.show();
        }
        else{
            intent = new Intent(this,Challenge.class);
            startActivity(intent);
        }

    }

    public void quick(View view) {
        intent = new Intent(this,QuickStart.class);
        startActivity(intent);
    }
}
