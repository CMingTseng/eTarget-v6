package com.example.etarge;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class StandBy extends AppCompatActivity {
    ListView ls;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stand_by);


        /*AlertDialog.Builder altdial = new AlertDialog.Builder(StandBy.this);
        altdial.setMessage("確定要開始射擊?").setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                    }
                });

        AlertDialog alert = altdial.create();
        alert.setTitle("");
        alert.show();*/



        ls =(ListView)findViewById(R.id.timels);
        //listview顯示內容
        String[] time= {"1","2","3"};
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1,time);
        ls.setAdapter(adapter);
    }
}
