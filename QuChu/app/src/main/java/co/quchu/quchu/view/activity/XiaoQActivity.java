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

import com.android.volley.VolleyError;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.model.SysMessage;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.XiaoQPresenter;
import co.quchu.quchu.view.adapter.XiaoQAdapter;

/**
 * Created by mwb on 16/9/19.
 */
public class XiaoQActivity extends BaseBehaviorActivity implements CommonListener<List<SysMessage>>,
    SwipeRefreshLayout.OnRefreshListener, XiaoQAdapter.OnMessageItemClickListener {

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
    mAdapter.setOnMessageItemClickListener(this);

    mSwipeRefreshLayout.setOnRefreshListener(this);

    mPresenter = new XiaoQPresenter(this);
    mPresenter.getMessage(pageNo, this);
  }

  private void initTitle() {
    EnhancedToolbar toolbar = getEnhancedToolbar();
    toolbar.getTitleTv().setText("小Q");
    ImageView rightImage = toolbar.getRightIv();
    rightImage.setImageResource(R.drawable.ic_shezhi);
    rightImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        SettingXioaQActivity.launch(XiaoQActivity.this);
      }
    });
  }

  @Override
  public void onItemClick(SysMessage message) {
    if (message == null) {
      return;
    }

    Intent intent = null;
    switch (message.getType()) {
      case SysMessage.TYPE_QUCHU_DETAIL:
        intent = new Intent(XiaoQActivity.this, QuchuDetailsActivity.class);
        intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_PID, Integer.valueOf(message.getId()));
        break;

      case SysMessage.TYPE_ARTICLE_DETAIL:
        ArticleDetailActivity.enterActivity(XiaoQActivity.this, message.getId(), "文章详情", "小Q聊天界面");
        break;

      case SysMessage.TYPE_USER:
        intent = new Intent(XiaoQActivity.this, UserCenterActivityNew.class);
        intent.putExtra(UserCenterActivityNew.REQUEST_KEY_USER_ID, Integer.valueOf(message.getId()));
        break;
    }

    if (intent != null) {
      startActivity(intent);
    }
  }

  @Override
  public void successListener(List<SysMessage> response) {
    mAdapter.initData(response);
    mSwipeRefreshLayout.setRefreshing(false);
  }

  @Override
  public void errorListener(VolleyError error, String exception, String msg) {
    makeToast(R.string.network_error);
    mSwipeRefreshLayout.setRefreshing(false);
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
