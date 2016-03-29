package work.yeshu.linechartview.view.render;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;

import work.yeshu.linechartview.view.LineChartView;
import work.yeshu.linechartview.view.model.AxisValueRange;
import work.yeshu.linechartview.view.model.PointValue;
import work.yeshu.linechartview.view.utils.ChartUtils;

/**
 * Created by yeshu on 16/3/9.
 */
public class LineChartRender {
    private static final int DEFAULT_LINE_STROKE_WIDTH_DP = 3;

    private LineChartView mChartView;

    private Paint mPointPaint = new Paint();        //画实心圆
    private Paint mSecondPointPaint = new Paint();  //画空心圆
    private Paint mPointLinePaint = new Paint(); //选中点的垂直线
    private Paint mLinePaint = new Paint();
    private Paint mTargetLinePaint = new Paint();
    private Paint mTargetTextPaint = new Paint();

    private Path path = new Path();
    private Path mTargetLinePath = new Path();
    private DashPathEffect mTargetLinePathEffect = new DashPathEffect(new float[]{8, 8}, 1);

    private Rect mViewRect = new Rect();        //控件View的大小
    private Rect mContentRect = new Rect();     //控件View中显示曲线图的区域大小
    private AxisValueRange mAxisValueRange = new AxisValueRange(); //x,y轴的取值范围

    protected float mDensity;

    public LineChartRender(LineChartView chartView) {
        mChartView = chartView;
        mDensity = chartView.getContext().getResources().getDisplayMetrics().density;
        initPaints();
    }

    private void initPaints() {
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeCap(Paint.Cap.ROUND);

        mPointPaint.setAntiAlias(true);
        mPointPaint.setStyle(Paint.Style.FILL);
        mSecondPointPaint.setAntiAlias(true);
        mSecondPointPaint.setStyle(Paint.Style.STROKE);
        mPointLinePaint.setAntiAlias(true);
        mPointLinePaint.setStyle(Paint.Style.STROKE);


        mTargetTextPaint.setAntiAlias(true);
        mTargetLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeCap(Paint.Cap.ROUND);

        mTargetLinePaint.setStyle(Paint.Style.STROKE);
        mTargetLinePaint.setPathEffect(mTargetLinePathEffect);
    }

    public void draw(Canvas canvas) {
        if (mChartView.getChartData().getDrawValues().isEmpty()) {
            return;
        }

        drawSmoothLine(canvas);
        drawSelectedPoint(canvas);

        if (mChartView.getChartData().isShowTarget()) {
            drawTargetValue(canvas);
        }

        //just for test
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        paint.setColor(0xffB2DFEE);
        canvas.drawRect(mViewRect.left, mViewRect.top, mViewRect.right, mViewRect.bottom, paint);
        canvas.drawRect(mContentRect.left, mContentRect.top, mContentRect.right, mContentRect.bottom, paint);

    }

    //todo 重构命名，意义不明确
    //检测滑动
    public void checkTouch(float touchX, float touchY) {
        int pointIndex = 0;
        for (PointValue pointValue : mChartView.getChartData().getDrawValues()) {
            if (isInArea(computeX(pointValue.getX()), touchX, getTouchRange()) && pointIndex != mChartView.getChartData().getSelectedPointIndex()) {
                mChartView.getChartData().setSelectedPointIndex(pointIndex);
                //need update ui
                mChartView.postInvalidate();
            }

            pointIndex++;
        }
    }

    //检测触摸点
    public void checkTouchPoint(float touchX, float touchY) {
        int pointIndex = 0;
        //todo 重构命名，意义不明确，直接将point重构为一个类，获取point.margin等等
        //这里获取大圆的半径，因为大圆半径要大
        float pointRadius = ChartUtils.dp2px(mDensity, mChartView.getChartData().getPointSecondRadius());
        for (PointValue pointValue : mChartView.getChartData().getDrawValues()) {
            final float rawValueX = computeX(pointValue.getX());
            final float rawValueY = computeY(pointValue.getY());
            if (isInPointArea(rawValueX, rawValueY, touchX, touchY, pointRadius) && pointIndex != mChartView.getChartData().getSelectedPointIndex()) {
                mChartView.getChartData().setSelectedPointIndex(pointIndex);
                //need update ui
                mChartView.postInvalidate();
            }
            pointIndex++;
        }
    }


