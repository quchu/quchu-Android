package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.QuChuHistoryModel;
import co.quchu.quchu.net.NetUtil;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.QuChuHistoryPresenter;
import co.quchu.quchu.view.adapter.QuHistoryAdapter;
import co.quchu.quchu.widget.ErrorView;

/**
 * 趣处浏览记录
 * <p>
 * Created by mwb on 16/11/5.
 */
public class QuchuHistoryActivity extends BaseBehaviorActivity implements SwipeRefreshLayout.OnRefreshListener {

  @Bind(R.id.qu_history_recycler_view) RecyclerView mRecyclerView;
  @Bind(R.id.qu_history_refresh_layout) SwipeRefreshLayout mRefreshLayout;
  @Bind(R.id.history_error_view) ErrorView mHistoryErrorView;

  private int pageNo = 1;
  private String mPlaceIds = "";
  private QuHistoryAdapter mAdapter;
  private boolean mHasMoreData;
  private boolean mIsLoadMore;
  private boolean mIsLoading;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_quchu_history);
    ButterKnife.bind(this);

    EnhancedToolbar toolbar = getEnhancedToolbar();
    TextView textView = toolbar.getTitleTv();
    textView.setText("历史记录");
    textView.setTextSize(16f);

    initView();

    if (!NetUtil.isNetworkConnected(this)) {
      mHistoryErrorView.showViewDefault(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (!NetUtil.isNetworkConnected(QuchuHistoryActivity.this)) {
            makeToast(R.string.network_error);
            return;
          }
          onRefresh();
        }
      });

    } else {
      mRefreshLayout.post(new Runnable() {
        @Override
        public void run() {
          onRefresh();
        }
      });
    }
  }

  private void initView() {
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    mAdapter = new QuHistoryAdapter(this,mRecyclerView);
    mAdapter.setQuChuHistoryClickListener(onItemClickListener);
    mRecyclerView.setAdapter(mAdapter);
    mRefreshLayout.setOnRefreshListener(this);
    mRefreshLayout.setEnabled(false);

    mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        mAdapter.resetBestList();

        //判断是否在顶部 false-顶部
        boolean canScrollDown = recyclerView.canScrollVertically(-1);
        //判断是否在底部 false-底部
        boolean canScrollUp = recyclerView.canScrollVertically(1);

        if (!canScrollDown) {
          //已经在顶部,可以下拉刷新
          mRefreshLayout.setEnabled(false);
        } else {
          //不能下拉刷新
          mRefreshLayout.setEnabled(false);
        }

        if (!canScrollUp && mHasMoreData) {
          //已经在底部,可以上拉加载更多
          queryMoreData();
        }
      }
    });
  }

  /**
   * 加载更多数据
   */
  private void queryMoreData() {
    if (mIsLoading) return;

    if (NetUtil.isNetworkConnected(this)) {
      DialogUtil.showProgress(this, R.string.loading_dialog_text);
    }

    mIsLoading = true;
    mIsLoadMore = true;
    pageNo++;

    QuChuHistoryPresenter.getHistory(this, mPlaceIds, pageNo, mCommonListener);
  }

  @Override
  public void onRefresh() {
    pageNo = 1;
    mIsLoadMore = false;
    mRefreshLayout.setRefreshing(true);
    QuChuHistoryPresenter.getHistory(this, mPlaceIds, pageNo, mCommonListener);
  }

  private CommonListener<QuChuHistoryModel> mCommonListener = new CommonListener<QuChuHistoryModel>() {
    @Override
    public void successListener(QuChuHistoryModel response) {
      mHistoryErrorView.hideView();

      mIsLoading = false;
      if (DialogUtil.isDialogShowing()) {
        DialogUtil.dismissProgress();
      }

      if (mRefreshLayout.isRefreshing()) {
        mRefreshLayout.setRefreshing(false);
      }

      if (response == null) {
        return;
      }

      if (!mIsLoadMore) {
        mPlaceIds = response.getPlaceIds();

        mAdapter.setBestList(response.getBestList());
      }

      QuChuHistoryModel.PlaceListBean placeListBean = response.getPlaceList();
      List<QuChuHistoryModel.PlaceListBean.ResultBean> resultBeanList = null;
      if (placeListBean != null) {
        resultBeanList = placeListBean.getResult();

        int pageCount = placeListBean.getPageCount();
        int pagesNo = placeListBean.getPagesNo();
        int resultCount = placeListBean.getResultCount();

        //判断是否存在分页
        if (pagesNo < pageCount) {
          mHasMoreData = true;
        } else {
          mHasMoreData = false;
        }

        if (pagesNo == 1) {
          mAdapter.setResultList(resultBeanList);
        } else {
          mAdapter.addMoreResultList(resultBeanList);
        }
      }
    }

    @Override
    public void errorListener(VolleyError error, String exception, String msg) {
      mIsLoading = false;
      DialogUtil.dismissProgress();
      mHistoryErrorView.hideView();

      if (mRefreshLayout.isRefreshing()) {
        mRefreshLayout.setRefreshing(false);
      }
    }
  };

  private QuHistoryAdapter.QuChuHistoryClickListener onItemClickListener = new QuHistoryAdapter.QuChuHistoryClickListener() {
    @Override
    public void onItemClick(QuChuHistoryModel.PlaceListBean.ResultBean resultBean) {
      if (resultBean != null) {
        Intent intent = new Intent(getApplicationContext(), QuchuDetailsActivity.class);
        intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_PID, resultBean.getPid());
        startActivity(intent);
      }
    }
  };

  @Override
  public ArrayMap<String, Object> getUserBehaviorArguments() {
    return null;
  }

  @Override
  public int getUserBehaviorPageId() {
    return 0;
  }

  @Override
  protected int activitySetup() {
    return TRANSITION_TYPE_LEFT;
  }

  @Override
  protected String getPageNameCN() {
    return null;
  }
}
