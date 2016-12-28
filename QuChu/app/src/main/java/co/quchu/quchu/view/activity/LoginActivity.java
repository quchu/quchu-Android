package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.ArrayMap;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.umeng.socialize.UMShareAPI;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.utils.SoftInputUtils;
import co.quchu.quchu.view.fragment.LoginFragment;
import co.quchu.quchu.widget.RitalinLayout;

public class LoginActivity extends BaseBehaviorActivity {

  @Override
  public ArrayMap<String, Object> getUserBehaviorArguments() {
    return null;
  }

  @Override
  public int getUserBehaviorPageId() {
    return 129;
  }

  @Override
  protected String getPageNameCN() {
    return getString(R.string.pname_login);
  }

  @Bind(R.id.flContent) RitalinLayout flContent;

  LoginFragment loginFragment;
  android.app.FragmentManager fragmentManager;
  public SsoHandler handler;
  public long mRequestVerifyCode = -1;

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    if (handler != null) handler.authorizeCallBack(requestCode, resultCode, data);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    ButterKnife.bind(this);

    getEnhancedToolbar().show();
    getEnhancedToolbar().setBackgroundColorRes(R.color.standard_color_white);

    loginFragment = new LoginFragment();
    fragmentManager = getFragmentManager();
    fragmentManager.beginTransaction()
        .add(R.id.flContent, loginFragment, LoginFragment.TAG)
        .commit();
    getFragmentManager().executePendingTransactions();
  }

  @Override
  protected void onRestart() {
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        SoftInputUtils.hideSoftInput(LoginActivity.this);
      }
    }, 200);
    super.onRestart();
  }

  @Override
  public void onBackPressed() {
    if (getFragmentManager().getBackStackEntryCount() > 0) {
      getFragmentManager().popBackStack();
    } else {
      super.onBackPressed();
    }
  }

  /**
   * 隐藏键盘
   */
  public void hideSoftware() {
    InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
      if (getCurrentFocus() != null)
        manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
    //InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    //manager
    //    .hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
  }

  @Override
  protected int activitySetup() {
    return TRANSITION_TYPE_LEFT;
  }
}
