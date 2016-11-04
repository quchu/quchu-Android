package co.quchu.quchu.widget.MFrameLayout;

import android.graphics.PointF;
import android.view.View;
import android.widget.AbsListView;

/**
 * Created by mwb on 16/11/3.
 */
public class MFrameHelper {

  private String TAG = "MFrameHelper";

  public final static int POS_START = 0;
  private PointF mLastPointer = new PointF();
  private float mOffsetX;
  private float mOffsetY;
  private int mCurrentPos = 0;
  private int mLastPos = 0;
  private int mPressedPos = 0;
  private float mResistance = 1.7f;//阻尼系数
  private boolean mIsUnderTouch = false;

  public boolean isUnderTouch() {
    return mIsUnderTouch;
  }

  public void onRelease() {
    mIsUnderTouch = false;
  }

  /**
   * 第一次触摸点
   */
  public void onPressDown(float x, float y) {
    mIsUnderTouch = true;
    mPressedPos = mCurrentPos;
    mLastPointer.set(x, y);

//    Log.d(TAG, "onPressDown() : " + " x = " + x + ", y = " + y);
  }

  /**
   * 开始滑动
   */
  public final void onMove(float x, float y) {
    float offsetX = x - mLastPointer.x;
    float offsetY = y - mLastPointer.y;
    processOnMove(x, y, offsetX, offsetY);
    mLastPointer.set(x, y);

//    Log.d(TAG, "onMove() : " +
//        "offsetX = " + offsetX + ", offsetY = " + offsetY + ", x = " + x + ", y = " + y);
  }

  /**
   * 重新处理滑动距离
   */
  protected void processOnMove(float currentX, float currentY, float offsetX, float offsetY) {
    setOffset(offsetX / mResistance, offsetY / mResistance);
  }

  protected void setOffset(float x, float y) {
    mOffsetX = x;
    mOffsetY = y;
  }

  public float getOffsetX() {
    return mOffsetX;
  }

  public float getOffsetY() {
    return mOffsetY;
  }

  public int getLastPosY() {
    return mLastPos;
  }

  public int getCurrentPosY() {
    return mCurrentPos;
  }

  /**
   * 更新界面之前更新当前位置
   */
  public final void setCurrentPos(int current) {
    mLastPos = mCurrentPos;
    mCurrentPos = current;
  }

  /**
   * 此时为移动
   */
  public boolean hasLeftStartPosition() {
    return mCurrentPos > POS_START;
  }

  /**
   * 区别点击还是移动
   */
  public boolean hasMovedAfterPressedDown() {
    return mCurrentPos != mPressedPos;
  }

  public boolean isInStartPosition() {
    return mCurrentPos == POS_START;
  }

  public boolean isAlreadyHere(int to) {
    return mCurrentPos == to;
  }

  public boolean willOverTop(int to) {
    return to < POS_START;
  }

  public static boolean checkCanDoRefresh(View view) {
    if (android.os.Build.VERSION.SDK_INT < 14) {
      if (view instanceof AbsListView) {
        final AbsListView absListView = (AbsListView) view;
        return absListView.getChildCount() > 0
            && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
            .getTop() < absListView.getPaddingTop());
      } else {
        return view.getScrollY() > 0;
      }
    } else {
      return view.canScrollVertically(-1);
    }
  }
}
