package co.quchu.quchu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * InnerListView
 * User: Chenhs
 * Date: 2015-11-17
 */
public class InnerListView extends ListView {
    Context context;
    public InnerListView(Context context) {
        super(context);
        this.context=context;
    }

    public InnerListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

    public InnerListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }


}
