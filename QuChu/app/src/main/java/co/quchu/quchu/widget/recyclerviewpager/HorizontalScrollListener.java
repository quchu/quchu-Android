//package co.quchu.quchu.widget.recyclerviewpager;
//
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.View;
//
//public class HorizontalScrollListener extends RecyclerView.OnScrollListener {
//    private RecyclerViewPager mRecyclerView;
//
//    public HorizontalScrollListener(RecyclerViewPager mRecyclerView) {
//        this.mRecyclerView = mRecyclerView;
//    }
//
//    @Override
//    public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
//        System.out.println("onScrolled"+"onScrollStateChanged  "+scrollState);
//    }
//
//    @Override
//    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//        System.out.println("onScrolled"+  dx + "|" + dy);
////                mPositionText.setText("First: " + mRecyclerView.getFirstVisiblePosition());
//        int childCount = mRecyclerView.getChildCount();
//        int width = mRecyclerView.getChildAt(0).getWidth();
//        int padding = (mRecyclerView.getWidth() - width) / 2;
//
//        for (int j = 0; j < childCount; j++) {
//            View v = recyclerView.getChildAt(j);
//            //往左 从 padding 到 -(v.getWidth()-padding) 的过程中，由大到小
//            float rate = 0;
//
//            if (v.getLeft() <= padding) {
//                if (v.getLeft() >= padding - v.getWidth()) {
//                    rate = (padding - v.getLeft()) * 1f / v.getWidth();
//                } else {
//                    rate = 1;
//                }
//
//                v.setScaleY(1 - rate * 0.1f);
//                v.setScaleX(1 - rate * 0.1f);
////                if (v.getLeft()==padding){
////                    v.setAlpha(1);
////                }else{
////                    v.setAlpha(1-rate+.5f);
////                }
//
//            } else {
//                //往右 从 padding 到 recyclerView.getWidth()-padding 的过程中，由大到小
//                if (v.getLeft() <= recyclerView.getWidth() - padding) {
//                    rate = (recyclerView.getWidth() - padding - v.getLeft()) * 1f / v.getWidth();
//                }
//                v.setScaleY(0.9f + rate * 0.1f);
//                v.setScaleX(0.9f + rate * 0.1f);
////                v.setAlpha(.5f+rate);
//
//            }
//        }
//    }
//}
//
//
//
//
