package co.quchu.quchu.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import co.quchu.quchu.R;
import co.quchu.quchu.utils.StringUtils;

/**
 * Created by no21 on 2016/5/23.
 * email:437943145@qq.com
 * desc : 包含一条竖线的view
 */
public class LineRelative extends RelativeLayout {

    private Paint paint;

    public LineRelative(Context context) {
        this(context, null);
    }

    public LineRelative(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineRelative(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(StringUtils.dip2px(getContext(), 0.5f));
        paint.setColor(ContextCompat.getColor(getContext(), R.color.standard_color_h3_dark));
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int positionX = StringUtils.dip2px(getContext(), 16);
        canvas.drawLine(positionX, 0, positionX, getHeight()-(positionX*1.5f), paint);
    }
}
