package co.quchu.quchu.view.fragment;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.presenter.UserLoginPresenter;
import co.quchu.quchu.thirdhelp.UserInfoHelper;
import co.quchu.quchu.thirdhelp.UserLoginListener;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.activity.UserLoginActivity;

/**
 * UserLoginMainFragment
 * User: Chenhs
 * Date: 2015-11-25
 */
public class PhoneLoginFragment extends Fragment {
    @Bind(R.id.phone_login_pnum_et)
    EditText phoneLoginPnumEt;
    @Bind(R.id.phone_login_pnum_ll)
    LinearLayout phoneLoginPnumLl;
    @Bind(R.id.phone_login_password_et)
    EditText phoneLoginPasswordEt;
    @Bind(R.id.phone_login_password_ll)
    LinearLayout phoneLoginPasswordLl;
    @Bind(R.id.authcode_login_password_et)
    EditText authcodeLoginPasswordEt;
    @Bind(R.id.getauthcode_login_tv)
    TextView getauthcodeLoginTv;
    @Bind(R.id.getauthcode_login_rl)
    RelativeLayout getauthcodeLoginRl;
    @Bind(R.id.authcode_login_password_ll)
    LinearLayout authcodeLoginPasswordLl;
    @Bind(R.id.phone_login_enter_tv)
    TextView phoneLoginEnterTv;
    @Bind(R.id.phone_login_progress_iv)
    ImageView phoneLoginProgressIv;
    @Bind(R.id.user_login_forget_tv)
    TextView userLoginForgetTv;
    @Bind(R.id.user_login_nickname_et)
    EditText userLoginNicknameEt;
    @Bind(R.id.user_login_nickname_ll)
    LinearLayout userLoginNicknameLl;

