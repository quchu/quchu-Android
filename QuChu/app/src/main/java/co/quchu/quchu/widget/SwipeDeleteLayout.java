package co.quchu.quchu.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.HorizontalScrollView;

import com.sina.weibo.sdk.utils.LogUtil;

import java.lang.reflect.Field;

import co.quchu.quchu.R;
import co.quchu.quchu.utils.StringUtils;

/**
 * Created by no21 on 2016/5/20.
 * email:437943145@qq.com
 * desc :
 */
public class SwipeDeleteLayout extends HorizontalScrollView {

    private View mActionView;

    public SwipeDeleteLayout(Context context) {
        this(context, null);
    }

    public SwipeDeleteLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeDeleteLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOverScrollMode(OVER_SCROLL_NEVER);
        setSmoothScrollingEnabled(false);
        try {
            Field field = getClass().getSuperclass().getDeclaredField("mMaximumVelocity");
            field.setAccessible(true);
            field.setInt(this, 2);

            field = getClass().getSuperclass().getDeclaredField("mMinimumVelocity");
            field.setAccessible(true);
            field.setInt(this, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                if (scrollX != 0) {
                    scrollBy(0, 0);
                    if (scrollX > mActionView.getWidth() / 2) {
                        animation(mActionView.getWidth() - scrollX);
                    } else {
                        animation(-scrollX);
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);


    }

    public void animation(int offset) {
        ValueAnimator animator = ValueAnimator.ofInt(offset);
        animator.setDuration(200);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            int firstValues = 0;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int values = (int) animation.getAnimatedValue();
                LogUtil.e("values", "values" + values);
                scrollBy(values - firstValues, 0);
                firstValues = values;
            }
        });
        animator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


        mActionView = findViewById(R.id.swipe_delete_action);
        View contentView = findViewById(R.id.swipe_delete_content);
        View itemView = getChildAt(0);

        int actionWidth = mActionView.getMeasuredWidth();
        int actionWidthSpec = MeasureSpec.makeMeasureSpec(actionWidth, MeasureSpec.EXACTLY);


        int contentWidth = getResources().getDisplayMetrics().widthPixels - StringUtils.dip2px(getContext(), 32);
        int ContentWidth = MeasureSpec.makeMeasureSpec(contentWidth, MeasureSpec.EXACTLY);

        itemView.measure(MeasureSpec.makeMeasureSpec(contentWidth + actionWidth, MeasureSpec.AT_MOST), heightMeasureSpec);

        contentView.measure(ContentWidth, heightMeasureSpec);

        int ContentHeight = MeasureSpec.makeMeasureSpec(contentView.getMeasuredHeight(), MeasureSpec.EXACTLY);
        mActionView.measure(actionWidthSpec, ContentHeight);


        setMeasuredDimension(contentWidth, ContentHeight);

    }

}
