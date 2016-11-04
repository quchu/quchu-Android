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

import java.util.ArrayList;
import java.util.List;

import co.quchu.quchu.R;

/**
 * Created by TacticalTwerking on 16/6/23.
 * GitHub   https://github.com/TacticalTwerking
 */
public class PolygonProgressView extends View {

  private static final long ANIMATION_DURATION = 1000l;
  private static final long ANIMATION_DELAY = 75l;
  private int mSize = -1;
  private int mCirclePadding = -1;
  private int mViewCenter = -1;
  private int mCircleStrokeWidth = 4;
  private int mSides = 6;
  private int mActuallyRadius = -1;
  private int mHighLightPadding = 5;
  private int mRotateOffset = 90;
  private float mPieces = 0;
  private float mMinimalValuesPercentage = .5f;
  private float[] mProgressValues;
  private float[] mAnimationProgress;
  private String[] mLabels = new String[]{};
  private Paint mPaintCircle;
  private Paint mPaintNodes;
  private Paint mPaintArcs;
  private Paint mPaintInnerLines;
  private Paint mPaintLabels;
  private Paint mPaintAvatarBg;
  private boolean mAnimationRunning = false;

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

  public void initial(int side, float[] values) {
    if (mAnimationRunning) {
      return;
    }
    mSides = side;
    mProgressValues = values;
    mPieces = mSides > 0 ? 360 / mSides : 0;
  }

  public void initial(int side, float[] values, String[] labels) {
    if (mAnimationRunning) {
      return;
    }
    mSides = side;
    mProgressValues = values;
    mLabels = labels;
    mPieces = mSides > 0 ? 360 / mSides : 0;
  }

