package co.quchu.quchu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

/**
 * Created by mwb on 16/10/18.
 */
public class ReboundLinearLayout extends LinearLayout {

  private static final int MAX_Y_OVERSCROLL_DISTANCE = 200;
  private static final float SCROLL_RATIO = 0.5f;// 阻尼系数
  private boolean isMoved;
  private float mY;
  private int mLeft;
  private int mTop;
  private int mRight;
  private int mBottom;
  private int mLastTop;

  public ReboundLinearLayout(Context context) {
    super(context);
  }

  public ReboundLinearLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    super.onLayout(changed, l, t, r, b);

    if (!isMoved) {
      mLeft = getLeft();
      mTop = getTop();
      mRight = getRight();
      mBottom = getBottom();
    }

    mLastTop = getTop();
    Log.e("---mwb", "mLastTop = " + mLastTop);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    int action = event.getAction();
    switch (action) {
      case MotionEvent.ACTION_DOWN:
        mY = event.getY();
        break;

      case MotionEvent.ACTION_UP:
        TranslateAnimation animation = new TranslateAnimation(0, 0, getTop(), mTop);
        animation.setDuration(1000);
        startAnimation(animation);
        Log.e("-----UP", "top = " + getTop() + ", mTop = " + mTop);
        layout(mLeft, mTop, mRight, mBottom);
        postInvalidate();
        break;

      case MotionEvent.ACTION_MOVE:
        isMoved = true;
        float newY = event.getY();
        int offsetY = (int) ((mY - newY) * 0.75);

        layout(mLeft, mTop - offsetY, mRight, mBottom - offsetY);
        postInvalidate();

        int lastTop = mTop - offsetY;
        Log.e("---mwb", "lastTop = " + lastTop);

        Log.e("-----MOVE", "top = " + getTop() + ", mTop = " + mTop);
        break;

      default:
        break;
    }
    return true;
  }
}
