package co.quchu.quchu.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
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
public class LinearProgressView extends View {

    private Paint paint;
    private int progress;
    private RectF rectF;
    private String text;
    private Rect rect;
    private LinearGradient linearGradient;
    private int[][] colors;

    public LinearProgressView(Context context) {
        this(context, null);
    }

    public LinearProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinearProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        Resources resources = getResources();


        int[] color = new int[]{resources.getColor(R.color.progress1), resources.getColor(R.color.progress1), resources.getColor(R.color.progress1)};
        int[] color1 = new int[]{resources.getColor(R.color.progress4), resources.getColor(R.color.progress5), resources.getColor(R.color.progress6)};
        int[] color2 = new int[]{resources.getColor(R.color.progress7), resources.getColor(R.color.progress8), resources.getColor(R.color.progress9)};
        int[] color3 = new int[]{resources.getColor(R.color.progress10), resources.getColor(R.color.progress11), resources.getColor(R.color.progress12)};
        colors = new int[][]{color, color1, color2, color3};

        rectF = new RectF();
        rect = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float radio = getHeight() / 2f;
        paint.reset();
        paint.setAntiAlias(true);
        paint.setShader(null);
        paint.setStrokeWidth(3);
        //draw text
        paint.setTextSize(getResources().getDimension(R.dimen.standard_text_size_h4));
        paint.setColor(colors[position][0]);


        String pro = progress + "%";
        paint.getTextBounds(pro, 0, pro.length(), rect);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        float y = getHeight() / 2f + rect.height() / 2f;
        canvas.drawText(pro, 0, y, paint);

        if (!TextUtils.isEmpty(text)) {
            paint.getTextBounds(text, 0, text.length(), rect);
            paint.setTypeface(Typeface.DEFAULT);
            paint.setShader(null);
            paint.setColor(ContextCompat.getColor(getContext(), R.color.standard_color_h3_dark));
            canvas.drawText(text, getWidth() / 2 - rect.width() / 2, getHeight() / 2 + rect.height() / 2, paint);
        }

        // draw  bg,文字宽度为50
        rectF.left = StringUtils.dip2px(getContext(), 30);
        rectF.right = getWidth() - paint.getStrokeWidth();
        rectF.top = 0;
        rectF.bottom = getHeight();

        paint.reset();
        paint.setShader(null);
        paint.setColor(ContextCompat.getColor(getContext(), R.color.standard_color_h3_dark));
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        canvas.drawRoundRect(rectF, radio, radio, paint);

        //draw progress
        rectF.left = StringUtils.dip2px(getContext(), 30) + 3;
        rectF.right = getWidth() * progress / 100f - 3;
        rectF.top = 3;
        rectF.bottom = getHeight() - 3;

        paint.reset();
        paint.setDither(true);
        paint.setAntiAlias(true);
        paint.setShader(linearGradient);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(rectF, radio, radio, paint);
    }

    int position;

    public void setProgress(int progress, String text, int position) {


//        if (progress < 0 || progress > 100)
//            throw new IllegalArgumentException("progress must between 0 100,current is" + progress);

        this.position = position;
        this.text = text;
        linearGradient = new LinearGradient(StringUtils.dip2px(getContext(), 30) - paint.getStrokeWidth(),
                0, getWidth() * progress / 100f, 0, colors[position], null, Shader.TileMode.MIRROR);

        ValueAnimator animator = ValueAnimator.ofInt(0, progress);
        animator.setDuration(800);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                LinearProgressView.this.progress = (int) animation.getAnimatedValue();

                invalidate();
            }
        });
        animator.start();
    }
}
