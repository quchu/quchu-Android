package co.quchu.quchu.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.model.FeedbackModel;
import co.quchu.quchu.net.NetUtil;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.FeedbackPresenter;
import co.quchu.quchu.utils.SoftInputUtils;
import co.quchu.quchu.view.adapter.FeedbackDetailAdapter;

import static co.quchu.quchu.R.id.inputEditText;

/**
 * 用户反馈详情
 * <p>
 * Created by mwb on 16/8/29.
 */
public class FeedbackDetailActivity extends BaseBehaviorActivity
    implements SwipeRefreshLayout.OnRefreshListener {

  @Bind(R.id.feedback_swipeRefreshLayout) SwipeRefreshLayout refreshLayout;
  @Bind(R.id.feedback_recycler_view) RecyclerView recyclerView;
  @Bind(inputEditText) EditText mInputEditText;
  @Bind(R.id.submitBtn) TextView mSubmitBtn;

  private static String INTENT_KEY_FEEDBACK_MODEL = "intent_key_feedback_model";
  private FeedbackDetailAdapter mAdapter;
  private int mFeedbackId;
  private FeedbackModel mFeedbackModel;

  public static void launch(Activity activity, FeedbackModel feedbackModel) {
    Intent intent = new Intent(activity, FeedbackDetailActivity.class);
    intent.putExtra(INTENT_KEY_FEEDBACK_MODEL, feedbackModel);
    activity.startActivity(intent);
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_feedback_detail);
    ButterKnife.bind(this);

    EnhancedToolbar toolbar = getEnhancedToolbar();
    TextView titleTv = toolbar.getTitleTv();
    titleTv.setText("意见和帮助");

    mFeedbackModel = (FeedbackModel) getIntent().getSerializableExtra(INTENT_KEY_FEEDBACK_MODEL);
    if (mFeedbackModel != null) {
      mFeedbackId = mFeedbackModel.getFeedbackId();

      getFeedbackDetail(mFeedbackModel.getFeedbackId());
    }

    refreshLayout.setOnRefreshListener(this);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    mAdapter = new FeedbackDetailAdapter(this);
    recyclerView.setAdapter(mAdapter);

    mInputEditText.addTextChangedListener(textChangedListener);
  }

  private TextWatcher textChangedListener = new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
      if (s.toString().trim().length() > 0) {
        mSubmitBtn.setEnabled(true);
      } else {
        mSubmitBtn.setEnabled(false);
      }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
  };

  /**
   * 获取反馈详情
   *
   * @param feedbackId
   */
  private void getFeedbackDetail(int feedbackId) {
    FeedbackPresenter
        .getFeedMsgList(this, String.valueOf(feedbackId), new CommonListener<FeedbackModel>() {
          @Override
          public void successListener(FeedbackModel response) {
            if (response != null) {

              List<FeedbackModel.MsgListBean> msgList = response.getMsgList();

              if (mFeedbackModel != null) {
                FeedbackModel.MsgListBean msgListBean = new FeedbackModel.MsgListBean();
                msgListBean.setContent(mFeedbackModel.getValue());
                msgListBean.setCreateDate(mFeedbackModel.getCreateDate());
                msgListBean.setType("0");
                msgList.add(msgListBean);
              }

              String userName;
              if (AppContext.user != null && !AppContext.user.isIsVisitors()) {
                userName = AppContext.user.getFullname();
              } else {
                userName = "未知生物";
              }

              for (int i = 0; i < msgList.size(); i++) {
                FeedbackModel.MsgListBean bean = msgList.get(i);

                if (bean.getType().equals("0")) {
                  bean.setUserName(userName);

                } else {
                  bean.setUserName("Felix");
                }

                if (i > 0) {
                  FeedbackModel.MsgListBean lastBean = msgList.get(i - 1);
                  if (lastBean.getType().equals(bean.getType())) {
                    if (lastBean.getCreateDate().equals(bean.getCreateDate())) {
                      bean.setHideUserInfo(true);
                    } else {
                      bean.setHideUserInfo(false);
                    }
                  } else {
                    bean.setHideUserInfo(false);
                  }
                }
              }

              mAdapter.initData(msgList);
              mAdapter.setLoadMoreEnable(false);
              mAdapter.setAvatar(response.getIphone(), response.getYphone());
              refreshLayout.setRefreshing(false);
            }
          }

          @Override
          public void errorListener(VolleyError error, String exception, String msg) {
            makeToast(R.string.network_error);
            mAdapter.setLoadMoreEnable(false);
            refreshLayout.setRefreshing(false);
          }
        });
  }

  /**
   * 下拉刷新
   */
  @Override
  public void onRefresh() {
    getFeedbackDetail(mFeedbackId);
  }

  @OnClick(R.id.submitBtn)
  public void onClick() {
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

    //提交反馈消息
    FeedbackPresenter
        .sendFeedMsg(this, String.valueOf(mFeedbackId), inputStr, new CommonListener() {
          @Override
          public void successListener(Object response) {
            makeToast("感谢您对我们的支持");
            mInputEditText.setText("");
            SoftInputUtils.hideSoftInput(FeedbackDetailActivity.this);
            getFeedbackDetail(mFeedbackId);
          }

          @Override
          public void errorListener(VolleyError error, String exception, String msg) {
            makeToast(R.string.network_error);
          }
        });
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
