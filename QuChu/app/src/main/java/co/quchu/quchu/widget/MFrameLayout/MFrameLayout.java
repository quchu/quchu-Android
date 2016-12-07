package co.quchu.quchu.widget.MFrameLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by mwb on 16/11/3.
 */
public class MFrameLayout extends ViewGroup {

  private String TAG = "MFrameLayout";

  private View mHeaderView;
  private View mContentView;
  private int mHeaderHeight;
  private MFrameHelper mFrameHelper;
  private ScrollChecker mScrollChecker;
  private boolean mPreventForHorizontal;//防止水平移动
  private MotionEvent mLastMoveEvent;
  private int mActivePointerId;//当前活动的触摸点

  public MFrameLayout(Context context) {
    this(context, null);
  }

  public MFrameLayout(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public MFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    mFrameHelper = new MFrameHelper();
    mScrollChecker = new ScrollChecker();
  }

  @Override
  protected void onFinishInflate() {
    int childCount = getChildCount();
    if (childCount < 2 || childCount > 2) {
      throw new IllegalStateException("MFrameLayout can only contains two children");
    } else {
      View child1 = getChildAt(0);
      View child2 = getChildAt(1);
      mHeaderView = child1;
      mContentView = child2;
    }
    super.onFinishInflate();
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    if (mScrollChecker != null) {
      mScrollChecker.destroy();
    }
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    if (mHeaderView != null) {
      measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
      mHeaderHeight = mHeaderView.getMeasuredHeight();
//      Log.d(TAG, "onMeasure() header view height = " + mHeaderHeight);
    }

    if (mContentView != null) {
      measureContentView(mContentView, widthMeasureSpec, heightMeasureSpec);
    }
  }

