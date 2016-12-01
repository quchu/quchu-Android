package co.quchu.quchu.refactor.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import co.quchu.quchu.base.BaseBehaviorActivity;

/**
 * Created by mwb on 2016/11/29.
 */
public abstract class MvpActivity<P extends BasePresenter> extends BaseBehaviorActivity {

  protected P mMvpPresenter;

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

  public void showLoading() {
    Toast.makeText(this, "start", Toast.LENGTH_SHORT).show();
  }

  public void hideLoading() {
    Toast.makeText(this, "end", Toast.LENGTH_SHORT).show();
  }
}
