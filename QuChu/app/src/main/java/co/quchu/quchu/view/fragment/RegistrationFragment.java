package co.quchu.quchu.view.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.net.NetUtil;
import co.quchu.quchu.presenter.UserLoginPresenter;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.activity.LoginActivity;
import co.quchu.quchu.view.activity.RecommendActivity;

import static co.quchu.quchu.base.AppContext.user;

/**
 * Created by Nico on 16/5/13.
 */
public class RegistrationFragment extends Fragment implements TextWatcher, View.OnFocusChangeListener {

  public static final String TAG = "RegistrationFragment";
  public static final String BUNDLE_KEY_VERIFY_CODE = "BUNDLE_KEY_VERIFY_CODE";
  public static final String BUNDLE_KEY_USERNAME = "BUNDLE_KEY_USERNAME";

  private boolean mRequestRunning = false;
  public String mUserName = "";
  public String mVerifyCode = "";

  @Bind(R.id.etUsername) EditText etUsername;
  @Bind(R.id.etPassword) EditText etPassword;
  @Bind(R.id.tvLoginViaPhone) TextView tvLoginViaPhone;
  private boolean mEmptyForum = false;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_registration, container, false);
    ButterKnife.bind(this, view);

    if (null != getArguments()) {
      mVerifyCode = getArguments().getString(BUNDLE_KEY_VERIFY_CODE);
      mUserName = getArguments().getString(BUNDLE_KEY_USERNAME);
    }

    etUsername.postDelayed(new Runnable() {
      @Override
      public void run() {
        etUsername.requestFocus();
      }
    }, 30);

    etUsername.setOnFocusChangeListener(this);
    etPassword.setOnFocusChangeListener(this);
    etUsername.addTextChangedListener(this);
    etPassword.addTextChangedListener(this);

    tvLoginViaPhone.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        register();
      }
    });

    return view;
  }

  /**
   * 注册
   */
  private void register() {
    if (!NetUtil.isNetworkConnected(getActivity())) {
      Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
      return;
    }

    if (!mEmptyForum && verifyForm()) {

      if (mRequestRunning) {
        return;
      }

      mRequestRunning = true;

      if (null != user && user.isIsVisitors()) {

        int visitorUid = user.getUserId();
        String pwd = etPassword.getText().toString();
        String nickName = etUsername.getText().toString();

        UserLoginPresenter.userRegiest(getActivity(), visitorUid, mUserName, pwd, nickName, mVerifyCode, new UserLoginPresenter.UserNameUniqueListener() {
          @Override
          public void isUnique(JSONObject msg) {
            Toast.makeText(getActivity(), R.string.promote_account_create_success, Toast.LENGTH_SHORT).show();
            SPUtils.putLoginType(SPUtils.LOGIN_TYPE_PHONE);
            getActivity().startActivity(new Intent(getActivity(), RecommendActivity.class).putExtra(RecommendActivity.REQUEST_KEY_FROM_LOGIN, true));
            getActivity().finish();
            mRequestRunning = false;
          }

          @Override
          public void notUnique(String msg) {
            Toast.makeText(getActivity(), R.string.promote_account_create_success_login_manually, Toast.LENGTH_SHORT).show();
            mRequestRunning = false;
          }
        });
      } else {
        mRequestRunning = false;
      }
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    ((BaseActivity) getActivity()).getEnhancedToolbar().getTitleTv().setText(R.string.registration_step_2);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  /**
   * 验证输入是否正确有效
   */
  private boolean verifyForm() {
    String userName = etUsername.getText().toString();
    String userPwd = etPassword.getText().toString();

    if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(userPwd)) {
      tvLoginViaPhone.setText(R.string.promote_empty_username_or_password);
      tvLoginViaPhone.setBackgroundColor(getResources().getColor(R.color.standard_color_red));
      tvLoginViaPhone.setTextColor(getResources().getColor(R.color.standard_color_white));
      tvLoginViaPhone.setClickable(false);
      return false;
    }

    if (!StringUtils.isGoodPassword(userPwd)) {
      tvLoginViaPhone.setText(R.string.promote_invalid_password);
      tvLoginViaPhone.setBackgroundColor(getResources().getColor(R.color.standard_color_red));
      tvLoginViaPhone.setTextColor(getResources().getColor(R.color.standard_color_white));
      tvLoginViaPhone.setClickable(false);
      return false;
    }

    tvLoginViaPhone.setBackgroundColor(getResources().getColor(R.color.standard_color_yellow));
    tvLoginViaPhone.setTextColor(getResources().getColor(R.color.standard_color_h0_dark));
    tvLoginViaPhone.setText(R.string.next);
    tvLoginViaPhone.setClickable(true);

    return true;
  }

  public void updateButtonStatus() {
    mEmptyForum = true;

    if (null == etUsername || null == etPassword) {
      return;
    }

    String userName = etUsername.getText().toString();
    String userPwd = etPassword.getText().toString();

    if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(userPwd)) {
      return;
    }

//    if (etPassword.hasFocus()) {
//      if (etPassword.getText().length() < 6) {
//        tvLoginViaPhone.setText("密码请输入6-12位数字或英文");
//        tvLoginViaPhone.setBackgroundColor(getResources().getColor(R.color.standard_color_red));
//        return;
//      }
//    }

    if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(userPwd)) {
      tvLoginViaPhone.setText(R.string.next);
      mEmptyForum = false;
      tvLoginViaPhone.setBackgroundColor(getResources().getColor(R.color.standard_color_yellow));
      tvLoginViaPhone.setTextColor(getResources().getColor(R.color.standard_color_h0_dark));
      tvLoginViaPhone.setClickable(true);
    } else {
      tvLoginViaPhone.setText(R.string.next);
      tvLoginViaPhone.setBackgroundColor(getResources().getColor(R.color.standard_color_red));
      tvLoginViaPhone.setTextColor(getResources().getColor(R.color.standard_color_white));
      tvLoginViaPhone.setClickable(true);
    }
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {
  }

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {
  }

  @Override
  public void afterTextChanged(Editable s) {
    updateButtonStatus();
  }

  @Override
  public void onFocusChange(View v, boolean hasFocus) {
    updateButtonStatus();
  }

  @OnClick(R.id.backgroundLayout)
  public void onClick() {
    ((LoginActivity) getActivity()).hideSoftware();
  }
}
