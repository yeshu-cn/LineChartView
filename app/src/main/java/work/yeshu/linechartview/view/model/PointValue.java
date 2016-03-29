package work.yeshu.linechartview.view.model;

/**
 * Created by yeshu on 16/3/9.
 */
public class PointValue {
    private float x;
    private float y;

    public PointValue(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "PointValue{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
