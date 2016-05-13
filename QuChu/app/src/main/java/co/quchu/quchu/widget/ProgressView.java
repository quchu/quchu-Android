package co.quchu.quchu.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by no21 on 2016/5/13.
 * email:437943145@qq.com
 * desc :颜色渐变进度条的View
 */
public class ProgressView extends View {

    private Paint paint;
    private int progress;

    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
//        new float[]{0, 0.3f, 0.8f}
        paint.setShader(new LinearGradient(0, 0, 2, 2, new int[]{Color.RED, Color.BLACK, Color.GRAY}, null, Shader.TileMode.REPEAT));
        paint.setStrokeWidth(50);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (progress != 0)
            canvas.drawLine(0, 0, progress, 0, paint);
    }

    public void setProgress(int progress) {
        if (progress < 0 || progress > 100)
            throw new IllegalArgumentException("progress must between 0 100");

        this.progress = (int) ((progress / 100f) * getWidth());
        invalidate();
    }
}
