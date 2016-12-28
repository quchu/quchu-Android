package co.quchu.quchu.view.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhuge.analysis.stat.ZhugeSDK;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.net.NetUtil;
import co.quchu.quchu.social.SocialHelper;
import co.quchu.quchu.social.UserLoginListener;
import co.quchu.quchu.utils.AppUtil;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.view.activity.RecommendActivity;

/**
 * Created by Nico on 16/5/13.
 */
public class LoginFragment extends Fragment implements View.OnClickListener, UserLoginListener {

  public static final String TAG = "LoginFragment";
  @Bind(R.id.llAuthorizationViaWeibo) LinearLayout llAuthorizationViaWeibo;
  @Bind(R.id.llAuthorizationViaMm) LinearLayout llAuthorizationViaMm;
  @Bind(R.id.thirdLoginContainer) LinearLayout thirdLoginContainer;
  @Bind(R.id.hellWord) TextView hellWord;
  @Bind(R.id.tvLoginViaPhone) TextView tvLoginViaPhone;
  @Bind(R.id.tvCreateAccountViaPhone) TextView tvCreateAccountViaPhone;
  @Bind(R.id.tvForgottenPassword) TextView tvForgottenPassword;
  private int mContainerId = -1;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_login_main, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public void onResume() {
    super.onResume();

    ((BaseActivity) getActivity()).getEnhancedToolbar().getTitleTv().setText("");
    ((BaseActivity) getActivity()).getEnhancedToolbar().getRightTv().setVisibility(View.GONE);
  }

  @Override
  public void onDestroyView() {

    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  public void sinaLogin() {
    SocialHelper.getPlatformInfo(getActivity(), SHARE_MEDIA.SINA, true, this);
  }

  public void weixinLogin() {
    SocialHelper.getPlatformInfo(getActivity(), SHARE_MEDIA.WEIXIN, true, this);
  }

  private FragmentTransaction getFragmentTransactor() {
    return getFragmentManager().beginTransaction()
        .setCustomAnimations(R.animator.card_flip_horizontal_right_in,
            R.animator.card_flip_horizontal_left_out, R.animator.card_flip_horizontal_left_in,
            R.animator.card_flip_horizontal_right_out);
  }

  @OnClick({R.id.tvForgottenPassword, R.id.tvLoginViaPhone, R.id.tvCreateAccountViaPhone,
      R.id.llAuthorizationViaMm, R.id.llAuthorizationViaWeibo})
  public void onClick(View v) {
    mContainerId = mContainerId == -1 ? ((ViewGroup) getView().getParent()).getId() : mContainerId;
    switch (v.getId()) {
      case R.id.tvForgottenPassword:
        //忘记密码
        PhoneValidationFragment pvfResetPwd = new PhoneValidationFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(PhoneValidationFragment.BUNDLE_KEY_REGISTRATION, false);
        pvfResetPwd.setArguments(bundle);
        getFragmentTransactor().replace(mContainerId, pvfResetPwd)
            .addToBackStack(TAG)
            .commitAllowingStateLoss();
        getFragmentManager().executePendingTransactions();
        ((BaseActivity) getActivity()).getEnhancedToolbar().show();
        break;

      case R.id.tvLoginViaPhone:
        //手机账号登录
        getFragmentTransactor().replace(mContainerId, new LoginByPhoneFragment())
            .addToBackStack(TAG)
            .commitAllowingStateLoss();
        getFragmentManager().executePendingTransactions();
        ((BaseActivity) getActivity()).getEnhancedToolbar().show();

        MobclickAgent.onEvent(getActivity(), "pop_login_c");
        break;

      case R.id.tvCreateAccountViaPhone:
        //手机号注册
        getFragmentTransactor().replace(mContainerId, new PhoneValidationFragment())
            .addToBackStack(TAG)
            .commitAllowingStateLoss();
        getFragmentManager().executePendingTransactions();
        ((BaseActivity) getActivity()).getEnhancedToolbar().show();
        MobclickAgent.onEvent(getActivity(), "pop_registerphone_c");
        break;

      case R.id.llAuthorizationViaMm:
        //微信登录
        if (NetUtil.isNetworkConnected(getActivity())) {
          weixinLogin();
        } else {
          Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
        }
        MobclickAgent.onEvent(getActivity(), "pop_loginwechat_c");
        break;

      case R.id.llAuthorizationViaWeibo:
        //微博登录
        if (NetUtil.isNetworkConnected(getActivity())) {
          sinaLogin();
        } else {
          Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
        }
        MobclickAgent.onEvent(getActivity(), "pop_loginweibo_c");
        break;
    }
  }

  @Override
  public void loginSuccess(int type, String token, String appId) {

    ArrayMap<String, Object> params = new ArrayMap<>();

    if (AppContext.user.isIsweixin()) {
      params.put("登陆方式", "微信登录");
    } else if (AppContext.user.isIsweibo()) {
      params.put("登陆方式", "微博登录");
    } else if (AppContext.user.isIsVisitors()) {
      params.put("登陆方式", "游客登录");
    } else {
      params.put("登陆方式", "手机号登录");
    }
    params.put("用户名", AppContext.user.getFullname());

    JSONObject jsonObject = new JSONObject();
    try {
      for (String key : params.keySet()) {
        jsonObject.put(key, params.get(key));
      }
      jsonObject.put("时间", System.currentTimeMillis());
    } catch (JSONException e) {
      e.printStackTrace();
    }
    ZhugeSDK.getInstance().track(getActivity(), "用户登录", jsonObject);

    AppUtil.resignUser(getActivity());
    startActivity(new Intent(getActivity(), RecommendActivity.class));
    EventBus.getDefault().post(new QuchuEventModel(EventFlags.EVENT_USER_LOGIN_SUCCESS));
  }

  @Override
  public void loginFail(String message) {
    LogUtils.e("LoginFragment", "login fail message : " + message);
  }
}
