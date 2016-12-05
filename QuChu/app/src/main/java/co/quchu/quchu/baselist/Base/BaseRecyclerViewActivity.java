package co.quchu.quchu.baselist.Base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import co.quchu.quchu.R;
import co.quchu.quchu.baselist.View.PageEndListener;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 * Created by Nico on 16/11/29.
 */

public abstract class BaseRecyclerViewActivity<T extends PagerDataModel> extends AppCompatActivity
    implements SwipeRefreshLayout.OnRefreshListener {

  protected boolean mOnRefresh = false;
  protected boolean mOnLoadMore = false;
  protected int mPageNo = 1;
  protected int mPageSize = -1;
  protected RecyclerView mRecyclerView;
  protected SwipeRefreshLayout mSwipeRefreshLayout;
  private BaseRecyclerViewAdapter mAdapter;
  private PageEndListener mEndlessRecyclerOnScrollListener;

  public abstract RecyclerView.LayoutManager  getLayoutManager();
  public abstract BaseViewHolderFactory getViewHolderFactory();
  public abstract BasePagerApi<T> getAPI();

  public boolean supportLoadMore(){
    return true;
  }


  public RecyclerView.ItemAnimator getDefaultItemAnimator(){
    return new DefaultItemAnimator();
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recyclerview);

    mRecyclerView = (RecyclerView) findViewById(R.id.rvContent);
    mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.rl);
    mRecyclerView.setLayoutManager(getLayoutManager());
    if (null!=getDefaultItemAnimator()){
      mRecyclerView.setItemAnimator(getDefaultItemAnimator());
    }
    mAdapter = new BaseRecyclerViewAdapter(getViewHolderFactory());
    mRecyclerView.setAdapter(mAdapter);
    mSwipeRefreshLayout.setOnRefreshListener(this);
    mEndlessRecyclerOnScrollListener = new PageEndListener() {
      @Override public void onLoadMore() {
        if (mOnLoadMore || mPageSize<0){
          return;
        }
        if (mPageNo>mPageSize){
          updateNoMore(true);
        }else{
          mOnLoadMore = true;
          onLoad(true);
        }

      }
    };
    if (supportLoadMore()){
      mRecyclerView.addOnScrollListener(mEndlessRecyclerOnScrollListener);
    }
    onRefresh();
  }

  void updateNoMore(boolean statue){
    mAdapter.updateNoMore(statue);
  }

  void setLoadMore(boolean status){
    mAdapter.updateLoadMore(status);
  }


  void onLoad(final boolean loadMore){
    getAPI().fetch(mPageNo+(loadMore?1:0)).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<T>() {
          public void onCompleted() {
            if (!loadMore){
              mOnRefresh = false;
              if (mSwipeRefreshLayout.isRefreshing()){
                mSwipeRefreshLayout.setRefreshing(false);
              }
            }else{
              mPageNo+=1;
              setLoadMore(false);
              mOnLoadMore = false;
            }
          }

          public void onError(Throwable e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
          }

          public void onNext(T data) {

            if (null== data.getData().getResult() && data.getData().getResult().size()<1){
              return;
            }

            if (!loadMore){
              updateNoMore(false);
              mPageNo = 1;
              mPageSize = data.getData().getPageCount();
              mAdapter.reloadData(data.getData().getResult());
            }else{
              mAdapter.addData(data.getData().getResult());
            }

          }
        });
  }

  @Override public void onRefresh() {
    mSwipeRefreshLayout.setRefreshing(true);
    mOnRefresh = true;
    onLoad(false);
  }
}
