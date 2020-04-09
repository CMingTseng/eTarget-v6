package com.example.etarge;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;


public class Result extends AppCompatActivity {
    private View mChart;

    /*private int count = 5; //幾邊形
    private int layerCount = 2; //層數
    private float angle; //每條邊對應的圓心角
    private int centerX; //圓心X
    private int centerY; //圓心Y
    private float radius; //半徑
    private Paint polygonPaint; //邊框paint
    private Paint linePaint; //連線paint
    private Paint txtPaint; //文字paint
    private Paint circlePaint; //圓點paint
    private Paint regionColorPaint; //覆蓋區域paint
    //private Double[] yAxisData = {0.91,0.35,0.12,0.8,0.5}; //覆蓋區域百分比
    private int[] yAxisData = {50, 20, 15, 30, 20, 60, 15, 40, 45, 10, 90, 18};
    private String[] axisData = {"速度","判斷","反應","火力","準確"};*/

    private int count = 6;                //数据个数
    private float angle = (float) (Math.PI*2/count);
    private float radius;                   //网格最大半径
    private int centerX;                  //中心X
    private int centerY;                  //中心Y
    private String[] titles = {"a","b","c","d","e","f"};
    private double[] data = {100,60,60,60,100,50,10,20}; //各维度分值
    private float maxValue = 100;             //数据最大值
    private Paint mainPaint;                //雷达区画笔
    private Paint valuePaint;               //数据区画笔
    private Paint textPaint;                //文本画笔
    RadarChart radarChart;
    String[] labels = {"速度","判斷","反應","火力","準確"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        radarChart=(RadarChart) findViewById(R.id.radar_chat);
        //radarChart.setBackgroundColor(Color.rgb(60,65,82));
        //radarChart.setWebLineWidth(1f);
        //radarChart.setWebColor(Color.WHITE);

        RadarDataSet dataSet1 = new RadarDataSet(dataValues1(),"Showroom 1");
        RadarDataSet dataSet2 = new RadarDataSet(dataValues2(),"Showroom 2");

        dataSet1.setColor(Color.RED);
        dataSet2.setColor(Color.BLUE);

        RadarData data = new RadarData();
            data.addDataSet(dataSet1);
            data.addDataSet(dataSet2);


        XAxis xAxis = radarChart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        radarChart.setData(data);
        radarChart.invalidate();
    }

    private ArrayList<RadarEntry> dataValues1(){
        ArrayList<RadarEntry>dataValues = new ArrayList<RadarEntry>();
        dataValues.add(new RadarEntry(4));
        dataValues.add(new RadarEntry(7));
        dataValues.add(new RadarEntry(1));
        dataValues.add(new RadarEntry(5));
        dataValues.add(new RadarEntry(9));
        return dataValues;
    }

    private ArrayList<RadarEntry>dataValues2(){
        ArrayList<RadarEntry>dataValues = new ArrayList<RadarEntry>();
        dataValues.add(new RadarEntry(7));
        dataValues.add(new RadarEntry(4));
        dataValues.add(new RadarEntry(8));
        dataValues.add(new RadarEntry(2));
        dataValues.add(new RadarEntry(6));
        return dataValues;
    }
}
