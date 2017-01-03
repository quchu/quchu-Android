package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.VolleyError;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.dialog.FeedbackSuccessDialog;
import co.quchu.quchu.model.FeedbackModel;
import co.quchu.quchu.net.NetUtil;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.FeedbackPresenter;
import co.quchu.quchu.view.adapter.FeedbackAdapter;
import co.quchu.quchu.widget.InputView;

/**
 * FeedbackActivity
 * User: Chenhs
 * Date: 2015-12-04
 * 快速反馈
 */
public class FeedbackActivity extends BaseBehaviorActivity {

  @Bind(R.id.feedback_recycler_view) RecyclerView recyclerView;
  @Bind(R.id.feedback_swipeRefreshLayout) SwipeRefreshLayout refreshLayout;
  @Bind(R.id.background_layout) RelativeLayout mBackgroundLayout;
  @Bind(R.id.input_view) InputView mInputView;

  private FeedbackAdapter adapter;
  private boolean mIsSubmitting;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_feedback);
    ButterKnife.bind(this);
    EnhancedToolbar toolbar = getEnhancedToolbar();
    TextView textView = toolbar.getTitleTv();
    textView.setText("意见帮助");

    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    adapter = new FeedbackAdapter(this);
    recyclerView.setAdapter(adapter);
    recyclerView.setOnTouchListener(onTouchListener);
    adapter.setOnFeedbackItemClickListener(onItemClickListener);
    refreshLayout.setOnRefreshListener(onRefreshListener);

    getFeedbackList();

    mInputView.setOnInputViewClickListener(new InputView.OnInputViewClickListener() {
      @Override
      public void onClick(String inputStr) {
        submitFeedbackClick(inputStr);
      }
    });
  }

  /**
   * 获取反馈列表
   */
  private void getFeedbackList() {
    FeedbackPresenter.getFeedbackList(this, new CommonListener<List<FeedbackModel>>() {
      @Override
      public void successListener(List<FeedbackModel> response) {
        if (response != null && response.size() > 0) {
          mBackgroundLayout.setVisibility(View.VISIBLE);
        } else {
          mBackgroundLayout.setVisibility(View.GONE);
        }
        adapter.initData(response);
        adapter.setLoadMoreEnable(false);
        refreshLayout.setRefreshing(false);
      }

      @Override
      public void errorListener(VolleyError error, String exception, String msg) {
        makeToast(R.string.network_error);
        adapter.setLoadMoreEnable(false);
        refreshLayout.setRefreshing(false);
        mBackgroundLayout.setVisibility(View.GONE);
      }
    });
  }

  /**
   * 聊天
   */
  private FeedbackAdapter.OnFeedbackItemClickListener onItemClickListener =
      new FeedbackAdapter.OnFeedbackItemClickListener() {
        @Override
        public void onItemClick(FeedbackModel feedbackModel, TextView textView) {
          textView.setVisibility(View.INVISIBLE);
          FeedbackDetailActivity.launch(FeedbackActivity.this, feedbackModel);
        }

        @Override
        public void onItemLongClick(final FeedbackModel feedbackModel) {
          MaterialDialog confirmDialog = null;
          if (feedbackModel.getState().equals("2")) {
            confirmDialog = new MaterialDialog.Builder(FeedbackActivity.this)
                .content("当前意见有回复,删除后无法查看回复,确定要删除吗?")
                .positiveText("是")
                .negativeText("否")
                .cancelable(false).build();

          } else {
            confirmDialog = new MaterialDialog.Builder(FeedbackActivity.this)
                .content("确定删除吗?")
                .positiveText("是")
                .negativeText("否")
                .cancelable(false).build();
          }

          //删除反馈
          confirmDialog.getBuilder().onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
              if (!NetUtil.isNetworkConnected(FeedbackActivity.this)) {
                makeToast(R.string.network_error);
                return;
              }

              FeedbackPresenter.deleteFeedback(FeedbackActivity.this, String.valueOf(feedbackModel.getFeedbackId()), new CommonListener() {
                @Override
                public void successListener(Object response) {
                  List<FeedbackModel> data = adapter.getData();
                  if (data.contains(feedbackModel)) {
                    data.remove(feedbackModel);
                  }

                  if (data.size() > 0) {
                    mBackgroundLayout.setVisibility(View.VISIBLE);
                  } else {
                    mBackgroundLayout.setVisibility(View.GONE);
                  }

                  adapter.initData(data);
                  adapter.setLoadMoreEnable(false);
                }

                @Override
                public void errorListener(VolleyError error, String exception, String msg) {

                }
              });
            }
          });
          confirmDialog.show();
        }
      };

  private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
    @Override
    public boolean onTouch(View v, MotionEvent event) {
      mInputView.hideSoftInput();
      return false;
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

    FeedbackPresenter.sendFeedback(this, "", inputStr, new CommonListener() {
      @Override
      public void successListener(Object response) {
        mInputView.init();

        mIsSubmitting = false;
//        makeToast("感谢您对我们的支持");
        FeedbackSuccessDialog successDialog = new FeedbackSuccessDialog(FeedbackActivity.this);
        successDialog.show();

        getFeedbackList();
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
    return 127;
  }

  @Override
  protected String getPageNameCN() {
    return "意见和帮助";
  }
}
