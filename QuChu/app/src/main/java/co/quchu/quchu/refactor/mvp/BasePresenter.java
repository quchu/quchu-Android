package co.quchu.quchu.refactor.mvp;

import co.quchu.quchu.refactor.QuChuApiService;
import co.quchu.quchu.refactor.retrofit.AppClient;
import co.quchu.quchu.refactor.rxjava.BaseSubscriber;
import rx.Observable;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by mwb on 2016/11/29.
 */
public abstract class BasePresenter<V extends BaseView> {

  protected V mMvpView;
  private AppClient mAppClient;
  protected QuChuApiService mService;
  private CompositeSubscription mCompositeSubscription;

  protected void attachView(V mvpView) {
    mMvpView = mvpView;
    mAppClient = AppClient.getInstance();
    mService = mAppClient.getService();
  }

  protected void detachView() {
    mMvpView = null;
    unSubscribe();
  }

  protected void execute(Observable observable, BaseSubscriber subscriber) {
    if (mCompositeSubscription == null) {
      mCompositeSubscription = new CompositeSubscription();
    }

    //保存 Subscription 在 Activity 销毁时取消订阅,防止 Memory Leak
    mCompositeSubscription.add(mAppClient.toSubscribe(observable, subscriber));
  }

  /**
   * 注意:一旦你调用了 CompositeSubscription.unsubscribe()，
   * 这个CompositeSubscription对象就不可用了,
   * 如果你还想使用CompositeSubscription，就必须在创建一个新的对象了。
   */
  protected void unSubscribe() {
    if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
      mCompositeSubscription.unsubscribe();
    }
  }
}