  private void measureContentView(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
    LayoutParams lp = child.getLayoutParams();

    final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
        getPaddingLeft() + getPaddingRight(), lp.width);
    final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,
        getPaddingTop() + getPaddingBottom(), lp.height);

    child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    layoutChildren();
  }

  private void layoutChildren() {
    int offset = mFrameHelper.getCurrentPosY();
    int paddingLeft = getPaddingLeft();
    int paddingTop = getPaddingTop();

    if (mHeaderView != null) {
      final int left = paddingLeft;
      final int top = -(mHeaderHeight - paddingTop - offset);
      final int right = left + mHeaderView.getMeasuredWidth();
      final int bottom = top + mHeaderView.getMeasuredHeight();
      mHeaderView.layout(left, top, right, bottom);
//      Log.d(TAG, "onLayout() header view : " +
//          "left = " + left + ", top = " + top + ", right = " + right + ", bottom = " + bottom);
    }

    if (mContentView != null) {
      final int left = paddingLeft;
      final int top = paddingTop + offset;
      final int right = left + mContentView.getMeasuredWidth();
      final int bottom = top + mContentView.getMeasuredHeight();
//      Log.d(TAG, "onLayout() content view : " +
//          "left = " + left + ", top = " + top + ", right = " + right + ", bottom = " + bottom);
      mContentView.layout(left, top, right, bottom);
    }
  }

  public boolean dispatchTouchEventSupper(MotionEvent e) {
    return super.dispatchTouchEvent(e);
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent e) {
    if (mContentView == null || mHeaderView == null) {
      return dispatchTouchEventSupper(e);
    }

    int action = e.getAction();
    switch (action) {
      case MotionEvent.ACTION_DOWN:
        int pointerIndex = e.getActionIndex();
        mActivePointerId = e.findPointerIndex(pointerIndex);

        mFrameHelper.onPressDown(e.getX(pointerIndex), e.getY(pointerIndex));

        mPreventForHorizontal = false;
        dispatchTouchEventSupper(e);
        return true;

      case MotionEvent.ACTION_MOVE:
        mLastMoveEvent = e;

        int activePointerIndex = e.findPointerIndex(mActivePointerId);

        if (activePointerIndex == MotionEvent.INVALID_POINTER_ID) {
          Log.e(TAG, "mActivePointerId = " + mActivePointerId + ", activePointerIndex = " + activePointerIndex);
          return dispatchTouchEventSupper(e);
        }

        mFrameHelper.onMove(e.getX(activePointerIndex), e.getY(activePointerIndex));

        float offsetX = mFrameHelper.getOffsetX();
        float offsetY = mFrameHelper.getOffsetY();

        //当水平滑动偏移量大于垂直滑动偏移量时禁用移动
        if (Math.abs(offsetX) > Math.abs(offsetY) && mFrameHelper.isInStartPosition()) {
          mPreventForHorizontal = true;
        }

        if (mPreventForHorizontal) {
          return dispatchTouchEventSupper(e);
        }

        boolean moveDown = offsetY > 0;
        boolean moveUp = !moveDown;
        boolean canMoveUp = mFrameHelper.hasLeftStartPosition();

        //当 content view 没有达到顶部时禁用移动
        if (moveDown && mFrameHelper.checkCanDoRefresh(mContentView)) {
          return dispatchTouchEventSupper(e);
        }

        //执行移动
        if ((moveUp && canMoveUp) || moveDown) {
          startMove(offsetY);
          return true;
        }

      case MotionEvent.ACTION_UP:
      case MotionEvent.ACTION_CANCEL:
//        mActivePointerId = MotionEvent.INVALID_POINTER_ID;

        mFrameHelper.onRelease();
        if (mFrameHelper.hasLeftStartPosition()) {
          tryScrollBackToTop();
          if (mFrameHelper.hasMovedAfterPressedDown()) {
            sendCancelEvent();
            return true;
          }
          return dispatchTouchEventSupper(e);
        } else {
          return dispatchTouchEventSupper(e);
        }

      case MotionEvent.ACTION_POINTER_DOWN:
        int multiDownPointerIndex = e.getActionIndex();
        int multiDownPointerId = e.findPointerIndex(multiDownPointerIndex);
        if (multiDownPointerId != mActivePointerId) {
          mActivePointerId = e.findPointerIndex(multiDownPointerIndex);
          mFrameHelper.onPressDown(e.getX(multiDownPointerIndex), e.getY(multiDownPointerIndex));
        }
        return true;

      case MotionEvent.ACTION_POINTER_UP:
        int multiUpPointerIndex = e.getActionIndex();
        int multiUpPointerId = e.findPointerIndex(multiUpPointerIndex);
        if (multiUpPointerId == mActivePointerId) {
          int newPointerIndex = multiUpPointerIndex == 0 ? 1 : 0;
          mActivePointerId = e.getPointerId(newPointerIndex);
          mFrameHelper.onPressDown(e.getX(newPointerIndex), e.getY(newPointerIndex));
        }
        return true;
    }
    return dispatchTouchEventSupper(e);
  }

  /**
   * 如果 deltaY > 0,开始移动 content view
   */
  private void startMove(float deltaY) {
    // has reached the top
    if ((deltaY < 0 && mFrameHelper.isInStartPosition())) {
//      Log.d(TAG, "has reached the top");
      return;
    }

    int to = mFrameHelper.getCurrentPosY() + (int) deltaY;

    if (mFrameHelper.willOverTop(to)) {
//      Log.d(TAG, "over top");
      to = mFrameHelper.POS_START;
    }

    mFrameHelper.setCurrentPos(to);
    int change = to - mFrameHelper.getLastPosY();
    mHeaderView.offsetTopAndBottom(change);
    mContentView.offsetTopAndBottom(change);
    invalidate();
  }

  /**
   * 手指抬起时回到顶部
   */
  private void tryScrollBackToTop() {
    if (!mFrameHelper.isUnderTouch()) {
//      Log.d(TAG, "try scroll back to top");
      mScrollChecker.tryToScrollTo(MFrameHelper.POS_START, 1000);
    }
  }

  /**
   * 当移动时,不响应点击事件
   */
  private void sendCancelEvent() {
    if (mLastMoveEvent == null) {
      return;
    }
    MotionEvent last = mLastMoveEvent;
    MotionEvent e = MotionEvent.obtain(last.getDownTime(), last.getEventTime() + ViewConfiguration.getLongPressTimeout(), MotionEvent.ACTION_CANCEL, last.getX(), last.getY(), last.getMetaState());
    dispatchTouchEventSupper(e);
  }

  class ScrollChecker implements Runnable {

    private int mLastFlingY;
    private Scroller mScroller;
    private boolean mIsRunning = false;
    private int mStart;
    private int mTo;

    public ScrollChecker() {
      mScroller = new Scroller(getContext());
    }

    public void run() {
      boolean finish = !mScroller.computeScrollOffset() || mScroller.isFinished();
      int curY = mScroller.getCurrY();
      int deltaY = curY - mLastFlingY;

      if (!finish) {
        mLastFlingY = curY;
        startMove(deltaY);
        post(this);
      } else {
        finish();
      }
    }

    private void finish() {
      reset();
    }

    private void reset() {
      mIsRunning = false;
      mLastFlingY = 0;
      removeCallbacks(this);
    }

    private void destroy() {
      reset();
      if (!mScroller.isFinished()) {
        mScroller.forceFinished(true);
      }
    }

    public void abortIfWorking() {
      if (mIsRunning) {
        if (!mScroller.isFinished()) {
          mScroller.forceFinished(true);
        }
        reset();
      }
    }

    public void tryToScrollTo(int to, int duration) {
      if (mFrameHelper.isAlreadyHere(to)) {
        return;
      }
      mStart = mFrameHelper.getCurrentPosY();
      mTo = to;
      int distance = to - mStart;

      removeCallbacks(this);

      mLastFlingY = 0;

      if (!mScroller.isFinished()) {
        mScroller.forceFinished(true);
      }

      mScroller.startScroll(0, 0, 0, distance, duration);
      post(this);
      mIsRunning = true;
    }
  }
}
