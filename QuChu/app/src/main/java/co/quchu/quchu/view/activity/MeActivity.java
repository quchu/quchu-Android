package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.base.EnhancedToolbar;

/**
 * Created by mwb on 16/10/25.
 */
public class MeActivity extends BaseBehaviorActivity {

  @Bind(R.id.user_mask_layout) RelativeLayout mUserMaskLayout;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_me_new);
    ButterKnife.bind(this);

    EnhancedToolbar toolbar = getEnhancedToolbar();
    TextView textView = toolbar.getTitleTv();
    textView.setText("");

    FragmentManager fm = getSupportFragmentManager();
    FragmentTransaction ft = fm.beginTransaction();
    ft.add(R.id.me_fragment_container, new MeAvatarFragment());
    ft.commit();

    initViews();
  }

  private void initViews() {
    if (AppContext.user != null && !AppContext.user.isIsVisitors()) {
      mUserMaskLayout.setVisibility(View.GONE);
    } else {
      mUserMaskLayout.setVisibility(View.VISIBLE);
    }
  }

  @OnClick({R.id.me_info_view, R.id.me_friend_view, R.id.me_social_account_view, R.id.me_change_password_view, R.id.user_logout_btn, R.id.user_login_btn})
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.me_info_view://个人信息
        makeToast("info");
        break;

      case R.id.me_friend_view://趣友圈
        makeToast("friend");
        break;

      case R.id.me_social_account_view://绑定社交账号
        makeToast("social");
        break;

      case R.id.me_change_password_view://修改密码
        makeToast("change");
        break;

      case R.id.user_logout_btn://退出登录
        logout();
        break;

      case R.id.user_login_btn://登录
        login();
        break;
    }
  }

  private void login() {

  }

  private void logout() {

  }

  @Override
  public ArrayMap<String, Object> getUserBehaviorArguments() {
    return null;
  }

  @Override
  public int getUserBehaviorPageId() {
    return 0;
  }

  @Override
  protected int activitySetup() {
    return TRANSITION_TYPE_LEFT;
  }

  @Override
  protected String getPageNameCN() {
    return null;
  }
}
