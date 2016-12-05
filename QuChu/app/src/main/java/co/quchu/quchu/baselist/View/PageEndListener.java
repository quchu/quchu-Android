package co.quchu.quchu.baselist.View;

/**
 * Created by Nico on 16/12/2.
 */

import android.support.v7.widget.RecyclerView;

public abstract class PageEndListener extends RecyclerView.OnScrollListener {


  @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
    super.onScrolled(recyclerView, dx, dy);
    if (!recyclerView.canScrollVertically(1)) {
      onLoadMore();
    }
  }

  public abstract void onLoadMore();
}
