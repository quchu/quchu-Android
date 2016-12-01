package co.quchu.quchu.refactor.rxjava;

import android.util.Log;

import co.quchu.quchu.refactor.exception.ResponseException;
import rx.Subscriber;

/**
 * Created by mwb on 16/9/25.
 */
public abstract class BaseSubscriber<T> extends Subscriber<T> {

  private String TAG = "BaseSubscriber";

  public abstract void onSuccess(T data);

  public abstract void onFailure(String msg, String exception);

  public abstract void onFinish();

  @Override
  public void onStart() {
    super.onStart();
    Log.d(TAG, "onStart()");
  }

  @Override
  public void onCompleted() {
    //请求完成
    Log.d(TAG, "onCompleted()");
    onFinish();
  }

  @Override
  public void onError(Throwable e) {
    //统一处理请求失败
    if (e instanceof ResponseException) {
      ResponseException throwable = (ResponseException) e;
      Log.d(TAG, "onError() msg : " + throwable.getMessage());
      onFailure(throwable.getMessage(), throwable.getException());
    } else {
      Log.d(TAG, "onError() msg : " + "未知异常");
      onFailure("未知异常", "未知异常");
    }

    onFinish();
  }

  @Override
  public void onNext(T t) {
    //请求成功
    Log.d(TAG, "onNext()");
    onSuccess(t);
  }
}
