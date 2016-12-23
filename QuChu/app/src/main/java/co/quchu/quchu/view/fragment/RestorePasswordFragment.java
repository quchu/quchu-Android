package co.quchu.quchu.view.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
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
import co.quchu.quchu.social.UserLoginListener;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.activity.LoginActivity;
import co.quchu.quchu.view.activity.RecommendActivity;

/**
 * Created by Nico on 16/5/13.
 */
public class RestorePasswordFragment extends Fragment {

  public static final String TAG = "RestorePasswordFragment";
  public static final String BUNDLE_KEY_VERIFY_CODE = "BUNDLE_KEY_VERIFY_CODE";
  public static final String BUNDLE_KEY_USERNAME = "BUNDLE_KEY_USERNAME";
  private boolean mRequestRunning = false;
  public String mUserName = "";
  public String mVerifyCode = "";

  @Bind(R.id.etPassword)
  EditText etPassword;
  @Bind(R.id.tvNext)
  TextView tvNext;
  @Bind(R.id.ivSwitchVisible)
  ImageView ivSwitchVisible;
  public boolean mDisplayPassword = false;
  private boolean hasPassword = false;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_restore_password, container, false);
    ButterKnife.bind(this, view);

    if (null != getArguments()) {
      mVerifyCode = getArguments().getString(BUNDLE_KEY_VERIFY_CODE);
      mUserName = getArguments().getString(BUNDLE_KEY_USERNAME);
    }

    etPassword.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
      }

      @Override
      public void afterTextChanged(Editable s) {
        String str = s.toString().trim();
        if (TextUtils.isEmpty(str)) {
          tvNext.setText(R.string.next);
          tvNext.setBackgroundColor(getResources().getColor(R.color.colorBackground_db));
          tvNext.setTextColor(getResources().getColor(R.color.standard_color_h3_dark));
          tvNext.setClickable(false);
          hasPassword = false;
        } else {
          tvNext.setText(R.string.next);
          tvNext.setBackgroundColor(getResources().getColor(R.color.standard_color_yellow));
          tvNext.setTextColor(getResources().getColor(R.color.standard_color_h0_dark));
          tvNext.setClickable(true);
          hasPassword = true;
        }
      }
    });

    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
    ((BaseActivity) getActivity()).getEnhancedToolbar().getTitleTv().setText(R.string.forget_pwd_step_2);
    etPassword.postDelayed(new Runnable() {

      @Override
      public void run() {
        etPassword.requestFocus();
        InputMethodManager keyboard =
            (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.showSoftInput(etPassword, 0);
      }
    }, 50);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  @OnClick({R.id.ivSwitchVisible, R.id.tvNext, R.id.backgroundLayout})
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.ivSwitchVisible:
        showOrHidePassword();
        break;

      case R.id.tvNext:
        submitClick();
        break;

      case R.id.backgroundLayout:
        ((LoginActivity) getActivity()).hideSoftware();
        break;
    }
  }

  private void submitClick() {
    if (!NetUtil.isNetworkConnected(getActivity())) {
      Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
      return;
    }

    if (mRequestRunning) {
      return;
    }

    mRequestRunning = true;

    if (hasPassword && StringUtils.isGoodPassword(etPassword.getText().toString())) {
      UserLoginPresenter.resetPassword(getActivity(), mUserName, etPassword.getText().toString(), mVerifyCode, new UserLoginPresenter.UserNameUniqueListener() {
        @Override
        public void isUnique(JSONObject msg) {
          UserLoginPresenter.userLogin(getActivity(), mUserName, etPassword.getText().toString(), new UserLoginListener() {
            @Override
            public void loginSuccess(int type, String token, String appId) {
              Toast.makeText(getActivity(), R.string.promote_password_update_success, Toast.LENGTH_SHORT).show();
              SPUtils.putLoginType(SPUtils.LOGIN_TYPE_PHONE);
              getActivity().startActivity(new Intent(getActivity(), RecommendActivity.class).putExtra(RecommendActivity.REQUEST_KEY_FROM_LOGIN, true));
              getActivity().finish();
              mRequestRunning = false;
            }

            @Override
            public void loginFail(String errorMsg) {
              mRequestRunning = false;
              Toast.makeText(getActivity(), R.string.promote_password_update_success_login_manually, Toast.LENGTH_SHORT).show();
            }
          });
        }

        @Override
        public void notUnique(String msg) {
          mRequestRunning = false;
          Toast.makeText(getActivity(), R.string.promote_password_update_failure, Toast.LENGTH_SHORT).show();
        }
      });
    } else {
      mRequestRunning = false;
      tvNext.setText(R.string.hint_new_password);
      tvNext.setBackgroundColor(getResources().getColor(R.color.standard_color_red));
      tvNext.setTextColor(getResources().getColor(R.color.standard_color_white));
      tvNext.setClickable(false);
    }
  }

  /**
   * 显示和隐藏密码
   */
  private void showOrHidePassword() {
    if (!mDisplayPassword) {
      etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
    } else {
      etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
    }
    mDisplayPassword = !mDisplayPassword;
  }
}
