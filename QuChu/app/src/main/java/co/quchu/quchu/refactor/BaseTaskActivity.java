package co.quchu.quchu.refactor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.refactor.retrofit.AppClient;
import co.quchu.quchu.refactor.rxjava.BaseSubscriber;
import rx.Observable;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by mwb on 2016/12/5.
 */
public class BaseTaskActivity extends BaseBehaviorActivity {

  private AppClient mAppClient;
  protected QuChuApiService mService;
  private CompositeSubscription mCompositeSubscription;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mAppClient = AppClient.getInstance();
    mService = mAppClient.getService();
  }

  @Override
  protected void onDestroy() {
    unSubscribe();
    super.onDestroy();
  }

  /**
   * 保存 Subscription 在 Activity 销毁时取消订阅,防止 Memory Leak
   */
  protected void executeTask(Observable observable, BaseSubscriber subscriber) {
    if (mCompositeSubscription == null) {
      mCompositeSubscription = new CompositeSubscription();
    }

    mCompositeSubscription.add(mAppClient.toSubscribe(observable, subscriber));
  }

  protected void unSubscribe() {
    if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
      mCompositeSubscription.unsubscribe();
    }
  }

  public void showLoading(String msg, boolean isCancelable) {
    DialogUtil.showProgess(this, msg, isCancelable);
  }

  public void showLoading(String msg) {
    DialogUtil.showProgess(this, msg);
  }

  public void hideLoading() {
    if (DialogUtil.isDialogShowing()) {
      DialogUtil.dismissProgess();
    }
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
}
