package work.yeshu.linechartview;

import android.graphics.drawable.Drawable;

import work.yeshu.linechartview.view.WeightInfoViewAdapter;
import work.yeshu.linechartview.view.model.LineChartData;

/**
 * Created by yeshu on 16/3/11.
 */
public class WeightInfoAdapter extends WeightInfoViewAdapter {
    private LineChartData mData = generateTestData();

    public WeightInfoAdapter(String title) {
        super(title);
    }

    @Override
    public LineChartData getChartData() {
        return mData;
    }

    @Override
    public String getScore(float value) {
        return "good";
    }

    @Override
    public String getUnit() {
        return "斤";
    }

    @Override
    public Drawable getNoDataImage() {
        return null;
    }

    @Override
    public String getNoDataText() {
        return "haha";
    }
}
