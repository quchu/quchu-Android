package co.quchu.quchu.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by no21 on 2016/5/17.
 * email:437943145@qq.com
 * desc :
 */
public class RoundProgressViewNew extends View {

    private Paint paint;

    public RoundProgressViewNew(Context context) {
        this(context, null);
    }

    public RoundProgressViewNew(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgressViewNew(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //圆背景
        paint.setColor(Color.RED);
        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, getWidth() / 2f, paint);


        paint.setColor(Color.RED);                           // set the color
        paint.setStrokeWidth(13);                            // set the size
        paint.setDither(true);                               // set the dither to true
        paint.setStyle(Paint.Style.STROKE);                  // set to STOKE
        paint.setStrokeJoin(Paint.Join.ROUND);               // set the join to round you want
        paint.setStrokeCap(Paint.Cap.ROUND);                   // set the paint cap to round too
        paint.setPathEffect(new CornerPathEffect(10));          // set the path effect when they join.
        paint.setAntiAlias(true);

        canvas.drawLine(0, 0, 50, 50, paint);

    }
}
