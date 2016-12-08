package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
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
  @Bind(R.id.background_layout) RelativeLayout mBackgroundLayout;

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
    adapter.setOnFeedbackItemClickListener(onItemClickListener);
    refreshLayout.setOnRefreshListener(onRefreshListener);

    mInputEditText.addTextChangedListener(textChangedListener);

    getFeedbackList();
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

    if (mIsSubmitting) {
      return;
    }
    mIsSubmitting = true;

    inputStr = mInputEditText.getText().toString();

    FeedbackPresenter.sendFeedback(this, "", inputStr, new CommonListener() {
      @Override
      public void successListener(Object response) {
        mIsSubmitting = false;
        makeToast("感谢您对我们的支持");
        mInputEditText.setText("");
        hideSoftware(mInputEditText);
        getFeedbackList();
      }

      @Override
      public void errorListener(VolleyError error, String exception, String msg) {
        mIsSubmitting = false;
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

  @OnClick(R.id.submitBtn)
  public void onClick() {
    submitFeedbackClick();
  }
}