    private void drawSmoothLine(Canvas canvas) {
        mLinePaint.setStrokeWidth(ChartUtils.dp2px(mDensity, mChartView.getChartData().getLineStrokeWidth()));
        mLinePaint.setColor(mChartView.getChartData().getLineColor());

        ArrayList<PointValue> drawPoints = mChartView.getChartData().getDrawValues();
        final int lineSize = drawPoints.size();
        float currentPointX = Float.NaN;
        float currentPointY = Float.NaN;
        float nextPointX = Float.NaN;
        float nextPointY = Float.NaN;

        for (int valueIndex = 0; valueIndex < lineSize - 1; ++valueIndex) {
            PointValue currentPoint = drawPoints.get(valueIndex);
            currentPointX = computeX(currentPoint.getX());
            currentPointY = computeY(currentPoint.getY());

            PointValue nextPoint = drawPoints.get(valueIndex + 1);
            nextPointX = computeX(nextPoint.getX());
            nextPointY = computeY(nextPoint.getY());

            if (valueIndex == 0) {
                // Move to start point.
                path.moveTo(currentPointX, currentPointY);
            }

            final float firstControlPointX = currentPointX + (nextPointX - currentPointX) / 2f;
            final float firstControlPointY = currentPointY;
            final float secondControlPointX = currentPointX + (nextPointX - currentPointX) / 2f;
            final float secondControlPointY = nextPointY;
            path.cubicTo(firstControlPointX, firstControlPointY, secondControlPointX, secondControlPointY,
                    nextPointX, nextPointY);
        }

        canvas.drawPath(path, mLinePaint);
        drawArea(canvas, drawPoints);
        path.reset();
    }


    private void drawArea(Canvas canvas, ArrayList<PointValue> values) {
        final int lineSize = values.size();
        if (lineSize < 2) {
            //No point to draw area for one point or empty line.
            return;
        }

        float baseValue = computeY(mChartView.getChartData().getBaseValue());
        final float left = Math.max(computeX(values.get(0).getX()), mViewRect.left);
        final float right = Math.min(computeX(values.get(lineSize - 1).getX()), mViewRect.right);

        path.lineTo(right, baseValue);
        path.lineTo(left, baseValue);
        path.close();

        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setAlpha(mChartView.getChartData().getAreaTransparency());
        canvas.drawPath(path, mLinePaint);
        mLinePaint.setStyle(Paint.Style.STROKE);
    }

    private void drawSelectedPoint(Canvas canvas) {
        PointValue pointValue = mChartView.getChartData().getSelectedPoint();
        if (null == pointValue) {
            return;
        }

        mPointPaint.setColor(mChartView.getChartData().getPointColor());
        mSecondPointPaint.setColor(mChartView.getChartData().getPointSecondColor());
        mSecondPointPaint.setStrokeWidth(ChartUtils.dp2px(mDensity, mChartView.getChartData().getPointSecondStrokeWidth()));
        mPointLinePaint.setColor(mChartView.getChartData().getPointColor());
        //默认线条宽度和大圆的一样
        mPointLinePaint.setStrokeWidth(ChartUtils.dp2px(mDensity, mChartView.getChartData().getPointSecondStrokeWidth()));

        int pointRadius = ChartUtils.dp2px(mDensity, mChartView.getChartData().getPointRadius());
        int pointSecondRadius = ChartUtils.dp2px(mDensity, mChartView.getChartData().getPointSecondRadius());

        float pointX = computeX(pointValue.getX());
        float pointY = computeY(pointValue.getY());

        canvas.drawCircle(pointX, pointY, pointRadius, mPointPaint);
        canvas.drawCircle(pointX, pointY, pointSecondRadius, mSecondPointPaint);

        if (mChartView.getChartData().isScrollToMoveChart()) {
            canvas.drawLine(pointX, pointY, pointX, computeY(0.0f), mPointLinePaint);
        }
    }

