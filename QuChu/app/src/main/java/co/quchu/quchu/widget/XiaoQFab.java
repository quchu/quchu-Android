package co.quchu.quchu.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import co.quchu.quchu.R;

/**
 * Created by Nico on 16/11/5.
 */


public class XiaoQFab extends FloatingActionButton {

  private boolean mAnimationRunning;
  public boolean mLoading = false;
  private float mAnimationProgress;
  private float mStrokeWidth = -1;
  private int mSize = -1;
  private int mHalfSize = -1;
  private int mOffSetOutBlk = -1;
  private int mOffSetSec = -1;
  private int mOffSetThd = -1;
  private int mOffSetF = -1;
  private boolean mShowPromote = false;
  private boolean mRevert = false;
  private boolean mLoadingAnimationStart = false;
  private float mLoadingDotsFact = 0;

  private float[] mAnimationValueSet = new float[]{1,1,1};

  private float mLoadingDotsRadius = -1;

  private Bitmap mBitmapAlice;

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

    if (-1 == mSize) mSize = Math.min(getWidth(), getHeight());
    if (-1 == mStrokeWidth) mStrokeWidth = mSize / 13f;
    if (-1 == mHalfSize) mHalfSize = mSize / 2;
    if (-1 == mOffSetOutBlk) mOffSetOutBlk = (mSize / 21);
    if (-1 == mOffSetSec) mOffSetSec = (int) (mSize / 7f);
    if (-1 == mOffSetThd) mOffSetThd = (int) (mSize / 4.5f);
    if (-1 == mOffSetF) mOffSetF = (int) (mSize / 3.15f);
    if (-1 == mLoadingDotsRadius) mLoadingDotsRadius = mSize/15;
    if (null == mBitmapAlice) {
      mBitmapAlice = BitmapFactory.decodeResource(getResources(), R.drawable.ic_alice);
    }

    Paint paint = new Paint();
    paint.setColor(Color.parseColor("#2d313c"));
    paint.setStrokeWidth(mStrokeWidth);
    paint.setStyle(Paint.Style.FILL);
    paint.setAntiAlias(true);
    paint.setStrokeCap(Paint.Cap.ROUND);

    Paint bg = new Paint();
    bg.setColor(Color.WHITE);
    bg.setStyle(Paint.Style.FILL);
    bg.setAntiAlias(true);

    canvas.drawCircle(mHalfSize, mHalfSize, mHalfSize - (mStrokeWidth / 2), bg);

    //canvas.drawBitmap(mBitmapAlice, (getWidth() - mBitmapAlice.getWidth()) / 2,
    //    (getHeight() - mBitmapAlice.getHeight()) / 2 - mOffSetOutBlk, paint);

    Paint pCircle = new Paint();
    pCircle.setStyle(Paint.Style.FILL);
    pCircle.setColor(Color.WHITE);



    float centerWidth = getWidth()/2;
    float centerHeigh = getHeight()/2 - mOffSetOutBlk;

    Paint p1,p2,p3;
    p1 = new Paint(paint);
    p1.setColor(Color.parseColor("#F44336"));

    p2 = new Paint(p1);
    p2.setColor(Color.parseColor("#4CAF50"));

    p3 = new Paint(p2);
    p3.setColor(Color.parseColor("#03A9F4"));
    canvas.drawBitmap(mBitmapAlice, (getWidth() - mBitmapAlice.getWidth()) / 2,
        (getHeight() - mBitmapAlice.getHeight()) / 2 - mOffSetOutBlk, paint);

