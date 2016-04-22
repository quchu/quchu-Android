//package co.quchu.quchu.view.adapter;
//
//import android.content.Context;
//import android.graphics.Rect;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//
//import co.quchu.quchu.utils.StringUtils;
//
///**
// * FlickrDecoration
// * User: Chenhs
// * Date: 2015-11-16
// */
//public class FlickrDecoration extends RecyclerView.ItemDecoration {
//
//    private int space;
//    private int lineNum=-1;
//    public FlickrDecoration(Context mContext,int space){
//        this.space= StringUtils.dip2px(mContext,space);
//    }
//
//    public void setLineNum(int lineNum){
//        this.lineNum=lineNum;
//    }
//    @Override
//    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        if (lineNum ==-1) {
//            outRect.top = space;
//            outRect.right = space;
//            outRect.left = space;
//            outRect.bottom = space;
//        }else{
//            if (parent.getChildPosition(view)==0){
//                outRect.top = space;
//                outRect.right =space;
//                outRect.left =8*space;
//                outRect.bottom = space;
//            }else if (parent.getChildPosition(view)%(lineNum-1)==0){
//                outRect.top = space;
//                outRect.right = 8* space;
//                outRect.left = space;
//                outRect.bottom = space;
//            }else if (parent.getChildPosition(view)%(lineNum)==0) {
//                outRect.top = space;
//                outRect.right =space;
//                outRect.left =  8 * space;
//                outRect.bottom = space;
//            }else{
//                outRect.top = space;
//                outRect.right =space;
//                outRect.left =  space;
//                outRect.bottom = space;
//            }
//        }
//
//    }
//}
