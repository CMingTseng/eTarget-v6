package com.example.etarge;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Set;
import java.util.UUID;



public class Challenge extends LifeActivity {
        // GUI Components
        private BluetoothAdapter mBTAdapter;
        private Set<BluetoothDevice> mPairedDevices;
        private ArrayAdapter<String> mBTArrayAdapter;
        private BufferedReader BlueInf;
        BluetoothDevice device;
        private Handler mHandler;
        // Our main handler that will receive callback notifications
        private ConnectedThread mConnectedThread;
        // bluetooth background worker thread to send and receive data
        private BluetoothSocket mBTSocket = null;
        // bi-directional client-to-client data path

        private static final UUID BTMODULEUUID = UUID.fromString
                ("00001101-0000-1000-8000-00805F9B34FB"); // "random" unique identifier

        // #defines for identifying shared types between calling functions
        private final static int REQUEST_ENABLE_BT = 1;
        // used to identify adding bluetooth names
        private final static int MESSAGE_READ = 2;
        // used in bluetooth handler to identify message update
        private final static int CONNECTING_STATUS = 3;
        // used in bluetooth handler to identify message status
        FrameLayout fragmentcontainer;
        private  String _recieveData = "";
        int[] img=new int[1024];
        Intent intent;
        Switch switch1;
        boolean isBtnLongPressed = false;
        final String[] time={"1秒","2秒","3秒","4秒","5秒","6秒","7秒","8秒","9秒","Random"};
        int[] confirm = new int[16];
        int c=0; //紀錄配對到t1-t16、紀錄t1-t16配對到TI1-TI16
        TextView audiotv,tv1,tv2,tv3,tv4,tv5,tv6,ivalue,seekbarv;
        Button choose,light,finish,targetbt,ti1,ti2,ti3,ti4,ti5,ti6,ti7,ti8,ti9,ti10,ti11,ti12,ti13,ti14,ti15,ti16,
                                     t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,t11,t12,t13,t14,t15,t16;
        NumberPicker np1, np2; //時間滾動軸
        int minute=0;
        int second=0;
        String address="";
        String name="";
        String seekbarV="";
        SeekBar seekBar;
        int chooseW=0;
        int ii=0; //搜尋選擇關卡編號
        //將資料轉換成<key,value>的型態
        //final List<Map<String, Object>> items = new ArrayList<Map<String,Object>>();
        //final Map<String, Object> item = new HashMap<String, Object>();
        //ListView targetls;
        SimpleAdapter SAdapter;
                         //關卡資訊{起始碼,起始碼,長度,功能模式,預留,編號1,編號2,燈號1,燈號2,開始1,開始2,結束1,結束2,時間-分,時間-秒,罰則,靶面數,靶1-分配,靶1-計分,靶1-發數,靶1-梯次(S),靶1-動態(T),靶1-動態(A),動態(R),目標發數1,目標發數2,校驗碼,結束碼,結束碼}
        final int[][] leavel={{0x4B,0x47,134,0,0, 1,0,1,0,4,1,1,0,60,60,0,2, 0,5,2,0,0,0,0, 0,10,1,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 3,0, 0,0x55,0xAA},
                           {0x4B,0x47,134,0,0 ,2,0,4,0,4,0,2,0,60,60,1,2, 0,5,2,0,0,0,0, 0,5,4,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0 ,6,0, 0,0x55,0xAA},
                           {0x4B,0x47,134,0,0, 3,0,2,0,0,1,2,0,60,60,0,3, 0,5,2,0,0,0,0 , 0,10,1,0,0,0,0, 0,10,-1,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0 ,3,0, 0,0x55,0xAA},
                           {0x4B,0x47,134,0,0, 4,0,5,0,0,1,2,0,60,60,1,3, 0,5,2,0,0,0,0 , 0,10,1,0,0,0,0, 0,10,1,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0 ,4,0, 0,0x55,0xAA},
                           {0x4B,0x47,134,0,0, 5,0,1,0,4,1,2,0,60,60,1,3, 0,5,2,0,0,0,0 , 0,5,2,0,0,0,0, 0,10,1,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0 ,5,0, 0,0x55,0xAA},
                           {0x4B,0x47,134,0,0, 6,0,1,0,4,1,2,0,60,60,1,3, 0,5,1,0,0,0,0 , 0,5,2,0,0,0,0, 0,5,3,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0 ,6,0, 0,0x55,0xAA}};

        //關卡名稱
        final String[] lvname={"莫桑比克(2-1)","F.A.S.T.(2-4)","莫桑比克(2-1)+1人質","莫桑比克(2-1)+1","莫桑比克(2-1)+2","1 to 3"};
        //靶面文字資訊
        String[] STI={"","","","","","","","","","","","","","","",""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_challenge);
        //ConnectedThread connectedThread = new ConnectedThread(mBTSocket);
        //接收藍芽MAC位址
        //定義執行緒 當收到不同的指令做對應的內容
        mBTAdapter = BluetoothAdapter.getDefaultAdapter();
        // get a handle on the bluetooth radio
        //address = getIntent().getStringExtra("address");
        //Log.e("log_tag_q", "Challenge-address="+address);
        intent = getIntent();
        String MAC =intent.getStringExtra("MAC");
        device = mBTAdapter.getRemoteDevice("DC:0D:30:00:1C:A0");
        //Log.e("log_tag_q", "device="+device);
        int test =1;
        audiotv =(TextView)findViewById(R.id.audiotv);
        targetbt =(Button)findViewById(R.id.targetbt);
        light = (Button) findViewById(R.id.light);
        finish = (Button) findViewById(R.id.finish);
        switch1 = (Switch) findViewById(R.id.switch1);
        choose = (Button) findViewById(R.id.chooselv);
        fragmentcontainer = (FrameLayout) findViewById(R.id.fragment_container);
        ti1 = (Button) findViewById(R.id.TI1);
        ti2 = (Button) findViewById(R.id.TI2);
        ti3 = (Button) findViewById(R.id.TI3);
        ti4 = (Button) findViewById(R.id.TI4);
        ti5 = (Button) findViewById(R.id.TI5);
        ti6 = (Button) findViewById(R.id.TI6);
        ti7 = (Button) findViewById(R.id.TI7);
        ti8 = (Button) findViewById(R.id.TI8);
        ti9 = (Button) findViewById(R.id.TI9);
        ti10 = (Button) findViewById(R.id.TI10);
        ti11 = (Button) findViewById(R.id.TI11);
        ti12 = (Button) findViewById(R.id.TI12);
        ti13 = (Button) findViewById(R.id.TI13);
        ti14 = (Button) findViewById(R.id.TI14);
        ti15 = (Button) findViewById(R.id.TI15);
        ti16 = (Button) findViewById(R.id.TI16);
        t1 = (Button) findViewById(R.id.t1);
        t2 = (Button) findViewById(R.id.t2);
        t3 = (Button) findViewById(R.id.t3);
        t4 = (Button) findViewById(R.id.t4);
        t5 = (Button) findViewById(R.id.t5);
        t6 = (Button) findViewById(R.id.t6);
        t7 = (Button) findViewById(R.id.t7);
        t8 = (Button) findViewById(R.id.t8);
        t9 = (Button) findViewById(R.id.t9);
        t10 = (Button) findViewById(R.id.t10);
        t11 = (Button) findViewById(R.id.t11);
        t12 = (Button) findViewById(R.id.t12);
        t13 = (Button) findViewById(R.id.t13);
        t14 = (Button) findViewById(R.id.t14);
        t15 = (Button) findViewById(R.id.t15);
        t16 = (Button) findViewById(R.id.t16);

        tv1=(TextView)findViewById(R.id.tv1);
        tv2=(TextView)findViewById(R.id.tv2);
        tv3=(TextView)findViewById(R.id.tv3);
        tv4=(TextView)findViewById(R.id.tv4);
        tv5=(TextView)findViewById(R.id.tv5);
        tv6=(TextView)findViewById(R.id.tv6);
        ivalue=(TextView)findViewById(R.id.ivalue);
        seekBar = (SeekBar) findViewById(R.id.seekBar3);
        seekbarv = (TextView) findViewById(R.id.seekBarValue);
        /*
        初始化藍芽模組訊息接收
        * */
        // Spawn a new thread to avoid blocking the GUI one
        // 詢問藍芽裝置權限
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        //初始靶面
        initTarget();

        //定義執行續
        mHandler = new Handler(){
          public void handleMessage(android.os.Message msg){
              if(msg.what == MESSAGE_READ){//收到MESSAGE_READ開始接收資料
                  String readMessage = null;
                  try {
                      readMessage = new String((byte[])   msg.obj, "UTF-8");
                      byte[] readBuf = (byte[]) msg.obj;
                      for(int i=0;i<readBuf.length;i++){
                          //將收到資料轉成整數存入img[ ]
                          img[i]=readBuf [i] & 0xFF; // & 0xFF轉成整數解決亂碼問題
                      }
                      for (int j=0;j<readBuf.length;j++){
                          Log.e("log_tag_q","["+j+"]="+img[j]); //Logcat顯示藍芽傳入資料
                      }

                      //判斷靶面資訊
                      //TargetIn(img);
                      if(img[5] == 1) {
                          t1.setEnabled(true);
                          t1.setBackground(getResources().getDrawable(R.drawable.target2));
                          t1.setText("1A\n"+Integer.toString(img[7]));
                      }
                      else {
                          t1.setBackground(getResources().getDrawable(R.drawable.target1));
                          t1.setEnabled(false);
                      }
                      if(img[29] == 1){
                          t2.setEnabled(true);
                          t2.setBackground(getResources().getDrawable(R.drawable.target2));
                          //Log.e("log_tag_q", "img[13]="+Integer.toString(img[13]));
                          t2.setText("2A\n"+Integer.toString(img[31]));
                          //t2.setText(seekbarV);
                      }
                      else {
                          t2.setBackground(getResources().getDrawable(R.drawable.target1));
                          t2.setEnabled(false);
                      }
                      if(img[53] == 1){
                          t3.setEnabled(true);
                          t3.setBackground(getResources().getDrawable(R.drawable.target2));
                          t3.setText("3A\n"+Integer.toString(img[55]));
                          //t3.setText(seekbarV);
                      }
                      else {
                          t3.setBackground(getResources().getDrawable(R.drawable.target1));
                          t3.setEnabled(false);
                      }
                      if(img[77] == 1){
                          t4.setEnabled(true);
                          t4.setBackground(getResources().getDrawable(R.drawable.target2));
                          t4.setText("4A\n"+Integer.toString(img[79]));
                          //t4.setText(seekbarV);
                      }
                      else {
                          t4.setBackground(getResources().getDrawable(R.drawable.target1));
                          t4.setEnabled(false);
                      }
                      if(img[11] == 1){
                          t5.setEnabled(true);
                          t5.setBackground(getResources().getDrawable(R.drawable.target2));
                          t5.setText("1B\n"+Integer.toString(img[13]));
                          //t5.setText(seekbarV);
                      }
                      else {
                          t5.setBackground(getResources().getDrawable(R.drawable.target1));
                          t5.setEnabled(false);
                      }
                      if(img[35] == 1){
                          t6.setEnabled(true);
                          t6.setBackground(getResources().getDrawable(R.drawable.target2));
                          t6.setText("2B\n"+Integer.toString(img[37]));
                          //t6.setText(seekbarV);
                      }
                      else {
                          t6.setBackground(getResources().getDrawable(R.drawable.target1));
                          t6.setEnabled(false);
                      }
                      if(img[59] == 1){
                          t7.setEnabled(true);
                          t7.setBackground(getResources().getDrawable(R.drawable.target2));
                          t7.setText("3B\n"+Integer.toString(img[61]));
                          //t7.setText(seekbarV);
                      }
                      else {
                          t7.setBackground(getResources().getDrawable(R.drawable.target1));
                          t7.setEnabled(false);
                      }
                      if(img[83] == 1){
                          t8.setEnabled(true);
                          t8.setBackground(getResources().getDrawable(R.drawable.target2));
                          t8.setText("4B\n"+Integer.toString(img[85]));
                          //t8.setText(seekbarV);
                      }
                      else {
                          t8.setBackground(getResources().getDrawable(R.drawable.target1));
                          t8.setEnabled(false);
                      }
                      if(img[17] == 1){
                          t9.setEnabled(true);
                          t9.setBackground(getResources().getDrawable(R.drawable.target2));
                          t9.setText("1C\n"+Integer.toString(img[19]));
                          //t9.setText(seekbarV);
                      }
                      else {
                          t9.setBackground(getResources().getDrawable(R.drawable.target1));
                          t9.setEnabled(false);
                      }
                      if(img[41] == 1){
                          t10.setEnabled(true);
                          t10.setBackground(getResources().getDrawable(R.drawable.target2));
                          t10.setText("2C\n"+Integer.toString(img[43]));
                          //t10.setText(seekbarV);
                      }
                      else {
                          t10.setBackground(getResources().getDrawable(R.drawable.target1));
                          t10.setEnabled(false);
                      }
                      if(img[65] == 1){
                          t11.setEnabled(true);
                          t11.setBackground(getResources().getDrawable(R.drawable.target2));
                          t11.setText("3C\n"+Integer.toString(img[67]));
                          //t11.setText(seekbarV);
                      }
                      else {
                          t11.setBackground(getResources().getDrawable(R.drawable.target1));
                          t11.setEnabled(false);
                      }
                      if(img[89] == 1){
                          t12.setEnabled(true);
                          t12.setBackground(getResources().getDrawable(R.drawable.target2));
                          t12.setText("4C\n"+Integer.toString(img[91]));
                          //t12.setText(seekbarV);
                      }
                      else {
                          t12.setBackground(getResources().getDrawable(R.drawable.target1));
                          t12.setEnabled(false);
                      }
                      if(img[23] == 1){
                          t13.setEnabled(true);
                          t13.setBackground(getResources().getDrawable(R.drawable.target2));
                          t13.setText("1D\n"+Integer.toString(img[25]));
                          //t13.setText(seekbarV);
                      }
                      else {
                          t13.setBackground(getResources().getDrawable(R.drawable.target1));
                          t13.setEnabled(false);
                      }
                      if(img[47] == 1){
                          t14.setEnabled(true);
                          t14.setBackground(getResources().getDrawable(R.drawable.target2));
                          t14.setText("2D\n"+Integer.toString(img[49]));
                          //t14.setText(seekbarV);
                      }
                      else {
                          t14.setBackground(getResources().getDrawable(R.drawable.target1));
                          t14.setEnabled(false);
                      }
                      if(img[71] == 1){
                          t15.setEnabled(true);
                          t15.setBackground(getResources().getDrawable(R.drawable.target2));
                          t15.setText("3D\n"+Integer.toString(img[73]));
                          //t15.setText(seekbarV);
                      }
                      else {
                          t15.setBackground(getResources().getDrawable(R.drawable.target1));
                          t15.setEnabled(false);
                      }
                      if(img[95] == 1){
                          t16.setEnabled(true);
                          t16.setBackground(getResources().getDrawable(R.drawable.target2));
                          t16.setText("4D\n"+Integer.toString(img[97]));
                      }
                      else {
                          t16.setBackground(getResources().getDrawable(R.drawable.target1));
                          t16.setEnabled(false);
                      }

                  } catch (UnsupportedEncodingException e) {
                      e.printStackTrace();
                  }
                  if(msg.what == CONNECTING_STATUS){
                      //收到CONNECTING_STATUS 顯示以下訊息
                      if(msg.arg1 == 1) {
                          //mBluetoothStatus.setText("Connected to Device: " + (String)(msg.obj));
                      }
                      else {
                          //mBluetoothStatus.setText("Connection Failed");
                      }
                  }
              }
          }
        };


