package co.quchu.quchu.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.model.XiaoQModel;
import co.quchu.quchu.presenter.PageLoadListener;
import co.quchu.quchu.presenter.XiaoQPresenter;
import co.quchu.quchu.view.adapter.AdapterBase;
import co.quchu.quchu.view.adapter.XiaoQAdapter;

/**
 * Created by mwb on 16/9/19.
 */
public class XiaoQActivity extends BaseBehaviorActivity implements PageLoadListener<List<XiaoQModel>>,
    AdapterBase.OnLoadmoreListener, SwipeRefreshLayout.OnRefreshListener {

  private int pageNo = 1;

  @Bind(R.id.recyclerView) RecyclerView mRecyclerView;
  @Bind(R.id.swipeRefreshLayout) SwipeRefreshLayout mSwipeRefreshLayout;

  private XiaoQAdapter mAdapter;
  private XiaoQPresenter mPresenter;

  public static void launch(Activity activity) {
    Intent intent = new Intent(activity, XiaoQActivity.class);
    activity.startActivity(intent);
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_xiaoq);
    ButterKnife.bind(this);

    initTitle();

    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    mAdapter = new XiaoQAdapter(this);
    mRecyclerView.setAdapter(mAdapter);
    mAdapter.setLoadmoreListener(this);

    mSwipeRefreshLayout.setOnRefreshListener(this);

    mPresenter = new XiaoQPresenter(this);
    mPresenter.getMessage(pageNo, this);
  }

  private void initTitle() {
    EnhancedToolbar toolbar = getEnhancedToolbar();
    toolbar.getTitleTv().setText("小Q");
    ImageView rightImage = toolbar.getRightIv();
    rightImage.setImageResource(R.mipmap.ic_shezhi);
    rightImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        SettingXioaQActivity.launch(XiaoQActivity.this);
      }
    });
  }

  @Override
  public void initData(List<XiaoQModel> data) {
    mAdapter.initData(data);
    mSwipeRefreshLayout.setRefreshing(false);
  }

  @Override
  public void moreData(List<XiaoQModel> data) {
    mSwipeRefreshLayout.setRefreshing(false);

//    pageNo = data.getPagesNo();
//    mAdapter.addMoreData(data);
  }

  @Override
  public void nullData() {
    if (mSwipeRefreshLayout != null) {
      mSwipeRefreshLayout.setRefreshing(false);
    }
    mAdapter.setLoadMoreEnable(false);
  }

  @Override
  public void netError(final int pageNo, String massage) {
    makeToast(R.string.network_error);
    mSwipeRefreshLayout.setRefreshing(false);

    mAdapter.setNetError(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mSwipeRefreshLayout.setRefreshing(false);
        mPresenter.getMessage(pageNo, XiaoQActivity.this);
      }
    });
  }

  @Override
  public void onLoadmore() {
    mPresenter.getMessage(pageNo + 1, this);
  }

  @Override
  public void onRefresh() {
    pageNo = 1;
    mPresenter.getMessage(pageNo, this);
  }

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
