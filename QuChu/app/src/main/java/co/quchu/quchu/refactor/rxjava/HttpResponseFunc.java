package co.quchu.quchu.refactor.rxjava;

import co.quchu.quchu.refactor.exception.ExceptionHandler;
import rx.Observable;
import rx.functions.Func1;

/**
 * 拦截服务器响应异常
 * <p>
 * Created by mwb on 16/9/25.
 */
public class HttpResponseFunc<T> implements Func1<Throwable, Observable<T>> {

  @Override
  public Observable<T> call(Throwable throwable) {
    return Observable.error(ExceptionHandler.handle(throwable));
  }
}
