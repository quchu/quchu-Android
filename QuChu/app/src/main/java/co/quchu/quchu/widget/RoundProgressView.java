package co.quchu.quchu.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;

import co.quchu.quchu.R;
import co.quchu.quchu.utils.LogUtils;

/**
 * Created by no21 on 2016/5/17.
 * email:437943145@qq.com
 * desc :
 */
public class RoundProgressView extends View {

    private Paint paint;
    private RectF rectF;
    int progress;
    private int[] bgColors;
    private LinearGradient roundBg;

    private int[] bgProgress;
    private SweepGradient sweepGradient;
    private Rect rect;

    public RoundProgressView(Context context) {
        this(context, null);
    }

    public RoundProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        rectF = new RectF();
        rect = new Rect();
        bgColors = new int[]{ContextCompat.getColor(context, R.color.bg_shade1),
                ContextCompat.getColor(context, R.color.bg_shade2),
                ContextCompat.getColor(context, R.color.bg_shade3)};

        bgProgress = new int[]{ContextCompat.getColor(context, R.color.bg_progress1)
                , ContextCompat.getColor(context, R.color.bg_progress2),
                ContextCompat.getColor(context, R.color.bg_progress3)};

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                roundBg = new LinearGradient(0, 0, 0, getHeight(), bgColors, null, Shader.TileMode.MIRROR);
                sweepGradient = new SweepGradient(getWidth() / 2, getHeight() / 2, bgProgress, null);
                Matrix matrix = new Matrix();
                matrix.setRotate(90, getWidth() / 2, getHeight() / 2);
                sweepGradient.setLocalMatrix(matrix);

            }
        });

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float arcWidth = getWidth() * .15f;
        float arcMargin = getWidth() * .15f;

        //圆背景
        paint.reset();
        paint.setShader(roundBg);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setDither(true);
        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, getWidth() / 2f, paint);


//        进度背景
        paint.reset();
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setShader(null);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(arcWidth);
        paint.setColor(ContextCompat.getColor(getContext(), R.color.bg_progress));
//        paint.setColor(ContextCompat.getColor(getContext(), R.color.standard_color_red));

        rectF.left = arcMargin;
        rectF.top = arcMargin;
        rectF.right = getWidth() - arcMargin;
        rectF.bottom = getHeight() - arcMargin;
//
        canvas.drawArc(rectF, 120, 300, false, paint);

        //画进度
        paint.reset();
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(arcWidth);
        paint.setShader(sweepGradient);
        paint.setDither(true);
        float pro = 300f * progress / 100;
        LogUtils.e("progress=:" + progress);
        if (pro != 0)
            canvas.drawArc(rectF, 120, pro, false, paint);

        //画中心圆
        paint.reset();
        paint.setShader(null);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(ContextCompat.getColor(getContext(), R.color.bg_round));

        float radius = getWidth() / 2 - arcMargin - arcWidth;
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, paint);

        //画进度文字
        paint.reset();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(ContextCompat.getColor(getContext(), R.color.standard_color_h2_dark));
        paint.setTextSize(radius / 1.5f);

        String text = progress + "%";
        paint.getTextBounds(text, 0, text.length(), rect);
        canvas.drawText(text, getWidth() / 2, getHeight() / 2f + rect.height() / 2, paint);

    }


    public void setProgress(int progress) {

        ValueAnimator animator = ValueAnimator.ofInt(0, progress);
        animator.setDuration(800);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                RoundProgressView.this.progress = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int withSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int size = Math.min(withSize, heightSize);
        int sizeSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);

        super.onMeasure(sizeSpec, sizeSpec);

    }
}
