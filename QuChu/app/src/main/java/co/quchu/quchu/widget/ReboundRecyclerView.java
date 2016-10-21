package co.quchu.quchu.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by mwb on 16/10/19.
 */
public class ReboundRecyclerView extends RecyclerView {

  private float mY;
  private int mLeft;
  private int mTop;
  private int mRight;
  private int mBottom;
  private boolean mIsMoved;
  private boolean mCanScrollDown;
  private boolean mCanScrollUp;

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
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    super.onLayout(changed, l, t, r, b);

    if (!changed) {
      mLeft = getLeft();
      mTop = getTop();
      mRight = getRight();
      mBottom = getBottom();
    }

//    Log.e("onLayout()", "getTop() = " + getTop());
  }

  @Override
  public void onScrolled(int dx, int dy) {
    super.onScrolled(dx, dy);

    //在顶部,手指不能继续下滑
    mCanScrollDown = canScrollVertically(-1);
    //在底部,手指不能继续上滑
    mCanScrollUp = canScrollVertically(1);

//    Log.e("onScrolled", "mCanScrollDown = " + mCanScrollDown + ", mCanScrollUp = " + mCanScrollUp);
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
//    Log.e("---mwb", "dispatchTouchEvent");

    int action = ev.getAction();
    switch (action) {
      case MotionEvent.ACTION_DOWN:
//        Log.e("dispatchTouchEvent", "ACTION_DOWN");

        mY = ev.getY();
        break;

      case MotionEvent.ACTION_UP:
//        Log.e("dispatchTouchEvent", "ACTION_UP");

        if (mIsMoved) {
          layout(mLeft, mTop, mRight, mBottom);
          mIsMoved = false;
          return true;
        }

        break;

      case MotionEvent.ACTION_MOVE:
//        Log.e("dispatchTouchEvent", "ACTION_MOVE");

        float newY = ev.getY();
        int offset = (int) (mY - newY);

        if (offset < 0) {
          //手指下滑
          if (!mCanScrollDown) {

            layout(mLeft, (int) (mTop - offset * 0.75), mRight, (int) (mBottom - offset * 0.75));

            mIsMoved = true;
            return true;
          }
        } else {
          //手指上滑
          if (!mCanScrollUp) {

            layout(mLeft, (int) (mTop - offset * 0.75), mRight, (int) (mBottom - offset * 0.75));

            mIsMoved = true;
            return true;
          }
        }
        break;

      default:
        break;
    }
    return super.dispatchTouchEvent(ev);
  }

//  @Override
//  public boolean onTouchEvent(MotionEvent event) {
////    Log.e("onTouchEvent", "mCanScrollDown = " + mCanScrollDown + ", mCanScrollUp = " + mCanScrollUp);
//
//    int action = event.getAction();
//    switch (action) {
//      case MotionEvent.ACTION_DOWN:
//        Log.e("----mwb", "ACTION_DOWN");
//
//        mY = event.getY();
//        break;
//
//      case MotionEvent.ACTION_UP:
//
//        Log.e("----mwb", "ACTION_UP");
//
//        if (!mIsMoved) {
//          return super.onTouchEvent(event);
//        }
//
//        layout(mLeft, mTop, mRight, mBottom);
////        postInvalidate();
//        break;
//
//      case MotionEvent.ACTION_MOVE:
//
//        Log.e("----mwb", "ACTION_MOVE");
//
//        float newY = event.getY();
//        int offset = (int) (mY - newY);
//
//        if (offset < 0) {
//          //手指下滑
//          if (!mCanScrollDown) {
//
//            Log.e("----mwb", "mCanScrollDown");
//
//
//            layout(mLeft, (int) (mTop - offset * 0.75), mRight, (int) (mBottom - offset * 0.75));
////            postInvalidate();
//
//            mIsMoved = true;
//            return true;
//          }
//
//        } else {
//          //手指上滑
//          if (!mCanScrollUp) {
//
//            Log.e("----mwb", "mCanScrollUp");
//
//
//            layout(mLeft, (int) (mTop - offset * 0.75), mRight, (int) (mBottom - offset * 0.75));
////            postInvalidate();
//
//            mIsMoved = true;
//            return true;
//          }
//        }
//        break;
//
//      default:
//        break;
//    }
//
//    Log.e("----mwb", "ACTION_END");
//
//    return super.onTouchEvent(event);
//  }
}