        new Thread()
        {
            public void run() {
                boolean fail = false;
                //取得裝置MAC找到連接的藍芽裝置
                try {
                    mBTSocket = createBluetoothSocket(device);
                    //建立藍芽socket
                } catch (IOException e) {
                    fail = true;
                    Toast.makeText(getBaseContext(), "Socket creation failed",
                            Toast.LENGTH_SHORT).show();
                }
                // Establish the Bluetooth socket connection.
                try {
                    mBTSocket.connect(); //建立藍芽連線
                } catch (IOException e) {
                    try {
                        fail = true;
                        mBTSocket.close(); //關閉socket
                        //開啟執行緒 顯示訊息
                        mHandler.obtainMessage(CONNECTING_STATUS, -1, -1)
                                .sendToTarget();
                    } catch (IOException e2) {
                        //insert code to deal with this
                        Toast.makeText(getBaseContext(), "Socket creation failed",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                if(fail == false) {
                    //開啟執行緒用於傳輸及接收資料
                    Log.e("log_tag_q", "success");
                    mConnectedThread = new ConnectedThread(mBTSocket);
                    mConnectedThread.start();
                    //開啟新執行緒顯示連接裝置名稱
                    mHandler.obtainMessage(CONNECTING_STATUS, 1, -1, name)
                            .sendToTarget();
                }
            }
        }.start();

        //seekbar設定
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekbarv.setText(String.valueOf(progress));  //用texview顯示bar條數值
                seekbarV=String.valueOf(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //監聽t1-t16長壓
        t1.setOnLongClickListener(new HoldListener());
        t2.setOnLongClickListener(new HoldListener());
        t3.setOnLongClickListener(new HoldListener());
        t4.setOnLongClickListener(new HoldListener());
        t5.setOnLongClickListener(new HoldListener());
        t6.setOnLongClickListener(new HoldListener());
        t7.setOnLongClickListener(new HoldListener());
        t8.setOnLongClickListener(new HoldListener());
        t9.setOnLongClickListener(new HoldListener());
        t10.setOnLongClickListener(new HoldListener());
        t11.setOnLongClickListener(new HoldListener());
        t12.setOnLongClickListener(new HoldListener());
        t13.setOnLongClickListener(new HoldListener());
        t14.setOnLongClickListener(new HoldListener());
        t15.setOnLongClickListener(new HoldListener());
        t16.setOnLongClickListener(new HoldListener());
        //switch開關設定
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    leavel[0][15]=1;
                }else leavel[0][15]=0;
            }
        });

        np1 = (NumberPicker)findViewById(R.id.np1);
        np2 = (NumberPicker)findViewById(R.id.np2);
        np1.setMaxValue(99); //定義分鐘最大值
        np1.setMinValue(0); //定義分鐘最小值

