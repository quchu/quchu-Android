package co.quchu.quchu.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by admin on 2016/3/8.
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int mSpace;
    private int mSpanCount = 1;

    public SpacesItemDecoration(int space) {
        this.mSpace = space;
    }public SpacesItemDecoration(int space,int spanCount) {
        this.mSpace = space;
        this.mSpanCount = spanCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,RecyclerView parent, RecyclerView.State state) {

        if (mSpanCount>0 && parent.getChildLayoutPosition(view)<=mSpanCount-1){
            outRect.top = mSpace *2;
        }else{
            outRect.top = mSpace;
        }


        if (parent.getChildLayoutPosition(view)>=state.getItemCount()-2){
            outRect.bottom = mSpace * 2;
        }else{
            outRect.bottom = mSpace;
        }

        outRect.right = mSpace;
        outRect.left = mSpace;

        // Add top margin only for the first item to avoid double space between items
//        if (parent.getChildLayoutPosition(view) == 0) {
//            outRect.left = 0;
//        } else {
//            outRect.left = space;
//        }
    }

}