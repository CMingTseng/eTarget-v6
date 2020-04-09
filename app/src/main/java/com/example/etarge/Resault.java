package com.example.etarge;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;

public class Resault extends AppCompatActivity {
    RadarChart radarChart;
    String[] labels={"速度","判斷","反應","火力","準確"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resault);
        radarChart = (RadarChart)findViewById(R.id.radar_chart);

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

    private ArrayList<RadarEntry> dataValues1()
    {
        ArrayList<RadarEntry> dataVals = new ArrayList<>();
        dataVals.add(new RadarEntry(4));
        dataVals.add(new RadarEntry(7));
        dataVals.add(new RadarEntry(1));
        dataVals.add(new RadarEntry(5));
        dataVals.add(new RadarEntry(9));
        return dataVals;
    }

    private ArrayList<RadarEntry> dataValues2()
    {
        ArrayList<RadarEntry> dataVals = new ArrayList<>();
        dataVals.add(new RadarEntry(7));
        dataVals.add(new RadarEntry(4));
        dataVals.add(new RadarEntry(8));
        dataVals.add(new RadarEntry(2));
        dataVals.add(new RadarEntry(6));
        return dataVals;
    }
}
