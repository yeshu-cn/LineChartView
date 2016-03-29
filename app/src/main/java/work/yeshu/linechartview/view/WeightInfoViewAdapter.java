package work.yeshu.linechartview.view;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

import work.yeshu.linechartview.view.model.AxisValueRange;
import work.yeshu.linechartview.view.model.LineChartData;
import work.yeshu.linechartview.view.model.PointValue;

/**
 * Created by yeshu on 16/3/11.
 */
public abstract class WeightInfoViewAdapter {
    private String mTitle;

    public WeightInfoViewAdapter(String title) {
        mTitle = title;
    }

    public abstract LineChartData getChartData();

    public abstract String getScore(float value);

    public abstract String getUnit();

    //获取没数据时的图片
    public abstract Drawable getNoDataImage();

    //获取没数据时的文字信息
    public abstract String getNoDataText();

    public String getValue(PointValue value) {
        return value.getY() + "";
    }

    public String getTitle() {
        return mTitle;
    }

    protected LineChartData generateTestData() {
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

}