        np2.setMaxValue(59); //定義秒鐘最大值
        np2.setMinValue(0); //定義秒鐘最小值
        np1.setValue(minute); //設定分鐘初值
        np1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override    //當NumberPicker的值發生改變時，將會激發該方法
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                minute = newVal;
                leavel[0][13]=minute;
            }
        });

        np2.setValue(second);
        np2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                second = newVal;
                leavel[0][14]=second;
            }
        });

    }

    //建立藍芽soccket
    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws
            IOException {
        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connection with BT device using UUID
    }

    //連接Thread class
    class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()
            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.available();
                    if(bytes != 0) {
                        SystemClock.sleep(100);
                        //pause and wait for rest of data
                        bytes = mmInStream.available();
                        // how many bytes are ready to be read?
                        bytes = mmInStream.read(buffer, 0, bytes);
                        // record how many bytes we actually read
                        mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                                .sendToTarget(); // Send the obtained bytes to the UI activity
                    }
                } catch (IOException e) {
                    e.printStackTrace();

                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(int input) {
            int[] send = new int[]{};
            byte bytes = (byte)input;           //converts entered String into bytes
            try {
                mmOutStream.write(bytes);
                //Log.e("log_tag_q", "bytes="+ bytes);

            } catch (IOException e) { }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    public void TargetIn(int img[]){


        if(img[5] == 1) {
            t1.setEnabled(true);
            t1.setBackground(getResources().getDrawable(R.drawable.target2));
            t1.setText("1A\n"+Integer.toString(img[7]));
        }
        else {
            t1.setBackground(getResources().getDrawable(R.drawable.target1));
            t1.setEnabled(false);
        }
        if(img[29] == 1){
            t2.setEnabled(true);
            t2.setBackground(getResources().getDrawable(R.drawable.target2));
            //Log.e("log_tag_q", "img[13]="+Integer.toString(img[13]));
            t2.setText("2A\n"+Integer.toString(img[31]));
            //t2.setText(seekbarV);
        }
        else {
            t2.setBackground(getResources().getDrawable(R.drawable.target1));
            t2.setEnabled(false);
        }
        if(img[53] == 1){
            t3.setEnabled(true);
            t3.setBackground(getResources().getDrawable(R.drawable.target2));
            t3.setText("3A\n"+Integer.toString(img[55]));
            //t3.setText(seekbarV);
        }
        else {
            t3.setBackground(getResources().getDrawable(R.drawable.target1));
            t3.setEnabled(false);
        }
        if(img[77] == 1){
            t4.setEnabled(true);
            t4.setBackground(getResources().getDrawable(R.drawable.target2));
            t4.setText("4A\n"+Integer.toString(img[79]));
            //t4.setText(seekbarV);
        }
        else {
            t4.setBackground(getResources().getDrawable(R.drawable.target1));
            t4.setEnabled(false);
        }
        if(img[11] == 1){
            t5.setEnabled(true);
            t5.setBackground(getResources().getDrawable(R.drawable.target2));
            t5.setText("1B\n"+Integer.toString(img[13]));
            //t5.setText(seekbarV);
        }
        else {
            t5.setBackground(getResources().getDrawable(R.drawable.target1));
            t5.setEnabled(false);
        }
        if(img[35] == 1){
            t6.setEnabled(true);
            t6.setBackground(getResources().getDrawable(R.drawable.target2));
            t6.setText("2B\n"+Integer.toString(img[37]));
            //t6.setText(seekbarV);
        }
        else {
            t6.setBackground(getResources().getDrawable(R.drawable.target1));
            t6.setEnabled(false);
        }
        if(img[59] == 1){
            t7.setEnabled(true);
            t7.setBackground(getResources().getDrawable(R.drawable.target2));
            t7.setText("3B\n"+Integer.toString(img[61]));
            //t7.setText(seekbarV);
        }
        else {
            t7.setBackground(getResources().getDrawable(R.drawable.target1));
            t7.setEnabled(false);
        }
        if(img[83] == 1){
            t8.setEnabled(true);
            t8.setBackground(getResources().getDrawable(R.drawable.target2));
            t8.setText("4B\n"+Integer.toString(img[85]));
            //t8.setText(seekbarV);
        }
        else {
            t8.setBackground(getResources().getDrawable(R.drawable.target1));
            t8.setEnabled(false);
        }
        if(img[17] == 1){
            t9.setEnabled(true);
            t9.setBackground(getResources().getDrawable(R.drawable.target2));
            t9.setText("1C\n"+Integer.toString(img[19]));
            //t9.setText(seekbarV);
        }
        else {
            t9.setBackground(getResources().getDrawable(R.drawable.target1));
            t9.setEnabled(false);
        }
        if(img[41] == 1){
            t10.setEnabled(true);
            t10.setBackground(getResources().getDrawable(R.drawable.target2));
            t10.setText("2C\n"+Integer.toString(img[43]));
            //t10.setText(seekbarV);
        }
        else {
            t10.setBackground(getResources().getDrawable(R.drawable.target1));
            t10.setEnabled(false);
        }
        if(img[65] == 1){
            t11.setEnabled(true);
            t11.setBackground(getResources().getDrawable(R.drawable.target2));
            t11.setText("3C\n"+Integer.toString(img[67]));
            //t11.setText(seekbarV);
        }
        else {
            t11.setBackground(getResources().getDrawable(R.drawable.target1));
            t11.setEnabled(false);
        }
        if(img[89] == 1){
            t12.setEnabled(true);
            t12.setBackground(getResources().getDrawable(R.drawable.target2));
            t12.setText("4C\n"+Integer.toString(img[91]));
            //t12.setText(seekbarV);
        }
        else {
            t12.setBackground(getResources().getDrawable(R.drawable.target1));
            t12.setEnabled(false);
        }
        if(img[23] == 1){
            t13.setEnabled(true);
            t13.setBackground(getResources().getDrawable(R.drawable.target2));
            t13.setText("1D\n"+Integer.toString(img[25]));
            //t13.setText(seekbarV);
        }
        else {
            t13.setBackground(getResources().getDrawable(R.drawable.target1));
            t13.setEnabled(false);
        }
        if(img[47] == 1){
            t14.setEnabled(true);
            t14.setBackground(getResources().getDrawable(R.drawable.target2));
            t14.setText("2D\n"+Integer.toString(img[49]));
            //t14.setText(seekbarV);
        }
        else {
            t14.setBackground(getResources().getDrawable(R.drawable.target1));
            t14.setEnabled(false);
        }
        if(img[71] == 1){
            t15.setEnabled(true);
            t15.setBackground(getResources().getDrawable(R.drawable.target2));
            t15.setText("3D\n"+Integer.toString(img[73]));
            //t15.setText(seekbarV);
        }
        else {
            t15.setBackground(getResources().getDrawable(R.drawable.target1));
            t15.setEnabled(false);
        }
        if(img[95] == 1){
            t16.setEnabled(true);
            t16.setBackground(getResources().getDrawable(R.drawable.target2));
            t16.setText("4D\n"+Integer.toString(img[97]));
        }
        else {
            t16.setBackground(getResources().getDrawable(R.drawable.target1));
            t16.setEnabled(false);
        }
    }



    public void signin(View view) {
        intent = new Intent(this,Setting.class);
        final Bundle power=new Bundle();
        power.putIntArray("power",img);
        intent.putExtras(power);
        startActivity(intent);
    }

    public void setting(View view) {
        intent = new Intent(this,Setting.class);
        startActivity(intent);
    }

    public void choose(View view) {
        AlertDialog.Builder dialog_list = new AlertDialog.Builder(Challenge.this);
        dialog_list.setTitle("選擇關卡");
        dialog_list.setItems(lvname, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                chooseW=which;
                AlertDialog.Builder check = new AlertDialog.Builder(Challenge.this);
                check.setTitle("是否確定更改關卡?");
                check.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        choose.setText(lvname[chooseW]);
                        //判斷關卡所需靶面
                        switch (leavel[chooseW][16]){
                            case 1:
                                initTI();
                                ti1.setBackground(getResources().getDrawable(R.drawable.target2));
                                ti1.setText(Integer.toString(leavel[chooseW][18])+"p\n"+Integer.toString(leavel[chooseW][19])+"s");
                                ti2.setText("");
                                ti3.setText("");
                                ti2.setBackground(getResources().getDrawable(R.drawable.target1));
                                ti3.setBackground(getResources().getDrawable(R.drawable.target1));
                                ti4.setBackground(getResources().getDrawable(R.drawable.target1));
                                ti5.setBackground(getResources().getDrawable(R.drawable.target1));
                                ti6.setBackground(getResources().getDrawable(R.drawable.target1));
                                ti7.setBackground(getResources().getDrawable(R.drawable.target1));
                                ti8.setBackground(getResources().getDrawable(R.drawable.target1));
                                ti9.setBackground(getResources().getDrawable(R.drawable.target1));
                                ti10.setBackground(getResources().getDrawable(R.drawable.target1));
                                ti11.setBackground(getResources().getDrawable(R.drawable.target1));
                                ti12.setBackground(getResources().getDrawable(R.drawable.target1));
                                ti13.setBackground(getResources().getDrawable(R.drawable.target1));
                                ti14.setBackground(getResources().getDrawable(R.drawable.target1));
                                ti15.setBackground(getResources().getDrawable(R.drawable.target1));
                                ti16.setBackground(getResources().getDrawable(R.drawable.target1));
                                break;
                            case 2:
                                initTI();
                                ti1.setBackground(getResources().getDrawable(R.drawable.target2));
                                ti1.setText(Integer.toString(leavel[chooseW][18])+"p\n"+Integer.toString(leavel[chooseW][19])+"s");
                                ti2.setBackground(getResources().getDrawable(R.drawable.target2));
                                ti2.setText(Integer.toString(leavel[chooseW][25])+"p\n"+Integer.toString(leavel[chooseW][26])+"s");
                                ti3.setText("");
                                ti3.setBackground(getResources().getDrawable(R.drawable.target1));
                                ti4.setBackground(getResources().getDrawable(R.drawable.target1));
                                ti5.setBackground(getResources().getDrawable(R.drawable.target1));
                                ti6.setBackground(getResources().getDrawable(R.drawable.target1));
                                ti7.setBackground(getResources().getDrawable(R.drawable.target1));
                                ti8.setBackground(getResources().getDrawable(R.drawable.target1));
                                ti9.setBackground(getResources().getDrawable(R.drawable.target1));
                                ti10.setBackground(getResources().getDrawable(R.drawable.target1));
                                ti11.setBackground(getResources().getDrawable(R.drawable.target1));
                                ti12.setBackground(getResources().getDrawable(R.drawable.target1));
                                ti13.setBackground(getResources().getDrawable(R.drawable.target1));
                                ti14.setBackground(getResources().getDrawable(R.drawable.target1));
                                ti15.setBackground(getResources().getDrawable(R.drawable.target1));
                                ti16.setBackground(getResources().getDrawable(R.drawable.target1));
                                break;
                            case 3:
                                initTI();
                                ti1.setBackground(getResources().getDrawable(R.drawable.target2));
                                ti1.setText(Integer.toString(leavel[chooseW][18])+"p\n"+Integer.toString(leavel[chooseW][19])+"s");

                                ti2.setBackground(getResources().getDrawable(R.drawable.target2));
                                ti2.setText(Integer.toString(leavel[chooseW][25])+"p\n"+Integer.toString(leavel[chooseW][26])+"s");

                                ti3.setBackground(getResources().getDrawable(R.drawable.target2));
                                ti3.setText(Integer.toString(leavel[chooseW][32])+"p\n"+Integer.toString(leavel[chooseW][33])+"s");
                                ti4.setText("");
                                ti4.setBackground(getResources().getDrawable(R.drawable.target1));
                                ti5.setBackground(getResources().getDrawable(R.drawable.target1));
                                break;
                            case 4:
                                initTI();
                                ti1.setBackground(getResources().getDrawable(R.drawable.target2));
                                ti2.setBackground(getResources().getDrawable(R.drawable.target2));
                                ti3.setBackground(getResources().getDrawable(R.drawable.target2));
                                ti4.setBackground(getResources().getDrawable(R.drawable.target2));
                                ti5.setBackground(getResources().getDrawable(R.drawable.target1));
                                break;
                            case 5:
                                initTI();
                                ti1.setBackground(getResources().getDrawable(R.drawable.target2));
                                ti2.setBackground(getResources().getDrawable(R.drawable.target2));
                                ti3.setBackground(getResources().getDrawable(R.drawable.target2));
                                ti4.setBackground(getResources().getDrawable(R.drawable.target2));
                                ti5.setBackground(getResources().getDrawable(R.drawable.target2));
                                break;
                        }
                        dialog.cancel();
                    }
                })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();

                            }
                        });
                 check.show();
            }
        });
        dialog_list.show();
    }
    //進入Stand By


    public void main(View view) throws IOException {
        mBTSocket.close();
        intent = new Intent(this,Main3Activity.class);
        startActivity(intent);
    }

    //選擇時間
    public void radioctrl(View view) {
        AlertDialog.Builder dialog_list = new AlertDialog.Builder(Challenge.this);
        dialog_list.setTitle("選擇開始時間");
        dialog_list.setItems(time, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e("log_tag","which="+which);
                Toast.makeText(Challenge.this, "開始時間設為" + time[which], Toast.LENGTH_SHORT).show();
                audiotv.setText(time[which]);
                Log.e("log_tag_q", "which="+which);
                leavel[0][9]= which+1;
                AlertDialog.Builder askAudio = new AlertDialog.Builder(Challenge.this);
                askAudio.setTitle("是否開啟音效?");
                askAudio.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        leavel[0][10]=1;
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                leavel[0][10]=0;
                            }
                        });
                askAudio.show();
            }
        });
        dialog_list.show();
    }

    //靶面資訊
    public void TOnclick(View v){
        if (c != 0) {
            switch (v.getId()){
                case R.id.TI1:
                    ti1.setEnabled(false);
                    ti1.setBackground(getResources().getDrawable(R.drawable.target3));
                    if(c==1){ //如果先按下t1靶面，就把ti1靶面資訊印上去，並且將ti1編號存入confirm陣列中
                        c=0;
                        confirm[0]=1;
                        leavel[ii][17]=1;//靶面ti1配置給靶t1
                        if(t1.getText().length()>4)
                            t1.setText("1A\n"+Integer.toString(img[7]));
                        t1.setText(t1.getText()+"\n"+ti1.getText());
                        STI[0]= (String) t1.getText();
                        t1.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==2){
                        c=0;
                        confirm[1]=1;
                        leavel[ii][24]=1; //靶面ti1配置給靶t2
                        if(t2.getText().length()>4)
                            t2.setText("2A\n"+Integer.toString(img[31]));
                        t2.setText(t2.getText()+"\n"+ti1.getText());
                        STI[1]= (String) t2.getText();
                        t2.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==3){
                        c=0;
                        confirm[2]=1;
                        leavel[ii][31]=1;//靶面ti1配置給靶t3
                        if(t3.getText().length()>4)
                            t3.setText("3A\n"+Integer.toString(img[55]));
                        t3.setText(t3.getText()+"\n"+ti1.getText());
                        STI[2]= (String) t3.getText();
                        t3.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==4){
                        c=0;
                        confirm[3]=1;
                        leavel[ii][38]=1;//靶面ti1配置給靶t4
                        if(t4.getText().length()>4)
                            t4.setText("4A\n"+Integer.toString(img[79]));
                        t4.setText(t4.getText()+"\n"+ti1.getText());
                        STI[3]= (String) t4.getText();
                        t4.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==5){
                        c=0;
                        confirm[4]=1;
                        leavel[ii][45]=1;//靶面ti1配置給靶t5
                        if(t5.getText().length()>4)
                            t5.setText("1B\n"+Integer.toString(img[13]));
                        t5.setText(t5.getText()+"\n"+ti1.getText());
                        STI[4]= (String) t5.getText();
                        t5.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==6){
                        c=0;
                        confirm[5]=1;
                        leavel[ii][52]=1;//靶面ti1配置給靶t6
                        if(t6.getText().length()>4)
                            t6.setText("2B\n"+Integer.toString(img[37]));
                        t6.setText(t6.getText()+"\n"+ti1.getText());
                        STI[5]= (String) t6.getText();
                        t6.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==7){
                        c=0;
                        confirm[6]=1;
                        leavel[ii][59]=1;
                        if(t7.getText().length()>4)
                            t7.setText("3B\n"+Integer.toString(img[61]));
                        t7.setText(t7.getText()+"\n"+ti1.getText());
                        STI[6]= (String) t7.getText();
                        t7.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==8){
                        c=0;
                        confirm[7]=1;
                        leavel[ii][66]=1;
                        if(t8.getText().length()>4)
                            t8.setText("4B\n"+Integer.toString(img[85]));
                        t8.setText(t8.getText()+"\n"+ti1.getText());
                        STI[7]= (String) t8.getText();
                        t8.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==9){
                        c=0;
                        confirm[8]=1;
                        leavel[ii][73]=1;
                        if(t9.getText().length()>4)
                            t9.setText("1C\n"+Integer.toString(img[19]));
                        t9.setText(t9.getText()+"\n"+ti1.getText());
                        STI[8]= (String) t9.getText();
                        t9.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==10){
                        c=0;
                        confirm[9]=1;
                        leavel[ii][80]=1;
                        if(t10.getText().length()>4)
                            t10.setText("2C\n"+Integer.toString(img[43]));
                        t10.setText(t10.getText()+"\n"+ti1.getText());
                        STI[9]= (String) t10.getText();
                        t10.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==11){
                        c=0;
                        confirm[10]=1;
                        leavel[ii][87]=1;
                        if(t11.getText().length()>4)
                            t11.setText("3C\n"+Integer.toString(img[67]));
                        t11.setText(t11.getText()+"\n"+ti1.getText());
                        STI[10]= (String) t11.getText();
                        t11.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==12){
                        c=0;
                        confirm[11]=1;
                        leavel[ii][94]=1;
                        if(t12.getText().length()>4)
                            t12.setText("4C\n"+Integer.toString(img[91]));
                        t12.setText(t12.getText()+"\n"+ti1.getText());
                        STI[11]= (String) t12.getText();
                        t12.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==13){
                        c=0;
                        confirm[12]=1;
                        leavel[ii][101]=1;
                        if(t13.getText().length()>4)
                            t13.setText("1D\n"+Integer.toString(img[25]));
                        t13.setText(t13.getText()+"\n"+ti1.getText());
                        STI[12]= (String) t13.getText();
                        t13.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==14){
                        c=0;
                        confirm[13]=1;
                        leavel[ii][108]=1;
                        if(t14.getText().length()>4)
                            t14.setText("2D\n"+Integer.toString(img[49]));
                        t14.setText(t14.getText()+"\n"+ti1.getText());
                        STI[13]= (String) t14.getText();
                        t14.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==15){
                        c=0;
                        confirm[14]=1;
                        leavel[ii][115]=1;
                        if(t15.getText().length()>4)
                            t15.setText("3D\n"+Integer.toString(img[73]));
                        t15.setText(t15.getText()+"\n"+ti1.getText());
                        STI[14]= (String) t15.getText();
                        t15.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==16){
                        c=0;
                        confirm[15]=1;
                        leavel[ii][122]=1;
                        if(t16.getText().length()>4)
                            t16.setText("4D\n"+Integer.toString(img[97]));
                        t16.setText(t16.getText()+"\n"+ti1.getText());
                        STI[15]= (String) t16.getText();
                        t16.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    break;
                case R.id.TI2:
                    ti2.setEnabled(false);
                    ti2.setBackground(getResources().getDrawable(R.drawable.target3));
                    if(c==1){
                        c=0;
                        confirm[0]=2;
                        leavel[ii][17]=2;
                        if(t1.getText().length()>4)
                            t1.setText("1A\n"+Integer.toString(img[7]));
                        t1.setText(t1.getText()+"\n"+ti2.getText());
                        STI[0]= (String) t1.getText(); //將ti靶面新增資訊存入STI陣列，以供ReadyFragment判斷此靶是否有配對
                        t1.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==2){
                        c=0;
                        confirm[1]=2;
                        leavel[ii][24]=2;
                        if(t2.getText().length()>4)
                            t2.setText("2A\n"+Integer.toString(img[31]));
                        t2.setText(t2.getText()+"\n"+ti2.getText());
                        STI[1]= (String) t2.getText();
                        t2.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==3){
                        c=0;
                        confirm[2]=2;
                        leavel[ii][31]=2;
                        if(t3.getText().length()>4)
                            t3.setText("3A\n"+Integer.toString(img[55]));
                        t3.setText(t3.getText()+"\n"+ti2.getText());
                        STI[2]= (String) t3.getText();
                        t3.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==4){
                        c=0;
                        confirm[3]=2;
                        leavel[ii][38]=2;
                        if(t4.getText().length()>4)
                            t4.setText("4A\n"+Integer.toString(img[79]));
                        t4.setText(t4.getText()+"\n"+ti2.getText());
                        STI[3]= (String) t4.getText();
                        t4.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==5){
                        c=0;
                        confirm[4]=2;
                        leavel[ii][45]=2;
                        if(t5.getText().length()>4)
                            t5.setText("1B\n"+Integer.toString(img[13]));
                        t5.setText(t5.getText()+"\n"+ti2.getText());
                        STI[4]= (String) t5.getText();
                        t5.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==6){
                        c=0;
                        confirm[5]=2;
                        leavel[ii][52]=2;
                        if(t6.getText().length()>4)
                            t6.setText("2B\n"+Integer.toString(img[37]));
                        t6.setText(t6.getText()+"\n"+ti2.getText());
                        STI[5]= (String) t6.getText();
                        t6.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==7){
                        c=0;
                        confirm[6]=2;
                        leavel[ii][59]=2;
                        if(t7.getText().length()>4)
                            t7.setText("3B\n"+Integer.toString(img[61]));
                        t7.setText(t7.getText()+"\n"+ti2.getText());
                        STI[6]= (String) t7.getText();
                        t7.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==8){
                        c=0;
                        confirm[7]=2;
                        leavel[ii][66]=2;
                        if(t8.getText().length()>4)
                            t8.setText("4B\n"+Integer.toString(img[85]));
                        t8.setText(t8.getText()+"\n"+ti2.getText());
                        STI[7]= (String) t8.getText();
                        t8.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==9){
                        c=0;
                        confirm[8]=2;
                        leavel[ii][73]=2;
                        if(t9.getText().length()>4)
                            t9.setText("1C\n"+Integer.toString(img[19]));
                        t9.setText(t9.getText()+"\n"+ti2.getText());
                        STI[8]= (String) t9.getText();
                        t9.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==10){
                        c=0;
                        confirm[9]=2;
                        leavel[ii][80]=2;
                        if(t10.getText().length()>4)
                            t10.setText("2C\n"+Integer.toString(img[43]));
                        t10.setText(t10.getText()+"\n"+ti2.getText());
                        STI[9]= (String) t10.getText();
                        t10.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==11){
                        c=0;
                        confirm[10]=2;
                        leavel[ii][87]=2;
                        if(t11.getText().length()>4)
                            t11.setText("3C\n"+Integer.toString(img[67]));
                        t11.setText(t11.getText()+"\n"+ti2.getText());
                        STI[10]= (String) t11.getText();
                        t11.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==12){
                        c=0;
                        confirm[11]=2;
                        leavel[ii][94]=2;
                        if(t12.getText().length()>4)
                            t12.setText("4C\n"+Integer.toString(img[91]));
                        t12.setText(t12.getText()+"\n"+ti2.getText());
                        STI[11]= (String) t12.getText();
                        t12.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==13){
                        c=0;
                        confirm[12]=2;
                        leavel[ii][101]=2;
                        if(t13.getText().length()>4)
                            t13.setText("1D\n"+Integer.toString(img[25]));
                        t13.setText(t13.getText()+"\n"+ti2.getText());
                        STI[12]= (String) t13.getText();
                        t13.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==14){
                        c=0;
                        confirm[13]=2;
                        leavel[ii][108]=2;
                        if(t14.getText().length()>4)
                            t14.setText("2D\n"+Integer.toString(img[49]));
                        t14.setText(t14.getText()+"\n"+ti2.getText());
                        STI[13]= (String) t14.getText();
                        t14.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==15){
                        c=0;
                        confirm[14]=2;
                        leavel[ii][115]=2;
                        if(t15.getText().length()>4)
                            t15.setText("3D\n"+Integer.toString(img[73]));
                        t15.setText(t15.getText()+"\n"+ti2.getText());
                        STI[14]= (String) t14.getText();
                        t15.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==16){
                        c=0;
                        confirm[15]=2;
                        leavel[ii][122]=2;
                        if(t16.getText().length()>4)
                            t16.setText("4D\n"+Integer.toString(img[97]));
                        t16.setText(t16.getText()+"\n"+ti2.getText());
                        STI[15]= (String) t16.getText();
                        t16.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    break;
                case R.id.TI3:
                    ti3.setEnabled(false);
                    ti3.setBackground(getResources().getDrawable(R.drawable.target3));
                    if(c==1){
                        c=0;
                        confirm[0]=3;
                        leavel[ii][17]=3;
                        if(t1.getText().length()>4)
                            t1.setText("1A\n"+Integer.toString(img[7]));
                        t1.setText(t1.getText()+"\n"+ti3.getText());
                        STI[0]= (String) t1.getText();
                        t1.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==2){
                        c=0;
                        confirm[1]=3;
                        leavel[ii][24]=3;
                        if(t2.getText().length()>4)
                            t2.setText("2A\n"+Integer.toString(img[31]));
                        t2.setText(t2.getText()+"\n"+ti3.getText());
                        STI[1]= (String) t2.getText();
                        t2.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==3){
                        c=0;
                        confirm[2]=3;
                        leavel[ii][31]=3;
                        if(t3.getText().length()>4)
                            t3.setText("3A\n"+Integer.toString(img[55]));
                        t3.setText(t3.getText()+"\n"+ti3.getText());
                        STI[2]= (String) t3.getText();
                        t3.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==4){
                        c=0;
                        confirm[3]=3;
                        leavel[ii][38]=3;
                        if(t4.getText().length()>4)
                            t4.setText("4A\n"+Integer.toString(img[79]));
                        t4.setText(t4.getText()+"\n"+ti3.getText());
                        STI[3]= (String) t4.getText();
                        t4.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==5){
                        c=0;
                        confirm[4]=3;
                        leavel[ii][45]=3;
                        if(t5.getText().length()>4)
                            t5.setText("1B\n"+Integer.toString(img[13]));
                        t5.setText(t5.getText()+"\n"+ti3.getText());
                        STI[4]= (String) t5.getText();
                        t5.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==6){
                        c=0;
                        confirm[5]=3;
                        leavel[ii][52]=3;
                        if(t6.getText().length()>4)
                            t6.setText("2B\n"+Integer.toString(img[37]));
                        t6.setText(t6.getText()+"\n"+ti3.getText());
                        STI[5]= (String) t6.getText();
                        t6.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==7){
                        c=0;
                        confirm[6]=3;
                        leavel[ii][59]=3;
                        if(t7.getText().length()>4)
                            t7.setText("3B\n"+Integer.toString(img[61]));
                        t7.setText(t7.getText()+"\n"+ti3.getText());
                        STI[6]= (String) t7.getText();
                        t7.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==8){
                        c=0;
                        confirm[7]=3;
                        leavel[ii][66]=3;
                        if(t8.getText().length()>4)
                            t8.setText("4B\n"+Integer.toString(img[85]));
                        t8.setText(t8.getText()+"\n"+ti3.getText());
                        STI[7]= (String) t8.getText();
                        t8.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==9){
                        c=0;
                        confirm[8]=3;
                        leavel[ii][73]=3;
                        if(t9.getText().length()>4)
                            t9.setText("1C\n"+Integer.toString(img[19]));
                        t9.setText(t9.getText()+"\n"+ti3.getText());
                        STI[8]= (String) t9.getText();
                        t9.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==10){
                        c=0;
                        confirm[9]=3;
                        leavel[ii][80]=3;
                        if(t10.getText().length()>4)
                            t10.setText("2C\n"+Integer.toString(img[43]));
                        t10.setText(t10.getText()+"\n"+ti3.getText());
                        STI[9]= (String) t10.getText();
                        t10.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==11){
                        c=0;
                        confirm[10]=3;
                        leavel[ii][87]=3;
                        if(t11.getText().length()>4)
                            t11.setText("3C\n"+Integer.toString(img[67]));
                        t11.setText(t11.getText()+"\n"+ti3.getText());
                        STI[10]= (String) t11.getText();
                        t11.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==12){
                        c=0;
                        confirm[11]=3;
                        leavel[ii][94]=3;
                        if(t12.getText().length()>4)
                            t12.setText("4C\n"+Integer.toString(img[91]));
                        t12.setText(t12.getText()+"\n"+ti3.getText());
                        STI[11]= (String) t12.getText();
                        t12.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==13){
                        c=0;
                        confirm[12]=3;
                        leavel[ii][101]=3;
                        if(t13.getText().length()>4)
                            t13.setText("1D\n"+Integer.toString(img[25]));
                        t13.setText(t13.getText()+"\n"+ti3.getText());
                        STI[12]= (String) t13.getText();
                        t13.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==14){
                        c=0;
                        confirm[13]=3;
                        leavel[ii][108]=3;
                        if(t14.getText().length()>4)
                            t14.setText("2D\n"+Integer.toString(img[49]));
                        t14.setText(t14.getText()+"\n"+ti3.getText());
                        t14.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==15){
                        c=0;
                        confirm[14]=3;
                        leavel[ii][115]=3;
                        if(t15.getText().length()>4)
                            t15.setText("3D\n"+Integer.toString(img[73]));
                        t15.setText(t15.getText()+"\n"+ti3.getText());
                        STI[14]= (String) t15.getText();
                        t15.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==16){
                        c=0;
                        confirm[15]=3;
                        leavel[ii][122]=3;
                        if(t16.getText().length()>4)
                            t16.setText("4D\n"+Integer.toString(img[97]));
                        t16.setText(t16.getText()+"\n"+ti3.getText());
                        STI[15]= (String) t16.getText();
                        t16.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    break;
                case R.id.TI4:
                    ti4.setEnabled(false);
                    ti4.setBackground(getResources().getDrawable(R.drawable.target3));
                    if(c==1){
                        c=0;
                        confirm[0]=4;
                        leavel[ii][17]=4;
                        if(t1.getText().length()>4)
                            t1.setText("1A\n"+Integer.toString(img[7]));
                        t1.setText(t1.getText()+"\n"+ti4.getText());
                        STI[0]= (String) t1.getText();
                        t1.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==2){
                        c=0;
                        confirm[1]=4;
                        leavel[ii][24]=4;
                        if(t2.getText().length()>4)
                            t2.setText("2A\n"+Integer.toString(img[31]));
                        t2.setText(t2.getText()+"\n"+ti4.getText());
                        STI[1]= (String) t2.getText();
                        t2.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==3){
                        c=0;
                        confirm[2]=4;
                        leavel[ii][31]=4;
                        if(t3.getText().length()>4)
                            t3.setText("3A\n"+Integer.toString(img[55]));
                        t3.setText(t3.getText()+"\n"+ti4.getText());
                        STI[2]= (String) t3.getText();
                        t3.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==4){
                        c=0;
                        confirm[3]=4;
                        leavel[ii][38]=4;
                        if(t4.getText().length()>4)
                            t4.setText("4A\n"+Integer.toString(img[79]));
                        t4.setText(t4.getText()+"\n"+ti4.getText());
                        STI[3]= (String) t4.getText();
                        t4.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==5){
                        c=0;
                        confirm[4]=4;
                        leavel[ii][45]=4;
                        if(t5.getText().length()>4)
                            t5.setText("1B\n"+Integer.toString(img[13]));
                        t5.setText(t5.getText()+"\n"+ti4.getText());
                        STI[4]= (String) t5.getText();
                        t5.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==6){
                        c=0;
                        confirm[5]=4;
                        leavel[ii][52]=4;
                        if(t6.getText().length()>4)
                            t6.setText("2B\n"+Integer.toString(img[37]));
                        t6.setText(t6.getText()+"\n"+ti4.getText());
                        STI[5]= (String) t6.getText();
                        t6.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==7){
                        c=0;
                        confirm[6]=4;
                        leavel[ii][59]=4;
                        if(t7.getText().length()>4)
                            t7.setText("3B\n"+Integer.toString(img[61]));
                        t7.setText(t7.getText()+"\n"+ti4.getText());
                        STI[6]= (String) t7.getText();
                        t7.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==8){
                        c=0;
                        confirm[7]=4;
                        leavel[ii][66]=4;
                        if(t8.getText().length()>4)
                            t8.setText("4B\n"+Integer.toString(img[85]));
                        t8.setText(t8.getText()+"\n"+ti4.getText());
                        STI[7]= (String) t8.getText();
                        t8.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==9){
                        c=0;
                        confirm[8]=4;
                        leavel[ii][73]=4;
                        if(t9.getText().length()>4)
                            t9.setText("1C\n"+Integer.toString(img[19]));
                        t9.setText(t9.getText()+"\n"+ti4.getText());
                        STI[8]= (String) t9.getText();
                        t9.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==10){
                        c=0;
                        confirm[9]=4;
                        leavel[ii][80]=4;
                        if(t10.getText().length()>4)
                            t10.setText("2C\n"+Integer.toString(img[43]));
                        t10.setText(t10.getText()+"\n"+ti4.getText());
                        STI[9]= (String) t10.getText();
                        t10.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==11){
                        c=0;
                        confirm[10]=4;
                        leavel[ii][87]=4;
                        if(t11.getText().length()>4)
                            t11.setText("3C\n"+Integer.toString(img[67]));
                        t11.setText(t11.getText()+"\n"+ti4.getText());
                        STI[10]= (String) t11.getText();
                        t11.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==12){
                        c=0;
                        confirm[11]=4;
                        leavel[ii][94]=4;
                        if(t12.getText().length()>4)
                            t12.setText("4C\n"+Integer.toString(img[91]));
                        t12.setText(t12.getText()+"\n"+ti4.getText());
                        STI[11]= (String) t12.getText();
                        t12.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==13){
                        c=0;
                        confirm[12]=4;
                        leavel[ii][101]=4;
                        if(t13.getText().length()>4)
                            t13.setText("1D\n"+Integer.toString(img[25]));
                        t13.setText(t13.getText()+"\n"+ti4.getText());
                        STI[12]= (String) t13.getText();
                        t13.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==14){
                        c=0;
                        confirm[13]=4;
                        leavel[ii][108]=4;
                        if(t14.getText().length()>4)
                            t14.setText("2D\n"+Integer.toString(img[49]));
                        t14.setText(t14.getText()+"\n"+ti4.getText());
                        STI[13]= (String) t14.getText();
                        t14.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==15){
                        c=0;
                        confirm[14]=4;
                        leavel[ii][115]=4;
                        if(t15.getText().length()>4)
                            t15.setText("3D\n"+Integer.toString(img[73]));
                        t15.setText(t15.getText()+"\n"+ti4.getText());
                        STI[14]= (String) t15.getText();
                        t15.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    if(c==16){
                        c=0;
                        confirm[15]=4;
                        leavel[ii][122]=4;
                        if(t16.getText().length()>4)
                            t16.setText("4D\n"+Integer.toString(img[97]));
                        t16.setText(t16.getText()+"\n"+ti4.getText());
                        STI[15]= (String) t16.getText();
                        t16.setBackground(getResources().getDrawable(R.drawable.target2));
                    }
                    break;
                case R.id.TI5:
                    if(c==1){
                        confirm[0]=5;
                        leavel[ii][17]=5;
                        if(t1.getText().length()>4)
                            t1.setText("1A\n"+Integer.toString(img[7]));
                        t1.setText(t1.getText()+"\n"+ti5.getText());
                    }
                    if(c==2){
                        confirm[1]=5;
                        leavel[ii][24]=5;
                        if(t2.getText().length()>4)
                            t2.setText("2A\n"+Integer.toString(img[31]));
                        t2.setText(t2.getText()+"\n"+ti5.getText());
                    }
                    if(c==3){
                        confirm[2]=5;
                        leavel[ii][31]=5;
                        if(t3.getText().length()>4)
                            t3.setText("3A\n"+Integer.toString(img[55]));
                        t3.setText(t3.getText()+"\n"+ti5.getText());
                    }
                    if(c==4){
                        confirm[3]=5;
                        leavel[ii][38]=5;
                        if(t4.getText().length()>4)
                            t4.setText("4A\n"+Integer.toString(img[79]));
                        t4.setText(t4.getText()+"\n"+ti5.getText());
                    }
                    if(c==5){
                        confirm[4]=5;
                        leavel[ii][45]=5;
                        if(t5.getText().length()>4)
                            t5.setText("1B\n"+Integer.toString(img[13]));
                        t5.setText(t5.getText()+"\n"+ti5.getText());
                    }
                    if(c==6){
                        confirm[5]=5;
                        leavel[ii][52]=5;
                        if(t6.getText().length()>4)
                            t6.setText("2B\n"+Integer.toString(img[37]));
                        t6.setText(t6.getText()+"\n"+ti5.getText());
                    }
                    if(c==7){
                        confirm[6]=5;
                        leavel[ii][59]=5;
                        if(t7.getText().length()>4)
                            t7.setText("3B\n"+Integer.toString(img[61]));
                        t7.setText(t7.getText()+"\n"+ti5.getText());
                    }
                    if(c==8){
                        confirm[7]=5;
                        leavel[ii][66]=5;
                        if(t8.getText().length()>4)
                            t8.setText("4B\n"+Integer.toString(img[85]));
                        t8.setText(t8.getText()+"\n"+ti5.getText());
                    }
                    if(c==9){
                        confirm[8]=5;
                        leavel[ii][73]=5;
                        if(t9.getText().length()>4)
                            t9.setText("1C\n"+Integer.toString(img[19]));
                        t9.setText(t9.getText()+"\n"+ti5.getText());
                    }
                    if(c==10){
                        confirm[9]=5;
                        leavel[ii][80]=5;
                        if(t10.getText().length()>4)
                            t10.setText("2C\n"+Integer.toString(img[43]));
                        t10.setText(t10.getText()+"\n"+ti5.getText());
                    }
                    if(c==11){
                        confirm[10]=5;
                        leavel[ii][87]=5;
                        if(t11.getText().length()>4)
                            t11.setText("3C\n"+Integer.toString(img[67]));
                        t11.setText(t11.getText()+"\n"+ti5.getText());
                    }
                    if(c==12){
                        confirm[11]=5;
                        leavel[ii][94]=5;
                        if(t12.getText().length()>4)
                            t12.setText("4C\n"+Integer.toString(img[91]));
                        t12.setText(t12.getText()+"\n"+ti5.getText());
                    }
                    if(c==13){
                        confirm[12]=5;
                        leavel[ii][101]=5;
                        if(t13.getText().length()>4)
                            t13.setText("1D\n"+Integer.toString(img[25]));
                        t13.setText(t13.getText()+"\n"+ti5.getText());
                    }
                    if(c==14){
                        confirm[13]=5;
                        leavel[ii][108]=5;
                        if(t14.getText().length()>4)
                            t14.setText("2D\n"+Integer.toString(img[49]));
                        t14.setText(t14.getText()+"\n"+ti5.getText());
                    }
                    if(c==15){
                        confirm[14]=5;
                        leavel[ii][115]=5;
                        if(t15.getText().length()>4)
                            t15.setText("3D\n"+Integer.toString(img[73]));
                        t15.setText(t15.getText()+"\n"+ti5.getText());
                    }
                    if(c==16){
                        confirm[15]=5;
                        leavel[ii][122]=5;
                        if(t16.getText().length()>4)
                            t16.setText("4D\n"+Integer.toString(img[97]));
                        t16.setText(t16.getText()+"\n"+ti5.getText());
                    }
                    break;
                case R.id.TI6:
                    if(c==1){
                        if(t1.getText().length()>4)
                            t1.setText("1A\n"+Integer.toString(img[7]));
                        t1.setText(t1.getText()+"\n"+ti6.getText());
                    }
                    if(c==2){
                        if(t2.getText().length()>4)
                            t2.setText("2A\n"+Integer.toString(img[31]));
                        t2.setText(t2.getText()+"\n"+ti6.getText());
                    }
                    if(c==3){
                        if(t3.getText().length()>4)
                            t3.setText("3A\n"+Integer.toString(img[55]));
                        t3.setText(t3.getText()+"\n"+ti6.getText());
                    }
                    if(c==4){
                        if(t4.getText().length()>4)
                            t4.setText("4A\n"+Integer.toString(img[79]));
                        t4.setText(t4.getText()+"\n"+ti6.getText());
                    }
                    if(c==5){
                        if(t5.getText().length()>4)
                            t5.setText("1B\n"+Integer.toString(img[13]));
                        t5.setText(t5.getText()+"\n"+ti6.getText());
                    }
                    if(c==6){
                        if(t6.getText().length()>4)
                            t6.setText("2B\n"+Integer.toString(img[37]));
                        t6.setText(t6.getText()+"\n"+ti6.getText());
                    }
                    if(c==7){
                        if(t7.getText().length()>4)
                            t7.setText("3B\n"+Integer.toString(img[61]));
                        t7.setText(t7.getText()+"\n"+ti6.getText());
                    }
                    if(c==8){
                        if(t8.getText().length()>4)
                            t8.setText("4B\n"+Integer.toString(img[85]));
                        t8.setText(t8.getText()+"\n"+ti6.getText());
                    }
                    if(c==9){
                        if(t9.getText().length()>4)
                            t9.setText("1C\n"+Integer.toString(img[19]));
                        t9.setText(t9.getText()+"\n"+ti6.getText());
                    }
                    if(c==10){
                        if(t10.getText().length()>4)
                            t10.setText("2C\n"+Integer.toString(img[43]));
                        t10.setText(t10.getText()+"\n"+ti6.getText());
                    }
                    if(c==11){
                        if(t11.getText().length()>4)
                            t11.setText("3C\n"+Integer.toString(img[67]));
                        t11.setText(t11.getText()+"\n"+ti6.getText());
                    }
                    if(c==12){
                        if(t12.getText().length()>4)
                            t12.setText("4C\n"+Integer.toString(img[91]));
                        t12.setText(t12.getText()+"\n"+ti6.getText());
                    }
                    if(c==13){
                        if(t13.getText().length()>4)
                            t13.setText("1D\n"+Integer.toString(img[25]));
                        t13.setText(t13.getText()+"\n"+ti6.getText());
                    }
                    if(c==14){
                        if(t14.getText().length()>4)
                            t14.setText("2D\n"+Integer.toString(img[49]));
                        t14.setText(t14.getText()+"\n"+ti6.getText());
                    }
                    if(c==15){
                        if(t15.getText().length()>4)
                            t15.setText("3D\n"+Integer.toString(img[73]));
                        t15.setText(t15.getText()+"\n"+ti6.getText());
                    }
                    if(c==16){
                        if(t16.getText().length()>4)
                            t16.setText("4D\n"+Integer.toString(img[97]));
                        t16.setText(t16.getText()+"\n"+ti6.getText());
                    }
                    break;
                case R.id.TI7:
                    if(c==1){
                        if(t1.getText().length()>4)
                            t1.setText("1A\n"+Integer.toString(img[7]));
                        t1.setText(t1.getText()+"\n"+ti7.getText());
                    }
                    if(c==2){
                        if(t2.getText().length()>4)
                            t2.setText("2A\n"+Integer.toString(img[31]));
                        t2.setText(t2.getText()+"\n"+ti7.getText());
                    }
                    if(c==3){
                        if(t3.getText().length()>4)
                            t3.setText("3A\n"+Integer.toString(img[55]));
                        t3.setText(t3.getText()+"\n"+ti7.getText());
                    }
                    if(c==4){
                        if(t4.getText().length()>4)
                            t4.setText("4A\n"+Integer.toString(img[79]));
                        t4.setText(t4.getText()+"\n"+ti7.getText());
                    }
                    if(c==5){
                        if(t5.getText().length()>4)
                            t5.setText("1B\n"+Integer.toString(img[13]));
                        t5.setText(t5.getText()+"\n"+ti7.getText());
                    }
                    if(c==6){
                        if(t6.getText().length()>4)
                            t6.setText("2B\n"+Integer.toString(img[37]));
                        t6.setText(t6.getText()+"\n"+ti7.getText());
                    }
                    if(c==7){
                        if(t7.getText().length()>4)
                            t7.setText("3B\n"+Integer.toString(img[61]));
                        t7.setText(t7.getText()+"\n"+ti7.getText());
                    }
                    if(c==8){
                        if(t8.getText().length()>4)
                            t8.setText("4B\n"+Integer.toString(img[85]));
                        t8.setText(t8.getText()+"\n"+ti7.getText());
                    }
                    if(c==9){
                        if(t9.getText().length()>4)
                            t9.setText("1C\n"+Integer.toString(img[19]));
                        t9.setText(t9.getText()+"\n"+ti7.getText());
                    }
                    if(c==10){
                        if(t10.getText().length()>4)
                            t10.setText("2C\n"+Integer.toString(img[43]));
                        t10.setText(t10.getText()+"\n"+ti7.getText());
                    }
                    if(c==11){
                        if(t11.getText().length()>4)
                            t11.setText("3C\n"+Integer.toString(img[67]));
                        t11.setText(t11.getText()+"\n"+ti7.getText());
                    }
                    if(c==12){
                        if(t12.getText().length()>4)
                            t12.setText("4C\n"+Integer.toString(img[91]));
                        t12.setText(t12.getText()+"\n"+ti7.getText());
                    }
                    if(c==13){
                        if(t13.getText().length()>4)
                            t13.setText("1D\n"+Integer.toString(img[25]));
                        t13.setText(t13.getText()+"\n"+ti7.getText());
                    }
                    if(c==14){
                        if(t14.getText().length()>4)
                            t14.setText("2D\n"+Integer.toString(img[49]));
                        t14.setText(t14.getText()+"\n"+ti7.getText());
                    }
                    if(c==15){
                        if(t15.getText().length()>4)
                            t15.setText("3D\n"+Integer.toString(img[73]));
                        t15.setText(t15.getText()+"\n"+ti7.getText());
                    }
                    if(c==16){
                        if(t16.getText().length()>4)
                            t16.setText("4D\n"+Integer.toString(img[97]));
                        t16.setText(t16.getText()+"\n"+ti7.getText());
                    }
                    break;
                case R.id.TI8:
                    if(c==1){
                        if(t1.getText().length()>4)
                            t1.setText("1A\n"+Integer.toString(img[7]));
                        t1.setText(t1.getText()+"\n"+ti8.getText());
                    }
                    if(c==2){
                        if(t2.getText().length()>4)
                            t2.setText("2A\n"+Integer.toString(img[31]));
                        t2.setText(t2.getText()+"\n"+ti8.getText());
                    }
                    if(c==3){
                        if(t3.getText().length()>4)
                            t3.setText("3A\n"+Integer.toString(img[55]));
                        t3.setText(t3.getText()+"\n"+ti8.getText());
                    }
                    if(c==4){
                        if(t4.getText().length()>4)
                            t4.setText("4A\n"+Integer.toString(img[79]));
                        t4.setText(t4.getText()+"\n"+ti8.getText());
                    }
                    if(c==5){
                        if(t5.getText().length()>4)
                            t5.setText("1B\n"+Integer.toString(img[13]));
                        t5.setText(t5.getText()+"\n"+ti8.getText());
                    }
                    if(c==6){
                        if(t6.getText().length()>4)
                            t6.setText("2B\n"+Integer.toString(img[37]));
                        t6.setText(t6.getText()+"\n"+ti8.getText());
                    }
                    if(c==7){
                        if(t7.getText().length()>4)
                            t7.setText("3B\n"+Integer.toString(img[61]));
                        t7.setText(t7.getText()+"\n"+ti8.getText());
                    }
                    if(c==8){
                        if(t8.getText().length()>4)
                            t8.setText("4B\n"+Integer.toString(img[85]));
                        t8.setText(t8.getText()+"\n"+ti8.getText());
                    }
                    if(c==9){
                        if(t9.getText().length()>4)
                            t9.setText("1C\n"+Integer.toString(img[19]));
                        t9.setText(t9.getText()+"\n"+ti8.getText());
                    }
                    if(c==10){
                        if(t10.getText().length()>4)
                            t10.setText("2C\n"+Integer.toString(img[43]));
                        t10.setText(t10.getText()+"\n"+ti8.getText());
                    }
                    if(c==11){
                        if(t11.getText().length()>4)
                            t11.setText("3C\n"+Integer.toString(img[67]));
                        t11.setText(t11.getText()+"\n"+ti8.getText());
                    }
                    if(c==12){
                        if(t12.getText().length()>4)
                            t12.setText("4C\n"+Integer.toString(img[91]));
                        t12.setText(t12.getText()+"\n"+ti8.getText());
                    }
                    if(c==13){
                        if(t13.getText().length()>4)
                            t13.setText("1D\n"+Integer.toString(img[25]));
                        t13.setText(t13.getText()+"\n"+ti8.getText());
                    }
                    if(c==14){
                        if(t14.getText().length()>4)
                            t14.setText("2D\n"+Integer.toString(img[49]));
                        t14.setText(t14.getText()+"\n"+ti8.getText());
                    }
                    if(c==15){
                        if(t15.getText().length()>4)
                            t15.setText("3D\n"+Integer.toString(img[73]));
                        t15.setText(t15.getText()+"\n"+ti8.getText());
                    }
                    if(c==16){
                        if(t16.getText().length()>4)
                            t16.setText("4D\n"+Integer.toString(img[97]));
                        t16.setText(t16.getText()+"\n"+ti8.getText());
                    }
                    break;
                case R.id.TI9:
                    if(c==1){
                        if(t1.getText().length()>4)
                            t1.setText("1A\n"+Integer.toString(img[7]));
                        t1.setText(t1.getText()+"\n"+ti9.getText());
                    }
                    if(c==2){
                        if(t2.getText().length()>4)
                            t2.setText("2A\n"+Integer.toString(img[31]));
                        t2.setText(t2.getText()+"\n"+ti9.getText());
                    }
                    if(c==3){
                        if(t3.getText().length()>4)
                            t3.setText("3A\n"+Integer.toString(img[55]));
                        t3.setText(t3.getText()+"\n"+ti9.getText());
                    }
                    if(c==4){
                        if(t4.getText().length()>4)
                            t4.setText("4A\n"+Integer.toString(img[79]));
                        t4.setText(t4.getText()+"\n"+ti9.getText());
                    }
                    if(c==5){
                        if(t5.getText().length()>4)
                            t5.setText("1B\n"+Integer.toString(img[13]));
                        t5.setText(t5.getText()+"\n"+ti9.getText());
                    }
                    if(c==6){
                        if(t6.getText().length()>4)
                            t6.setText("2B\n"+Integer.toString(img[37]));
                        t6.setText(t6.getText()+"\n"+ti9.getText());
                    }
                    if(c==7){
                        if(t7.getText().length()>4)
                            t7.setText("3B\n"+Integer.toString(img[61]));
                        t7.setText(t7.getText()+"\n"+ti9.getText());
                    }
                    if(c==8){
                        if(t8.getText().length()>4)
                            t8.setText("4B\n"+Integer.toString(img[85]));
                        t8.setText(t8.getText()+"\n"+ti9.getText());
                    }
                    if(c==9){
                        if(t9.getText().length()>4)
                            t9.setText("1C\n"+Integer.toString(img[19]));
                        t9.setText(t9.getText()+"\n"+ti9.getText());
                    }
                    if(c==10){
                        if(t10.getText().length()>4)
                            t10.setText("2C\n"+Integer.toString(img[43]));
                        t10.setText(t10.getText()+"\n"+ti9.getText());
                    }
                    if(c==11){
                        if(t11.getText().length()>4)
                            t11.setText("3C\n"+Integer.toString(img[67]));
                        t11.setText(t11.getText()+"\n"+ti9.getText());
                    }
                    if(c==12){
                        if(t12.getText().length()>4)
                            t12.setText("4C\n"+Integer.toString(img[91]));
                        t12.setText(t12.getText()+"\n"+ti9.getText());
                    }
                    if(c==13){
                        if(t13.getText().length()>4)
                            t13.setText("1D\n"+Integer.toString(img[25]));
                        t13.setText(t13.getText()+"\n"+ti9.getText());
                    }
                    if(c==14){
                        if(t14.getText().length()>4)
                            t14.setText("2D\n"+Integer.toString(img[49]));
                        t14.setText(t14.getText()+"\n"+ti9.getText());
                    }
                    if(c==15){
                        if(t15.getText().length()>4)
                            t15.setText("3D\n"+Integer.toString(img[73]));
                        t15.setText(t15.getText()+"\n"+ti9.getText());
                    }
                    if(c==16){
                        if(t16.getText().length()>4)
                            t16.setText("4D\n"+Integer.toString(img[97]));
                        t16.setText(t16.getText()+"\n"+ti9.getText());
                    }
                    break;
                case R.id.TI10:
                    if(c==1){
                        if(t1.getText().length()>4)
                            t1.setText("1A\n"+Integer.toString(img[7]));
                        t1.setText(t1.getText()+"\n"+ti10.getText());
                    }
                    if(c==2){
                        if(t2.getText().length()>4)
                            t2.setText("2A\n"+Integer.toString(img[31]));
                        t2.setText(t2.getText()+"\n"+ti10.getText());
                    }
                    if(c==3){
                        if(t3.getText().length()>4)
                            t3.setText("3A\n"+Integer.toString(img[55]));
                        t3.setText(t3.getText()+"\n"+ti10.getText());
                    }
                    if(c==4){
                        if(t4.getText().length()>4)
                            t4.setText("4A\n"+Integer.toString(img[79]));
                        t4.setText(t4.getText()+"\n"+ti10.getText());
                    }
                    if(c==5){
                        if(t5.getText().length()>4)
                            t5.setText("1B\n"+Integer.toString(img[13]));
                        t5.setText(t5.getText()+"\n"+ti10.getText());
                    }
                    if(c==6){
                        if(t6.getText().length()>4)
                            t6.setText("2B\n"+Integer.toString(img[37]));
                        t6.setText(t6.getText()+"\n"+ti10.getText());
                    }
                    if(c==7){
                        if(t7.getText().length()>4)
                            t7.setText("3B\n"+Integer.toString(img[61]));
                        t7.setText(t7.getText()+"\n"+ti10.getText());
                    }
                    if(c==8){
                        if(t8.getText().length()>4)
                            t8.setText("4B\n"+Integer.toString(img[85]));
                        t8.setText(t8.getText()+"\n"+ti10.getText());
                    }
                    if(c==9){
                        if(t9.getText().length()>4)
                            t9.setText("1C\n"+Integer.toString(img[19]));
                        t9.setText(t9.getText()+"\n"+ti10.getText());
                    }
                    if(c==10){
                        if(t10.getText().length()>4)
                            t10.setText("2C\n"+Integer.toString(img[43]));
                        t10.setText(t10.getText()+"\n"+ti10.getText());
                    }
                    if(c==11){
                        if(t11.getText().length()>4)
                            t11.setText("3C\n"+Integer.toString(img[67]));
                        t11.setText(t11.getText()+"\n"+ti10.getText());
                    }
                    if(c==12){
                        if(t12.getText().length()>4)
                            t12.setText("4C\n"+Integer.toString(img[91]));
                        t12.setText(t12.getText()+"\n"+ti10.getText());
                    }
                    if(c==13){
                        if(t13.getText().length()>4)
                            t13.setText("1D\n"+Integer.toString(img[25]));
                        t13.setText(t13.getText()+"\n"+ti10.getText());
                    }
                    if(c==14){
                        if(t14.getText().length()>4)
                            t14.setText("2D\n"+Integer.toString(img[49]));
                        t14.setText(t14.getText()+"\n"+ti10.getText());
                    }
                    if(c==15){
                        if(t15.getText().length()>4)
                            t15.setText("3D\n"+Integer.toString(img[73]));
                        t15.setText(t15.getText()+"\n"+ti10.getText());
                    }
                    if(c==16){
                        if(t16.getText().length()>4)
                            t16.setText("4D\n"+Integer.toString(img[97]));
                        t16.setText(t16.getText()+"\n"+ti10.getText());
                    }
                    break;
                case R.id.TI11:
                    if(c==1){
                        if(t1.getText().length()>4)
                            t1.setText("1A\n"+Integer.toString(img[7]));
                        t1.setText(t1.getText()+"\n"+ti11.getText());
                    }
                    if(c==2){
                        if(t2.getText().length()>4)
                            t2.setText("2A\n"+Integer.toString(img[31]));
                        t2.setText(t2.getText()+"\n"+ti11.getText());
                    }
                    if(c==3){
                        if(t3.getText().length()>4)
                            t3.setText("3A\n"+Integer.toString(img[55]));
                        t3.setText(t3.getText()+"\n"+ti11.getText());
                    }
                    if(c==4){
                        if(t4.getText().length()>4)
                            t4.setText("4A\n"+Integer.toString(img[79]));
                        t4.setText(t4.getText()+"\n"+ti11.getText());
                    }
                    if(c==5){
                        if(t5.getText().length()>4)
                            t5.setText("1B\n"+Integer.toString(img[13]));
                        t5.setText(t5.getText()+"\n"+ti11.getText());
                    }
                    if(c==6){
                        if(t6.getText().length()>4)
                            t6.setText("2B\n"+Integer.toString(img[37]));
                        t6.setText(t6.getText()+"\n"+ti11.getText());
                    }
                    if(c==7){
                        if(t7.getText().length()>4)
                            t7.setText("3B\n"+Integer.toString(img[61]));
                        t7.setText(t7.getText()+"\n"+ti11.getText());
                    }
                    if(c==8){
                        if(t8.getText().length()>4)
                            t8.setText("4B\n"+Integer.toString(img[85]));
                        t8.setText(t8.getText()+"\n"+ti11.getText());
                    }
                    if(c==9){
                        if(t9.getText().length()>4)
                            t9.setText("1C\n"+Integer.toString(img[19]));
                        t9.setText(t9.getText()+"\n"+ti11.getText());
                    }
                    if(c==10){
                        if(t10.getText().length()>4)
                            t10.setText("2C\n"+Integer.toString(img[43]));
                        t10.setText(t10.getText()+"\n"+ti11.getText());
                    }
                    if(c==11){
                        if(t11.getText().length()>4)
                            t11.setText("3C\n"+Integer.toString(img[67]));
                        t11.setText(t11.getText()+"\n"+ti11.getText());
                    }
                    if(c==12){
                        if(t12.getText().length()>4)
                            t12.setText("4C\n"+Integer.toString(img[91]));
                        t12.setText(t12.getText()+"\n"+ti11.getText());
                    }
                    if(c==13){
                        if(t13.getText().length()>4)
                            t13.setText("1D\n"+Integer.toString(img[25]));
                        t13.setText(t13.getText()+"\n"+ti11.getText());
                    }
                    if(c==14){
                        if(t14.getText().length()>4)
                            t14.setText("2D\n"+Integer.toString(img[49]));
                        t14.setText(t14.getText()+"\n"+ti11.getText());
                    }
                    if(c==15){
                        if(t15.getText().length()>4)
                            t15.setText("3D\n"+Integer.toString(img[73]));
                        t15.setText(t15.getText()+"\n"+ti11.getText());
                    }
                    if(c==16){
                        if(t16.getText().length()>4)
                            t16.setText("4D\n"+Integer.toString(img[97]));
                        t16.setText(t16.getText()+"\n"+ti11.getText());
                    }
                    break;
                case R.id.TI12:
                    if(c==1){
                        if(t1.getText().length()>4)
                            t1.setText("1A\n"+Integer.toString(img[7]));
                        t1.setText(t1.getText()+"\n"+ti12.getText());
                    }
                    if(c==2){
                        if(t2.getText().length()>4)
                            t2.setText("2A\n"+Integer.toString(img[31]));
                        t2.setText(t2.getText()+"\n"+ti12.getText());
                    }
                    if(c==3){
                        if(t3.getText().length()>4)
                            t3.setText("3A\n"+Integer.toString(img[55]));
                        t3.setText(t3.getText()+"\n"+ti12.getText());
                    }
                    if(c==4){
                        if(t4.getText().length()>4)
                            t4.setText("4A\n"+Integer.toString(img[79]));
                        t4.setText(t4.getText()+"\n"+ti12.getText());
                    }
                    if(c==5){
                        if(t5.getText().length()>4)
                            t5.setText("1B\n"+Integer.toString(img[13]));
                        t5.setText(t5.getText()+"\n"+ti12.getText());
                    }
                    if(c==6){
                        if(t6.getText().length()>4)
                            t6.setText("2B\n"+Integer.toString(img[37]));
                        t6.setText(t6.getText()+"\n"+ti12.getText());
                    }
                    if(c==7){
                        if(t7.getText().length()>4)
                            t7.setText("3B\n"+Integer.toString(img[61]));
                        t7.setText(t7.getText()+"\n"+ti12.getText());
                    }
                    if(c==8){
                        if(t8.getText().length()>4)
                            t8.setText("4B\n"+Integer.toString(img[85]));
                        t8.setText(t8.getText()+"\n"+ti12.getText());
                    }
                    if(c==9){
                        if(t9.getText().length()>4)
                            t9.setText("1C\n"+Integer.toString(img[19]));
                        t9.setText(t9.getText()+"\n"+ti12.getText());
                    }
                    if(c==10){
                        if(t10.getText().length()>4)
                            t10.setText("2C\n"+Integer.toString(img[43]));
                        t10.setText(t10.getText()+"\n"+ti12.getText());
                    }
                    if(c==11){
                        if(t11.getText().length()>4)
                            t11.setText("3C\n"+Integer.toString(img[67]));
                        t11.setText(t11.getText()+"\n"+ti12.getText());
                    }
                    if(c==12){
                        if(t12.getText().length()>4)
                            t12.setText("4C\n"+Integer.toString(img[91]));
                        t12.setText(t12.getText()+"\n"+ti12.getText());
                    }
                    if(c==13){
                        if(t13.getText().length()>4)
                            t13.setText("1D\n"+Integer.toString(img[25]));
                        t13.setText(t13.getText()+"\n"+ti12.getText());
                    }
                    if(c==14){
                        if(t14.getText().length()>4)
                            t14.setText("2D\n"+Integer.toString(img[49]));
                        t14.setText(t14.getText()+"\n"+ti12.getText());
                    }
                    if(c==15){
                        if(t15.getText().length()>4)
                            t15.setText("3D\n"+Integer.toString(img[73]));
                        t15.setText(t15.getText()+"\n"+ti12.getText());
                    }
                    if(c==16){
                        if(t16.getText().length()>4)
                            t16.setText("4D\n"+Integer.toString(img[97]));
                        t16.setText(t16.getText()+"\n"+ti12.getText());
                    }
                    break;
                case R.id.TI13:
                    if(c==1){
                        if(t1.getText().length()>4)
                            t1.setText("1A\n"+Integer.toString(img[7]));
                        t1.setText(t1.getText()+"\n"+ti13.getText());
                    }
                    if(c==2){
                        if(t2.getText().length()>4)
                            t2.setText("2A\n"+Integer.toString(img[31]));
                        t2.setText(t2.getText()+"\n"+ti13.getText());
                    }
                    if(c==3){
                        if(t3.getText().length()>4)
                            t3.setText("3A\n"+Integer.toString(img[55]));
                        t3.setText(t3.getText()+"\n"+ti13.getText());
                    }
                    if(c==4){
                        if(t4.getText().length()>4)
                            t4.setText("4A\n"+Integer.toString(img[79]));
                        t4.setText(t4.getText()+"\n"+ti13.getText());
                    }
                    if(c==5){
                        if(t5.getText().length()>4)
                            t5.setText("1B\n"+Integer.toString(img[13]));
                        t5.setText(t5.getText()+"\n"+ti13.getText());
                    }
                    if(c==6){
                        if(t6.getText().length()>4)
                            t6.setText("2B\n"+Integer.toString(img[37]));
                        t6.setText(t6.getText()+"\n"+ti13.getText());
                    }
                    if(c==7){
                        if(t7.getText().length()>4)
                            t7.setText("3B\n"+Integer.toString(img[61]));
                        t7.setText(t7.getText()+"\n"+ti13.getText());
                    }
                    if(c==8){
                        if(t8.getText().length()>4)
                            t8.setText("4B\n"+Integer.toString(img[85]));
                        t8.setText(t8.getText()+"\n"+ti13.getText());
                    }
                    if(c==9){
                        if(t9.getText().length()>4)
                            t9.setText("1C\n"+Integer.toString(img[19]));
                        t9.setText(t9.getText()+"\n"+ti13.getText());
                    }
                    if(c==10){
                        if(t10.getText().length()>4)
                            t10.setText("2C\n"+Integer.toString(img[43]));
                        t10.setText(t10.getText()+"\n"+ti13.getText());
                    }
                    if(c==11){
                        if(t11.getText().length()>4)
                            t11.setText("3C\n"+Integer.toString(img[67]));
                        t11.setText(t11.getText()+"\n"+ti13.getText());
                    }
                    if(c==12){
                        if(t12.getText().length()>4)
                            t12.setText("4C\n"+Integer.toString(img[91]));
                        t12.setText(t12.getText()+"\n"+ti13.getText());
                    }
                    if(c==13){
                        if(t13.getText().length()>4)
                            t13.setText("1D\n"+Integer.toString(img[25]));
                        t13.setText(t13.getText()+"\n"+ti13.getText());
                    }
                    if(c==14){
                        if(t14.getText().length()>4)
                            t14.setText("2D\n"+Integer.toString(img[49]));
                        t14.setText(t14.getText()+"\n"+ti13.getText());
                    }
                    if(c==15){
                        if(t15.getText().length()>4)
                            t15.setText("3D\n"+Integer.toString(img[73]));
                        t15.setText(t15.getText()+"\n"+ti13.getText());
                    }
                    if(c==16){
                        if(t16.getText().length()>4)
                            t16.setText("4D\n"+Integer.toString(img[97]));
                        t16.setText(t16.getText()+"\n"+ti13.getText());
                    }
                    break;
                case R.id.TI14:
                    if(c==1){
                        if(t1.getText().length()>4)
                            t1.setText("1A\n"+Integer.toString(img[7]));
                        t1.setText(t1.getText()+"\n"+ti14.getText());
                    }
                    if(c==2){
                        if(t2.getText().length()>4)
                            t2.setText("2A\n"+Integer.toString(img[31]));
                        t2.setText(t2.getText()+"\n"+ti14.getText());
                    }
                    if(c==3){
                        if(t3.getText().length()>4)
                            t3.setText("3A\n"+Integer.toString(img[55]));
                        t3.setText(t3.getText()+"\n"+ti14.getText());
                    }
                    if(c==4){
                        if(t4.getText().length()>4)
                            t4.setText("4A\n"+Integer.toString(img[79]));
                        t4.setText(t4.getText()+"\n"+ti14.getText());
                    }
                    if(c==5){
                        if(t5.getText().length()>4)
                            t5.setText("1B\n"+Integer.toString(img[13]));
                        t5.setText(t5.getText()+"\n"+ti14.getText());
                    }
                    if(c==6){
                        if(t6.getText().length()>4)
                            t6.setText("2B\n"+Integer.toString(img[37]));
                        t6.setText(t6.getText()+"\n"+ti14.getText());
                    }
                    if(c==7){
                        if(t7.getText().length()>4)
                            t7.setText("3B\n"+Integer.toString(img[61]));
                        t7.setText(t7.getText()+"\n"+ti14.getText());
                    }
                    if(c==8){
                        if(t8.getText().length()>4)
                            t8.setText("4B\n"+Integer.toString(img[85]));
                        t8.setText(t8.getText()+"\n"+ti14.getText());
                    }
                    if(c==9){
                        if(t9.getText().length()>4)
                            t9.setText("1C\n"+Integer.toString(img[19]));
                        t9.setText(t9.getText()+"\n"+ti14.getText());
                    }
                    if(c==10){
                        if(t10.getText().length()>4)
                            t10.setText("2C\n"+Integer.toString(img[43]));
                        t10.setText(t10.getText()+"\n"+ti14.getText());
                    }
                    if(c==11){
                        if(t11.getText().length()>4)
                            t11.setText("3C\n"+Integer.toString(img[67]));
                        t11.setText(t11.getText()+"\n"+ti14.getText());
                    }
                    if(c==12){
                        if(t12.getText().length()>4)
                            t12.setText("4C\n"+Integer.toString(img[91]));
                        t12.setText(t12.getText()+"\n"+ti14.getText());
                    }
                    if(c==13){
                        if(t13.getText().length()>4)
                            t13.setText("1D\n"+Integer.toString(img[25]));
                        t13.setText(t13.getText()+"\n"+ti14.getText());
                    }
                    if(c==14){
                        if(t14.getText().length()>4)
                            t14.setText("2D\n"+Integer.toString(img[49]));
                        t14.setText(t14.getText()+"\n"+ti14.getText());
                    }
                    if(c==15){
                        if(t15.getText().length()>4)
                            t15.setText("3D\n"+Integer.toString(img[73]));
                        t15.setText(t15.getText()+"\n"+ti14.getText());
                    }
                    if(c==16){
                        if(t16.getText().length()>4)
                            t16.setText("4D\n"+Integer.toString(img[97]));
                        t16.setText(t16.getText()+"\n"+ti14.getText());
                    }
                    break;
                case R.id.TI15:
                    if(c==1){
                        if(t1.getText().length()>4)
                            t1.setText("1A\n"+Integer.toString(img[7]));
                        t1.setText(t1.getText()+"\n"+ti15.getText());
                    }
                    if(c==2){
                        if(t2.getText().length()>4)
                            t2.setText("2A\n"+Integer.toString(img[31]));
                        t2.setText(t2.getText()+"\n"+ti15.getText());
                    }
                    if(c==3){
                        if(t3.getText().length()>4)
                            t3.setText("3A\n"+Integer.toString(img[55]));
                        t3.setText(t3.getText()+"\n"+ti15.getText());
                    }
                    if(c==4){
                        if(t4.getText().length()>4)
                            t4.setText("4A\n"+Integer.toString(img[79]));
                        t4.setText(t4.getText()+"\n"+ti15.getText());
                    }
                    if(c==5){
                        if(t5.getText().length()>4)
                            t5.setText("1B\n"+Integer.toString(img[13]));
                        t5.setText(t5.getText()+"\n"+ti15.getText());
                    }
                    if(c==6){
                        if(t6.getText().length()>4)
                            t6.setText("2B\n"+Integer.toString(img[37]));
                        t6.setText(t6.getText()+"\n"+ti15.getText());
                    }
                    if(c==7){
                        if(t7.getText().length()>4)
                            t7.setText("3B\n"+Integer.toString(img[61]));
                        t7.setText(t7.getText()+"\n"+ti15.getText());
                    }
                    if(c==8){
                        if(t8.getText().length()>4)
                            t8.setText("4B\n"+Integer.toString(img[85]));
                        t8.setText(t8.getText()+"\n"+ti15.getText());
                    }
                    if(c==9){
                        if(t9.getText().length()>4)
                            t9.setText("1C\n"+Integer.toString(img[19]));
                        t9.setText(t9.getText()+"\n"+ti15.getText());
                    }
                    if(c==10){
                        if(t10.getText().length()>4)
                            t10.setText("2C\n"+Integer.toString(img[43]));
                        t10.setText(t10.getText()+"\n"+ti15.getText());
                    }
                    if(c==11){
                        if(t11.getText().length()>4)
                            t11.setText("3C\n"+Integer.toString(img[67]));
                        t11.setText(t11.getText()+"\n"+ti15.getText());
                    }
                    if(c==12){
                        if(t12.getText().length()>4)
                            t12.setText("4C\n"+Integer.toString(img[91]));
                        t12.setText(t12.getText()+"\n"+ti15.getText());
                    }
                    if(c==13){
                        if(t13.getText().length()>4)
                            t13.setText("1D\n"+Integer.toString(img[25]));
                        t13.setText(t13.getText()+"\n"+ti15.getText());
                    }
                    if(c==14){
                        if(t14.getText().length()>4)
                            t14.setText("2D\n"+Integer.toString(img[49]));
                        t14.setText(t14.getText()+"\n"+ti15.getText());
                    }
                    if(c==15){
                        if(t15.getText().length()>4)
                            t15.setText("3D\n"+Integer.toString(img[73]));
                        t15.setText(t15.getText()+"\n"+ti15.getText());
                    }
                    if(c==16){
                        if(t16.getText().length()>4)
                            t16.setText("4D\n"+Integer.toString(img[97]));
                        t16.setText(t16.getText()+"\n"+ti15.getText());
                    }
                    break;
                case R.id.TI16:
                    if(c==1){
                        if(t1.getText().length()>4)
                            t1.setText("1A\n"+Integer.toString(img[7]));
                        t1.setText(t1.getText()+"\n"+ti16.getText());
                    }
                    if(c==2){
                        if(t2.getText().length()>4)
                            t2.setText("2A\n"+Integer.toString(img[31]));
                        t2.setText(t2.getText()+"\n"+ti16.getText());
                    }
                    if(c==3){
                        if(t3.getText().length()>4)
                            t3.setText("3A\n"+Integer.toString(img[55]));
                        t3.setText(t3.getText()+"\n"+ti16.getText());
                    }
                    if(c==4){
                        if(t4.getText().length()>4)
                            t4.setText("4A\n"+Integer.toString(img[79]));
                        t4.setText(t4.getText()+"\n"+ti16.getText());
                    }
                    if(c==5){
                        if(t5.getText().length()>4)
                            t5.setText("1B\n"+Integer.toString(img[13]));
                        t5.setText(t5.getText()+"\n"+ti16.getText());
                    }
                    if(c==6){
                        if(t6.getText().length()>4)
                            t6.setText("2B\n"+Integer.toString(img[37]));
                        t6.setText(t6.getText()+"\n"+ti16.getText());
                    }
                    if(c==7){
                        if(t7.getText().length()>4)
                            t7.setText("3B\n"+Integer.toString(img[61]));
                        t7.setText(t7.getText()+"\n"+ti16.getText());
                    }
                    if(c==8){
                        if(t8.getText().length()>4)
                            t8.setText("4B\n"+Integer.toString(img[85]));
                        t8.setText(t8.getText()+"\n"+ti16.getText());
                    }
                    if(c==9){
                        if(t9.getText().length()>4)
                            t9.setText("1C\n"+Integer.toString(img[19]));
                        t9.setText(t9.getText()+"\n"+ti16.getText());
                    }
                    if(c==10){
                        if(t10.getText().length()>4)
                            t10.setText("2C\n"+Integer.toString(img[43]));
                        t10.setText(t10.getText()+"\n"+ti16.getText());
                    }
                    if(c==11){
                        if(t11.getText().length()>4)
                            t11.setText("3C\n"+Integer.toString(img[67]));
                        t11.setText(t11.getText()+"\n"+ti16.getText());
                    }
                    if(c==12){
                        if(t12.getText().length()>4)
                            t12.setText("4C\n"+Integer.toString(img[91]));
                        t12.setText(t12.getText()+"\n"+ti16.getText());
                    }
                    if(c==13){
                        if(t13.getText().length()>4)
                            t13.setText("1D\n"+Integer.toString(img[25]));
                        t13.setText(t13.getText()+"\n"+ti16.getText());
                    }
                    if(c==14){
                        if(t14.getText().length()>4)
                            t14.setText("2D\n"+Integer.toString(img[49]));
                        t14.setText(t14.getText()+"\n"+ti16.getText());
                    }
                    if(c==15){
                        if(t15.getText().length()>4)
                            t15.setText("3D\n"+Integer.toString(img[73]));
                        t15.setText(t15.getText()+"\n"+ti16.getText());
                    }
                    if(c==16){
                        if(t16.getText().length()>4)
                            t16.setText("4D\n"+Integer.toString(img[97]));
                        t16.setText(t16.getText()+"\n"+ti16.getText());
                    }
                    break;
            }
        }

    }

    //靶面配對
    public void TonPair(View v){
        switch (v.getId()){
            case R.id.t1:
                c=1;
                t1.setBackground(getResources().getDrawable(R.drawable.target3));
                break;
            case R.id.t2:
                c=2;
                t2.setBackground(getResources().getDrawable(R.drawable.target3));
                break;
            case R.id.t3:
                c=3;
                t3.setBackground(getResources().getDrawable(R.drawable.target3));
                break;
            case R.id.t4:
                c=4;
                t4.setBackground(getResources().getDrawable(R.drawable.target3));
                break;
            case R.id.t5:
                c=5;
                t5.setBackground(getResources().getDrawable(R.drawable.target3));
                break;
            case R.id.t6:
                c=6;
                t6.setBackground(getResources().getDrawable(R.drawable.target3));
                break;
            case R.id.t7:
                c=7;
                t7.setBackground(getResources().getDrawable(R.drawable.target3));
                break;
            case R.id.t8:
                c=8;
                t8.setBackground(getResources().getDrawable(R.drawable.target3));
                break;
            case R.id.t9:
                c=9;
                t9.setBackground(getResources().getDrawable(R.drawable.target3));
                break;
            case R.id.t10:
                c=10;
                t10.setBackground(getResources().getDrawable(R.drawable.target3));
                break;
            case R.id.t11:
                c=11;
                t11.setBackground(getResources().getDrawable(R.drawable.target3));
                break;
            case R.id.t12:
                c=12;
                t12.setBackground(getResources().getDrawable(R.drawable.target3));
                break;
            case R.id.t13:
                c=13;
                t13.setBackground(getResources().getDrawable(R.drawable.target3));
                break;
            case R.id.t14:
                c=14;
                t14.setBackground(getResources().getDrawable(R.drawable.target3));
                break;
            case R.id.t15:
                c=15;
                t15.setBackground(getResources().getDrawable(R.drawable.target3));
                break;
            case R.id.t16:
                c=16;
                t16.setBackground(getResources().getDrawable(R.drawable.target3));
                break;
        }
    }

    public void next(View view) {
       for(int i=0;i<leavel[ii].length;i++){
            Log.e("log_tag","leavel["+i+"]="+leavel[ii][i]);
        }
        openFragment(img,STI);
    }


    //靶面長按設定
   private class HoldListener implements View.OnLongClickListener{
        @Override
        public boolean onLongClick(View v) {
            isBtnLongPressed = true;
            switch (v.getId()){
                case R.id.t1:
                    t1.setText(t1.getText().subSequence(0,5));
                    STI[0]= (String) t1.getText();
                    leavel[ii][17]=0;
                    switch (confirm[0]){
                        case 1:
                            ti1.setEnabled(true);
                            ti1.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 2:
                            ti2.setEnabled(true);
                            ti2.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 3:
                            ti3.setEnabled(true);
                            ti3.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 4:
                            ti4.setEnabled(true);
                            ti4.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 5:
                            ti5.setEnabled(true);
                            ti5.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 6:
                            ti6.setEnabled(true);
                            ti6.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 7:
                            ti7.setEnabled(true);
                            ti7.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 8:
                            ti8.setEnabled(true);
                            ti8.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 9:
                            ti9.setEnabled(true);
                            ti9.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 10:
                            ti10.setEnabled(true);
                            ti10.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 11:
                            ti11.setEnabled(true);
                            ti11.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 12:
                            ti12.setEnabled(true);
                            ti12.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 13:
                            ti13.setEnabled(true);
                            ti13.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 14:
                            ti14.setEnabled(true);
                            ti14.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 15:
                            ti15.setEnabled(true);
                            ti15.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 16:
                            ti16.setEnabled(true);
                            ti16.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                    }
                    break;
                case R.id.t2:
                    t2.setText(t2.getText().subSequence(0,5));
                    STI[1]= (String) t2.getText();
                    leavel[ii][24]=0;
                    switch (confirm[1]){
                        case 1:
                            ti1.setEnabled(true);
                            ti1.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 2:
                            ti2.setEnabled(true);
                            ti2.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 3:
                            ti3.setEnabled(true);
                            ti3.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 4:
                            ti4.setEnabled(true);
                            ti4.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 5:
                            ti5.setEnabled(true);
                            ti5.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 6:
                            ti6.setEnabled(true);
                            ti6.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 7:
                            ti7.setEnabled(true);
                            ti7.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 8:
                            ti8.setEnabled(true);
                            ti8.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 9:
                            ti9.setEnabled(true);
                            ti9.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 10:
                            ti10.setEnabled(true);
                            ti10.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 11:
                            ti11.setEnabled(true);
                            ti11.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 12:
                            ti12.setEnabled(true);
                            ti12.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 13:
                            ti13.setEnabled(true);
                            ti13.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 14:
                            ti14.setEnabled(true);
                            ti14.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 15:
                            ti15.setEnabled(true);
                            ti15.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 16:
                            ti16.setEnabled(true);
                            ti16.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                    }
                    break;
                case R.id.t3:
                    t3.setText(t3.getText().subSequence(0,5));
                    STI[2]= (String) t3.getText();
                    leavel[ii][31]=0;
                    switch (confirm[2]){
                        case 1:
                            ti1.setEnabled(true);
                            ti1.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 2:
                            ti2.setEnabled(true);
                            ti2.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 3:
                            ti3.setEnabled(true);
                            ti3.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 4:
                            ti4.setEnabled(true);
                            ti4.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 5:
                            ti5.setEnabled(true);
                            ti5.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 6:
                            ti6.setEnabled(true);
                            ti6.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 7:
                            ti7.setEnabled(true);
                            ti7.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 8:
                            ti8.setEnabled(true);
                            ti8.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 9:
                            ti9.setEnabled(true);
                            ti9.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 10:
                            ti10.setEnabled(true);
                            ti10.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 11:
                            ti11.setEnabled(true);
                            ti11.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 12:
                            ti12.setEnabled(true);
                            ti12.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 13:
                            ti13.setEnabled(true);
                            ti13.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 14:
                            ti14.setEnabled(true);
                            ti14.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 15:
                            ti15.setEnabled(true);
                            ti15.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 16:
                            ti16.setEnabled(true);
                            ti16.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                    }
                    break;
                case R.id.t4:
                    t4.setText(t4.getText().subSequence(0,5));
                    STI[3]= (String) t4.getText();
                    leavel[ii][38]=0;
                    switch (confirm[3]){
                        case 1:
                            ti1.setEnabled(true);
                            ti1.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 2:
                            ti2.setEnabled(true);
                            ti2.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 3:
                            ti3.setEnabled(true);
                            ti3.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 4:
                            ti4.setEnabled(true);
                            ti4.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 5:
                            ti5.setEnabled(true);
                            ti5.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 6:
                            ti6.setEnabled(true);
                            ti6.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 7:
                            ti7.setEnabled(true);
                            ti7.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 8:
                            ti8.setEnabled(true);
                            ti8.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 9:
                            ti9.setEnabled(true);
                            ti9.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 10:
                            ti10.setEnabled(true);
                            ti10.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 11:
                            ti11.setEnabled(true);
                            ti11.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 12:
                            ti12.setEnabled(true);
                            ti12.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 13:
                            ti13.setEnabled(true);
                            ti13.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 14:
                            ti14.setEnabled(true);
                            ti14.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 15:
                            ti15.setEnabled(true);
                            ti15.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 16:
                            ti16.setEnabled(true);
                            ti16.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                    }
                    break;
                case R.id.t5:
                    t5.setText(t5.getText().subSequence(0,5));
                    STI[4]= (String) t5.getText();
                    leavel[ii][45]=0;
                    switch (confirm[4]){
                        case 1:
                            ti1.setEnabled(true);
                            ti1.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 2:
                            ti2.setEnabled(true);
                            ti2.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 3:
                            ti3.setEnabled(true);
                            ti3.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 4:
                            ti4.setEnabled(true);
                            ti4.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 5:
                            ti5.setEnabled(true);
                            ti5.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 6:
                            ti6.setEnabled(true);
                            ti6.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 7:
                            ti7.setEnabled(true);
                            ti7.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 8:
                            ti8.setEnabled(true);
                            ti8.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 9:
                            ti9.setEnabled(true);
                            ti9.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 10:
                            ti10.setEnabled(true);
                            ti10.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 11:
                            ti11.setEnabled(true);
                            ti11.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 12:
                            ti12.setEnabled(true);
                            ti12.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 13:
                            ti13.setEnabled(true);
                            ti13.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 14:
                            ti14.setEnabled(true);
                            ti14.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 15:
                            ti15.setEnabled(true);
                            ti15.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 16:
                            ti16.setEnabled(true);
                            ti16.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                    }
                    break;
                case R.id.t6:
                    t6.setText(t6.getText().subSequence(0,5));
                    STI[5]= (String) t6.getText();
                    leavel[ii][52]=0;
                    switch (confirm[5]){
                        case 1:
                            ti1.setEnabled(true);
                            ti1.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 2:
                            ti2.setEnabled(true);
                            ti2.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 3:
                            ti3.setEnabled(true);
                            ti3.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 4:
                            ti4.setEnabled(true);
                            ti4.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 5:
                            ti5.setEnabled(true);
                            ti5.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 6:
                            ti6.setEnabled(true);
                            ti6.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 7:
                            ti7.setEnabled(true);
                            ti7.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 8:
                            ti8.setEnabled(true);
                            ti8.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 9:
                            ti9.setEnabled(true);
                            ti9.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 10:
                            ti10.setEnabled(true);
                            ti10.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 11:
                            ti11.setEnabled(true);
                            ti11.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 12:
                            ti12.setEnabled(true);
                            ti12.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 13:
                            ti13.setEnabled(true);
                            ti13.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 14:
                            ti14.setEnabled(true);
                            ti14.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 15:
                            ti15.setEnabled(true);
                            ti15.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 16:
                            ti16.setEnabled(true);
                            ti16.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                    }
                    break;
                case R.id.t7:
                    t7.setText(t7.getText().subSequence(0,5));
                    STI[6]= (String) t7.getText();
                    leavel[ii][59]=0;
                    switch (confirm[6]){
                        case 1:
                            ti1.setEnabled(true);
                            ti1.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 2:
                            ti2.setEnabled(true);
                            ti2.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 3:
                            ti3.setEnabled(true);
                            ti3.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 4:
                            ti4.setEnabled(true);
                            ti4.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 5:
                            ti5.setEnabled(true);
                            ti5.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 6:
                            ti6.setEnabled(true);
                            ti6.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 7:
                            ti7.setEnabled(true);
                            ti7.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 8:
                            ti8.setEnabled(true);
                            ti8.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 9:
                            ti9.setEnabled(true);
                            ti9.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 10:
                            ti10.setEnabled(true);
                            ti10.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 11:
                            ti11.setEnabled(true);
                            ti11.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 12:
                            ti12.setEnabled(true);
                            ti12.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 13:
                            ti13.setEnabled(true);
                            ti13.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 14:
                            ti14.setEnabled(true);
                            ti14.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 15:
                            ti15.setEnabled(true);
                            ti15.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 16:
                            ti16.setEnabled(true);
                            ti16.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                    }
                    break;
                case R.id.t8:
                    t8.setText(t8.getText().subSequence(0,5));
                    STI[7]= (String) t8.getText();
                    leavel[ii][66]=0;
                    switch (confirm[7]){
                        case 1:
                            ti1.setEnabled(true);
                            ti1.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 2:
                            ti2.setEnabled(true);
                            ti2.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 3:
                            ti3.setEnabled(true);
                            ti3.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 4:
                            ti4.setEnabled(true);
                            ti4.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 5:
                            ti5.setEnabled(true);
                            ti5.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 6:
                            ti6.setEnabled(true);
                            ti6.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 7:
                            ti7.setEnabled(true);
                            ti7.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 8:
                            ti8.setEnabled(true);
                            ti8.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 9:
                            ti9.setEnabled(true);
                            ti9.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 10:
                            ti10.setEnabled(true);
                            ti10.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 11:
                            ti11.setEnabled(true);
                            ti11.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 12:
                            ti12.setEnabled(true);
                            ti12.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 13:
                            ti13.setEnabled(true);
                            ti13.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 14:
                            ti14.setEnabled(true);
                            ti14.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 15:
                            ti15.setEnabled(true);
                            ti15.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 16:
                            ti16.setEnabled(true);
                            ti16.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                    }
                    break;
                case R.id.t9:
                    t9.setText(t9.getText().subSequence(0,5));
                    STI[8]= (String) t9.getText();
                    leavel[ii][73]=0;
                    switch (confirm[8]){
                        case 1:
                            ti1.setEnabled(true);
                            ti1.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 2:
                            ti2.setEnabled(true);
                            ti2.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 3:
                            ti3.setEnabled(true);
                            ti3.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 4:
                            ti4.setEnabled(true);
                            ti4.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 5:
                            ti5.setEnabled(true);
                            ti5.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 6:
                            ti6.setEnabled(true);
                            ti6.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 7:
                            ti7.setEnabled(true);
                            ti7.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 8:
                            ti8.setEnabled(true);
                            ti8.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 9:
                            ti9.setEnabled(true);
                            ti9.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 10:
                            ti10.setEnabled(true);
                            ti10.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 11:
                            ti11.setEnabled(true);
                            ti11.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 12:
                            ti12.setEnabled(true);
                            ti12.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 13:
                            ti13.setEnabled(true);
                            ti13.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 14:
                            ti14.setEnabled(true);
                            ti14.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 15:
                            ti15.setEnabled(true);
                            ti15.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 16:
                            ti16.setEnabled(true);
                            ti16.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                    }
                    break;
                case R.id.t10:
                    t10.setText(t10.getText().subSequence(0,5));
                    STI[9]= (String) t10.getText();
                    leavel[ii][80]=0;
                    switch (confirm[9]){
                        case 1:
                            ti1.setEnabled(true);
                            ti1.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 2:
                            ti2.setEnabled(true);
                            ti2.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 3:
                            ti3.setEnabled(true);
                            ti3.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 4:
                            ti4.setEnabled(true);
                            ti4.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 5:
                            ti5.setEnabled(true);
                            ti5.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 6:
                            ti6.setEnabled(true);
                            ti6.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 7:
                            ti7.setEnabled(true);
                            ti7.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 8:
                            ti8.setEnabled(true);
                            ti8.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 9:
                            ti9.setEnabled(true);
                            ti9.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 10:
                            ti10.setEnabled(true);
                            ti10.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 11:
                            ti11.setEnabled(true);
                            ti11.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 12:
                            ti12.setEnabled(true);
                            ti12.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 13:
                            ti13.setEnabled(true);
                            ti13.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 14:
                            ti14.setEnabled(true);
                            ti14.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 15:
                            ti15.setEnabled(true);
                            ti15.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 16:
                            ti16.setEnabled(true);
                            ti16.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                    }
                    break;
                case R.id.t11:
                    t11.setText(t11.getText().subSequence(0,5));
                    STI[10]= (String) t11.getText();
                    leavel[ii][87]=0;
                    switch (confirm[10]){
                        case 1:
                            ti1.setEnabled(true);
                            ti1.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 2:
                            ti2.setEnabled(true);
                            ti2.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 3:
                            ti3.setEnabled(true);
                            ti3.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 4:
                            ti4.setEnabled(true);
                            ti4.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 5:
                            ti5.setEnabled(true);
                            ti5.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 6:
                            ti6.setEnabled(true);
                            ti6.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 7:
                            ti7.setEnabled(true);
                            ti7.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 8:
                            ti8.setEnabled(true);
                            ti8.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 9:
                            ti9.setEnabled(true);
                            ti9.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 10:
                            ti10.setEnabled(true);
                            ti10.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 11:
                            ti11.setEnabled(true);
                            ti11.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 12:
                            ti12.setEnabled(true);
                            ti12.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 13:
                            ti13.setEnabled(true);
                            ti13.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 14:
                            ti14.setEnabled(true);
                            ti14.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 15:
                            ti15.setEnabled(true);
                            ti15.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 16:
                            ti16.setEnabled(true);
                            ti16.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                    }
                    break;
                case R.id.t12:
                    t12.setText(t12.getText().subSequence(0,5));
                    STI[11]= (String) t12.getText();
                    leavel[ii][94]=0;
                    switch (confirm[11]){
                        case 1:
                            ti1.setEnabled(true);
                            ti1.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 2:
                            ti2.setEnabled(true);
                            ti2.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 3:
                            ti3.setEnabled(true);
                            ti3.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 4:
                            ti4.setEnabled(true);
                            ti4.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 5:
                            ti5.setEnabled(true);
                            ti5.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 6:
                            ti6.setEnabled(true);
                            ti6.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 7:
                            ti7.setEnabled(true);
                            ti7.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 8:
                            ti8.setEnabled(true);
                            ti8.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 9:
                            ti9.setEnabled(true);
                            ti9.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 10:
                            ti10.setEnabled(true);
                            ti10.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 11:
                            ti11.setEnabled(true);
                            ti11.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 12:
                            ti12.setEnabled(true);
                            ti12.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 13:
                            ti13.setEnabled(true);
                            ti13.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 14:
                            ti14.setEnabled(true);
                            ti14.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 15:
                            ti15.setEnabled(true);
                            ti15.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 16:
                            ti16.setEnabled(true);
                            ti16.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                    }
                    break;
                case R.id.t13:
                    t13.setText(t13.getText().subSequence(0,5));
                    STI[12]= (String) t13.getText();
                    leavel[ii][101]=0;
                    switch (confirm[12]){
                        case 1:
                            ti1.setEnabled(true);
                            ti1.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 2:
                            ti2.setEnabled(true);
                            ti2.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 3:
                            ti3.setEnabled(true);
                            ti3.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 4:
                            ti4.setEnabled(true);
                            ti4.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 5:
                            ti5.setEnabled(true);
                            ti5.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 6:
                            ti6.setEnabled(true);
                            ti6.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 7:
                            ti7.setEnabled(true);
                            ti7.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 8:
                            ti8.setEnabled(true);
                            ti8.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 9:
                            ti9.setEnabled(true);
                            ti9.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 10:
                            ti10.setEnabled(true);
                            ti10.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 11:
                            ti11.setEnabled(true);
                            ti11.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 12:
                            ti12.setEnabled(true);
                            ti12.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 13:
                            ti13.setEnabled(true);
                            ti13.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 14:
                            ti14.setEnabled(true);
                            ti14.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 15:
                            ti15.setEnabled(true);
                            ti15.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 16:
                            ti16.setEnabled(true);
                            ti16.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                    }
                    break;
                case R.id.t14:
                    t14.setText(t14.getText().subSequence(0,5));
                    STI[13]= (String) t14.getText();
                    leavel[ii][108]=0;
                    switch (confirm[13]){
                        case 1:
                            ti1.setEnabled(true);
                            ti1.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 2:
                            ti2.setEnabled(true);
                            ti2.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 3:
                            ti3.setEnabled(true);
                            ti3.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 4:
                            ti4.setEnabled(true);
                            ti4.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 5:
                            ti5.setEnabled(true);
                            ti5.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 6:
                            ti6.setEnabled(true);
                            ti6.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 7:
                            ti7.setEnabled(true);
                            ti7.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 8:
                            ti8.setEnabled(true);
                            ti8.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 9:
                            ti9.setEnabled(true);
                            ti9.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 10:
                            ti10.setEnabled(true);
                            ti10.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 11:
                            ti11.setEnabled(true);
                            ti11.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 12:
                            ti12.setEnabled(true);
                            ti12.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 13:
                            ti13.setEnabled(true);
                            ti13.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 14:
                            ti14.setEnabled(true);
                            ti14.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 15:
                            ti15.setEnabled(true);
                            ti15.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 16:
                            ti16.setEnabled(true);
                            ti16.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                    }
                    break;
                case R.id.t15:
                    t15.setText(t15.getText().subSequence(0,5));
                    STI[14]= (String) t15.getText();
                    leavel[ii][115]=0;
                    switch (confirm[14]){
                        case 1:
                            ti1.setEnabled(true);
                            ti1.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 2:
                            ti2.setEnabled(true);
                            ti2.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 3:
                            ti3.setEnabled(true);
                            ti3.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 4:
                            ti4.setEnabled(true);
                            ti4.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 5:
                            ti5.setEnabled(true);
                            ti5.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 6:
                            ti6.setEnabled(true);
                            ti6.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 7:
                            ti7.setEnabled(true);
                            ti7.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 8:
                            ti8.setEnabled(true);
                            ti8.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 9:
                            ti9.setEnabled(true);
                            ti9.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 10:
                            ti10.setEnabled(true);
                            ti10.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 11:
                            ti11.setEnabled(true);
                            ti11.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 12:
                            ti12.setEnabled(true);
                            ti12.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 13:
                            ti13.setEnabled(true);
                            ti13.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 14:
                            ti14.setEnabled(true);
                            ti14.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 15:
                            ti15.setEnabled(true);
                            ti15.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 16:
                            ti16.setEnabled(true);
                            ti16.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                    }
                    break;
                case R.id.t16:
                    t16.setText(t16.getText().subSequence(0,5));
                    STI[15]= (String) t16.getText();
                    leavel[ii][122]=0;
                    switch (confirm[15]){
                        case 1:
                            ti1.setEnabled(true);
                            ti1.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 2:
                            ti2.setEnabled(true);
                            ti2.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 3:
                            ti3.setEnabled(true);
                            ti3.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 4:
                            ti4.setEnabled(true);
                            ti4.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 5:
                            ti5.setEnabled(true);
                            ti5.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 6:
                            ti6.setEnabled(true);
                            ti6.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 7:
                            ti7.setEnabled(true);
                            ti7.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 8:
                            ti8.setEnabled(true);
                            ti8.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 9:
                            ti9.setEnabled(true);
                            ti9.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 10:
                            ti10.setEnabled(true);
                            ti10.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 11:
                            ti11.setEnabled(true);
                            ti11.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 12:
                            ti12.setEnabled(true);
                            ti12.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 13:
                            ti13.setEnabled(true);
                            ti13.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 14:
                            ti14.setEnabled(true);
                            ti14.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 15:
                            ti15.setEnabled(true);
                            ti15.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                        case 16:
                            ti16.setEnabled(true);
                            ti16.setBackground(getResources().getDrawable(R.drawable.target2));
                            break;
                    }
                    break;
            }
            return true;
        }
    }

    //燈號設定
    public void light(View view) {
        final String[] lighta = {"關閉","練習","競賽"};
        AlertDialog.Builder LightDL = new AlertDialog.Builder(Challenge.this);
        LightDL.setTitle("燈號選擇");
        LightDL.setItems(lighta, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Log.e("log_tag_q","which="+which);
                switch (which){
                    case 0:
                        light.setBackground(getResources().getDrawable(R.drawable.nolight));
                        leavel[ii][7]=0;
                        break;
                    case 1:
                        light.setBackground(getResources().getDrawable(R.drawable.moon));
                        leavel[ii][7]=1;
                        break;
                    case 2:
                        light.setBackground(getResources().getDrawable(R.drawable.champion));
                        leavel[ii][7]=2;
                        break;
                }
            }
        });
        LightDL.show();
    }
    //結束模式設定
    public void finish(View view) {
        final String[] finisha = {"按鈕/固定","指定靶面","完成-ALL"};
        AlertDialog.Builder LightDL = new AlertDialog.Builder(Challenge.this);
        LightDL.setTitle("結束模式選擇");
        LightDL.setItems(finisha, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Log.e("log_tag_q","which="+which);
                switch (which){
                    case 0:
                        finish.setBackground(getResources().getDrawable(R.drawable.touchclock));
                        leavel[ii][11]=0;
                        break;
                    case 1:
                        finish.setBackground(getResources().getDrawable(R.drawable.touchtarget));
                        leavel[ii][11]=1;
                        break;
                    case 2:
                        finish.setBackground(getResources().getDrawable(R.drawable.checklist));
                        leavel[ii][11]=2;
                        break;
                }
            }
        });
        LightDL.show();
    }


    public void random(View view) {
        AlertDialog.Builder random = new AlertDialog.Builder(Challenge.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_random,null);
        random.setTitle("亂數（設定靶面數&目標發數）");
        EditText tn = (EditText) mView.findViewById(R.id.tn);
        EditText r1 = (EditText) mView.findViewById(R.id.r1);
        EditText r2 = (EditText) mView.findViewById(R.id.r2);
        //Button yes = (Button) mView.findViewById(R.id.insert);
        //Button cancle = (Button) mView.findViewById(R.id.cancle);
        random.setView(mView);
        AlertDialog dialog = random.create();
        dialog.show();
        dialog.getWindow().setLayout(1000, 500);
    }



    //靶面初始函式
    public void initTarget(){
        t1.setEnabled(false);
        t2.setEnabled(false);
        t3.setEnabled(false);
        t4.setEnabled(false);
        t5.setEnabled(false);
        t6.setEnabled(false);
        t7.setEnabled(false);
        t8.setEnabled(false);
        t9.setEnabled(false);
        t10.setEnabled(false);
        t11.setEnabled(false);
        t12.setEnabled(false);
        t13.setEnabled(false);
        t14.setEnabled(false);
        t15.setEnabled(false);
        t16.setEnabled(false);
    }

    //初始配對靶面
    public void initTI(){

        if(t1.getText().length()>5) {
            t1.setText(t1.getText().subSequence(0, 5));
            STI[0]= (String) t1.getText();
            leavel[ii][17]=0;
            Log.e("log_tag","STI[0]="+STI[0]);
        }
        if(t2.getText().length()>5) {
            t2.setText(t2.getText().subSequence(0, 5));
            STI[1]= (String) t2.getText();
            leavel[ii][24]=0;
        }
        if(t3.getText().length()>5) {
            t3.setText(t3.getText().subSequence(0, 5));
            STI[2]= (String) t2.getText();
            leavel[ii][31]=0;
        }
        if(t4.getText().length()>5) {
            t4.setText(t4.getText().subSequence(0, 5));
            STI[3]= (String) t4.getText();
            leavel[ii][38]=0;
        }
        if(t5.getText().length()>5){
            t5.setText(t5.getText().subSequence(0,5));
            STI[4]= (String) t5.getText();
            leavel[ii][45]=0;
        }
        if(t6.getText().length()>5) {
            t6.setText(t6.getText().subSequence(0, 5));
            STI[5]= (String) t6.getText();
            leavel[ii][52]=0;
        }
        if(t7.getText().length()>5) {
            t7.setText(t7.getText().subSequence(0, 5));
            STI[6]= (String) t7.getText();
            leavel[ii][59]=0;
        }
        if(t8.getText().length()>5) {
            t8.setText(t8.getText().subSequence(0, 5));
            STI[7]= (String) t8.getText();
            leavel[ii][66]=0;
        }
        if(t9.getText().length()>5) {
            t9.setText(t9.getText().subSequence(0, 5));
            STI[8]= (String) t9.getText();
            leavel[ii][73]=0;
        }
        if(t10.getText().length()>5) {
            t10.setText(t10.getText().subSequence(0, 5));
            STI[9]= (String) t10.getText();
            leavel[ii][80]=0;
        }
        if(t11.getText().length()>5) {
            t11.setText(t11.getText().subSequence(0, 5));
            STI[10]= (String) t11.getText();
            leavel[ii][87]=0;
        }
        if(t12.getText().length()>5) {
            t12.setText(t12.getText().subSequence(0, 5));
            STI[11]= (String) t12.getText();
            leavel[ii][94]=0;
        }
        if(t13.getText().length()>5) {
            t13.setText(t13.getText().subSequence(0, 5));
            STI[12]= (String) t13.getText();
            leavel[ii][101]=0;
        }
        if(t14.getText().length()>5) {
            t14.setText(t14.getText().subSequence(0, 5));
            STI[13]= (String) t14.getText();
            leavel[ii][108]=0;
        }
        if(t15.getText().length()>5) {
            t15.setText(t15.getText().subSequence(0, 5));
            STI[14]= (String) t15.getText();
            leavel[ii][115]=0;
        }
        if(t16.getText().length()>5) {
            t16.setText(t16.getText().subSequence(0, 5));
            STI[15]= (String) t16.getText();
            leavel[ii][122]=0;
        }

        //歸零confirm
        for(int i=0;i<confirm.length;i++){
            confirm[i]=0;
        }
        ti1.setEnabled(true);
        ti2.setEnabled(true);
        ti3.setEnabled(true);
        ti4.setEnabled(true);
        ti5.setEnabled(true);
        ti6.setEnabled(true);
        ti7.setEnabled(true);
        ti8.setEnabled(true);
        ti9.setEnabled(true);
        ti10.setEnabled(true);
        ti11.setEnabled(true);
        ti12.setEnabled(true);
        ti13.setEnabled(true);
        ti14.setEnabled(true);
        ti15.setEnabled(true);
        ti16.setEnabled(true);
    }
    //呼叫frfagment
    public void openFragment(int[] data, String[] sdata){
        //Log.e("log_tag_q","data="+ data.get(0));
        ReadyFragment fragment = ReadyFragment.newInstance(data,sdata);
        FragmentManager fragmentManager = getSupportFragmentManager(); //管理fragment
        FragmentTransaction transaction = fragmentManager.beginTransaction(); //執行切換fragment
        transaction.setCustomAnimations(R.anim.next_in,R.anim.next_out,R.anim.next_in,R.anim.next_out);
        transaction.addToBackStack(null);
        transaction.add(R.id.fragment_container,fragment,"ReadyFragment").commit();
    }

    //返回鍵控制

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 当按下返回键时所执行的命令
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            /*try {
                mBTSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }
}

