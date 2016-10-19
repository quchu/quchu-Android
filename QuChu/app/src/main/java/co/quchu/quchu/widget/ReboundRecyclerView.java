package co.quchu.quchu.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by mwb on 16/10/19.
 */
public class ReboundRecyclerView extends RecyclerView {

  public ReboundRecyclerView(Context context) {
    super(context);
  }

  public ReboundRecyclerView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  public boolean onTouchEvent(MotionEvent e) {
    return super.onTouchEvent(e);
  }
}
