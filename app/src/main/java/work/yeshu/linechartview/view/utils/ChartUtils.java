package work.yeshu.linechartview.view.utils;

/**
 * Created by yeshu on 16/3/10.
 */
public class ChartUtils {
    public static int dp2px(float density, int dp) {
        if (dp == 0) {
            return 0;
        }
        return (int) (dp * density + 0.5f);

    }

    public static int dp2px(float density, float dp) {
        if (dp == 0) {
            return 0;
        }
        return (int) (dp * density + 0.5f);

    }
}
