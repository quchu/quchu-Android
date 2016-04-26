package co.quchu.quchu.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.NestedScrollingParent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;

import co.quchu.quchu.utils.LogUtils;

/**
 * Created by no21 on 2016/4/18.
 * email:437943145@qq.com
 * desc :
 */
public class DampView extends ViewGroup implements NestedScrollingParent {


    private int ORIENTATION = 0;
    public static final int ORIENTATION_VERTICAL = 0;
    public static final int ORIENTATION_HORIZONTAL = 1;


    private RefreshView refreshView;
    private View headView;
    private View footView;


    //刷新中
    private boolean refreshing;
    //头部可见
    private boolean headViewVisiliby;
    private int childCount;

    public DampView(Context context) {
        super(context);
    }

    public DampView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DampView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DampView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        LogUtils.e("子View个数:" + childCount);
        int height = getMeasuredHeight();
        int width = getMeasuredWidth();

        if (childCount < 1) {
            return;
        }
        int left;
        int top;
        int childWidth;
        int childHeight;

        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            if (view == headView) {
                top = -view.getMeasuredHeight();
                childHeight = view.getMeasuredHeight();
            } else {
                top = getPaddingTop();
                childHeight = height - getPaddingTop() - getPaddingBottom();
            }
            left = getPaddingLeft();
            childWidth = width - getPaddingRight() - getPaddingLeft();

            view.layout(left, top, left + childWidth, top + childHeight);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        childCount = getChildCount();

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        measureChildren(widthMeasureSpec, heightMeasureSpec);


        setMeasuredDimension(sizeWidth, sizeHeight);

//        for (int i = 0; i < childCount; i++) {
//            View view = getChildAt(i);
//            if (view != footView && view != footView) {//scrollView
//                view.measure(MeasureSpec.makeMeasureSpec(
//                        getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
//                        MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(
//                        getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY));
//            } else {
//                measureChild(view, widthMeasureSpec, heightMeasureSpec);
//            }
//        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setRefreshView(null);
        // TODO: 2016/4/26  测试
    }

    public void setRefreshView(RefreshView refreshView) {
        this.refreshView = new RefreshView();
        this.headView = LayoutInflater.from(getContext()).inflate(this.refreshView.getHeadViewId(), this, false);
        addView(headView, 0);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        if (dyUnconsumed < 0) {//下拉
            headViewVisiliby = true;
            onPull((float) dyUnconsumed / 3);
        } else {
            headViewVisiliby = false;
        }
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if (dy > 0 && headViewPushable) {//scrollView向上滑动
            consumed[1] = dy;
            onPull(dy);
        }
    }

    //HeadView是否可以向上移动
    boolean headViewPushable = false;

    /**
     * 下拉
     *
     * @param unconsumed 下拉的距离
     */
    private boolean onPull(float unconsumed) {
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            if (view != footView) {
                // TODO: 2016/4/26  方向
                float translation = getTranslation(view) - unconsumed;
                if (translation < 0) {
                    translation = 0;
                    headViewPushable = false;
                } else {
                    headViewPushable = true;
                }
                view.setTranslationY(translation);
            }
        }
        return headViewPushable;
    }

    private float getTranslation(View view) {
        if (ORIENTATION == ORIENTATION_VERTICAL) {
            return view.getTranslationY();
        }
        return view.getTranslationX();
    }

    private void release() {
        headViewVisiliby = false;
        AnimatorSet set = new AnimatorSet();
        set.setDuration(300);
        set.setInterpolator(new DecelerateInterpolator());

        List<Animator> animators = new ArrayList<>(3);
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            // TODO: 2016/4/26  方向以及footView
            ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", view.getTranslationY(), 0);
            animators.add(animator);
        }
        set.playTogether(animators);
        set.start();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                if (headViewVisiliby) {
                    release();
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return super.onNestedFling(target, velocityX, velocityY, consumed);
    }


    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        super.onNestedScrollAccepted(child, target, nestedScrollAxes);
    }


    public void onStopNestedScroll(View target) {
        super.onStopNestedScroll(target);
    }


    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(target, dx, dy, consumed);
    }


    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return super.onNestedPreFling(target, velocityX, velocityY);
    }


    public int getNestedScrollAxes(){
        return super.getNestedScrollAxes();
    }
}
