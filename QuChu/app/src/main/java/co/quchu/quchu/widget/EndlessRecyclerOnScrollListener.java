package co.quchu.quchu.widget;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
  public static String TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();

  private int previousTotal = 0; // The total number of items in the dataset after the last load
  private boolean loading = true; // True if we are still waiting for the last set of data to load.
  private int visibleThreshold = 0; // The minimum amount of items to have below your current scroll position before loading more.
  int firstVisibleItem, visibleItemCount, totalItemCount;

  private int current_page = 1;

  private RecyclerView.LayoutManager mLinearLayoutManager;

  private int scrolledDistanceY = 0;
  private int mTargetHeight;
  boolean isFirst = true;

  public EndlessRecyclerOnScrollListener(RecyclerView.LayoutManager layoutManager) {
    this.mLinearLayoutManager = layoutManager;
  }

  @Override
  public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
    super.onScrolled(recyclerView, dx, dy);

    scrolledDistanceY += dy;

    visibleItemCount = recyclerView.getChildCount();
    totalItemCount = mLinearLayoutManager.getItemCount();

    if (mLinearLayoutManager instanceof LinearLayoutManager) {
      firstVisibleItem = ((LinearLayoutManager) mLinearLayoutManager).findFirstVisibleItemPosition();

      View firstView = ((LinearLayoutManager) mLinearLayoutManager).findViewByPosition(0);
      if (isFirst && firstView != null) {
        mTargetHeight = firstView.getHeight();
        isFirst = false;
      }

      if (loading) {
        if (totalItemCount > previousTotal) {
          loading = false;
          previousTotal = totalItemCount;
        }
      }
      if (!loading && (totalItemCount - visibleItemCount)
          <= (firstVisibleItem + visibleThreshold)) {
        // End has been reached

        // Do something
        current_page++;

        onLoadMore(current_page);

        loading = true;
      }
    } else if (mLinearLayoutManager instanceof StaggeredGridLayoutManager) {
      int[] items = new int[3];
      ((StaggeredGridLayoutManager) mLinearLayoutManager).findFirstVisibleItemPositions(items);
      firstVisibleItem = items[0];

      if (loading) {
        if (totalItemCount > previousTotal) {
          loading = false;
          previousTotal = totalItemCount;
        }
      }
      if (!loading && (totalItemCount - visibleItemCount)
          <= (firstVisibleItem + visibleThreshold)) {
        // End has been reached

        // Do something
        current_page++;

        onLoadMore(current_page);

        loading = true;
      }
    }

    onScrolledDistance(scrolledDistanceY, mTargetHeight);
  }

  /**
   * @param scrolledDistanceY 垂直方向的滑动距离
   * @param targetHeight 第一个 item 的高度
   */
  public void onScrolledDistance(int scrolledDistanceY, int targetHeight) {

  }

  public abstract void onLoadMore(int current_page);

  public void loadingComplete() {
    this.loading = false;
  }
}