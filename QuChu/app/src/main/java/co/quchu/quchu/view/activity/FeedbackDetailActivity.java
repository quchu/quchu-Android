package co.quchu.quchu.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.model.FeedbackModel;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.FeedbackPresenter;
import co.quchu.quchu.view.adapter.FeedbackDetailAdapter;

/**
 * 用户反馈详情
 *
 * Created by mwb on 16/8/29.
 */
public class FeedbackDetailActivity extends BaseBehaviorActivity
    implements SwipeRefreshLayout.OnRefreshListener {

  @Bind(R.id.feedback_content_tv) TextView contentTv;
  @Bind(R.id.feedback_swipeRefreshLayout) SwipeRefreshLayout refreshLayout;
  @Bind(R.id.feedback_recycler_view) RecyclerView recyclerView;
  @Bind(R.id.feedback_detail_et) EditText inputEditText;
  @Bind(R.id.feedback_detail_submit_btn) Button submitBtn;

  private static String INTENT_KEY_FEEDBACK_MODEL = "intent_key_feedback_model";
  private FeedbackDetailAdapter mAdapter;
  private int mFeedbackId;

  public static void launch(Activity activity, FeedbackModel feedbackModel) {
    Intent intent = new Intent(activity, FeedbackDetailActivity.class);
    intent.putExtra(INTENT_KEY_FEEDBACK_MODEL, feedbackModel);
    activity.startActivity(intent);
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_feedback_detail);
    ButterKnife.bind(this);

    EnhancedToolbar toolbar = getEnhancedToolbar();
    TextView titleTv = toolbar.getTitleTv();

    FeedbackModel feedbackModel =
        (FeedbackModel) getIntent().getSerializableExtra(INTENT_KEY_FEEDBACK_MODEL);
    if (feedbackModel != null) {
      titleTv.setText(feedbackModel.getTitle());
      contentTv.setText(feedbackModel.getValue());

      mFeedbackId = feedbackModel.getFeedbackId();

      getFeedbackDetail(mFeedbackId);
    } else {
      titleTv.setText("Felix");
    }

    refreshLayout.setOnRefreshListener(this);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    mAdapter = new FeedbackDetailAdapter(this);
    recyclerView.setAdapter(mAdapter);
  }

  /**
   * 获取反馈详情
   *
   * @param feedbackId
   */
  private void getFeedbackDetail(int feedbackId) {
    FeedbackPresenter
        .getFeedMsgList(this, String.valueOf(feedbackId), new CommonListener<FeedbackModel>() {
          @Override public void successListener(FeedbackModel response) {
            if (response != null) {
              mAdapter.initData(response.getMsgList());
              refreshLayout.setRefreshing(false);
            }
          }

          @Override public void errorListener(VolleyError error, String exception, String msg) {
            makeToast(R.string.network_error);
            refreshLayout.setRefreshing(false);
          }
        });
  }

  /**
   * 下拉刷新
   */
  @Override public void onRefresh() {
    getFeedbackDetail(mFeedbackId);
  }

  @OnClick(R.id.feedback_detail_submit_btn) public void onClick() {
    String inputStr = inputEditText.getText().toString().trim();
    if (TextUtils.isEmpty(inputStr)) {
      makeToast("请输入反馈内容");
      return;
    }

    //提交反馈消息
    FeedbackPresenter
        .sendFeedMsg(this, String.valueOf(mFeedbackId), inputStr, new CommonListener() {
          @Override public void successListener(Object response) {
            getFeedbackDetail(mFeedbackId);
            inputEditText.setText("");
            hideSoftware(inputEditText);
          }

          @Override public void errorListener(VolleyError error, String exception, String msg) {

          }
        });
  }

  /**
   * 隐藏键盘
   */
  private void hideSoftware(EditText editText) {
    InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    manager
        .hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
  }

  @Override public ArrayMap<String, Object> getUserBehaviorArguments() {
    return null;
  }

  @Override public int getUserBehaviorPageId() {
    return 0;
  }

  @Override protected int activitySetup() {
    return TRANSITION_TYPE_LEFT;
  }

  @Override protected String getPageNameCN() {
    return null;
  }
}
