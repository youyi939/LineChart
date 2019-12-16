package com.example.linechart;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private LineChart lineChart;
    private List<Entry> entryList ;
    private LineData lineData;
    private LineDataSet lineDataSet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lineChart = findViewById(R.id.lineChart);
        entryList = new ArrayList<>();
        init();
        start();

    }

    private void start() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                final float i = (float) new Random().nextInt(90-70)+70;
                final int xCount = lineData.getDataSetByIndex(0).getEntryCount();
                Entry entry = new Entry(xCount, i);           // 创建一个点
                lineData.addEntry(entry, 0);                    // 将entry添加到指定索引处的折线中
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lineData.notifyDataChanged();
                        lineChart.notifyDataSetChanged();
                        lineChart.moveViewToAnimated(xCount - 4, i, YAxis.AxisDependency.LEFT, 1000);
                        lineChart.invalidate();
                    }
                });
            }
        },0,1000);
    }

    private void init(){
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //X轴设置显示位置在底部
        xAxis.setAxisMinimum(0f);                               // 设置X轴的最小值
        xAxis.setAxisMaximum(500);                              // 设置X轴的最大值
        xAxis.setLabelCount(20, false);              // 设置X轴的刻度数量，第二个参数表示是否平均分配
        xAxis.setGranularity(1f);                               // 设置X轴坐标之间的最小间隔
        lineChart.setVisibleXRangeMaximum(5);                   // 当前统计图表中最多在x轴坐标线上显示的总量
        //保证Y轴从0开始，不然会上移一点
        YAxis leftYAxis = lineChart.getAxisLeft();
        leftYAxis.setAxisMinimum(0f);
        leftYAxis.setAxisMaximum(100f);
        leftYAxis.setGranularity(1f);
        leftYAxis.setLabelCount(20);
        leftYAxis.setEnabled(false);
        YAxis rightYAxis = lineChart.getAxisRight();
        rightYAxis.setAxisMaximum(100f);
        rightYAxis.setGranularity(1f);
        rightYAxis.setAxisMinimum(0f);

        lineChart.setVisibleYRangeMaximum(30, YAxis.AxisDependency.LEFT);       // 当前统计图表中最多在Y轴坐标线上显示的总量
        lineChart.setVisibleYRangeMaximum(30, YAxis.AxisDependency.RIGHT);      // 当前统计图表中最多在Y轴坐标线上显示的总量
        //设置数据
        lineDataSet = new LineDataSet(entryList,"实时用电量监控");
        lineDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
    }

}
