package co.quchu.quchu.refactor.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;

import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.dialog.DialogUtil;
import rx.Observable;
import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by mwb on 2016/11/29.
 */
public abstract class MvpActivity<P extends BasePresenter> extends BaseBehaviorActivity {

  protected P mMvpPresenter;
  private CompositeSubscription mCompositeSubscription;

  protected abstract P createPresenter();

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    mMvpPresenter = createPresenter();

    super.onCreate(savedInstanceState);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (mMvpPresenter != null) {
      mMvpPresenter.detachView();
    }
  }

  /**
   * 保存 Subscription 在 Activity 销毁时取消订阅,防止 Memory Leak
   *
   * @param observable 观察者
   * @param subscriber 订阅
   */
  protected void addSubscription(Observable observable, Subscriber subscriber) {
    if (mCompositeSubscription == null) {
      mCompositeSubscription = new CompositeSubscription();
    }

    mCompositeSubscription.add(observable.subscribe(subscriber));
  }

  protected void unSubscribe() {
    if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
      mCompositeSubscription.unsubscribe();
    }
  }

  public void showLoading(String msg, boolean isCancelable) {
    DialogUtil.showProgess(this, msg, isCancelable);
  }

  public void hideLoading() {
    if (DialogUtil.isDialogShowing()) {
      DialogUtil.dismissProgess();
    }
  }
}
