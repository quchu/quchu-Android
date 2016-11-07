package co.quchu.quchu.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

/**
 * Created by Nico on 16/11/5.
 */


public class XiaoQFab extends FloatingActionButton {

  private boolean mAnimationRunning;
  private float[] mAnimationProgress;
  private int mSides = 5;
  public boolean mLoading = false;


  public XiaoQFab(Context context) {
    super(context);
  }

  public XiaoQFab(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public XiaoQFab(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override protected void onDraw(Canvas canvas) {

    int size = Math.min(getWidth(), getHeight());

    Paint paint = new Paint();
    paint.setColor(Color.BLACK);
    paint.setStrokeWidth(size/10);
    paint.setStyle(Paint.Style.STROKE);
    paint.setAntiAlias(true);


    Paint bg = new Paint();
    bg.setColor(Color.WHITE);
    bg.setStyle(Paint.Style.FILL);
    bg.setAntiAlias(true);
    canvas.drawCircle(size/2,size/2,size/2,bg);

    if (!mLoadingAnimationStart){
      if (null != mAnimationProgress) {
        int offSet = (size / 21);
        canvas.drawArc(new RectF(offSet, offSet, size - offSet, size - offSet),
            360 * mAnimationProgress[0], 360 * mAnimationProgress[0], false, paint);

        paint.setColor(Color.WHITE);
        int offSetSec = (int) (size / 7f);
        canvas.drawArc(new RectF(offSetSec, offSetSec, size - offSetSec, size - offSetSec),
            180 * mAnimationProgress[1], 360 * mAnimationProgress[1], false, paint);

        paint.setStrokeWidth(size/9);
        paint.setColor(Color.parseColor("#ffd702"));
        int offSetThd = (int) (size / 4.5f);
        canvas.drawArc(new RectF(offSetThd, offSetThd, size - offSetThd, size - offSetThd),
            120 * mAnimationProgress[2], -360 * mAnimationProgress[2], false, paint);

        paint.setStrokeWidth(size/8);
        paint.setColor(Color.BLACK);
        int offSetF = (int) (size / 3.15f);
        canvas.drawArc(new RectF(offSetF, offSetF, size - offSetF, size - offSetF),
            240 * mAnimationProgress[3], 360 * mAnimationProgress[3], false, paint);

        canvas.save();
        canvas.rotate(-180 + (mAnimationProgress[4] * 180), getWidth() / 2, getHeight() / 2);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#eaeaea"));
        canvas.drawCircle(size / 4, size - (size / 4), (size/20) * mAnimationProgress[3], paint);
        canvas.restore();
      }


    }else{
      //canvas.save();
      //canvas.rotate((revert?360:-360)* mAnimationProgress[0],getWidth()/2,getHeight()/2);
      int offSet = (int) (size / 21);
      canvas.drawArc(new RectF(offSet, offSet, size - offSet, size - offSet),
          (revert?360:-360)  * mAnimationProgress[0], (revert?360:-360)  * mAnimationProgress[0], false, paint);
      //canvas.restore();
    }
  }


  public void endLoading(){
    if (!mLoading){
      return;
    }
    mLoading = false;
  }


  public void animateLoading() {
    if (mAnimationRunning||mLoadingAnimationStart) {
      return;
    }
    mLoading = true;

    ValueAnimator animator = new ValueAnimator().ofFloat(1,0);
    animator.setDuration(500);
    animator.setInterpolator(new AccelerateDecelerateInterpolator());
    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator animation) {

        float i = (float) animation.getAnimatedValue();
        for (int j = 0; j < mSides; j++) {
          mAnimationProgress[j]=i;
        }
        invalidate();
      }
    });
    animator.start();

    ValueAnimator animator2 = new ValueAnimator().ofFloat(0,1);
    animator2.setDuration(2000);
    animator2.setInterpolator(new AccelerateDecelerateInterpolator());
    animator2.setStartDelay(500);
    animator2.setRepeatMode(ValueAnimator.REVERSE);
    animator2.setRepeatCount(ValueAnimator.INFINITE);
    animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator animation) {
        float i = (float) animation.getAnimatedValue();
        mAnimationProgress[0] = i;
        invalidate();
      }
    });
    animator2.addListener(new Animator.AnimatorListener() {
      @Override public void onAnimationStart(Animator animation) {
        mLoadingAnimationStart = true;
      }

      @Override public void onAnimationEnd(Animator animation) {
        mLoadingAnimationStart = false;
        revert = false;
        animateInitial();
      }

      @Override public void onAnimationCancel(Animator animation) {
        mLoadingAnimationStart = false;
      }

      @Override public void onAnimationRepeat(Animator animation) {
        revert = !revert;
        if (!mLoading){
          animation.end();
        }
      }
    });
    animator2.start();

  }

  private boolean revert = false;

  private boolean mLoadingAnimationStart = false;


  public void animateInitial(){
    mAnimationProgress = new float[mSides];
    animateInitial(-1);
  }

  private void animateInitial(float startAt) {

    if (mAnimationRunning) {
      return;
    }

    for (int i = 0; i < mSides; i++) {
      mAnimationRunning = true;
      final int finalI = i;
      ValueAnimator animator;

      if (startAt == -1){
        animator = ValueAnimator.ofFloat(0, 1);
      }else {
        animator = ValueAnimator.ofFloat(startAt,0,1);
      }


      animator.setDuration(500);
      animator.setInterpolator(new AccelerateDecelerateInterpolator());
      animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
        @Override public void onAnimationUpdate(ValueAnimator animation) {
          mAnimationProgress[finalI] = (float) animation.getAnimatedValue();
          invalidate();
        }
      });
      animator.setStartDelay(75 * i);
      if (finalI == mSides - 1) {
        animator.addListener(new Animator.AnimatorListener() {
          @Override public void onAnimationStart(Animator animation) {}

          @Override public void onAnimationEnd(Animator animation) {
            mAnimationRunning = false;
          }

          @Override public void onAnimationCancel(Animator animation) {
            mAnimationRunning = false;
          }

          @Override public void onAnimationRepeat(Animator animation) {}
        });
      }
      animator.start();

    }

    ValueAnimator animator1 = new ValueAnimator().ofFloat(0, 1);
    animator1.setDuration(700);
    animator1.setInterpolator(new OvershootInterpolator(2));
    animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator animation) {
        mAnimationProgress[4] = (float) animation.getAnimatedValue();
        invalidate();
      }
    });
    animator1.start();

  }
}