package work.yeshu.linechartview.view.gesture;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import work.yeshu.linechartview.view.LineChartView;
import work.yeshu.linechartview.view.listener.LineChartOnValueSelectListener;

/**
 * Created by yeshu on 16/3/9.
 */
public class ChartTouchHandler extends GestureDetector.SimpleOnGestureListener{
    private LineChartView mChartView;
    private GestureDetector mGestureDetector;

    private int mSelectedPointIndex = -1;
    private int mOldSelectedPointIndex = -1;

    //统计向左向右滑动了多少次，当滑动距离超过x轴最小单位时算一次滑动
    private int mScrollCount = 0;

    public ChartTouchHandler(LineChartView chartView) {
        mChartView = chartView;
        mGestureDetector = new GestureDetector(chartView.getContext(), this);
    }

    public void handlerTouchEvent(MotionEvent event){
        mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.i("yeshu", "----------->chartTouchHandler onDown");
        //每次开始滑动时，统计重置
        mScrollCount = 0;
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        if (!mChartView.getChartData().isScrollToMoveChart()) {
            checkTouchPoint(e);
        }

        return super.onSingleTapConfirmed(e);
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.i("yeshu", "----------->chartTouchHandler onScroll");

        if (mChartView.getChartData().isScrollToMoveChart()) {
            checkScrollToMoveChart(e1, e2);
        }

//        checkScrollToMovePoint(e2);
        return super.onScroll(e1, e2, distanceX, distanceY);
    }



    //检查触摸点
    private void checkTouchPoint(MotionEvent e) {
        mChartView.getLineChartRender().checkTouchPoint(e.getX(), e.getY());
        mSelectedPointIndex = mChartView.getChartData().getSelectedPointIndex();
        if (-1 != mSelectedPointIndex && mOldSelectedPointIndex != mSelectedPointIndex) {
            mOldSelectedPointIndex = mSelectedPointIndex;

            LineChartOnValueSelectListener listener = mChartView.getLineChartOnValueSelectListener();
            if (null != listener) {
                listener.onValueSelected(mSelectedPointIndex, mChartView.getChartData().getValues().get(mSelectedPointIndex));
            }
        }
    }

    //检测滑动图标时让点滑动
    private void checkScrollToMovePoint(MotionEvent e2) {
        mChartView.getLineChartRender().checkTouch(e2.getX(), e2.getY());
        mSelectedPointIndex = mChartView.getChartData().getSelectedPointIndex();
        if (-1 != mSelectedPointIndex && mOldSelectedPointIndex != mSelectedPointIndex) {
            mOldSelectedPointIndex = mSelectedPointIndex;

            LineChartOnValueSelectListener listener = mChartView.getLineChartOnValueSelectListener();
            if (null != listener) {
                listener.onValueSelected(mSelectedPointIndex, mChartView.getChartData().getValues().get(mSelectedPointIndex));
            }
        }
    }

    //检测滑动图标时让图标滑动
    private void checkScrollToMoveChart(MotionEvent e1, MotionEvent e2) {
        float step = mChartView.getLineChartRender().getStep();
        float scrollDistance = e2.getX() - e1.getX();
        //+0.5强转是为了进行四舍五入
        int currentScrollCount = (int) (scrollDistance / step + 0.5f);
        if (mScrollCount - currentScrollCount != 0) {
            int selectedIndex = mChartView.getChartData().getSelectedPointIndex() + mScrollCount - currentScrollCount;
            if (selectedIndex > mChartView.getChartData().getValues().size() - 1) {
                selectedIndex = mChartView.getChartData().getValues().size() - 1;
            }

            if (selectedIndex < 0) {
                selectedIndex = 0;
            }
            mScrollCount = currentScrollCount;
            mChartView.setSelectedPoint(selectedIndex);
            LineChartOnValueSelectListener listener = mChartView.getLineChartOnValueSelectListener();
            if (null != listener) {
                listener.onValueSelected(selectedIndex, mChartView.getChartData().getValues().get(selectedIndex));
            }
        }
    }


}
