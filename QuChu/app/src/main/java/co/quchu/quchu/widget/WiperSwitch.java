package co.quchu.quchu.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Looper;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;

import co.quchu.quchu.R;
import co.quchu.quchu.utils.StringUtils;

/**
 * @author xiaanming
 */
public class WiperSwitch extends View {
    private static final String SWIPE_LOGIN_TEXT = "滑动退出登陆";

    private static final int BACK_COLOR = Color.parseColor("#40000000");
    private static final int FRONT_COLOR = Color.parseColor("#d9000000");

    private int width;
    private int height;

    private Paint paint;
    private Paint textPaint;
    private float text_X;
    private float text_Y;

    private RectF frontCircleRect;
    private RectF middleCircleRect;
    private RectF backCircleRect;

    private boolean isOpen;
    private int alpha;
    private int max_left;
    private int min_left;
    private int frontRect_left;
    private int frontRect_left_begin;
    private int eventStartX;
    private int eventLastX;
    private int diffX;
    private boolean slideable;

    private Bitmap bitmap;
private Context context;
    private StatusListener listener;

    public WiperSwitch(Context context) {
        super(context);
        this.context= context;
        initDrawingVal();
    }

    public WiperSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context= context;
        initDrawingVal();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
        initDrawingVal();
    }
    BitmapFactory.Options options;
    private void initDrawingVal() {
        this.slideable = true;

        this.paint = new Paint();
        this.paint.setAntiAlias(true);

        this.textPaint = new Paint();
        this.textPaint.setAntiAlias(true);
        this.textPaint.setTextSize(StringUtils.dip2px(context,16));
        this.textPaint.setStyle(Paint.Style.STROKE);
        this.textPaint.setFakeBoldText(true);

        this.frontCircleRect = new RectF();
        this.middleCircleRect = new RectF();
        this.backCircleRect = new RectF(0, 0, width, height);
        options = new BitmapFactory.Options();
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.switch_thumb, options);
        this.min_left = 15;
        this.max_left = width - (height - 30) - 15;

        if (this.isOpen) {
            this.frontRect_left = this.max_left;
            this.alpha = 255;
        } else {
            this.frontRect_left = 15;
            this.alpha = 0;
        }

        this.frontRect_left_begin = this.frontRect_left;

        calTextStartPoint(SWIPE_LOGIN_TEXT, textPaint);

    }

    private void calTextStartPoint(String text, Paint paint) {
        if (text == null) return;
        final Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        final float textWidth = paint.measureText(text);
        final float textHeight = fontMetrics.bottom + fontMetrics.top;

        text_X = this.backCircleRect.centerX() - textWidth / 2f;
        text_Y = this.backCircleRect.centerY() - textHeight / 2f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int radius = (int) this.backCircleRect.height() / 2;

        //draw back circleRect
        this.paint.setColor(BACK_COLOR);
        canvas.drawRoundRect(this.backCircleRect, (float) radius, (float) radius, this.paint);

        //draw black text
        this.textPaint.setColor(Color.WHITE);
        canvas.drawText(SWIPE_LOGIN_TEXT, text_X, text_Y, textPaint);

        //draw middle circleRect
        this.paint.setColor(FRONT_COLOR);
        this.paint.setAlpha(this.alpha);
        float middleCircleRectWidth = this.frontRect_left + this.backCircleRect.height() - 15;
        float middleCircleRectHeight = this.backCircleRect.height();
        middleCircleRect.set(0, 0, middleCircleRectWidth, middleCircleRectHeight);
        canvas.drawRoundRect(this.middleCircleRect, (float) radius, (float) radius, this.paint);

        //draw white text
        this.textPaint.setColor(Color.parseColor("#e9dd34"));
        String whiteText = subTextWithRect(this.textPaint, SWIPE_LOGIN_TEXT, this.middleCircleRect);
        canvas.drawText(whiteText, text_X, text_Y, textPaint);

        this.frontCircleRect.set(this.frontRect_left, 15, this.frontRect_left + this.backCircleRect.height() - 30, this.backCircleRect.height() - 15);
        this.paint.setColor(Color.YELLOW);
  /*     canvas.drawRoundRect(this.frontCircleRect, (float) radius, (float) radius, this.paint);*/
      //  canvas.drawBitmap(iconbit, 0,0, this.paint);
        if (this.bitmap != null) {
            drawBitmapCenter(canvas, this.bitmap, this.frontCircleRect.centerX()-4, this.frontCircleRect.centerY()+6, 1, this.paint);
        }

    }

    private String subTextWithRect(Paint paint, String text, RectF rectF) {
        float rectWidth = rectF.width();
        final float textWidth = paint.measureText(text) + text_X;
        if (textWidth < rectWidth) {
            return text;
        } else {
            if (rectWidth < text_X) {
                return "";
            }
            final float singleTextWidth = paint.measureText(text, 0, 1);
            int endIndex = (int) ((rectWidth - text_X) / singleTextWidth);
            if (endIndex > 0) {
                endIndex -= 1;
            }
            return text.substring(0, endIndex);
        }
    }

    public static void drawBitmapCenter(Canvas canvas, Bitmap bitmap, float posX, float posY, float scale, Paint paint) {
        final Matrix matrix = new Matrix();
        if (bitmap != null) {
            matrix.setTranslate(-bitmap.getWidth() / 2, -bitmap.getHeight() / 2);
            matrix.postScale(scale, scale);
            matrix.postTranslate(posX, posY);
            canvas.drawBitmap(bitmap, matrix, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!this.slideable) {
            return super.onTouchEvent(event);
        } else {
            int action = MotionEventCompat.getActionMasked(event);
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    this.eventStartX = (int) event.getRawX();
                    break;
                case MotionEvent.ACTION_UP:
                    int wholeX = (int) (event.getRawX() - (float) this.eventStartX);
                    if (Math.abs(wholeX) < 5) {
                        break;
                    }
                    this.frontRect_left_begin = this.frontRect_left;
                    boolean toRight = this.frontRect_left_begin > this.max_left*4 / 5;
                    this.moveToDest(toRight);
                    break;
                case MotionEvent.ACTION_MOVE:
                    this.eventLastX = (int) event.getRawX();
                    this.diffX = this.eventLastX - this.eventStartX;
                    if (Math.abs(diffX) < 5) {
                        break;
                    }
                    int tempX = this.diffX + this.frontRect_left_begin;
                    tempX = tempX > this.max_left ? this.max_left : tempX;
                    tempX = tempX < this.min_left ? this.min_left : tempX;
                    if (tempX >= this.min_left && tempX <= this.max_left) {
                        this.frontRect_left = tempX;
                        this.alpha = (int) (255.0F * (float) tempX / (float) this.max_left);
                        this.invalidateView();
                    }
            }

            return true;
        }
    }

    public void moveToDest(final boolean toRight) {
        ValueAnimator toDestAnim = ValueAnimator.ofInt(new int[]{this.frontRect_left, toRight ? this.max_left : this.min_left});
        toDestAnim.setDuration(500L);
        toDestAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        toDestAnim.start();
        toDestAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                WiperSwitch.this.frontRect_left = ((Integer) animation.getAnimatedValue()).intValue();
                WiperSwitch.this.alpha = (int) (255.0F * (float) WiperSwitch.this.frontRect_left / (float) WiperSwitch.this.max_left);
                WiperSwitch.this.invalidateView();
            }
        });
        toDestAnim.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                if (toRight) {
                    WiperSwitch.this.isOpen = true;
                    if (WiperSwitch.this.listener != null) {
                        WiperSwitch.this.listener.statusOpen();
                    }

                    WiperSwitch.this.frontRect_left_begin = WiperSwitch.this.max_left;
                } else {
                    WiperSwitch.this.isOpen = false;
                    if (WiperSwitch.this.listener != null) {
                        WiperSwitch.this.listener.statusClose();
                    }

                    WiperSwitch.this.frontRect_left_begin = WiperSwitch.this.min_left;
                }

            }
        });
    }

    private void invalidateView() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            this.invalidate();
        } else {
            this.postInvalidate();
        }

    }

    public void setSlideable(boolean slideable) {
        this.slideable = slideable;
    }

    public void setStatusListener(StatusListener listener) {
        this.listener = listener;
    }

    public void setFrontBitmap(int resId) {
        bitmap = BitmapFactory.decodeResource(getResources(), resId);
    }

    public interface StatusListener {
        void statusOpen();

        void statusClose();
    }

}