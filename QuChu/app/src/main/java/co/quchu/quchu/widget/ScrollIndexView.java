package co.quchu.quchu.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import co.quchu.quchu.R;

/**
 * Created by no21 on 2016/4/7.
 * email:437943145@qq.com
 * desc :
 */
public class ScrollIndexView extends FrameLayout {
    public ScrollIndexView(Context context) {
        super(context);
        init();
    }


    public ScrollIndexView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScrollIndexView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ScrollIndexView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.layout_scroll_index, this);
    }

}
