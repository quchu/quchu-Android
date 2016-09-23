package io.rong.imkit;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by mwb on 16/9/23.
 */
public class ListViewEx extends ListView {

  public ListViewEx(Context context) {
    super(context);
  }

  public ListViewEx(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public ListViewEx(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
        MeasureSpec.AT_MOST);
    super.onMeasure(widthMeasureSpec, expandSpec);
  }
}
