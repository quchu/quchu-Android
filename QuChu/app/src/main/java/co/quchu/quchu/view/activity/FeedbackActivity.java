package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.model.FeedbackModel;
import co.quchu.quchu.net.NetUtil;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.FeedbackPresenter;
import co.quchu.quchu.view.adapter.FeedbackAdapter;

/**
 * FeedbackActivity
 * User: Chenhs
 * Date: 2015-12-04
 * 快速反馈
 */
public class FeedbackActivity extends BaseBehaviorActivity {

  private FeedbackAdapter adapter;

  @Bind(R.id.feedback_recycler_view) RecyclerView recyclerView;
  @Bind(R.id.feedback_swipeRefreshLayout) SwipeRefreshLayout refreshLayout;
  @Bind(R.id.inputEditText) EditText mInputEditText;
  @Bind(R.id.submitBtn) TextView mSubmitBtn;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_feedback);
    ButterKnife.bind(this);
    EnhancedToolbar toolbar = getEnhancedToolbar();
    TextView textView = toolbar.getTitleTv();
    textView.setText("意见和帮助");

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
      @Override
      public void successListener(List<FeedbackModel> response) {
        adapter.initData(response);
        adapter.setLoadMoreEnable(false);
        refreshLayout.setRefreshing(false);
      }

      @Override
      public void errorListener(VolleyError error, String exception, String msg) {
        makeToast(R.string.network_error);
        adapter.setLoadMoreEnable(false);
        refreshLayout.setRefreshing(false);
      }
    });
  }

  /**
   * 聊天
   */
  private FeedbackAdapter.OnFeedbackItemClickListener onItemClickListener =
      new FeedbackAdapter.OnFeedbackItemClickListener() {
        @Override
        public void onItemClick(FeedbackModel feedbackModel) {
          FeedbackDetailActivity.launch(FeedbackActivity.this, feedbackModel);
        }
      };

  /**
   * 下拉刷新
   */
  private SwipeRefreshLayout.OnRefreshListener onRefreshListener =
      new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
          getFeedbackList();
        }
      };

  /**
   * 提交反馈
   */
  private void submitFeedbackClick() {
    String inputStr = mInputEditText.getText().toString().trim();
    if (TextUtils.isEmpty(inputStr)) {
      makeToast("请输入您的宝贵意见");
      return;
    }

    if (!NetUtil.isNetworkConnected(this)) {
      makeToast(R.string.network_error);
      return;
    }

    inputStr = mInputEditText.getText().toString();

    FeedbackPresenter.sendFeedback(this, "", inputStr, new CommonListener() {
      @Override
      public void successListener(Object response) {
        makeToast("感谢您对我们的支持");
        mInputEditText.setText("");
        hideSoftware(mInputEditText);
        getFeedbackList();
      }

      @Override
      public void errorListener(VolleyError error, String exception, String msg) {
        makeToast(R.string.network_error);
      }
    });
  }

  /**
   * 隐藏键盘
   */
  private void hideSoftware(EditText editText) {
    InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
      if (getCurrentFocus() != null)
        manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
    //InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    //manager
    //    .hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
  }

  @Override
  public ArrayMap<String, Object> getUserBehaviorArguments() {
    return null;
  }

  @Override
  public int getUserBehaviorPageId() {
    return 127;
  }

  @Override
  protected String getPageNameCN() {
    return "意见和帮助";
  }

  @Override
  protected int activitySetup() {
    return TRANSITION_TYPE_LEFT;
  }

  @OnClick(R.id.submitBtn)
  public void onClick() {
    submitFeedbackClick();
  }
}
