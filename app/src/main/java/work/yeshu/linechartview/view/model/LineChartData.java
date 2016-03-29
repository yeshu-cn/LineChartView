package work.yeshu.linechartview.view.model;

import android.graphics.Color;

import java.util.ArrayList;

/**
 * Created by yeshu on 16/3/9.
 */
public class LineChartData {
    private static final float DEFAULT_BASE_VALUE = 0.0f;
    private static final int DEFAULT_LINE_COLOR = Color.BLACK;
    private static final int DEFAULT_LINE_STROKE_WIDTH_DP = 3;
    private static final int DEFAULT_POINT_RADIUS_DP = 10;
    private static final int DEFAULT_POINT_SECOND_RADIUS_DP = 15;
    private static final int DEFAULT_POINT_COLOR = Color.RED;
    private static final int DEFAULT_AREA_TRANSPARENCY = 64;
    private static final int DEFAULT_TARGET_TEXT_SIZE = 24;
    private static final int DEFAULT_POINT_STROKE_WIDTH = 1;

    private ArrayList<PointValue> mValues = new ArrayList<PointValue>();
    private int mSelectedPointIndex = -1;

    private float mBaseValue = DEFAULT_BASE_VALUE;  //用于填充曲线范围的基准线

    private int mAreaTransparency = DEFAULT_AREA_TRANSPARENCY; //填充曲线区域的透明度
    private int mLineColor = DEFAULT_LINE_COLOR;
    private int mLineStrokeWidth = DEFAULT_LINE_STROKE_WIDTH_DP; //单位dp

    //todo point样式可以抽象成一个类，实现各种各样的point
    //双圆的point
    private int mPointColor = DEFAULT_POINT_COLOR;
    private int mPointSecondColor = DEFAULT_POINT_COLOR;
    private int mPointSecondStrokeWidth = DEFAULT_POINT_STROKE_WIDTH; //外圈的线条粗细
    private int mPointRadius = DEFAULT_POINT_RADIUS_DP; //单位dp //小圆半径
    private int mPointSecondRadius = DEFAULT_POINT_SECOND_RADIUS_DP; //空心圆半径

    private int mTargetColor = DEFAULT_LINE_COLOR; //目标文字和线的颜色
    private float mTargetValue;     //目标值
    private boolean mShowTarget; //是否开启这个功能
    private String mTargetText; //文字
    private int mTargetTextSize = DEFAULT_TARGET_TEXT_SIZE;
    private float mTargetLineStrokeWidth = DEFAULT_LINE_STROKE_WIDTH_DP;

    //图标中显示点的数目
    private int mDrawPointCount = 10;
    protected boolean mIsScrollToMoveChart = true;


    private AxisValueRange mAxisValueRange = new AxisValueRange();

    public ArrayList<PointValue> getScrollValuesToDraw() {
        ArrayList<PointValue> drawPoints = new ArrayList<>(mDrawPointCount);
        for (int i = 0; i < 10; i++) {
            int index = mSelectedPointIndex - i;
            if (index < 0) {
                break;
            }

            PointValue tempPoint = mValues.get(mSelectedPointIndex - i);
            tempPoint.setX(mDrawPointCount - 1 - i);
            drawPoints.add(i, tempPoint);
        }
        return drawPoints;
    }

    public ArrayList<PointValue> getDrawValues() {
        if (mIsScrollToMoveChart) {
            return getScrollValuesToDraw();
        } else {
            return mValues;
        }
    }


    public ArrayList<PointValue> getValues() {
        return mValues;
    }

    public LineChartData() {

    }

    public void setValues(ArrayList<PointValue> values) {
        if (null == values) {
            return;
        }

        mValues = values;

        if (!mValues.isEmpty()) {
            mSelectedPointIndex = mValues.size() - 1;
        }
    }