    private View view;
    private AnimationDrawable animationDrawable;
    private InputMethodManager inputManager;
    /**
     * isRegiest=0 注册  =1登录  =2 重置密码
     */
    private int isRegiest = 0;
    private long waitTime = 3 * 1000;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            stopUserLoginProgress(); //隐藏进度条
            LogUtils.json("handler");
            switch (msg.what) {
                case 0x00:
                    toRegiest();
                    break;
                case 0x01:
                    toLogin();
                    break;
                case 0x02:
                    counterText();
                    break;
                case 0x03:
                    view.clearFocus();
                    phoneLoginPnumEt.setFocusable(true);
                    phoneLoginPnumEt.setFocusableInTouchMode(true);
                    phoneLoginPnumEt.requestFocus();
                    inputManager =
                            (InputMethodManager) phoneLoginPnumEt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.showSoftInput(phoneLoginPnumEt, 0);
                    break;
                case 0x04://用户注册 昵称获取输入框
                    view.clearFocus();
                    userLoginNicknameEt.setFocusable(true);
                    userLoginNicknameEt.setFocusableInTouchMode(true);
                    userLoginNicknameEt.requestFocus();
                    inputManager =
                            (InputMethodManager) userLoginNicknameEt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.showSoftInput(userLoginNicknameEt, 0);
                    break;
                case 0x05://用户登录 密码获取输入框
                    view.clearFocus();
                    phoneLoginPasswordEt.setFocusable(true);
                    phoneLoginPasswordEt.setFocusableInTouchMode(true);
                    phoneLoginPasswordEt.requestFocus();
                    inputManager =
                            (InputMethodManager) phoneLoginPasswordEt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.showSoftInput(phoneLoginPasswordEt, 0);
                    break;
            }
        }
    };

    private int mAuthCounter = 60;
    private String mAuthDesc = "%ds后重新获取";

    private void counterText() {
        if (mAuthCounter > 0) {
            getauthcodeLoginTv.setText(String.format(mAuthDesc, mAuthCounter));
            mAuthCounter--;
            handler.sendMessageDelayed(handler.obtainMessage(0x02), 1000);
        } else {
            getauthcodeLoginTv.setText("再次获取验证码");
            getauthcodeLoginTv.setClickable(true);
            mAuthCounter = 60;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_login_phone, null);
        ButterKnife.bind(this, view);
        phoneLoginPnumEt.addTextChangedListener(new PhoneNumWatcher());
        initEditText();
        handler.sendMessageDelayed(handler.obtainMessage(0x03), 300);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * 点击事件
     */
    @OnClick({R.id.getauthcode_login_tv, R.id.phone_login_enter_tv, R.id.user_login_forget_tv})
    public void userLoginClick(View view) {
        switch (view.getId()) {
            case R.id.getauthcode_login_tv:

                getAuthCode();

                break;
            case R.id.phone_login_enter_tv:
                if (isRegiest == 0) {
                    userRegiest();
                } else if (isRegiest == 2) {
                    userResetPassword();
                } else {
                    userLogin();
                }
                break;
            case R.id.user_login_forget_tv:
                if (isRegiest == 1) {

                    LogUtils.json("user_login_forget_tv");
                    forgetPassword();
                } else if (isRegiest == 2) {
                    toLogin();
                }
                break;
        }
    }


    /**
     * 开始播放帧动画
     */
    private void startUserLoginProgress() {
        phoneLoginProgressIv.setImageResource(R.drawable.user_login_progress);
        animationDrawable = (AnimationDrawable) phoneLoginProgressIv.getDrawable();
        phoneLoginProgressIv.setVisibility(View.VISIBLE);
        animationDrawable.start();
    }

    private void stopUserLoginProgress() {
        if (phoneLoginProgressIv != null) {
            //   if (phoneLoginProgressIv.getVisibility() == View.VISIBLE) {
            phoneLoginProgressIv.setImageResource(R.drawable.user_login_progress);
            animationDrawable = (AnimationDrawable) phoneLoginProgressIv.getDrawable();
            if (animationDrawable.isRunning())
                animationDrawable.stop();
            phoneLoginProgressIv.setVisibility(View.GONE);
        }
        // }
    }


    /**
     * 手机号码唯一
     * 展示注册界面
     */
    private void toRegiest() {
        hintOtherView();
        isRegiest = 0;
        phoneLoginPasswordLl.setVisibility(View.VISIBLE);
        authcodeLoginPasswordLl.setVisibility(View.VISIBLE);
        phoneLoginEnterTv.setVisibility(View.VISIBLE);
        userLoginForgetTv.setVisibility(View.VISIBLE);
        userLoginNicknameLl.setVisibility(View.VISIBLE);
        phoneLoginEnterTv.setText("创建");
        //   userLoginForgetTv.setText("创建账户即代表同意并遵守《趣处用户协议》");
        userLoginForgetTv.setText(
                Html.fromHtml(
                        "创建账户即代表同意并遵守" +
                                "<font color=#f4e727><a href=\"http://www.quchu.co/user-agreement.html\">《趣处用户协议》</a> </font> "));
        userLoginForgetTv.setMovementMethod(LinkMovementMethod.getInstance());
        handler.sendMessageDelayed(handler.obtainMessage(0x04), 180);
    }

    /**
     * 展示登录界面
     */
    private void toLogin() {
        hintOtherView();
        isRegiest = 1;
        LogUtils.json("toLogin");
        phoneLoginPasswordLl.setVisibility(View.VISIBLE);
        phoneLoginEnterTv.setVisibility(View.VISIBLE);
        userLoginForgetTv.setVisibility(View.VISIBLE);
        phoneLoginEnterTv.setText("登录");
        userLoginForgetTv.setText("忘记密码");
        handler.sendMessageDelayed(handler.obtainMessage(0x05), 180);
    }

    private void forgetPassword() {
        isRegiest = 2;
        phoneLoginPasswordLl.setVisibility(View.VISIBLE);
        phoneLoginEnterTv.setVisibility(View.VISIBLE);
        authcodeLoginPasswordLl.setVisibility(View.VISIBLE);
        userLoginForgetTv.setText("想起密码");
    }


    private void hintOtherView() {
        phoneLoginPasswordLl.setVisibility(View.GONE);
        authcodeLoginPasswordLl.setVisibility(View.GONE);
        phoneLoginEnterTv.setVisibility(View.GONE);
        userLoginForgetTv.setVisibility(View.GONE);
        userLoginNicknameLl.setVisibility(View.GONE);
        authcodeLoginPasswordEt.setText("");
        phoneLoginPasswordEt.setText("");
        userLoginNicknameEt.setText("");
    }

    private void initEditText() {
        //输入密码
        phoneLoginPasswordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isRegiest == 0) {
                    setRegiestButtonClickable(s.length() > 0 && authcodeLoginPasswordEt.getText().toString().trim().length() > 0 && userLoginNicknameEt.getText().toString().trim().length() > 0);
                } else if (isRegiest == 1) {
                    setRegiestButtonClickable(phoneLoginPnumEt.getText().toString().trim().length() > 0 && s.length() > 0);
                } else {
                    setRegiestButtonClickable(s.length() > 0 && authcodeLoginPasswordEt.getText().toString().trim().length() > 0 && phoneLoginPnumEt.getText().toString().trim().length() > 0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        userLoginNicknameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isRegiest == 0) {
                    setRegiestButtonClickable(s.length() > 0 && authcodeLoginPasswordEt.getText().toString().trim().length() > 0 && phoneLoginPasswordEt.getText().toString().trim().length() > 0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        authcodeLoginPasswordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isRegiest == 0) {
                    setRegiestButtonClickable(s.length() > 0 && userLoginNicknameEt.getText().toString().trim().length() > 0 && phoneLoginPasswordEt.getText().toString().trim().length() > 0);
                } else if (isRegiest == 2) {
                    setRegiestButtonClickable(s.length() > 0 && phoneLoginPasswordEt.getText().toString().trim().length() > 0 && phoneLoginPnumEt.getText().toString().trim().length() > 0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private Handler hander = new Handler() {
        public void handleMessage(android.os.Message msg) {

            InputMethodManager inputManager = (InputMethodManager) phoneLoginPnumEt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(phoneLoginPnumEt, 0);
        }

        ;
    };
    long startTime = 0l;

    /**
     * 手机号输入监听
     */
    private class PhoneNumWatcher implements TextWatcher {
        long requestTime = 0l;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, final int start, int before, int count) {

            if (s.length() >= 11) {
                String phoneNo = s.toString().trim();
                if (phoneNo.length() >= 11) {
                    if (StringUtils.isMobileNO(phoneNo)) {
                        hintOtherView();
                        startUserLoginProgress();
                        startTime = System.currentTimeMillis();
                        UserLoginPresenter.decideMobileCanLogin(getActivity(), phoneNo,
                                new UserLoginPresenter.UserNameUniqueListener() {
                                    @Override
                                    public void isUnique(JSONObject msg) {
                                        isRegiest = 0;
                                        requestTime = System.currentTimeMillis() - startTime;
                                        if (requestTime > waitTime) {
                                            handler.sendMessageDelayed(handler.obtainMessage(0x00), 1000);
                                        } else {
                                            handler.sendMessageDelayed(handler.obtainMessage(0x00), waitTime - requestTime);
                                        }
                                    }

                                    @Override
                                    public void notUnique(String msg) {
                                        isRegiest = 1;
                                        if (requestTime > waitTime) {
                                            handler.sendMessageDelayed(handler.obtainMessage(0x01), 1000);
                                        } else {
                                            handler.sendMessageDelayed(handler.obtainMessage(0x01), waitTime - requestTime);
                                        }

                                    }
                                });
                    } else {
                        Toast.makeText(getActivity(), "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                        stopUserLoginProgress();
                    }
                } else {
                    Toast.makeText(getActivity(), "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                    stopUserLoginProgress();
                }
            } else {
                stopUserLoginProgress();
                hintOtherView();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private void setRegiestButtonClickable(boolean clickable) {
        if (clickable) {
            phoneLoginEnterTv.setBackgroundColor(getResources().getColor(R.color.user_login_textbg_clickable));
            phoneLoginEnterTv.setTextColor(getResources().getColor(R.color.user_login_text_clickable));
        } else {
            phoneLoginEnterTv.setBackgroundColor(getResources().getColor(R.color.user_login_text_hint_text_color));
            phoneLoginEnterTv.setTextColor(getResources().getColor(R.color.white));
        }
        phoneLoginEnterTv.setClickable(clickable);
    }


    /**
     * 获取验证码
     */
    private void getAuthCode() {
        getauthcodeLoginTv.setClickable(false);
        handler.sendMessage(handler.obtainMessage(0x02));
        UserLoginPresenter.getCaptcha(getActivity(), phoneLoginPnumEt.getText().toString().trim(), isRegiest == 0 ? UserLoginPresenter.getCaptcha_regiest : UserLoginPresenter.getCaptcha_reset, new UserLoginPresenter.UserNameUniqueListener() {
            @Override
            public void isUnique(JSONObject msg) {

            }

            @Override
            public void notUnique(String msg) {
                LogUtils.json(msg);
            }
        });
    }

    /**
     * 用户注册
     */
    private void userRegiest() {
        UserLoginPresenter.userRegiest(getActivity(), phoneLoginPnumEt.getText().toString().trim(),
                phoneLoginPasswordEt.getText().toString().trim(), userLoginNicknameEt.getText().toString().trim(),
                authcodeLoginPasswordEt.getText().toString().trim(), new UserLoginPresenter.UserNameUniqueListener() {
                    @Override
                    public void isUnique(JSONObject msg) {
                        LogUtils.json("user regiest " + msg);
                        UserInfoHelper.saveUserInfo(msg);
                        ((UserLoginActivity) getActivity()).userRegiestSuccess();

                    }

                    @Override
                    public void notUnique(String msg) {

                    }
                });
    }

    /**
     * 用户登录
     */
    private void userLogin() {
        UserLoginPresenter.userLogin(getActivity(), phoneLoginPnumEt.getText().toString().trim(),
                phoneLoginPasswordEt.getText().toString().trim(), new UserLoginListener() {
                    @Override
                    public void loginSuccess() {
                        ((UserLoginActivity) getActivity()).loginSuccess();
                    }
                });
    }

    /**
     * 重置密码
     */
    private void userResetPassword() {
        UserLoginPresenter.resetPassword(getActivity(), phoneLoginPnumEt.getText().toString().trim(),
                phoneLoginPasswordEt.getText().toString().trim(), authcodeLoginPasswordEt.getText().toString().trim(), new UserLoginPresenter.UserNameUniqueListener() {
                    @Override
                    public void isUnique(JSONObject msg) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.text_login_resetpassowrd), Toast.LENGTH_SHORT).show();
                        toLogin();
                    }

                    @Override
                    public void notUnique(String msg) {

                    }
                });
    }
}
