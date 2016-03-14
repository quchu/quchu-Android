package co.quchu.quchu.widget.AnimationViewPager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.AccelerateInterpolator;

import java.lang.reflect.Field;

/**
 * NoScrollViewPager
 * User: Chenhs
 * Date: 2015-12-08
 */
public class NoScrollViewPager extends ViewPager {
    private boolean noScroll = true;

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public void setNoScroll(boolean noScroll) {
        this.noScroll = noScroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        return !noScroll && super.onTouchEvent(arg0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return !noScroll && super.onInterceptTouchEvent(arg0);
    }


    public void setSlowAnimation(ViewPager viewPager,int animationDuration) {
        try {
            Field field = viewPager.getClass().getDeclaredField("mScroller");
            field.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(viewPager.getContext(),
                    new AccelerateInterpolator());
            field.set(viewPager, scroller);
            scroller.setmDuration(animationDuration);
        } catch (Exception e) {
        }
    }

    public void setSlowAnimation(ViewPager viewPager) {
        setSlowAnimation(viewPager,2000);
    }
}
