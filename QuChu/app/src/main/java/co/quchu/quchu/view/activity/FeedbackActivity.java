package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.dialog.CommonDialog;
import co.quchu.quchu.dialog.FeedbackDialog;
import co.quchu.quchu.model.FeedbackModel;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.FeedbackPresenter;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.view.adapter.FeedbackAdapter;

/**
 * FeedbackActivity
 * User: Chenhs
 * Date: 2015-12-04
 * 快速反馈
 */
public class FeedbackActivity extends BaseBehaviorActivity {

  private FeedbackAdapter adapter;
  private FeedbackDialog feedbackDialog;

  @Bind(R.id.feedback_recycler_view) RecyclerView recyclerView;
  @Bind(R.id.feedback_swipeRefreshLayout) SwipeRefreshLayout refreshLayout;
  @Bind(R.id.submit_feedback_btn) ImageView submitFeedbackBtn;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_feedback);
    ButterKnife.bind(this);
    EnhancedToolbar toolbar = getEnhancedToolbar();
    TextView textView = toolbar.getTitleTv();
    textView.setText("Felix");

    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    adapter = new FeedbackAdapter(this);
    recyclerView.setAdapter(adapter);
    adapter.setOnFeedbackItemClickListener(onItemClickListener);
    refreshLayout.setOnRefreshListener(onRefreshListener);

    getFeedbackList();
  }

  /**
   * 获取反馈列表
   */
  private void getFeedbackList() {
    FeedbackPresenter.getFeedbackList(this, new CommonListener<List<FeedbackModel>>() {
      @Override public void successListener(List<FeedbackModel> response) {
        adapter.initData(response);
        refreshLayout.setRefreshing(false);
      }

      @Override public void errorListener(VolleyError error, String exception, String msg) {
        makeToast(R.string.network_error);
        refreshLayout.setRefreshing(false);
      }
    });
  }

  /**
   * 聊天
   */
  private FeedbackAdapter.OnFeedbackItemClickListener onItemClickListener =
      new FeedbackAdapter.OnFeedbackItemClickListener() {
        @Override public void onItemClick(FeedbackModel feedbackModel) {
          FeedbackDetailActivity.launch(FeedbackActivity.this, feedbackModel);
        }
      };

  /**
   * 下拉刷新
   */
  private SwipeRefreshLayout.OnRefreshListener onRefreshListener =
      new SwipeRefreshLayout.OnRefreshListener() {
        @Override public void onRefresh() {
          getFeedbackList();
        }
      };

  /**
   * 提交反馈
   */
  private void submitFeedbackClick() {
    feedbackDialog = new FeedbackDialog(this, new FeedbackDialog.DialogConfirmListener() {
      @Override public void confirm(String title, String content) {
        FeedbackPresenter.sendFeedback(FeedbackActivity.this, title, content, new CommonListener() {
          @Override public void successListener(Object response) {
            SPUtils.setFeedback("", "");
            feedbackDialog.dismiss();

            CommonDialog commonDialog = CommonDialog.newInstance("提交成功", "感谢您对我们的支持", true);
            commonDialog.show(getSupportFragmentManager(), "");

            getFeedbackList();
          }

          @Override public void errorListener(VolleyError error, String exception, String msg) {
            makeToast(R.string.network_error);
          }
        });
      }
    });
    feedbackDialog.show();
  }

  @Override public ArrayMap<String, Object> getUserBehaviorArguments() {
    return null;
  }

  @Override public int getUserBehaviorPageId() {
    return 127;
  }

  @Override protected String getPageNameCN() {
    return "Felix";
  }

  @Override protected int activitySetup() {
    return TRANSITION_TYPE_LEFT;
  }

  @OnClick(R.id.submit_feedback_btn) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.submit_feedback_btn:
        submitFeedbackClick();
        break;
    }
  }
}
