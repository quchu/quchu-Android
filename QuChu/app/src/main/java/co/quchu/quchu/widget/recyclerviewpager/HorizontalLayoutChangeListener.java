package co.quchu.quchu.widget.recyclerviewpager;

import android.view.View;

/**
 * HorizontalLayoutChangeListener
 * User: Chenhs
 * Date: 2015-11-04
 */
public class HorizontalLayoutChangeListener implements View.OnLayoutChangeListener {
    RecyclerViewPager mRecyclerView;
    public HorizontalLayoutChangeListener(RecyclerViewPager mRecyclerView) {
        this.mRecyclerView=mRecyclerView;
    }
    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if (mRecyclerView.getChildCount() < 3) {
            if (mRecyclerView.getChildAt(1) != null) {
                if(mRecyclerView.getCurrentPosition()==0) {
                    View v1 = mRecyclerView.getChildAt(1);
                    v1.setScaleY(0.85f);
                    v1.setScaleX(0.93f);
                } else {
                    View v1 = mRecyclerView.getChildAt(0);
                    v1.setScaleY(0.85f);
                    v1.setScaleX(0.93f);
                }
            }
        } else {
            if (mRecyclerView.getChildAt(0) != null) {
                View v0 = mRecyclerView.getChildAt(0);
                v0.setScaleY(0.85f);
                v0.setScaleX(0.93f);
            }
            if (mRecyclerView.getChildAt(2) != null) {
                View v2 = mRecyclerView.getChildAt(2);
                v2.setScaleY(0.85f);
                v2.setScaleX(0.93f);
            }
        }

    }
}
