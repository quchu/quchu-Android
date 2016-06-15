package co.quchu.quchu.view.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import co.quchu.quchu.utils.StringUtils;

/**
 * FlickrDecoration
 * User: Chenhs
 * Date: 2015-11-16
 */
public class ClassifyDecoration extends RecyclerView.ItemDecoration {

    private int space;
    private int lineNum = -1;

    public ClassifyDecoration(Context mContext) {
        this.space = StringUtils.dip2px(mContext, 8);
    }

    public void setLineNum(int lineNum) {
        this.lineNum = lineNum;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int itemCount = parent.getAdapter().getItemCount() - 1;
        if (lineNum == -1) {
            if (parent.getChildPosition(view) == 0) {
                outRect.top = 2 * space;
                outRect.right = 0;
                outRect.left = 0;
                outRect.bottom = space;
            } else if (parent.getChildPosition(view) == itemCount) {
                outRect.top = space;
                outRect.right = 0;
                outRect.left = 0;
                outRect.bottom = 3 * space;
            } else {
                outRect.top = space;
                outRect.right = 0;
                outRect.left = 0;
                outRect.bottom = space;
            }

        } else {
            if (parent.getChildPosition(view) == 0) {
                outRect.top = 8 * space;
                outRect.right = 3 * space;
                outRect.left = 8 * space;
                outRect.bottom = space;
            } else if (parent.getChildPosition(view) % (lineNum - 1) == 0) {
                outRect.top = space;
                outRect.right = 8 * space;
                outRect.left = space;
                outRect.bottom = space;
            } else if (parent.getChildPosition(view) % (lineNum) == 0) {
                outRect.top = space;
                outRect.right = space;
                outRect.left = 8 * space;
                outRect.bottom = space;
            } else {
                outRect.top = space;
                outRect.right = space;
                outRect.left = space;
                outRect.bottom = space;
            }
        }

    }
}
