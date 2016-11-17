package co.quchu.quchu.widget;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Nico on 16/11/17.
 */

public class DynamicItemDecoration extends RecyclerView.ItemDecoration  {

  public DynamicItemDecoration() {
    super();
  }

  @Override public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
    super.onDraw(c, parent, state);
  }

  @Override public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {


  }

}
