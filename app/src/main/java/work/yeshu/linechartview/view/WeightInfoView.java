package work.yeshu.linechartview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import work.yeshu.linechartview.R;
import work.yeshu.linechartview.view.listener.LineChartOnValueSelectListener;
import work.yeshu.linechartview.view.listener.OnWeightInfoViewValueSelectListener;
import work.yeshu.linechartview.view.model.PointValue;

/**
 * Created by yeshu on 16/3/9.
 */

//todo 有6种infoView,可不可以做成抽象的基类，和6个子类
public class WeightInfoView extends LinearLayout{
    private TextView mTvTitle;
    private TextView mTvValue;
    private TextView mTvValueUnit;
    private TextView mTvScore;
    private LineChartView mChartView;
    private LinearLayout mDataContentView;
    private LinearLayout mNoDataContentView;
    private TextView mTvNoDataText;
    private ImageView mIvNoDataChart;

    private WeightInfoViewAdapter mAdapter = null;
    private OnWeightInfoViewValueSelectListener mOnValueSelectedListener = null;


    public WeightInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_weight_info, this, true);

        mTvTitle = (TextView) findViewById(R.id.weight_info_title);
        mTvValue = (TextView) findViewById(R.id.weight_info_value);
        mTvValueUnit = (TextView) findViewById(R.id.weight_info_vlaue_unit);
        mTvScore = (TextView) findViewById(R.id.weight_info_score);
        mChartView = (LineChartView) findViewById(R.id.weight_info_chart);
        mDataContentView = (LinearLayout) findViewById(R.id.weight_info_data_content);
        mNoDataContentView = (LinearLayout) findViewById(R.id.weight_info_no_data_content);
        mTvNoDataText = (TextView) findViewById(R.id.weight_info_text_no_data);
        mIvNoDataChart = (ImageView) findViewById(R.id.weight_info_chart_no_data);

        mChartView.setLineChartOnValueSelectListener(new LineChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int pointIndex, PointValue value) {
                updateView();
                if (null != mOnValueSelectedListener) {
                    mOnValueSelectedListener.onValueSelected(pointIndex);
                }
            }
        });
    }

    public void setAdapter(WeightInfoViewAdapter adapter) {
        if (null == adapter) {
            return;
        }

        mAdapter = adapter;
        updateView();
    }

    private void updateView() {
        if (mAdapter.getChartData().getValues().isEmpty()) {
            //无数据界面
            mTvNoDataText.setText(mAdapter.getNoDataText());
            mIvNoDataChart.setImageDrawable(mAdapter.getNoDataImage());
            mNoDataContentView.setVisibility(View.VISIBLE);
            mDataContentView.setVisibility(View.GONE);
        } else {
            mNoDataContentView.setVisibility(View.GONE);
            mDataContentView.setVisibility(View.VISIBLE);
            //有数据的界面
            mTvTitle.setText(mAdapter.getTitle());
            mChartView.setChartData(mAdapter.getChartData());

            //maybe return null
            PointValue pointValue = mChartView.getChartData().getSelectedPoint();
            if (null == pointValue) {
                return;
            }
            mTvValue.setText(mAdapter.getValue(pointValue));
            mTvScore.setText(mAdapter.getScore(pointValue.getY()));
            mTvValueUnit.setText(mAdapter.getUnit());
        }
    }

    public void setOnValueSelectedListener(OnWeightInfoViewValueSelectListener onValueSelectedListener) {
        mOnValueSelectedListener = onValueSelectedListener;
    }

    public WeightInfoViewAdapter getAdapter() {
        return mAdapter;
    }

    public void setSelectedPoint(int index) {
        mChartView.setSelectedPoint(index);
        updateView();
    }

    public void moveSelectedPointToLeft() {
        int selectedIndex = mChartView.getChartData().getSelectedPointIndex();
        System.out.println("------>" + selectedIndex);
        mChartView.setSelectedPoint(selectedIndex - 1);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i("yeshu", "----------->weightInfoView onInterceptTouchEvent");
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("yeshu", "----------->weightInfoView onTouchEvent");
        return super.onTouchEvent(event);
    }
}
