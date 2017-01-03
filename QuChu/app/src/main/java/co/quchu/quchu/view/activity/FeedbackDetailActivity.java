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
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.model.FeedbackModel;
import co.quchu.quchu.net.NetUtil;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.FeedbackPresenter;
import co.quchu.quchu.utils.SoftInputUtils;
import co.quchu.quchu.view.adapter.FeedbackDetailAdapter;
import co.quchu.quchu.widget.InputView;

/**
 * 用户反馈详情
 * <p>
 * Created by mwb on 16/8/29.
 */
public class FeedbackDetailActivity extends BaseBehaviorActivity
    implements SwipeRefreshLayout.OnRefreshListener, View.OnTouchListener {

  @Bind(R.id.feedback_swipeRefreshLayout) SwipeRefreshLayout refreshLayout;
  @Bind(R.id.feedback_recycler_view) RecyclerView recyclerView;
  @Bind(R.id.input_view) InputView mInputView;

  private static String INTENT_KEY_FEEDBACK_MODEL = "intent_key_feedback_model";
  private FeedbackDetailAdapter mAdapter;
  private int mFeedbackId;
  private FeedbackModel mFeedbackModel;
  private boolean mIsSubmitting;

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
    toolbar.getRightIv().setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });

    mFeedbackModel = (FeedbackModel) getIntent().getSerializableExtra(INTENT_KEY_FEEDBACK_MODEL);
    if (mFeedbackModel != null) {
      mFeedbackId = mFeedbackModel.getFeedbackId();

      getFeedbackDetail(mFeedbackModel.getFeedbackId());
    }

    refreshLayout.setOnRefreshListener(this);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    mAdapter = new FeedbackDetailAdapter(this);
    recyclerView.setAdapter(mAdapter);
    recyclerView.setOnTouchListener(this);

    mInputView.setOnInputViewClickListener(new InputView.OnInputViewClickListener() {
      @Override
      public void onClick(String inputStr) {
        submitFeedbackClick(inputStr);
      }
    });
  }

  @Override
  public void onBackPressed() {
    SoftInputUtils.hideSoftInput(this);
    super.onBackPressed();
  }

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

              Collections.reverse(msgList);

              if (mFeedbackModel != null) {
                FeedbackModel.MsgListBean msgListBean = new FeedbackModel.MsgListBean();
                msgListBean.setContent(mFeedbackModel.getValue());
                msgListBean.setCreateDate(mFeedbackModel.getCreateDate());
                msgListBean.setType("0");
                msgList.add(0, msgListBean);
              }

//              String userName;
//              if (AppContext.user != null && !AppContext.user.isIsVisitors()) {
//                userName = AppContext.user.getFullname();
//              } else {
//                userName = "未知生物";
//              }

              for (int i = 0; i < msgList.size(); i++) {
                FeedbackModel.MsgListBean bean = msgList.get(i);

                if (bean.getType().equals("0")) {
                  bean.setUserName("你");

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

  private void submitFeedbackClick(String inputStr) {
    if (TextUtils.isEmpty(inputStr)) {
      makeToast("请输入您的宝贵意见");
      return;
    }

    if (!NetUtil.isNetworkConnected(this)) {
      makeToast(R.string.network_error);
      return;
    }

    if (mIsSubmitting) {
      return;
    }
    mIsSubmitting = true;

    //提交反馈消息
    FeedbackPresenter
        .sendFeedMsg(this, String.valueOf(mFeedbackId), inputStr, new CommonListener() {
          @Override
          public void successListener(Object response) {
            mInputView.init();
            mIsSubmitting = false;
            makeToast("感谢您对我们的支持");
            getFeedbackDetail(mFeedbackId);
          }

          @Override
          public void errorListener(VolleyError error, String exception, String msg) {
            mIsSubmitting = false;
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
  protected String getPageNameCN() {
    return null;
  }

  @Override
  public boolean onTouch(View v, MotionEvent event) {
    mInputView.hideSoftInput();
    return false;
  }
}
