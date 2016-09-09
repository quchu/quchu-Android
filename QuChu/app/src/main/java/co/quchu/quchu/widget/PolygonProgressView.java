package co.quchu.quchu.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.List;

import co.quchu.quchu.R;
import co.quchu.quchu.utils.ArrayUtils;


/**
 * Created by Nico on 16/7/4.
 */
public class PolygonProgressView extends View {


    private static final long ADR = 1000l;
    private static final long AD = 75l;
    private int s = -1;
    private int p = -1;
    private int c = -1;
    private int sw = 6;
    private int n = 6;
    private int r = -1;
    private int ap = 5;
    private int rO = 360 / 4;
    private float pic = 0;
    private float min = .5f;
    private float[] pv;
    private float[] apv;
    private boolean ar = false;

    private String[] lbl = new String[]{};
    private Paint pr;
    private Paint pn;
    private Paint pa;
    private Paint pil;
    private Paint pl;
    private Paint pab;
    private Paint pac;
    private Rect rBB4;
    private Rect rBAft;
    private Bitmap bmBackground;
    private Bitmap ba;
    private Bitmap[] bl;


    public PolygonProgressView(Context context) {
        super(context);
        init();
    }

    public PolygonProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PolygonProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PolygonProgressView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void animateProgress() {

        if (ar) {
            return;
        }


        apv = new float[n];
        for (int i = 0; i < n; i++) {
            ar = true;
            final int finalI = i;
            ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
            animator.setDuration(ADR);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    apv[finalI] = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            animator.setStartDelay(AD * i);
            if (finalI == n - 1) {
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ar = false;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        ar = false;
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
            }
            animator.start();
        }

    }


    private void init() {

        pr = new Paint();
        pr.setStrokeWidth(sw);
        pr.setColor(Color.parseColor("#bed4da"));
        pr.setAntiAlias(true);
        pr.setStrokeCap(Paint.Cap.ROUND);
        pr.setShader(null);
        pr.setStyle(Paint.Style.STROKE);

        pn = new Paint(pr);
        pn.setColor(Color.parseColor("#ffd102"));
        pn.setStrokeWidth(2);
        pn.setStyle(Paint.Style.FILL);

        pa = new Paint(pr);
        pa.setColor(Color.parseColor("#ffd102"));
        pa.setStrokeCap(Paint.Cap.SQUARE);
        pa.setStrokeWidth(6);

        pil = new Paint(pr);
        pil.setStrokeWidth(2);
        pil.setStyle(Paint.Style.FILL);
        pil.setColor(Color.parseColor("#e2edf1"));

        pab = new Paint();
        pab.setAntiAlias(true);
        pab.setColor(Color.WHITE);
        pab.setStyle(Paint.Style.FILL);

        pac = new Paint();
        pac.setAntiAlias(true);
        pac.setColor(Color.WHITE);
        pac.setStyle(Paint.Style.FILL);


    }

    public void initial(int side, float[] values, String[] labels,Bitmap []pbl) {

        if (ar) {
            return;
        }
        n = side;
        pv = values;
        lbl = labels;
        pic = n > 0 ? 360 / n : 0;
        bl = pbl;

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initProperties();
        drawCircle(canvas);

        drawInnerPattern(canvas);


        for (int i = 0; i < n; i++) {
            double angle = ((Math.PI * 2 / n) * i) - (Math.toRadians(rO));

//            drawInnerLines(canvas, angle);
            drawArcs(canvas, i);
            drawLabels(canvas, i, angle);
        }


        drawNodes(canvas);
        drawAvatar(canvas);
    }

    private void drawInnerPattern(Canvas canvas) {
        canvas.save();
        canvas.rotate(rO,c,c);
        canvas.drawBitmap(bmBackground,rBB4,rBAft,pil);
        canvas.restore();

    }


    private void drawAvatar(Canvas canvas) {


        canvas.drawCircle(c, c, r * min * .6f * apv[0], pac);
        pa.setAlpha((int) (apv[0]*255));
//        canvas.drawBitmap(ba, c - (ba.getWidth() >> 1), c - (ba.getHeight() >> 1), pa);
    }


    private void drawArcs(Canvas canvas, int i) {

        List<Integer> maxValues = ArrayUtils.getMaxValues((pv));

        RectF rectF = new RectF(p, p, (r * 2) + p, (r * 2) + p);
        if (maxValues.size() > i) {
            int sweepAngle = (int) (pic * apv[maxValues.get(i)]);
            int startAngle = (int) ((maxValues.get(i) * pic) - (sweepAngle / 2) - rO);
            if (n == 0) {
                ap = 0;
            }

            if (sweepAngle != 0) {
                canvas.drawArc(rectF, startAngle + ap, sweepAngle - ap, false, pa);
            }
        }
    }

    private void drawLabels(Canvas canvas, int i, double angle) {
        if (null == lbl || lbl.length != n) {
            return;
        }

        int maxLength = r;
        Bitmap bitmap = bl[i];

        float actuallyValues = maxLength -20 + bitmap.getWidth();

        float x = (int) (Math.cos(angle) * actuallyValues + c );
        float y = (int) (Math.sin(angle) * actuallyValues + c);

        pab.setAlpha((int) (apv[0]*255));

        canvas.drawBitmap(bitmap,x - bitmap.getWidth()/2,y-bitmap.getHeight()/2,pab);

    }


    private void drawNodes(Canvas canvas) {

        Path path = new Path();
        float minimalValues = 0;
        float startX = 0;
        float startY = 0;
        int maxHill = r - sw / 2;

        if (min > 0) {
            minimalValues = maxHill * min;
        }
        for (int i = 0; i < n; i++) {

            float actuallyValues = pv[i] * maxHill * apv[i];
            if (minimalValues > 0) {
                actuallyValues = (minimalValues + (maxHill - minimalValues) * pv[i] )* apv[i];
            }

            double angle = ((Math.PI * 2 / n) * i) - (Math.toRadians(rO));

            float x = (int) (Math.cos(angle) * actuallyValues + c);
            float y = (int) (Math.sin(angle) * actuallyValues + c);

            if (i == 0) {
                startX = x;
                startY = y;
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }
        path.lineTo(startX, startY);
        canvas.drawPath(path, pn);
    }

    private void drawCircle(Canvas canvas) {
        canvas.drawCircle(c, c, r, pr);
        //canvas.drawCircle(c, c, r * .6f, pil);
    }

    private void initProperties() {
        if (s == -1) {
            s = Math.min(getHeight(), getWidth());
            p = s / 9;
            c = s / 2;
            r = c - p;
            apv = new float[n];
            pv = new float[n];


            pl = new Paint(pil);
            pl.setTextSize(s / 30);
            pl.setColor(Color.BLACK);
            pl.setStyle(Paint.Style.FILL);
            pl.setAntiAlias(true);
            pic = n > 0 ? 360 / n : 0;
            ba = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_logo),(int)(r*.4f),(int)(r*.4f),false);
            bmBackground = BitmapFactory.decodeResource(getResources(),R.mipmap.bg);
            rBB4 = new Rect(0,0,bmBackground.getHeight(),bmBackground.getWidth());
            rBAft = new Rect(p,p,s-p,s-p);
        }
    }

}
