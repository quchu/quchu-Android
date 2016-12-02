package co.quchu.quchu.refactor.mvp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

import co.quchu.quchu.dialog.DialogUtil;

/**
 * Created by mwb on 2016/11/30.
 */
public abstract class MvpFragment<P extends BasePresenter> extends Fragment {

  protected P mMvpPresenter;

  protected abstract P createPresenter();

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    mMvpPresenter = createPresenter();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    if (mMvpPresenter != null) {
      mMvpPresenter.detachView();
    }
  }

  public void showLoading(String msg, boolean isCancelable) {
    DialogUtil.showProgess(getActivity(), msg, isCancelable);
  }

  public void hideLoading() {
    if (DialogUtil.isDialogShowing()) {
      DialogUtil.dismissProgess();
    }
  }
}
