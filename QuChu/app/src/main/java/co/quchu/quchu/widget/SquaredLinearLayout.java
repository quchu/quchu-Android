package co.quchu.quchu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by Nico on 16/7/4.
 */
public class SquaredLinearLayout extends LinearLayout {
    public SquaredLinearLayout(Context context) {
        super(context);
    }

    public SquaredLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquaredLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
