package co.quchu.quchu.view.fragment;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.Timer;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.net.NetUtil;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.UserLoginPresenter;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.VCodeTimeService;
import co.quchu.quchu.widget.ErrorView;

/**
 * Created by Nico on 16/5/13.
 */
public class PhoneValidationFragment extends Fragment {

  @Bind(R.id.ivIconUserName)
  ImageView ivIconUserName;
  @Bind(R.id.etUsername)
  EditText etUsername;
  @Bind(R.id.ivIconClear)
  ImageView ivIconClear;
  @Bind(R.id.rlUserNameField)
  RelativeLayout rlUserNameField;
  @Bind(R.id.ivIconValidCode)
  ImageView ivIconValidCode;
  @Bind(R.id.etValidCode)
  EditText etValidCode;
  @Bind(R.id.tvSendValidCode)
  TextView tvSendValidCode;
  @Bind(R.id.rlValidCode)
  RelativeLayout rlValidCode;
  @Bind(R.id.tvNext)
  TextView tvNext;
  @Bind(R.id.tvLoginViaThisNumber)
  TextView tvLoginViaThisNumber;

  @Bind(R.id.errorView)
  ErrorView errorView;
  private int mSecs = 0;
  private boolean isRunning = false;
  private boolean registed = false;
  public static final String TAG = "PhoneValidationFragment";
  private boolean mEmptyForum = false;
  private long mRequestTimeStamp = -1;
  private Timer mCountingTimer;
  private boolean mIsRegistration = true;
  private boolean mVerifyed = false;
  public static final String BUNDLE_KEY_REGISTRATION = "BUNDLE_KEY_REGISTRATION";
  public static final String BUNDLE_KEY_PHONE_NUMBER = "BUNDLE_KEY_PHONE_NUMBER";
  private String mPhoneNumber = "";
  private boolean codeSent = false;
  boolean isVerifying = false;

  private int mContainerId = R.id.flContent;

