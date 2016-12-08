package co.quchu.quchu.widget;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

/**
 * Created by Nico on 16/11/7.
 */

public class ScrollToLinearLayoutManager extends LinearLayoutManager {
  private static final float MILLISECONDS_PER_INCH = 200f;
  private Context mContext;

  public ScrollToLinearLayoutManager(Context context) {
    super(context);
    mContext = context;
  }

  @Override public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state,
      final int position) {

    float factor;
    if (position - findFirstVisibleItemPosition()>50){
      factor = .001f;
    }else if (position - findFirstVisibleItemPosition()>25){
      factor = 0.05f;
    } else if (position - findFirstVisibleItemPosition()>10){
      factor = 0.5f;
    } else{
      factor = 1;
    }

    final float finalFactor = factor;
    LinearSmoothScroller smoothScroller = new LinearSmoothScroller(mContext) {

      //This controls the direction in which smoothScroll looks
      //for your view
      @Override public PointF computeScrollVectorForPosition(int targetPosition) {
        return ScrollToLinearLayoutManager.this.computeScrollVectorForPosition(targetPosition);
      }

      //This returns the milliseconds it takes to
      //scroll one pixel.
      @Override protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {


        return (MILLISECONDS_PER_INCH * finalFactor)/ displayMetrics.densityDpi;
      }
    };

    smoothScroller.setTargetPosition(position);
    startSmoothScroll(smoothScroller);
  }
}