    if (mLoadingAnimationStart){
      canvas.drawArc(
          new RectF(mOffSetOutBlk, mOffSetOutBlk, mSize - mOffSetOutBlk, mSize - mOffSetOutBlk), 20,
          360* mAnimationProgress, true, pCircle);

      canvas.drawCircle((centerWidth/2)+(1-mLoadingDotsFact)*(centerWidth/2),centerHeigh,mLoadingDotsRadius*mAnimationValueSet[0]*mLoadingDotsFact,p2);
      canvas.drawCircle(centerWidth,centerHeigh,mLoadingDotsRadius*mAnimationValueSet[1]*mLoadingDotsFact,p1);
      canvas.drawCircle(centerWidth+((centerWidth/2)*mLoadingDotsFact),centerHeigh,mLoadingDotsRadius*mAnimationValueSet[2]*mLoadingDotsFact,p3);
    }else{

      canvas.drawArc(
          new RectF(mOffSetOutBlk, mOffSetOutBlk, mSize - mOffSetOutBlk, mSize - mOffSetOutBlk), 20,
          (mRevert ? 360 : -360) * mAnimationProgress, true, pCircle);
    }

  }

  public void setPromote(boolean showPromote) {
    mShowPromote = showPromote;
    invalidate();
  }

  public void endLoading() {
    if (!mLoading) {
      return;
    }
    mLoading = false;
  }

  public void animateLoading() {
    if (mAnimationRunning || mLoadingAnimationStart) {
      return;
    }
    mLoading = true;

    ValueAnimator animationHideAlice = new ValueAnimator().ofFloat(0, 1);
    animationHideAlice.setDuration(750);
    animationHideAlice.setInterpolator(new AccelerateDecelerateInterpolator());
    animationHideAlice.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator animation) {
        float i = (float) animation.getAnimatedValue();
        mAnimationProgress = i;
        invalidate();
      }
    });

    animationHideAlice.start();

    ValueAnimator dotsAppearAnimation = new ValueAnimator().ofFloat(0,1);
    dotsAppearAnimation.setStartDelay(750);
    dotsAppearAnimation.setDuration(500);
    dotsAppearAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator animation) {
        mLoadingDotsFact = (float) animation.getAnimatedValue();
        invalidate();
      }
    });
    dotsAppearAnimation.addListener(new Animator.AnimatorListener() {

      @Override public void onAnimationStart(Animator animation) {
        mLoadingAnimationStart = true;
      }

      @Override public void onAnimationEnd(Animator animation) {}

      @Override public void onAnimationCancel(Animator animation) {}

      @Override public void onAnimationRepeat(Animator animation) {}
    });
    dotsAppearAnimation.start();


    for (int i = 0; i < 3; i++) {
      ValueAnimator valueAnimator = new ValueAnimator().ofFloat(1,.5f);
      valueAnimator.setDuration(500);
      valueAnimator.setInterpolator(new LinearInterpolator());
      valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
      valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
      final int finalI = i;
      valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
        @Override public void onAnimationUpdate(ValueAnimator animation) {
          mAnimationValueSet[finalI] = (float) animation.getAnimatedValue();
          invalidate();
        }
      });
      valueAnimator.setStartDelay((i*200)+750+500);
      valueAnimator.start();
      valueAnimator.addListener(new Animator.AnimatorListener() {
        @Override public void onAnimationStart(Animator animation) {}

        @Override public void onAnimationEnd(Animator animation) {
          mLoadingAnimationStart = false;
          mRevert = false;
          animateInitial();
        }

        @Override public void onAnimationCancel(Animator animation) {
          mLoadingAnimationStart = false;
        }

        @Override public void onAnimationRepeat(Animator animation) {
          mRevert = !mRevert;
          if (!mLoading && mRevert) {
            animation.end();
          }
        }
      });
    }


  }

  public void animateInitial() {
    mAnimationProgress = 1;
    animateInitial(-1);
  }

  private void animateInitial(float startAt) {

    if (mAnimationRunning) {
      return;
    }

    mAnimationRunning = true;
    ValueAnimator animator;

    if (startAt == -1) {
      animator = ValueAnimator.ofFloat(1, 0);
    } else {
      animator = ValueAnimator.ofFloat(startAt, 0);
    }

    animator.setDuration(750);
    animator.setInterpolator(new AccelerateDecelerateInterpolator());
    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator animation) {
        mAnimationProgress = (float) animation.getAnimatedValue();
        invalidate();
      }
    });
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
    animator.start();
  }
}