    private void drawPoints(Canvas canvas) {
        mPointPaint.setColor(mChartView.getChartData().getPointColor());
        int pointRadius = ChartUtils.dp2px(mDensity, mChartView.getChartData().getPointRadius());
        for (PointValue pointValue : mChartView.getChartData().getDrawValues()) {
            canvas.drawCircle(computeX(pointValue.getX()), computeY(pointValue.getY()), pointRadius, mPointPaint);
            canvas.drawLine(computeX(pointValue.getX()), mViewRect.top, computeX(pointValue.getX()), mViewRect.bottom, mPointPaint);
            Log.i("yeshu", "point valuse is " + pointValue.toString() + " convert y is :" + computeY(pointValue.getY()));
        }
    }

    private void drawTargetValue(Canvas canvas) {
        mTargetTextPaint.setTextSize(mChartView.getChartData().getTargetTextSize());
        mTargetTextPaint.setColor(mChartView.getChartData().getTargetColor());

        mTargetLinePaint.setColor(mChartView.getChartData().getTargetColor());
        mTargetLinePaint.setStrokeWidth(ChartUtils.dp2px(mDensity, mChartView.getChartData().getTargetLineStrokeWidth()));
        float y = computeY(mChartView.getChartData().getTargetValue());

        String text = mChartView.getChartData().getTargetText();

        canvas.drawText(text, mContentRect.left, y, mTargetTextPaint);

        float textWidth = mTargetTextPaint.measureText(text);
        mTargetLinePath.moveTo(mContentRect.left + textWidth, y);
        mTargetLinePath.lineTo(mContentRect.right, y);
        canvas.drawPath(mTargetLinePath, mTargetLinePaint);
    }

    private float getTouchRange() {
        return mContentRect.width() / mChartView.getChartData().getDrawPointCount() / 2f;
    }

    //获取x轴坐标的最小单位距离
    public float getStep() {
        return mContentRect.width() / mAxisValueRange.width();
    }

    //检测滑动的时候是否触摸到了点
    private boolean isInArea(float x, float touchX, float touchRange) {
        if (touchX >= x - touchRange && touchX <= x + touchRange) {
            return true;
        } else {
            return false;
        }
    }

    //检测是否触摸到了点
    private boolean isInPointArea(float x, float y, float touchX, float touchY, float radius) {
        float diffX = touchX - x;
        float diffY = touchY - y;
        return Math.pow(diffX, 2) + Math.pow(diffY, 2) <= 2 * Math.pow(radius, 2);
    }

    private float computeX(float xValue) {
        float offset = (xValue - mAxisValueRange.left) * (mContentRect.width() / mAxisValueRange.width());
        return mContentRect.left + offset;
    }

    private float computeY(float yValue) {
        float offset = (yValue - mAxisValueRange.bottom) * (mContentRect.height() / mAxisValueRange.height());
        return mContentRect.bottom - offset;
    }


    //测试时函数调用顺序为,onChartDataChange, onChartSizeChanged, onDraw，但自己不能确保onChartSizeChanged和onChartDataChanged的顺序，所以这两个地方都进行mContentRect计算，防止遗漏
    public void onChartSizeChanged(int width, int height, int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        mViewRect.set(paddingLeft, paddingTop, width - paddingRight, height - paddingBottom);

        //calculate mContentRect, min pointRadius
        mContentRect.set(mViewRect);

        constantContentRect();
    }

    public void onChartDataChanged() {
        mAxisValueRange.set(mChartView.getChartData().getAxisValueRange());

        //calculate mContentRect, min pointRadius
        constantContentRect();
        mChartView.postInvalidate();
    }

    public AxisValueRange getAxisValueRange() {
        return mAxisValueRange;
    }

    public void setAxisValueRange(AxisValueRange axisValueRange) {
        mAxisValueRange = axisValueRange;
    }

    //计算内部的margin,例如画point的半径等。。
    private int computeInnerMargin() {
        return ChartUtils.dp2px(mDensity, mChartView.getChartData().getPointSecondRadius());
    }

    //计算曲线图的视图大小
    private void constantContentRect() {
        int innerMargin = computeInnerMargin();
        //加线的宽度，让圆能画完整
        int pointCircleStrokeWidth = (int) mChartView.getChartData().getPointSecondStrokeWidth();

        mContentRect.set(mViewRect.left + innerMargin, mViewRect.top + innerMargin + pointCircleStrokeWidth, mViewRect.right - innerMargin, mViewRect.bottom - innerMargin);
    }
}
