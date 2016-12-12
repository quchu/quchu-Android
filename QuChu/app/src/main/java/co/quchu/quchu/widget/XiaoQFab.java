package co.quchu.quchu.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.DiscretePathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

/**
 * Created by Nico on 16/11/5.
 */


public class XiaoQFab extends FloatingActionButton {

  private boolean mAnimationRunning;
  private float[] mAnimationProgress;
  private int mSides = 5;
  public boolean mLoading = false;
  private int mSize = -1;
  private float mStrokeWidth = -1;
  private int mHalfSize = -1;
  private int mOffSetOutBlk = -1;
  private int mOffSetSec = -1;
  private int mOffSetThd = -1;
  private int mOffSetF = -1;
  private boolean mShowPromote = false;

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

    if(-1==mSize)mSize =  Math.min(getWidth(), getHeight());
    if(-1==mStrokeWidth) mStrokeWidth = mSize/13f;
    if(-1==mHalfSize)mHalfSize =  mSize/2;
    if(-1==mOffSetOutBlk)mOffSetOutBlk =  (mSize / 21);
    if(-1==mOffSetSec) mOffSetSec = (int) (mSize / 7f);
    if(-1==mOffSetThd) mOffSetThd = (int) (mSize / 4.5f);
    if(-1==mOffSetF) mOffSetF = (int) (mSize / 3.15f);

    Paint paint = new Paint();
    paint.setColor(Color.BLACK);
    paint.setStrokeWidth(mStrokeWidth);
    paint.setStyle(Paint.Style.STROKE);
    paint.setAntiAlias(true);
    paint.setStrokeCap(Paint.Cap.ROUND);


    Paint bg = new Paint();
    bg.setColor(Color.WHITE);
    bg.setStyle(Paint.Style.FILL);
    bg.setAntiAlias(true);


    canvas.drawCircle(mHalfSize,mHalfSize,mHalfSize-(mStrokeWidth/2),bg);



    if (!mLoadingAnimationStart){
      if (null != mAnimationProgress) {

        float radius1 = 360 *mAnimationProgress[0];
        float radius2 = 360 *mAnimationProgress[1];
        float radius3 = 360 *mAnimationProgress[2];
        float radius4 = 360 *mAnimationProgress[3];

        canvas.drawArc(new RectF(mOffSetOutBlk, mOffSetOutBlk, mSize - mOffSetOutBlk, mSize - mOffSetOutBlk),
            radius1, radius1, false, paint);

        paint.setColor(Color.WHITE);

        canvas.drawArc(new RectF(mOffSetSec, mOffSetSec, mSize - mOffSetSec, mSize - mOffSetSec),
            180 * mAnimationProgress[1], radius2, false, paint);

        paint.setStrokeWidth(mSize/12);
        paint.setColor(Color.parseColor("#ffd702"));

        canvas.drawArc(new RectF(mOffSetThd, mOffSetThd, mSize - mOffSetThd, mSize - mOffSetThd),
            120 * mAnimationProgress[2], radius3, false, paint);

        paint.setStrokeWidth(mSize/9);
        paint.setColor(Color.BLACK);
        canvas.drawArc(new RectF(mOffSetF, mOffSetF, mSize - mOffSetF, mSize - mOffSetF),
            240 * mAnimationProgress[3], radius4, false, paint);

        canvas.save();
        canvas.rotate(-180 + (mAnimationProgress[4] * 180), getWidth() / 2, getHeight() / 2);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#eaeaea"));
        canvas.drawCircle(mSize / 4, mSize - (mSize/4), (mHalfSize/7) * mAnimationProgress[3], paint);
        canvas.restore();

      }


    }else{
      //canvas.save();
      //canvas.rotate((revert?360:-360)* mAnimationProgress[0],getWidth()/2,getHeight()/2);
      canvas.drawArc(new RectF(mOffSetOutBlk, mOffSetOutBlk, mSize - mOffSetOutBlk, mSize - mOffSetOutBlk),
          (revert?360:-360)  * mAnimationProgress[0], (revert?360:-360)  * mAnimationProgress[0], false, paint);
      //canvas.restore();
    }


    if (mShowPromote){
      float width = getWidth()/2.5f;
      float indicatorDiameter = width/2.5f;

      float whiteDotsSize = indicatorDiameter/5;
      float radius = indicatorDiameter/2;

      Paint paintIndicator = new Paint();
      paintIndicator.setColor(Color.RED);
      paintIndicator.setStyle(Paint.Style.FILL);
      paintIndicator.setAntiAlias(true);
      float offset = getWidth()-width;
      canvas.drawRect(offset+radius,indicatorDiameter,getWidth()-radius,indicatorDiameter+indicatorDiameter,paintIndicator);
      canvas.drawCircle(offset+radius,radius+indicatorDiameter,radius,paintIndicator);
      canvas.drawCircle(getWidth()-radius,radius+indicatorDiameter,radius,paintIndicator);

      Paint paintIndicatorWhite = new Paint(paintIndicator);
      paintIndicatorWhite.setColor(Color.WHITE);
      canvas.drawCircle(getWidth()-radius,radius+indicatorDiameter,whiteDotsSize,paintIndicatorWhite);
      canvas.drawCircle(offset+radius,radius+indicatorDiameter,whiteDotsSize,paintIndicatorWhite);
      canvas.drawCircle(offset+(width/2),radius+indicatorDiameter,whiteDotsSize,paintIndicatorWhite);
    }

  }


  public void setPromote(boolean showPromote){
    mShowPromote = showPromote;
    invalidate();
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
    animator2.setInterpolator(new LinearInterpolator());
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
          if (finalI==4){
            return;
          }
          mAnimationProgress[finalI] = (float) animation.getAnimatedValue();
          invalidate();
        }
      });
      animator.setStartDelay(100 * i);
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
    animator1.setStartDelay(500);
    animator1.setInterpolator(new OvershootInterpolator(4f));
    animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator animation) {
        mAnimationProgress[4] = (float) animation.getAnimatedValue();
        invalidate();
      }
    });
    animator1.start();
  }

}