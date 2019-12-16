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

public class MainActivity extends AppCompatActivity {

    public static final int MSG_START = 1; // handler消息，开始添加点
    private LineChart lineChart;
    private List<Entry> entryList ;
    private LineData lineData;
    private LineDataSet lineDataSet;
    private DemoHandler mDemoHandler; // 自定义Handler
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDemoHandler = new MainActivity.DemoHandler(this);
        lineChart = findViewById(R.id.lineChart);
        entryList = new ArrayList<>();
        lineChart.animateX(1500);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //X轴设置显示位置在底部
        xAxis.setAxisMaximum(100); // 设置X轴的最大值
        xAxis.setLabelCount(20, false); // 设置X轴的刻度数量，第二个参数表示是否平均分配
        xAxis.setGranularity(1f); // 设置X轴坐标之间的最小间隔
        lineChart.setVisibleXRangeMaximum(5);// 当前统计图表中最多在x轴坐标线上显示的总量
        //保证Y轴从0开始，不然会上移一点
        YAxis leftYAxis = lineChart.getAxisLeft();
        YAxis rightYAxis = lineChart.getAxisRight();
        leftYAxis.setAxisMinimum(0f);
        rightYAxis.setAxisMinimum(0f);
        leftYAxis.setAxisMaximum(100f);
        rightYAxis.setAxisMaximum(100f);
        leftYAxis.setGranularity(1f);
        rightYAxis.setGranularity(1f);
        leftYAxis.setLabelCount(20);
        lineChart.setVisibleYRangeMaximum(30, YAxis.AxisDependency.LEFT);// 当前统计图表中最多在Y轴坐标线上显示的总量
        lineChart.setVisibleYRangeMaximum(30, YAxis.AxisDependency.RIGHT);// 当前统计图表中最多在Y轴坐标线上显示的总量
        leftYAxis.setEnabled(false);
        Description description = new Description();                    //隐藏右下角的描述
        description.setEnabled(false);
        lineChart.setDescription(description);


            entryList.add(new Entry(0,(float) new Random().nextInt(90-70)+70));

        lineDataSet = new LineDataSet(entryList,"实时用电量监控");
        lineDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
         lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        sendStartAddEntry();

    }
    private void sendStartAddEntry() {
        if (!mDemoHandler.hasMessages(MSG_START)) { // 判断是否有消息队列此消息，如果没有则发送
            mDemoHandler.sendEmptyMessageDelayed(MSG_START, 1000);
        }
    }

    private void add(){

        float i = (float) new Random().nextInt(90-70)+70;
        addEntry(lineData,lineChart,i,0);
    }

    public void addEntry(LineData lineData, LineChart lineChart, float yValues, int index) {
        // 通过索引得到一条折线，之后得到折线上当前点的数量
        int xCount = lineData.getDataSetByIndex(index).getEntryCount();
        Entry entry = new Entry(xCount, yValues); // 创建一个点
        lineData.addEntry(entry, index); // 将entry添加到指定索引处的折线中
        //通知数据已经改变
        lineData.notifyDataChanged();
        lineChart.notifyDataSetChanged();

        //把yValues移到指定索引的位置
        lineChart.moveViewToAnimated(xCount - 4, yValues, YAxis.AxisDependency.LEFT, 1000);
        lineChart.invalidate();
    }

    private static class DemoHandler extends Handler {
        WeakReference<MainActivity> mReference;
        DemoHandler(MainActivity activity) {
            mReference = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MainActivity lineChartDemo = mReference.get();
            if (lineChartDemo == null) {
                return;
            }
            switch (msg.what) {
                case MSG_START:
                    lineChartDemo.add();
                    lineChartDemo.sendStartAddEntry();
                    break;
                default:
            }
        }
    }


}
