package work.yeshu.linechartview;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import work.yeshu.linechartview.view.LineChartView;
import work.yeshu.linechartview.view.WeightInfoView;
import work.yeshu.linechartview.view.listener.OnWeightInfoViewValueSelectListener;
import work.yeshu.linechartview.view.model.AxisValueRange;
import work.yeshu.linechartview.view.model.LineChartData;
import work.yeshu.linechartview.view.model.PointValue;

public class MainActivity extends Activity implements OnWeightInfoViewValueSelectListener {
    private LineChartView mChartViewToClick;
    private LineChartView mChartViewToScroll;
    private WeightInfoView mWeightInfoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mChartViewToClick = (LineChartView) findViewById(R.id.line_chart_view_to_click);
        mChartViewToScroll = (LineChartView) findViewById(R.id.line_chart_view_to_scroll);
        mWeightInfoView = (WeightInfoView) findViewById(R.id.mainac_weight_info_view);

        mChartViewToClick.setChartData(getClickChartData());
        mChartViewToScroll.setChartData(getScrollChartData());
        mWeightInfoView.setAdapter(new WeightInfoAdapter("体重"));

//        mWeightInfoView.setOnValueSelectedListener(this);
//        mMuscleInfoView.setOnValueSelectedListener(this);

        mChartViewToClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("yeshu", "----->onClick");
            }
        });

        mWeightInfoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("yeshu", "----->onClick");
            }
        });

    }

    private LineChartData getClickChartData() {
        LineChartData data = new LineChartData();

        ArrayList<PointValue> values = new ArrayList<>();
        for (int i=0; i< 30; i++) {
            values.add(new PointValue(i, (float) Math.random() * 100f) );
        }

        data.setValues(values);

        data.setPointRadius(6);
        data.setPointColor(0xFF00BA8B);
        data.setPointSecondRadius(8);
        data.setPointSecondColor(0xCC00BA8B);

        data.setLineColor(0xFF00BA8B);
        data.setLineStrokeWidth(2);
        data.setAreaTransparency(61);

        AxisValueRange valueRange = new AxisValueRange();
        valueRange.left = 0;
        valueRange.right = 10;
        valueRange.top = 100;
        valueRange.bottom = 0;
        data.setAxisValueRange(valueRange);


        data.setTargetValue(20);
        data.setTargetText(" 20kg ");
        data.setShowTarget(true);
        data.setTargetLineStrokeWidth(0.3f);
        data.setTargetColor(0xA300BA8B);

        data.setScrollToMoveChart(false);
        return data;
    }

    private LineChartData getScrollChartData() {
        LineChartData data = new LineChartData();

        ArrayList<PointValue> values = new ArrayList<>();
        for (int i=0; i< 30; i++) {
            values.add(new PointValue(i, (float) Math.random() * 100f) );
        }

        data.setValues(values);

        data.setPointRadius(6);
        data.setPointColor(0xFF00BA8B);
        data.setPointSecondRadius(8);
        data.setPointSecondColor(0xCC00BA8B);

        data.setLineColor(0xFF00BA8B);
        data.setLineStrokeWidth(2);
        data.setAreaTransparency(61);

        AxisValueRange valueRange = new AxisValueRange();
        valueRange.left = 0;
        valueRange.right = 10;
        valueRange.top = 100;
        valueRange.bottom = 0;
        data.setAxisValueRange(valueRange);

        data.setTargetValue(20);
        data.setTargetText(" 20kg ");
        data.setShowTarget(true);
        data.setTargetLineStrokeWidth(0.3f);
        data.setTargetColor(0xA300BA8B);

        return data;
    }

    @Override
    public void onValueSelected(int pointIndex) {

    }


    public void OnBtnClick(View v){
        mWeightInfoView.moveSelectedPointToLeft();
    }
}
