//package co.quchu.quchu.widget;
//
//import android.graphics.Rect;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//
///**
// * Created by admin on 2016/3/8.
// */
//public class GridItemDecoration extends RecyclerView.ItemDecoration {
//    private int mSpace;
//    private int mSpanCount;
//
//    public GridItemDecoration(int space,int spanCount) {
//        this.mSpace = space;
//        this.mSpanCount = spanCount;
//
//    }
//
//    @Override
//    public void getItemOffsets(Rect outRect, View view,RecyclerView parent, RecyclerView.State state) {
//
//        if (parent.getChildCount()<=mSpanCount-1||parent.getChildLayoutPosition(view) <= mSpanCount-1){
//           outRect.right = 0;
//           outRect.bottom = 0;
//           outRect.top = 0;
//           outRect.left = 0;
//        }else{
//            outRect.right = 0;
//            outRect.bottom = 0;
//            outRect.top = mSpace;
//            outRect.left = 0;
//        }
//        // Add top margin only for the first item to avoid double space between items
//
//    }
//
//}