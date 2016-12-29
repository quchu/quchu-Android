package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.dialog.BindPhoneNumDialog;
import co.quchu.quchu.model.UserInfoModel;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.ResponseListener;
import co.quchu.quchu.social.SocialHelper;
import co.quchu.quchu.social.UserLoginListener;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.UserInfoHelper;

public class BindActivity extends BaseBehaviorActivity implements UserLoginListener, View.OnClickListener {

  @Override
  protected String getPageNameCN() {
    return getString(R.string.pname_bind);
  }

  @Override
  public ArrayMap<String, Object> getUserBehaviorArguments() {
    return null;
  }

  @Override
  public int getUserBehaviorPageId() {
    return 125;
  }

  @Bind(R.id.bind_sina) Button bindSina;
  @Bind(R.id.bind_wecha) Button bindWecha;
  @Bind(R.id.bind_phone) Button bindPhone;
  public static final String TYPE_WEIBO = "weibo";
  public static final String TYPE_Wecha = "weixin";

  private BindPhoneNumDialog mDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_bind);
    ButterKnife.bind(this);
    EnhancedToolbar toolbar = getEnhancedToolbar();
    toolbar.getTitleTv().setText("绑定");

    initViews();

    bindSina.setOnClickListener(this);
    bindWecha.setOnClickListener(this);
    bindPhone.setOnClickListener(this);
  }

  private void initViews() {
    if (AppContext.user.isIsweixin()) {
      bindWecha.setText("解除绑定");
      bindWecha.setBackgroundResource(R.drawable.shape_lineframe_gray_fill);
      bindWecha.setTextColor(ContextCompat.getColor(this, R.color.standard_color_h3_dark));

    } else {
      bindWecha.setText("绑定");
      bindWecha.setBackgroundResource(R.drawable.shape_lineframe_christmas_dark_fill);
      bindWecha.setTextColor(ContextCompat.getColor(this, R.color.standard_color_white));
    }

    if (AppContext.user.isIsweibo()) {
      bindSina.setText("解除绑定");
      bindSina.setBackgroundResource(R.drawable.shape_lineframe_gray_fill);
      bindSina.setTextColor(ContextCompat.getColor(this, R.color.standard_color_h3_dark));

    } else {
      bindSina.setText("绑定");
      bindSina.setBackgroundResource(R.drawable.shape_lineframe_christmas_dark_fill);
      bindSina.setTextColor(ContextCompat.getColor(this, R.color.standard_color_white));
    }

    if (AppContext.user.isphone()) {
      bindPhone.setText("解除绑定");
      bindPhone.setBackgroundResource(R.drawable.shape_lineframe_gray_fill);
      bindPhone.setTextColor(ContextCompat.getColor(this, R.color.standard_color_h3_dark));

    } else {
      bindPhone.setText("绑定");
      bindPhone.setBackgroundResource(R.drawable.shape_lineframe_christmas_dark_fill);
      bindPhone.setTextColor(ContextCompat.getColor(this, R.color.standard_color_white));
    }
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.bind_phone:
        if (AppContext.user.isphone()) {
          new MaterialDialog.Builder(this)
              .content("手机号是唯一的身份标示,解除绑定后将无法正常登录.")
              .positiveText("知道了")
              .show();
        } else {
          mDialog = BindPhoneNumDialog.newInstance();
          mDialog.setCancelable(false);
          mDialog.show(getSupportFragmentManager(), "");
        }
        break;

      case R.id.bind_wecha:
        if (AppContext.user.isIsweixin()) {
          if (SPUtils.getLoginType().equals(SPUtils.LOGIN_TYPE_WEIXIN)) {
            Toast.makeText(this, "当前登录账号不能解绑", Toast.LENGTH_SHORT).show();
          } else {
            unBind(true, TYPE_Wecha);
          }
        } else {
          SocialHelper.getPlatformInfo(this, SHARE_MEDIA.WEIXIN, false, this);
        }
        break;

      case R.id.bind_sina:
        if (AppContext.user.isIsweibo()) {
          if (SPUtils.getLoginType().equals(SPUtils.LOGIN_TYPE_WEIBO)) {
            Toast.makeText(this, "当前登录账号不能解绑", Toast.LENGTH_SHORT).show();
          } else {
            unBind(false, TYPE_WEIBO);
          }
        } else {
          SocialHelper.getPlatformInfo(this, SHARE_MEDIA.SINA, false, this);
        }
        break;
    }
  }

  @Override
  protected int activitySetup() {
    return TRANSITION_TYPE_LEFT;
  }

  @Override
  public void loginSuccess(int type, String token, String appId) {
    switch (type) {
      case 2://微信
        bind(true, token, appId);
        break;
      case 3://微bo
        bind(false, token, appId);
        break;
    }
  }

  @Override
  public void loginFail(String message) {

  }

  /**
   * 合并账号
   */
  private void merger(final int type, final String token, final String appId) {

    new MaterialDialog.Builder(this)
        .title("注意")
        .content("该账号已被使用,是否将此账号与当前账号进行合并,合并后不影响使用")
        .positiveText("确定")
        .negativeText("取消")
        .neutralText("什么是账号合并")
        .cancelable(false)
        .onPositive(new MaterialDialog.SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            Map<String, String> params = new HashMap<>();
            params.put("open_type", type + "");
            params.put("open_token", token);
            params.put("open_id", appId);
            params.put("token", SPUtils.getUserToken(BindActivity.this));

            LogUtils.e("open_type:" + type + "open_token:" + token + "open_id:" + appId);

            GsonRequest request = new GsonRequest<>(Request.Method.POST, NetApi.accoundMerger, params, UserInfoModel.class, new ResponseListener<UserInfoModel>() {
              @Override
              public void onErrorResponse(@Nullable VolleyError error) {
                Toast.makeText(BindActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
              }

              @Override
              public void onResponse(UserInfoModel response, boolean result, @Nullable String exception, @Nullable String msg) {
                Toast.makeText(BindActivity.this, "合并成功", Toast.LENGTH_SHORT).show();
                UserInfoHelper.saveUserInfo(response);
                //数据不变 暂时不刷新
                initViews();
                //                            EventBus.getDefault().post(new QuchuEventModel(EventFlags.EVENT_USER_INFO_UPDATA, response));
              }
            });
            request.start(BindActivity.this);
          }
        })
        .onNeutral(new MaterialDialog.SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            Intent intent = new Intent(BindActivity.this, StatementActivity.class);
            intent.putExtra(StatementActivity.REQUEST_KEY_TITLE, "什么是账号合并");
            intent.putExtra(StatementActivity.REQUEST_KEY_CONTENT, "账号合并是将用户已经绑定的第三方账号与当前帐号的数据进行合并，合并后允许用户使用多种方式登录，个人信息和行为会被融合，例如收藏，点赞，发表的脚印等，合并后趣处将为用户更全面的推荐内容。\n" +
                "\n账号合并的目的：由于人工智能算法是针对用户行为进行分析的，所以每个账号都记录了用户的行为数据，趣处认为如果用户曾经使用其他账号登录过并留下行为数据，那些数据应当被视为用户行为的一个重要组成部分被保存，所以在工程师的努力下，我们有幸能将自己的特长转化为你的愉悦体验，并将为此一直努力下去。\n" +
                "\n\n\n" +
                "——趣处人工智能实验室");
            startActivity(intent);
          }
        })
        .show();
  }

  /**
   * 绑定第三方账号
   */
  private void bind(final boolean isWeChat, final String token, final String appId) {
    Map<String, String> params = new HashMap<>();
    params.put("token", token);
    params.put("openId", appId);
    params.put("type", "bind");
    params.put("accesstoken", SPUtils.getUserToken(this));

    GsonRequest<Object> request = new GsonRequest<>(isWeChat ? NetApi.WechatBind : NetApi.WeiboBind, Object.class, params, new ResponseListener<Object>() {
      @Override
      public void onErrorResponse(@Nullable VolleyError error) {
        Toast.makeText(BindActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
      }

      @Override
      public void onResponse(Object response, boolean isNull, String exception, @Nullable String msg) {
        if (isNull) {
          Toast.makeText(BindActivity.this, "绑定成功", Toast.LENGTH_SHORT).show();
          saveInfo(true, isWeChat);
        } else {
          switch (exception) {
            case "10132":
              merger(isWeChat ? 2 : 3, token, appId);
              break;
            case "10133":
            case "10134":
              Toast.makeText(BindActivity.this, "该账号已被使用" + exception, Toast.LENGTH_SHORT).show();
              break;
            default:
              Toast.makeText(BindActivity.this, "该账号已被使用" + exception, Toast.LENGTH_SHORT).show();
          }

        }
      }
    });
    request.start(this);
  }

  /**
   * 解除绑定第三方账号
   */
  private void unBind(final boolean isWaChat, final String type) {
    Map<String, String> params = new HashMap<>();
    params.put("type", type);
    params.put("accesstoken", SPUtils.getUserToken(this));

    GsonRequest<Object> request = new GsonRequest<>(NetApi.unBindThrid, Object.class, params, new ResponseListener<Object>() {
      @Override
      public void onErrorResponse(@Nullable VolleyError error) {
        Toast.makeText(BindActivity.this, "解绑失败", Toast.LENGTH_SHORT).show();
      }

      @Override
      public void onResponse(Object response, boolean result, @Nullable String exception, @Nullable String msg) {
        if (result) {
          Toast.makeText(BindActivity.this, "解绑成功", Toast.LENGTH_SHORT).show();
          saveInfo(false, isWaChat);
        } else {
          Toast.makeText(BindActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
      }
    });
    request.start(this);
  }

  /**
   * 绑定成功后更新数据
   *
   * @param isWeChat 是不是绑定了微信
   */
  private void saveInfo(boolean isBind, boolean isWeChat) {
    UserInfoModel user = AppContext.user;
    if (isWeChat) {
      user.setIsweixin(isBind);
    } else {
      user.setIsweibo(isBind);
    }

    UserInfoHelper.saveUserInfo(user);
    initViews();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
  }
}
