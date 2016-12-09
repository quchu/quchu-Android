package co.quchu.quchu.view.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.presenter.UserLoginPresenter;
import co.quchu.quchu.utils.AppKey;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.utils.SPUtils;

import static co.quchu.quchu.R.id.userNameTv;
import static co.quchu.quchu.base.AppContext.user;

/**
 * Created by mwb on 16/10/25.
 */
public class MeActivity extends BaseBehaviorActivity {

  @Bind(R.id.enhancedToolbarDivider) View mEnhancedToolbarDivider;
  @Bind(R.id.userAvatarImg) SimpleDraweeView mUserAvatarImg;
  @Bind(R.id.userGenderImg) SimpleDraweeView mUserGenderImg;
  @Bind(userNameTv) TextView mUserNameTv;
  @Bind(R.id.userMarkTv) TextView mUserMarkTv;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_me);
    ButterKnife.bind(this);

    EventBus.getDefault().register(this);

    EnhancedToolbar toolbar = getEnhancedToolbar();
    TextView textView = toolbar.getTitleTv();
    toolbar.setBackground(null);
    textView.setText("");
    mEnhancedToolbarDivider.setVisibility(View.GONE);

    getGenes();

    fillViews();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    EventBus.getDefault().unregister(this);
  }

  private void getGenes() {

  }

  private void fillViews() {
    int geneAvatar = AppContext.user.getGeneAvatar();
    if (geneAvatar != -1) {
      mUserAvatarImg.getHierarchy().setPlaceholderImage(geneAvatar);
    } else {
      mUserAvatarImg.setImageURI(Uri.parse(AppContext.user.getPhoto()));
    }

    mUserGenderImg.setImageURI(Uri.parse("res:///"
        + (AppContext.user.getGender().equals("男") ? R.drawable.ic_male : R.drawable.ic_female)));

    mUserNameTv.setText(user.getFullname());

    mUserMarkTv.setText(SPUtils.getUserMark());
  }

  @OnClick({R.id.me_info_view, R.id.me_social_account_view, R.id.me_change_password_view
      , R.id.user_logout_btn, R.id.user_genes_btn})
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.me_info_view://个人信息
        UMEvent("profile_c");
        startActivity(AccountSettingActivity.class);
        break;

      case R.id.me_social_account_view://绑定社交账号
        startActivity(BindActivity.class);
        break;

      case R.id.me_change_password_view://修改密码
        if (user == null) {
          return;
        }

        if (!user.isphone()) {
          return;
        }

        startActivity(ChangePasswordActivity.class);
        break;

      case R.id.user_logout_btn://退出登录
        logout();
        break;

      case R.id.user_genes_btn://趣基因介绍
        startActivity(UserGenesActivity.class);
        break;
    }
  }

  @Subscribe
  public void onMessageEvent(QuchuEventModel event) {
    switch (event.getFlag()) {
      case EventFlags.EVENT_USER_INFO_UPDATE:
        //用户信息
        fillViews();
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
        DialogUtil.showProgess(MeActivity.this, "正在退出登录", false);

        SPUtils.clearUserinfo(AppContext.mContext);
        user = null;
        SPUtils.clearSpMap(MeActivity.this, AppKey.LOGIN_TYPE);

        //退出当前账号登录的融云信息
//        new IMPresenter().logout();

        UserLoginPresenter.visitorRegiest(MeActivity.this, new UserLoginPresenter.UserNameUniqueListener() {
          @Override
          public void isUnique(JSONObject msg) {
            ArrayMap<String, Object> params = new ArrayMap<>();
            params.put("用户名", user.getFullname());
            params.put("登陆方式", "游客模式");
            ZGEvent(params, "用户登陆");

            //重新获取融云token连接服务器
//            new IMPresenter().getToken(MeActivity.this, null);

            confirmDialog.dismiss();
            DialogUtil.dismissProgess();

            startActivity(RecommendActivity.class);
          }

          @Override
          public void notUnique(String msg) {
            DialogUtil.dismissProgess();
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
