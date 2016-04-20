package co.quchu.quchu.widget;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.NestedScrollingParent;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by no21 on 2016/4/18.
 * email:437943145@qq.com
 * desc :
 */
public class DampView extends ViewGroup implements NestedScrollingParent {

    private View contentView;

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

        if (getChildCount() < 1) {
            return;
        }
        int height = getMeasuredHeight();
        int width = getMeasuredWidth();

        int left = getPaddingLeft();
        int top = getPaddingTop();

        getContentView();
        int right = width - getPaddingLeft() - contentView.getPaddingRight();
        int bottom = height - getPaddingTop() - getPaddingBottom();
        contentView.layout(left, top, left + right, top + bottom);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        if (dyUnconsumed != 0)
            getContentView().setTranslationY(getContentView().getTranslationY() - dyUnconsumed / 3);
    }

    private View getContentView() {
        if (contentView == null) {
            contentView = getChildAt(0);
        }
        return contentView;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                if (getContentView().getTranslationY() != 0) {
                    ObjectAnimator animator = ObjectAnimator.ofFloat(contentView, "translationY", contentView.getTranslationY(), 0);
                    animator.setDuration(300);
                    animator.setInterpolator(new DecelerateInterpolator());
                    animator.start();
                }
                break;
        }


        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        getContentView().measure(MeasureSpec.makeMeasureSpec(
                getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
                MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(
                getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY));

    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return super.onNestedFling(target, velocityX, velocityY, consumed);
    }
}
