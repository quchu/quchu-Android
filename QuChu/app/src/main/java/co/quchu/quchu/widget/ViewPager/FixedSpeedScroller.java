package co.quchu.quchu.widget.ViewPager;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Created by mwb on 2016/12/14.
 */
public class FixedSpeedScroller extends Scroller {

  private int mDuration = 300;

  public FixedSpeedScroller(Context context) {
    super(context);
  }

  public FixedSpeedScroller(Context context, Interpolator interpolator) {
    super(context, interpolator);
  }

  @Override
  public void startScroll(int startX, int startY, int dx, int dy) {
    super.startScroll(startX, startY, dx, dy);
  }

  @Override
  public void startScroll(int startX, int startY, int dx, int dy, int duration) {
    super.startScroll(startX, startY, dx, dy, mDuration);
  }

  public void setmDuration(int time) {
    mDuration = time;
  }

  public int getmDuration() {
    return mDuration;
  }
}
