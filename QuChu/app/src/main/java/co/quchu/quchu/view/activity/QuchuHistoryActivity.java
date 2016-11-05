package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.android.volley.VolleyError;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.model.QuChuHistoryModel;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.QuChuHistoryPresenter;
import co.quchu.quchu.view.adapter.AdapterBase;
import co.quchu.quchu.view.adapter.QuChuHistoryAdapter;

/**
 * 趣处浏览记录
 * <p>
 * Created by mwb on 16/11/5.
 */
public class QuChuHistoryActivity extends BaseBehaviorActivity implements SwipeRefreshLayout.OnRefreshListener {

  @Bind(R.id.qu_history_recycler_view) RecyclerView mRecyclerView;
  @Bind(R.id.qu_history_refresh_layout) SwipeRefreshLayout mRefreshLayout;

  private int pageNo = 1;
  private String mPlaceIds = "";
  private QuChuHistoryAdapter mAdapter;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_quchu_history);
    ButterKnife.bind(this);

    EnhancedToolbar toolbar = getEnhancedToolbar();
    TextView textView = toolbar.getTitleTv();
    textView.setText("Alice");

    initView();
  }

  private void initView() {
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    mAdapter = new QuChuHistoryAdapter(this);
    mRecyclerView.setAdapter(mAdapter);
    mRefreshLayout.setOnRefreshListener(this);
    mRefreshLayout.post(new Runnable() {
      @Override
      public void run() {
        onRefresh();
      }
    });
  }

  private void queryData() {
    QuChuHistoryPresenter.getHistory(this, mPlaceIds, pageNo, mCommonListener);
  }

  @Override
  public void onRefresh() {
    pageNo = 1;
    mRefreshLayout.setRefreshing(true);
    QuChuHistoryPresenter.getHistory(this, mPlaceIds, pageNo, mCommonListener);
  }

  private CommonListener<QuChuHistoryModel> mCommonListener = new CommonListener<QuChuHistoryModel>() {
    @Override
    public void successListener(QuChuHistoryModel response) {
      if (response == null) {
        return;
      }

      mRefreshLayout.setRefreshing(false);

      List<QuChuHistoryModel.BestListBean> bestList = response.getBestList();
      QuChuHistoryModel.PlaceListBean placeListBean = response.getPlaceList();
      List<QuChuHistoryModel.PlaceListBean.ResultBean> resultBeanList = null;
      if (placeListBean != null) {
        resultBeanList = placeListBean.getResult();
      }

      mAdapter.setData(bestList, resultBeanList);
    }

    @Override
    public void errorListener(VolleyError error, String exception, String msg) {

    }
  };

  private AdapterBase.OnLoadmoreListener loadMoreListener = new AdapterBase.OnLoadmoreListener() {
    @Override
    public void onLoadmore() {

    }
  };

  private AdapterBase.OnItemClickListener onItemClickListener = new AdapterBase.OnItemClickListener() {
    @Override
    public void itemClick(RecyclerView.ViewHolder holder, Object item, int type, @Deprecated int position) {

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
