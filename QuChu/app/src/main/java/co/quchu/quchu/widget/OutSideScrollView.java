package co.quchu.quchu.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ScrollView;

import co.quchu.quchu.utils.LogUtils;

/**
 * OutSideScrollView
 * User: Chenhs
 * Date: 2015-11-17
 */
public class OutSideScrollView extends ScrollView {
    private OverScrolledListener listener;

    public OutSideScrollView(Context context) {
        super(context);
    }

    public OutSideScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OutSideScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOverScrollListener(OverScrolledListener listener) {
        this.listener = listener;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public OutSideScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        LogUtils.json("scrollY==" + scrollY);
        if (listener != null)
            listener.onOverScrolled(scrollX, scrollY);
    }

   public interface OverScrolledListener {
        void onOverScrolled(int scrollX, int scrollY);
    }
}
