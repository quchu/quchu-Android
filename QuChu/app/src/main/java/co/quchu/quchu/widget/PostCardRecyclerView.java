package co.quchu.quchu.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.View;

import com.nineoldandroids.animation.Animator;

import java.util.ArrayList;
import java.util.List;

import co.quchu.quchu.utils.LogUtils;

/**
 * PostCardRecyclerView
 * User: Chenhs
 * Date: 2015-11-11
 */
public class PostCardRecyclerView extends RecyclerView {
    public PostCardRecyclerView(Context context) {
        super(context);
    }

    public PostCardRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PostCardRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure( int widthMeasureSpec, int heightMeasureSpec ) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int childCount = getChildCount();
        if ( childCount > 0 ) {
            int childHeightSpec = MeasureSpec.makeMeasureSpec( childHeight, MeasureSpec.EXACTLY );
            for ( int i = 0 ; i < childCount ; i++ ) {
                View child = getChildAt( i );
                child.measure( widthMeasureSpec, childHeightSpec );
            }
        }
    }
    private int width;
    private int height;
    private final SparseIntArray childOffsets = new SparseIntArray();
    private int childHeight;
    private int focusedIndex = -1;
    private final List<Animator> animators = new ArrayList<Animator>();
    private float xDistance, yDistance, lastX, lastY;
    private float startY, startX, beforeY;
    private float bottomSpace;
    private float centerX;
    private int itemHeight;
    @Override
    protected void onSizeChanged( int w, int h, int oldw, int oldh ) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
        this.childHeight = Math.round( h * 10.87f );
        this.bottomSpace = height - childHeight;
    }


    @Override
    protected void onLayout(boolean changed, int left, int t, int right, int b) {
        LogUtils.json("changed=="+changed+"///ll="+left+"//i=="+t+"//r="+right+"//b"+b);
        final int childCount = getChildCount();
        centerX = ( float ) (right - left) / 2f;
        if ( childCount > 0 ) {
            int paddingTop = getPaddingTop();
            itemHeight = Math.round( (height - paddingTop) / (childCount + 1) );
            for ( int i = 0 ; i < childCount ; i++ ) {
                View child = getChildAt( i );
                child.setDrawingCacheEnabled( true );
                child.setDrawingCacheQuality( View.DRAWING_CACHE_QUALITY_HIGH );
                int itemTop = i * itemHeight + paddingTop;
                child.layout( 0, itemTop, width, itemTop + childHeight );
                childOffsets.put( i, itemTop );
            }
        }
        super.onLayout(changed,left, t, right, b);
    }

    @Override
    public void onViewAdded(View child) {

        LogUtils.json("onViewAdded      child.getY()= "+   child.getY()+"////==   child.getX"+child.getX());
        super.onViewAdded(child);
    }

    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);
    }
}
