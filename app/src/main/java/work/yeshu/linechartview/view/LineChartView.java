package work.yeshu.linechartview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import work.yeshu.linechartview.view.gesture.ChartTouchHandler;
import work.yeshu.linechartview.view.listener.LineChartOnValueSelectListener;
import work.yeshu.linechartview.view.model.LineChartData;
import work.yeshu.linechartview.view.render.LineChartRender;

/**
 * Created by yeshu on 16/3/9.
 */

//todo 添加自定义属性
public class LineChartView extends View {
    protected LineChartData mData = new LineChartData();
    protected LineChartOnValueSelectListener mLineChartOnValueSelectListener;

    protected LineChartRender mLineChartRender;
    protected ChartTouchHandler mTouchHandler;

    protected boolean mIsInteractive = true;


    public LineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mLineChartRender = new LineChartRender(this);
        mTouchHandler = new ChartTouchHandler(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mLineChartRender.onChartSizeChanged(getWidth(), getHeight(), getPaddingLeft(), getPaddingTop(), getPaddingRight(),
                getPaddingBottom());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        Paint paint = new Paint();
//        paint.setColor(Color.BLUE);
//        paint.setAntiAlias(true);
//        paint.setStyle(Paint.Style.FILL);
//        canvas.drawCircle(100, 100, 50, paint);
//
//        Paint textPaint = new Paint();
//        textPaint.setColor(Color.BLUE);
//        textPaint.setAntiAlias(true);
//        textPaint.setStyle(Paint.Style.STROKE);
//        textPaint.setTextSize(42);
//        canvas.drawText("120kg", 200, 200, textPaint);
//
//        String string = "120kg";
//        float width = textPaint.measureText(string);
//
//        DashPathEffect pathEffect = new DashPathEffect(new float[] { 8, 8}, 1);
//        Paint linePaint = new Paint();
//        linePaint.reset();
//        linePaint.setStyle(Paint.Style.STROKE);
//        linePaint.setLineStrokeWidth(1);
//        linePaint.setColor(Color.BLUE);
//        linePaint.setAntiAlias(true);
//        linePaint.setPathEffect(pathEffect);
//        Path path = new Path();
//        path.moveTo(200 + getTextWidth(linePaint, string), 200);
//        path.lineTo(800, 200);
//        canvas.drawPath(path, linePaint);
//        System.out.println("measureText is : " + width + " getTextWidth is :" + getTextWidth(textPaint, string));
        mLineChartRender.draw(canvas);
    }

    public float getTextWidth(Paint paint, String str) {
        float iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += widths[j];
            }
        }
        return iRet;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("yeshu", "----------->lineChartView onTouchEvent");
        if (mIsInteractive) {
            mTouchHandler.handlerTouchEvent(event);
            super.onTouchEvent(event);
            return true;
        } else {
            return super.onTouchEvent(event);
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        boolean ret = super.dispatchTouchEvent(event);
        Log.i("yeshu", "----------->lineChartView dispatchTouchEvent : " + ret);
        return ret;
    }


    public void setChartData(LineChartData data) {
        if (null != data) {
            mData = data;
            mLineChartRender.onChartDataChanged();
        }
    }

    public LineChartData getChartData() {
        return mData;
    }

    public void setLineChartOnValueSelectListener(LineChartOnValueSelectListener listener) {
        if (null != listener) {
            mLineChartOnValueSelectListener = listener;
        }
    }

    public LineChartOnValueSelectListener getLineChartOnValueSelectListener() {
        return mLineChartOnValueSelectListener;
    }

    public LineChartRender getLineChartRender() {
        return mLineChartRender;
    }

    public void setSelectedPoint(int index) {
        mData.setSelectedPointIndex(index);
        postInvalidate();
    }

    public boolean isInteractive() {
        return mIsInteractive;
    }

    public void setInteractive(boolean interactive) {
        mIsInteractive = interactive;
    }
}
