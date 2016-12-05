package co.quchu.quchu.refactor.mvp;

/**
 * Created by mwb on 2016/11/29.
 */
public interface BaseView {

  void showLoading(String msg, boolean isCancelable);

  void hideLoading();

  void onFailure(String msg, String exception);
}
