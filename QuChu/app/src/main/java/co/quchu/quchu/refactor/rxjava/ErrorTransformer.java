package co.quchu.quchu.refactor.rxjava;

import co.quchu.quchu.refactor.entity.BaseResponse;
import rx.Observable;

/**
 * Created by mwb on 2016/12/5.
 */
public class ErrorTransformer<T> implements Observable.Transformer<BaseResponse<T>, T> {

  private static ErrorTransformer mErrorTransformer = null;

  @Override
  public Observable<T> call(Observable<BaseResponse<T>> observable) {
    return observable.map(new HttpResultFunc<T>())
        .onErrorResumeNext(new HttpResponseFunc<T>());
  }

  public static <T> ErrorTransformer<T> getInstance() {

    if (mErrorTransformer == null) {
      synchronized (ErrorTransformer.class) {
        if (mErrorTransformer == null) {
          mErrorTransformer = new ErrorTransformer<>();
        }
      }
    }
    return mErrorTransformer;
  }
}
