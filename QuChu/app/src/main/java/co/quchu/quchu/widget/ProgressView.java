package co.quchu.quchu.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import co.quchu.quchu.R;
import co.quchu.quchu.utils.StringUtils;

/**
 * Created by no21 on 2016/5/13.
 * email:437943145@qq.com
 * desc :颜色渐变进度条的View
 */
public class ProgressView extends View {

    private Paint paint;
    private int progress;
    private RectF rectF;
    private String text;
    private Rect rect;

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
//        paint.setShader(new LinearGradient(0, 0, 2, 2, new int[]{Color.RED, Color.BLACK, Color.GRAY}, null, Shader.TileMode.REPEAT));


        rectF = new RectF();
        rect = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //draw text
        paint.setTextSize(getResources().getDimension(R.dimen.standard_text_size_h4));
        paint.setColor(getResources().getColor(R.color.colorPrimary));

        float y = getHeight() / 2f + paint.getTextSize() / 2f;
        canvas.drawText(progress + "%", 0, y, paint);

        if (!TextUtils.isEmpty(text)) {
            paint.getTextBounds(text, 0, text.length(), rect);
            canvas.drawText(text, getWidth() / 2 - rect.width() / 2, getHeight() / 2 + rect.height() / 2, paint);
        }

        // draw  bg,文字宽度为50
        rectF.left = StringUtils.dip2px(getContext(), 30);
        rectF.right = getWidth();
        rectF.bottom = getHeight();

        paint.setColor(getResources().getColor(R.color.standard_color_description));
        paint.setStrokeWidth(3);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRoundRect(rectF, 25f, 25f, paint);

        //draw progress
        rectF.left = StringUtils.dip2px(getContext(), 30) - paint.getStrokeWidth();
        rectF.right = (getWidth() - rectF.left) * progress / 100f;
        rectF.bottom = getHeight();

        paint.setColor(getResources().getColor(R.color.colorPrimary));
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(rectF, 25f, 25f, paint);


    }

    public void setProgress(int progress, String text) {
        if (progress < 0 || progress > 100)
            throw new IllegalArgumentException("progress must between 0 100");

        this.text = text;
        ValueAnimator animator = ValueAnimator.ofInt(0, progress);
        animator.setDuration(800);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
        ProgressView.this.progress = (int) animation.getAnimatedValue();

                invalidate();
            }
        });
        animator.start();
    }
}