  private VCodeTimeService mCodeTimeService;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_validation_phone, container, false);
    ButterKnife.bind(this, view);

    if (null != getArguments()) {
      mIsRegistration = getArguments().getBoolean(BUNDLE_KEY_REGISTRATION, true);
      mPhoneNumber = getArguments().getString(BUNDLE_KEY_PHONE_NUMBER);
    }
    //((BaseActivity)getActivity()).getEnhancedToolbar().getTitleTv().setText(mIsRegistration?R.string.registration_step_1:R.string.forget_pwd_step_1);

    tvLoginViaThisNumber.setVisibility(View.GONE);
    ivIconClear.setVisibility(View.INVISIBLE);
    etUsername.addTextChangedListener(new TextWatcher() {
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
    });

    etUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        updateButtonStatus();
      }
    });

    etValidCode.addTextChangedListener(new TextWatcher() {
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
    });

    if (null != mPhoneNumber) {
      etUsername.setText(mPhoneNumber);
    }

    //验证码倒计时
    mCodeTimeService = new VCodeTimeService();
    mCodeTimeService.setOnTimeListener(onTimeListener);
    mCodeTimeService.onCreate(savedInstanceState);

    return view;
  }

  public void updateButtonStatus() {
    if (registed && null != tvLoginViaThisNumber) {
      tvLoginViaThisNumber.setVisibility(View.GONE);
    }

    if (null == etUsername) {
      return;
    }

    String userName = null == etUsername.getText() ? "" : etUsername.getText().toString();
    ivIconClear.setVisibility(userName.length() > 0 ? View.VISIBLE : View.INVISIBLE);
    if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(etValidCode.getText())) {
      tvNext.setBackgroundColor(getResources().getColor(R.color.standard_color_yellow));
      tvNext.setText(R.string.next);
      mEmptyForum = false;
    } else {
      tvNext.setText(R.string.next);
      mEmptyForum = true;
      tvNext.setBackgroundColor(Color.parseColor("#dbdbdb"));
    }

    if (!codeSent) {
      if (StringUtils.isMobileNO(userName)) {
        tvSendValidCode.setBackgroundResource(R.drawable.shape_lineframe_yellow_fill);
      } else {
        tvSendValidCode.setBackgroundColor(getResources().getColor(R.color.standard_color_h3_dark));
      }
    }
  }

  private boolean verifyForm(boolean verifyValidCode) {
    boolean status = false;
    String userName = etUsername.getText().toString();

    if (TextUtils.isEmpty(userName)) {
      tvNext.setText(R.string.promote_empty_phone);
      tvNext.setBackgroundColor(getResources().getColor(R.color.colorAccent));
    } else if (!StringUtils.isMobileNO(userName)) {
      tvNext.setText(R.string.promote_invalid_phone_number);
      tvNext.setBackgroundColor(getResources().getColor(R.color.colorAccent));
    } else if (verifyValidCode && !TextUtils.isEmpty(userName) && !TextUtils.isEmpty(etValidCode.getText())) {
      tvNext.setBackgroundColor(getResources().getColor(R.color.standard_color_yellow));
      status = true;
    } else if (!verifyValidCode) {
      status = true;
    } else {
      tvNext.setBackgroundColor(Color.parseColor("#dbdbdb"));
    }
    return status;
  }

  /**
   * 倒计时监听
   */
  private VCodeTimeService.OnVCodeTimeListener onTimeListener = new VCodeTimeService.OnVCodeTimeListener() {

    @Override
    public void timeRemaining(int leftSecond) {
      codeSent = true;
      if (tvSendValidCode != null) {
        tvSendValidCode.setBackgroundResource(R.color.colorBorder);
        tvSendValidCode.setText("(" + leftSecond + ")秒后重新发送");
        tvSendValidCode.setEnabled(false);
      }
    }

    @Override
    public void onTimeOut() {
      codeSent = false;
      if (tvSendValidCode != null) {
        tvSendValidCode.setText(R.string.send_valid_code);
        tvSendValidCode.setBackgroundResource(R.color.colorAccent);
        tvSendValidCode.setEnabled(true);
      }
    }

    @Override
    public void onReset() {
    }
  };

  private void verifySms() {
    if (!NetUtil.isNetworkConnected(getActivity())) {
      Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
      return;
    }

    if (isVerifying) {
      return;
    }

    isVerifying = true;
    UserLoginPresenter.verifyNext(getActivity(), etUsername.getText().toString(), etValidCode.getText().toString(), new CommonListener() {
      @Override
      public void successListener(Object response) {
        Toast.makeText(getActivity(), R.string.promote_verify_pass, Toast.LENGTH_SHORT).show();
        isVerifying = false;
        if (mIsRegistration) {
          RegistrationFragment registrationFragment = new RegistrationFragment();
          Bundle bundle = new Bundle();
          bundle.putString(RegistrationFragment.BUNDLE_KEY_USERNAME, etUsername.getText().toString());
          bundle.putString(RegistrationFragment.BUNDLE_KEY_VERIFY_CODE, etValidCode.getText().toString());

          registrationFragment.setArguments(bundle);
          getFragmentManager().beginTransaction().setCustomAnimations(
              R.animator.card_flip_horizontal_right_in,
              R.animator.card_flip_horizontal_left_out,
              R.animator.card_flip_horizontal_left_in,
              R.animator.card_flip_horizontal_right_out)
              .replace(mContainerId, registrationFragment)
              .addToBackStack(TAG)
              .commitAllowingStateLoss();
          getFragmentManager().executePendingTransactions();
          ((BaseActivity) getActivity()).getEnhancedToolbar().show();

        } else {
          RestorePasswordFragment restorePasswordFragment = new RestorePasswordFragment();
          Bundle bundleRestorePwd = new Bundle();
          bundleRestorePwd.putString(RestorePasswordFragment.BUNDLE_KEY_USERNAME, etUsername.getText().toString());
          bundleRestorePwd.putString(RestorePasswordFragment.BUNDLE_KEY_VERIFY_CODE, etValidCode.getText().toString());
          restorePasswordFragment.setArguments(bundleRestorePwd);
          getFragmentManager().beginTransaction().setCustomAnimations(
              R.animator.card_flip_horizontal_right_in,
              R.animator.card_flip_horizontal_left_out,
              R.animator.card_flip_horizontal_left_in,
              R.animator.card_flip_horizontal_right_out)
              .replace(mContainerId, restorePasswordFragment)
              .addToBackStack(TAG)
              .commitAllowingStateLoss();
          getFragmentManager().executePendingTransactions();
          ((BaseActivity) getActivity()).getEnhancedToolbar().show();
        }
      }

      @Override
      public void errorListener(VolleyError error, String exception, String msg) {
        Toast.makeText(getActivity(), R.string.promote_verify_fail, Toast.LENGTH_SHORT).show();
        isVerifying = false;
      }
    });

  }

  @Override
  public void onPause() {
    super.onPause();
    View view = getActivity().getCurrentFocus();
    if (view != null) {
      InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    ((BaseActivity) getActivity()).getEnhancedToolbar().getTitleTv().setText(mIsRegistration ? R.string.registration_step_1 : R.string.forget_pwd_step_1);

    etUsername.requestFocus();
    etUsername.postDelayed(new Runnable() {

      @Override
      public void run() {
        InputMethodManager keyboard = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.showSoftInput(etUsername, 0);
      }
    }, 350);
  }

  private void getValidCode() {
    if (!NetUtil.isNetworkConnected(getActivity())) {
      Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
      return;
    }

    if (isRunning) {
      return;
    }
    isRunning = true;

    if (mIsRegistration) {
      UserLoginPresenter.decideMobileCanLogin(getActivity(), etUsername.getText().toString(), new UserLoginPresenter.UserNameUniqueListener() {
        @Override
        public void isUnique(JSONObject msg) {
          isRunning = false;
          if (mSecs > 0) {
            return;
          }
          errorView.showLoading();

          UserLoginPresenter.requestVerifySms(getActivity(), etUsername.getText().toString(), UserLoginPresenter.getCaptcha_regiest, new UserLoginPresenter.UserNameUniqueListener() {
            @Override
            public void isUnique(JSONObject msg) {
              errorView.hideView();
              startTimer();
              mVerifyed = true;
              etValidCode.requestFocus();
            }

            @Override
            public void notUnique(String msg) {
              errorView.hideView();
              Toast.makeText(getActivity(), R.string.promote_verify_fail, Toast.LENGTH_SHORT).show();

              mCodeTimeService.reset();
            }
          });
        }

        @Override
        public void notUnique(String msg) {
          isRunning = false;
          tvNext.setText(R.string.promote_duplicate_username);
          registed = true;
          tvLoginViaThisNumber.setVisibility(View.VISIBLE);
          tvNext.setBackgroundColor(getResources().getColor(R.color.colorAccent));

        }
      });
    } else {
      isRunning = false;
      if (mSecs > 0) {
        return;
      }
      errorView.showLoading();

      UserLoginPresenter.decideMobileCanLogin(getActivity(), etUsername.getText().toString(), new UserLoginPresenter.UserNameUniqueListener() {
        @Override
        public void isUnique(JSONObject msg) {
          Toast.makeText(getActivity(), R.string.promote_username_not_existed, Toast.LENGTH_SHORT).show();
          isRunning = false;
          errorView.hideView();
        }

        @Override
        public void notUnique(String msg) {
          isRunning = false;

          UserLoginPresenter.requestVerifySms(getActivity(), etUsername.getText().toString(), UserLoginPresenter.getCaptcha_reset, new UserLoginPresenter.UserNameUniqueListener() {
            @Override
            public void isUnique(JSONObject msg) {
              errorView.hideView();
              startTimer();
              mVerifyed = true;
              isRunning = false;
            }

            @Override
            public void notUnique(String msg) {
              errorView.hideView();
              Toast.makeText(getActivity(), R.string.promote_verify_fail, Toast.LENGTH_SHORT).show();

              mCodeTimeService.reset();
              isRunning = false;
            }
          });
        }
      });
    }
  }

  /**
   * 开始倒计时
   */
  private void startTimer() {
    mCodeTimeService.reset();
    mCodeTimeService.startTimer();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  @OnClick({R.id.ivIconUserName, R.id.etUsername, R.id.ivIconClear, R.id.rlUserNameField, R.id.ivIconValidCode, R.id.etValidCode, R.id.tvSendValidCode, R.id.rlValidCode, R.id.tvLoginViaThisNumber, R.id.tvNext, R.id.errorView})
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.ivIconClear:
        //清空
        etUsername.setText("");
        updateButtonStatus();
        break;

      case R.id.tvSendValidCode:
        //获取验证码
        if (verifyForm(false)) {
          if (null != etUsername.getText() && StringUtils.isMobileNO(etUsername.getText().toString())) {
            getValidCode();
          }
        }
        break;

      case R.id.tvLoginViaThisNumber:
        Fragment f = new LoginByPhoneFragment();
        Bundle bundle = new Bundle();
        bundle.putString(LoginByPhoneFragment.BUNDLE_KEY_PHONE_NUMBER, etUsername.getText().toString());
        f.setArguments(bundle);
        getFragmentManager().beginTransaction()
            .replace(mContainerId, f)
            .addToBackStack(TAG)
            .commitAllowingStateLoss();
        getFragmentManager().executePendingTransactions();
        ((BaseActivity) getActivity()).getEnhancedToolbar().show();
        break;

      case R.id.tvNext:
        if (codeSent && verifyForm(true)) {
          if (mVerifyed) {
            verifySms();
          } else {
            tvNext.setText(R.string.promote_verify_fail);
            tvNext.setBackgroundColor(getResources().getColor(R.color.colorAccent));
          }
        }
        break;
    }
  }
}
