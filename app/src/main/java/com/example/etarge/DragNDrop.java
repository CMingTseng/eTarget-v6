package com.example.etarge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DragNDrop extends Activity {
    private ViewGroup rootLayout;
    private TextView tv,tv2;
    private int _xDelta;
    private int _yDelta;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_ndrop);
        rootLayout = (ViewGroup) findViewById(R.id.view_root);
        tv = (TextView) rootLayout.findViewById(R.id.textView1);
        tv2 = (TextView) rootLayout.findViewById(R.id.textView2);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(250,350);
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(250,350);
        //img.setLayoutParams(layoutParams);
        //img.setOnTouchListener(new ChoiceTouchListener());
        tv.setLayoutParams(layoutParams);
        tv.setOnTouchListener(new ChoiceTouchListener());
        tv.setText("A1+A2");
        tv2.setLayoutParams(layoutParams2);
        tv2.setOnTouchListener(new ChoiceTouchListener());
    }



    private final class ChoiceTouchListener implements View.OnTouchListener{

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            final int X = (int) event.getRawX();
            final int Y = (int) event.getRawY();
            switch (event.getAction() & MotionEvent.ACTION_MASK){
                case MotionEvent.ACTION_DOWN:
                    RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                    _xDelta = X - lParams.leftMargin;
                    _yDelta = Y - lParams.topMargin;
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    break;
                case MotionEvent.ACTION_MOVE:
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                    layoutParams.leftMargin = X - _xDelta;
                    layoutParams.topMargin = Y - _yDelta;
                    layoutParams.rightMargin = -250;
                    layoutParams.bottomMargin = -250;
                    v.setLayoutParams(layoutParams);
                    break;
            }
            rootLayout.invalidate();
            return true;
        }
    }
}
