package co.quchu.quchu.refactor;

import co.quchu.quchu.refactor.mvp.BaseView;

/**
 * Created by mwb on 2016/11/30.
 */
public interface QuChuView<T> extends BaseView {

  void onSuccess(T data);

  void onFailure(String msg, String exception);
}
