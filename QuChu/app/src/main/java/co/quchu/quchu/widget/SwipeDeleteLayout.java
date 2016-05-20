package co.quchu.quchu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

import co.quchu.quchu.R;
import co.quchu.quchu.utils.StringUtils;

/**
 * Created by no21 on 2016/5/20.
 * email:437943145@qq.com
 * desc :
 */
public class SwipeDeleteLayout extends HorizontalScrollView {
    public SwipeDeleteLayout(Context context) {
        this(context, null);
    }

    public SwipeDeleteLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeDeleteLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


        View actionView = findViewById(R.id.swipe_delete_action);
        View contentView = findViewById(R.id.swipe_delete_content);
        View itemView = getChildAt(0);

        int actionWidth = actionView.getMeasuredWidth();
        int actionWidthSpec = MeasureSpec.makeMeasureSpec(actionWidth, MeasureSpec.UNSPECIFIED);


        int ContentHeight = MeasureSpec.makeMeasureSpec(contentView.getMeasuredHeight(), MeasureSpec.EXACTLY);
        int contentWidth = getResources().getDisplayMetrics().widthPixels - StringUtils.dip2px(getContext(), 32);
        int ContentWidth = MeasureSpec.makeMeasureSpec(contentWidth, MeasureSpec.EXACTLY);

        contentView.measure(ContentWidth, ContentHeight);

        actionView.measure(actionWidthSpec, ContentHeight);

        itemView.measure(MeasureSpec.makeMeasureSpec(contentWidth + actionWidth, MeasureSpec.AT_MOST), heightMeasureSpec);



        setMeasuredDimension(contentWidth, ContentHeight);

    }
}
