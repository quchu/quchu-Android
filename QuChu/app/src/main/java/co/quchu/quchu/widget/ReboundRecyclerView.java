package co.quchu.quchu.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by mwb on 16/10/19.
 */
public class ReboundRecyclerView extends RecyclerView {

  private static String TAG = "ReboundRecyclerView";

  private float mY;
  private boolean mIsMoved;
  private boolean mCanScrollDown;
  private boolean mCanScrollUp;
  private Rect mNormal = new Rect();
  private View mInner;
  private boolean mCanMove;

  public ReboundRecyclerView(Context context) {
    super(context);
  }

  public ReboundRecyclerView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public ReboundRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();

    mInner = this;
  }

  @Override
  public void onScrolled(int dx, int dy) {
    super.onScrolled(dx, dy);

    //在顶部,手指不能继续下滑
    mCanScrollDown = canScrollVertically(-1);
    //在底部,手指不能继续上滑
    mCanScrollUp = canScrollVertically(1);
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    if (mInner == null) {
      return super.onTouchEvent(ev);
    }

    int action = ev.getAction();

    switch (action) {
      case MotionEvent.ACTION_DOWN:
        Log.e(TAG, "ACTION_DOWN");
        mY = ev.getY();
        mCanMove = true;
        break;

      case MotionEvent.ACTION_UP:
        Log.e(TAG, "ACTION_UP");
        if (mIsMoved) {
          layout(mNormal.left, mNormal.top, mNormal.right, mNormal.bottom);
          mIsMoved = false;
          mCanMove = true;
          return true;
        }
        break;

      case MotionEvent.ACTION_POINTER_DOWN:
        Log.e(TAG, "ACTION_POINTER_DOWN");
        mCanMove = false;
        break;

      case MotionEvent.ACTION_POINTER_UP:
        Log.e(TAG, "ACTION_POINTER_UP");
        mCanMove = false;
        break;

      case MotionEvent.ACTION_MOVE:
        Log.e(TAG, "ACTION_MOVE");
        float preY = mY;
        float nowY = ev.getY();
        int offset = (int) (preY - nowY);

        if (mNormal.isEmpty()) {
          mNormal.set(mInner.getLeft(), mInner.getTop(), mInner.getRight(), mInner.getBottom());
        }

        if (mCanMove) {
          if (offset < 0) {
            //手指下滑
            if (!mCanScrollDown) {
              mInner.layout(mNormal.left, (int) (mNormal.top - offset * 0.75), mNormal.right, (int) (mNormal.bottom - offset * 0.75));
              mIsMoved = true;
              Log.e(TAG, "ACTION_MOVE" + ", " + getLeft() + ", " + getTop() + ", " + getRight() + ", " + getBottom());
              return true;
            }
          } else {
            //手指上滑
            if (!mCanScrollUp) {
              mInner.layout(mNormal.left, (int) (mNormal.top - offset * 0.75), mNormal.right, (int) (mNormal.bottom - offset * 0.75));
              mIsMoved = true;
              Log.e(TAG, "ACTION_MOVE" + ", " + getLeft() + ", " + getTop() + ", " + getRight() + ", " + getBottom());
              return true;
            }
          }
        }

        mY = nowY;
        break;

      default:
        break;
    }
    return super.dispatchTouchEvent(ev);
  }
}
