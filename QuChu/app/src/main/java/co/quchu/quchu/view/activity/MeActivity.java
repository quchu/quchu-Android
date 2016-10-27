package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.presenter.UserLoginPresenter;
import co.quchu.quchu.utils.AppKey;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.utils.SPUtils;

/**
 * Created by mwb on 16/10/25.
 */
public class MeActivity extends BaseBehaviorActivity {

  @Bind(R.id.user_mask_layout) RelativeLayout mUserMaskLayout;
  @Bind(R.id.user_operate_layout) LinearLayout mUserOperateLayout;

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
      mUserOperateLayout.setVisibility(View.VISIBLE);
    } else {
      mUserMaskLayout.setVisibility(View.VISIBLE);
      mUserOperateLayout.setVisibility(View.INVISIBLE);
    }
  }

  @OnClick({R.id.me_info_view, R.id.me_friend_view, R.id.me_social_account_view, R.id.me_change_password_view, R.id.user_logout_btn, R.id.user_login_btn})
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.me_info_view://个人信息
        UMEvent("profile_c");
        startActivity(AccountSettingActivity.class);
        break;

      case R.id.me_friend_view://趣友圈
        UMEvent("community_c");
        startActivity(QuFriendsActivity.class);
        break;

      case R.id.me_social_account_view://绑定社交账号
        startActivity(BindActivity.class);
        break;

      case R.id.me_change_password_view://修改密码
        if (AppContext.user == null) {
          return;
        }

        if (!AppContext.user.isphone()) {
          return;
        }

        makeToast("change");
        break;

      case R.id.user_logout_btn://退出登录
        logout();
        break;

      case R.id.user_login_btn://登录
        startActivity(LoginActivity.class);
        break;
    }
  }

  /**
   * 退出登录
   */
  private void logout() {
    final MaterialDialog confirmDialog = new MaterialDialog.Builder(this)
        .title("确认退出")
        .content("退出后将以游客模式登录")
        .positiveText("是")
        .negativeText("否")
        .cancelable(false).build();

    confirmDialog.getBuilder().onPositive(new MaterialDialog.SingleButtonCallback() {
      @Override
      public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
        SPUtils.clearUserinfo(AppContext.mContext);
        AppContext.user = null;
        SPUtils.clearSpMap(MeActivity.this, AppKey.LOGIN_TYPE);

        //退出当前账号登录的融云信息
//        new IMPresenter().logout();

        UserLoginPresenter.visitorRegiest(MeActivity.this, new UserLoginPresenter.UserNameUniqueListener() {
          @Override
          public void isUnique(JSONObject msg) {
            ArrayMap<String, Object> params = new ArrayMap<>();
            params.put("用户名", AppContext.user.getFullname());
            params.put("登陆方式", "游客模式");
            ZGEvent(params, "用户登陆");

            //重新获取融云token连接服务器
//            new IMPresenter().getToken(MeActivity.this, null);

            confirmDialog.dismiss();
            EventBus.getDefault().post(new QuchuEventModel(EventFlags.EVENT_USER_LOGOUT));
            finish();
          }

          @Override
          public void notUnique(String msg) {

          }
        });
      }
    });

    confirmDialog.show();
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
