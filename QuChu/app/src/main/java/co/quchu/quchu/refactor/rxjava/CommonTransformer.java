package co.quchu.quchu.refactor.rxjava;

import co.quchu.quchu.refactor.entity.BaseResponse;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mwb on 2016/12/5.
 */
public class CommonTransformer<T> implements Observable.Transformer<BaseResponse<T>, T> {

  @Override
  public Observable<T> call(Observable<BaseResponse<T>> observable) {
    return observable.subscribeOn(Schedulers.io())
        .unsubscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .compose(ErrorTransformer.<T>getInstance());
  }
}