  public void animateProgress() {
    if (mAnimationRunning) {
      return;
    }

    mAnimationProgress = new float[mSides];
    for (int i = 0; i < mSides; i++) {
      mAnimationRunning = true;
      final int finalI = i;
      ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
      animator.setDuration(ANIMATION_DURATION);
      animator.setInterpolator(new AccelerateDecelerateInterpolator());
      animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
          mAnimationProgress[finalI] = (float) animation.getAnimatedValue();
          invalidate();
        }
      });
      animator.setStartDelay(ANIMATION_DELAY * i);
      if (finalI == mSides - 1) {
        animator.addListener(new Animator.AnimatorListener() {
          @Override
          public void onAnimationStart(Animator animation) {
          }

          @Override
          public void onAnimationEnd(Animator animation) {
            mAnimationRunning = false;
          }

          @Override
          public void onAnimationCancel(Animator animation) {
            mAnimationRunning = false;
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
    mPaintCircle = new Paint();
    mPaintCircle.setStrokeWidth(mCircleStrokeWidth);
    mPaintCircle.setColor(Color.parseColor("#bed4da"));
    mPaintCircle.setAntiAlias(true);
    mPaintCircle.setStrokeCap(Paint.Cap.ROUND);
    mPaintCircle.setShader(null);
    mPaintCircle.setStyle(Paint.Style.STROKE);

    mPaintNodes = new Paint(mPaintCircle);
    mPaintNodes.setColor(Color.parseColor("#ffd102"));
    mPaintNodes.setStrokeWidth(2);
    mPaintNodes.setStyle(Paint.Style.FILL);

    mPaintArcs = new Paint(mPaintCircle);
    mPaintArcs.setColor(Color.parseColor("#ffd102"));
    mPaintArcs.setStrokeCap(Paint.Cap.SQUARE);
    mPaintArcs.setStrokeWidth(5);

    mPaintInnerLines = new Paint(mPaintCircle);
    mPaintInnerLines.setStrokeWidth(1);
    mPaintInnerLines.setColor(Color.parseColor("#e2edf1"));

    mPaintAvatarBg = new Paint();
    mPaintAvatarBg.setAntiAlias(true);
    mPaintAvatarBg.setColor(Color.WHITE);
    mPaintAvatarBg.setStyle(Paint.Style.FILL);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    initProperties();
    drawCircle(canvas);
    drawInnerPattern(canvas);

    for (int i = 0; i < mSides; i++) {
      double angle = ((Math.PI * 2 / mSides) * i) - (Math.toRadians(mRotateOffset));

      drawInnerLines(canvas, angle);
      drawArcs(canvas, i);
      //DrawNode belong here
      drawLabels(canvas, i, angle);
    }

    drawNodes(canvas);
    drawAvatar(canvas);
  }

  private void drawInnerPattern(Canvas canvas) {
    canvas.save();
    canvas.rotate(90, mViewCenter, mViewCenter);
    int scaleSize = mSize / 7;
    canvas.drawBitmap(mBitmapBackground
        , new Rect(0, 0, mBitmapBackground.getHeight(), mBitmapBackground.getWidth())
        , new Rect(mCirclePadding, mCirclePadding, mSize - mCirclePadding, mSize - mCirclePadding), mPaintArcs);
    canvas.restore();
  }

  private Bitmap mBitmapBackground;

  private void drawAvatar(Canvas canvas) {
    canvas.drawCircle(mViewCenter, mViewCenter, mActuallyRadius * mMinimalValuesPercentage * .6f * mAnimationProgress[0], mPaintAvatarBg);
  }

  private void drawArcs(Canvas canvas, int i) {
    List<Integer> maxValues = getMaxValues(mProgressValues);
    RectF rectF = new RectF(mCirclePadding, mCirclePadding, (mActuallyRadius * 2) + mCirclePadding, (mActuallyRadius * 2) + mCirclePadding);
    if (maxValues.size() > i) {
      int sweepAngle = (int) (mPieces * mAnimationProgress[maxValues.get(i)]);
      int startAngle = (int) ((maxValues.get(i) * mPieces) - (sweepAngle / 2) - mRotateOffset);
      if (mSides == 0) {
        mHighLightPadding = 0;
      }

      if (sweepAngle != 0) {
        canvas.drawArc(rectF, startAngle + mHighLightPadding, sweepAngle - mHighLightPadding, false, mPaintArcs);
      }
    }
  }

  private void drawLabels(Canvas canvas, int i, double angle) {
    if (null == mLabels || mLabels.length != mSides) {
      return;
    }

    int maxLength = mActuallyRadius;

//    String strNumericValues = new DecimalFormat("##").format(mProgressValues[i] * mAnimationProgress[i] * 100);

    Rect textBoundLabel = new Rect();
//    Rect textBoundNumeric = new Rect();

    mPaintLabels.getTextBounds(mLabels[i], 0, mLabels[i].length(), textBoundLabel);
//    mPaintLabels.getTextBounds(strNumericValues, 0, strNumericValues.length(), textBoundNumeric);

    float actuallyValues = maxLength + textBoundLabel.width();

    float x = (int) (Math.cos(angle) * actuallyValues + mViewCenter);
    float y = (int) (Math.sin(angle) * actuallyValues + mViewCenter);

    canvas.drawText(mLabels[i], x - (textBoundLabel.width() / 2), y + (textBoundLabel.height() / 2), mPaintLabels);

//    canvas.drawText(strNumericValues, x - (textBoundNumeric.width() / 2), y - (textBoundLabel.height() / 2), mPaintLabels);
  }

  private void drawInnerLines(Canvas canvas, double angle) {
    float actuallyValues = mActuallyRadius;
    float x = (int) (Math.cos(angle) * actuallyValues + mViewCenter);
    float y = (int) (Math.sin(angle) * actuallyValues + mViewCenter);
    canvas.drawLine(mViewCenter, mViewCenter, x, y, mPaintInnerLines);
  }

  private void drawNodes(Canvas canvas) {
    Path path = new Path();
    float minimalValues = 0;
    float startX = 0;
    float startY = 0;
    int maxHill = mActuallyRadius - mCircleStrokeWidth * 2;

    if (mMinimalValuesPercentage > 0) {
      minimalValues = maxHill * mMinimalValuesPercentage;
    }
    for (int i = 0; i < mSides; i++) {

      float actuallyValues = mProgressValues[i] * maxHill * mAnimationProgress[i];
      if (minimalValues > 0) {
        actuallyValues = minimalValues + (maxHill - minimalValues) * mProgressValues[i] * mAnimationProgress[i];
      }

      double angle = ((Math.PI * 2 / mSides) * i) - (Math.toRadians(mRotateOffset));

      float x = (int) (Math.cos(angle) * actuallyValues + mViewCenter);
      float y = (int) (Math.sin(angle) * actuallyValues + mViewCenter);

      if (i == 0) {
        startX = x;
        startY = y;
        path.moveTo(x, y);
      } else {
        path.lineTo(x, y);
      }
    }
    path.lineTo(startX, startY);
    canvas.drawPath(path, mPaintNodes);
  }

  private void drawCircle(Canvas canvas) {
    canvas.drawCircle(mViewCenter, mViewCenter, mActuallyRadius, mPaintCircle);
    canvas.drawCircle(mViewCenter, mViewCenter, mActuallyRadius / 2, mPaintInnerLines);
  }

  private void initProperties() {
    if (mSize == -1) {
      mSize = Math.min(getHeight(), getWidth());
      mCirclePadding = mSize / 7;
      mViewCenter = mSize / 2;
      mActuallyRadius = mViewCenter - mCirclePadding;
      mProgressValues = new float[mSides];
      mAnimationProgress = new float[mSides];

      mPaintLabels = new Paint(mPaintInnerLines);
      mPaintLabels.setTextSize(mSize / 30);
      mPaintLabels.setColor(Color.parseColor("#bed4da"));
      mPaintLabels.setStyle(Paint.Style.FILL);
      mPaintLabels.setAntiAlias(true);
      mPieces = mSides > 0 ? 360 / mSides : 0;
      mBitmapBackground = BitmapFactory.decodeResource(getResources(), R.mipmap.bg);
    }
  }

  private List<Integer> getMaxValues(float[] source) {
    List<Integer> maxArrayIndex = new ArrayList<>();
    float maxValue = source[0];
    int maxValuesIndex = 0;
    boolean notUnique = false;
    for (int i = 1; i < source.length; i++) {
      if (maxValue < source[i]) {
        maxValue = source[i];
        maxValuesIndex = i;
      } else if (maxValue == source[i]) {
        notUnique = true;
      }
    }
    if (notUnique) {
      for (int i = 0; i < source.length; i++) {
        if (maxValue == source[i]) {
          maxArrayIndex.add(i);
        }
      }
    } else {
      maxArrayIndex.add(maxValuesIndex);
    }
    return maxArrayIndex;
  }
}