    /**
     * @return return -1 when mValues is empty
     */
    public int getSelectedPointIndex() {
        if (mValues.isEmpty()) {
            return -1;
        } else {
            return mSelectedPointIndex;
        }
    }


    public void setSelectedPointIndex(int selectedPointIndex) {
        if (selectedPointIndex >= 0 && selectedPointIndex < mValues.size()) {
            mSelectedPointIndex = selectedPointIndex;
        }
    }

    /**
     * @return return null when mValues is empty
     */
    public PointValue getSelectedPoint() {
        if (mValues.isEmpty()) {
            return null;
        } else {
            return mValues.get(mSelectedPointIndex);
        }
    }

    public float getTargetValue() {
        return mTargetValue;
    }

    public void setTargetValue(float targetValue) {
        mTargetValue = targetValue;
    }

    public boolean isShowTarget() {
        return mShowTarget;
    }

    public void setShowTarget(boolean showTarget) {
        mShowTarget = showTarget;
    }


    public float getBaseValue() {
        return mBaseValue;
    }

    public void setBaseValue(float baseValue) {
        mBaseValue = baseValue;
    }

    public int getAreaTransparency() {
        return mAreaTransparency;
    }

    public void setAreaTransparency(int areaTransparency) {
        mAreaTransparency = areaTransparency;
    }

    public int getLineColor() {
        return mLineColor;
    }

    public void setLineColor(int lineColor) {
        mLineColor = lineColor;
    }

    public int getPointColor() {
        return mPointColor;
    }

    public void setPointColor(int pointColor) {
        mPointColor = pointColor;
    }

    public int getTargetColor() {
        return mTargetColor;
    }

    public void setTargetColor(int targetColor) {
        mTargetColor = targetColor;
    }

    public int getPointRadius() {
        return mPointRadius;
    }

    public void setPointRadius(int pointRadius) {
        mPointRadius = pointRadius;
    }

    public int getLineStrokeWidth() {
        return mLineStrokeWidth;
    }

    public void setLineStrokeWidth(int lineStrokeWidth) {
        mLineStrokeWidth = lineStrokeWidth;
    }

    public AxisValueRange getAxisValueRange() {
        return mAxisValueRange;
    }

    public void setAxisValueRange(AxisValueRange axisValueRange) {
        mAxisValueRange = axisValueRange;
    }

    public String getTargetText() {
        return mTargetText;
    }

    public void setTargetText(String targetText) {
        mTargetText = targetText;
    }

    public int getTargetTextSize() {
        return mTargetTextSize;
    }

    public void setTargetTextSize(int targetTextSize) {
        mTargetTextSize = targetTextSize;
    }

    public float getTargetLineStrokeWidth() {
        return mTargetLineStrokeWidth;
    }

    public void setTargetLineStrokeWidth(float targetLineStrokeWidth) {
        mTargetLineStrokeWidth = targetLineStrokeWidth;
    }

    public int getPointSecondColor() {
        return mPointSecondColor;
    }

    public void setPointSecondColor(int pointSecondColor) {
        mPointSecondColor = pointSecondColor;
    }

    public int getPointSecondStrokeWidth() {
        return mPointSecondStrokeWidth;
    }

    public void setPointSecondStrokeWidth(int pointSecondStrokeWidth) {
        mPointSecondStrokeWidth = pointSecondStrokeWidth;
    }

    public int getPointSecondRadius() {
        return mPointSecondRadius;
    }

    public void setPointSecondRadius(int pointSecondRadius) {
        mPointSecondRadius = pointSecondRadius;
    }

    public int getDrawPointCount() {
        return mDrawPointCount;
    }

    public void setDrawPointCount(int drawPointCount) {
        mDrawPointCount = drawPointCount;
    }

    public boolean isScrollToMoveChart() {
        return mIsScrollToMoveChart;
    }

    public void setScrollToMoveChart(boolean scrollToMoveChart) {
        mIsScrollToMoveChart = scrollToMoveChart;
    }
}
