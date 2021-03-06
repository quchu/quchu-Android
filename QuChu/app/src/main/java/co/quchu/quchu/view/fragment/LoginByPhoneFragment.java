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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.net.NetUtil;
import co.quchu.quchu.presenter.UserLoginPresenter;
import co.quchu.quchu.thirdhelp.UserLoginListener;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.activity.RecommendActivity;
import co.quchu.quchu.widget.ErrorView;


/**
 * Created by Nico on 16/5/13.
 */
public class LoginByPhoneFragment extends Fragment implements TextWatcher, View.OnFocusChangeListener {
    @Bind(R.id.ivIconUserName)
    ImageView ivIconUserName;
    @Bind(R.id.etUsername)
    EditText etUsername;
    @Bind(R.id.ivIconClear)
    ImageView ivIconClear;
    @Bind(R.id.rlUserNameField)
    RelativeLayout rlUserNameField;
    @Bind(R.id.ivIconPassword)
    ImageView ivIconPassword;
    @Bind(R.id.etPassword)
    EditText etPassword;
    @Bind(R.id.ivSwitchVisible)
    ImageView ivSwitchVisible;
    @Bind(R.id.tvLoginViaPhone)
    TextView tvLoginViaPhone;
    @Bind(R.id.rlPasswordField)
    RelativeLayout rlPasswordField;
    @Bind(R.id.errorView)
    ErrorView errorView;
    @Bind(R.id.tvForgetPassword)
    TextView tvForgetPassword;
    private boolean mEmptyForum = false;

    public static final String TAG = "LoginByPhoneFragment";
    public static final String BUNDLE_KEY_PHONE_NUMBER = "BUNDLE_KEY_PHONE_NUMBER";
    public String mPhoneNumber = "";


    public boolean mDisplayPassword = false;

    public void updateButtonStatus(){

        if (null==etUsername ||null==etPassword){
            return ;
        }
        String userName = null==etUsername.getText()?"":etUsername.getText().toString();
        String userPwd = null == etPassword.getText()?"":etPassword.getText().toString();

        ivIconClear.setVisibility(userName.length()>0?View.VISIBLE:View.INVISIBLE);
        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(userPwd)){
            tvLoginViaPhone.setText(R.string.login);
            mEmptyForum = false;
            tvLoginViaPhone.setBackgroundColor(getResources().getColor(R.color.standard_color_yellow));
        }else{
            mEmptyForum = true;
            tvLoginViaPhone.setText(R.string.login);
            tvLoginViaPhone.setBackgroundColor(getResources().getColor(R.color.standard_color_black));
        }
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone_login, container, false);
        ButterKnife.bind(this, view);
        if (null!=getArguments()){
            mPhoneNumber = getArguments().getString(BUNDLE_KEY_PHONE_NUMBER);
        }
        if (StringUtils.isMobileNO(mPhoneNumber)){
            etUsername.setText(mPhoneNumber);
        }
        etUsername.setOnFocusChangeListener(this);
        etPassword.setOnFocusChangeListener(this);
        etUsername.addTextChangedListener(this);
        etPassword.addTextChangedListener(this);

        tvLoginViaPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mEmptyForum&&verifyForm()) {
                    userLogin(etUsername.getText().toString(),etPassword.getText().toString());
                }
            }
        });


        ivIconClear.setVisibility(View.INVISIBLE);
        ivIconClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etUsername.setText("");
            }
        });
        ivSwitchVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mDisplayPassword){
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else{
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                mDisplayPassword = !mDisplayPassword;
            }
        });

        //rlUserNameField.animate().translationY(200).setDuration(500).setInterpolator(new AccelerateDecelerateInterpolator()).start();
        //rlPasswordField.animate().translationY(200).setDuration(500).setInterpolator(new AccelerateDecelerateInterpolator()).start();
        return view;
    }

    private boolean verifyForm() {
        boolean status = false;

        String userName = etUsername.getText().toString();
        String userPwd = etPassword.getText().toString();

        if(TextUtils.isEmpty(userName) || TextUtils.isEmpty(userPwd)){
            tvLoginViaPhone.setText(R.string.promote_empty_username_or_password);
            tvLoginViaPhone.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }else if (!StringUtils.isMobileNO(userName)){
            tvLoginViaPhone.setText(R.string.promote_invalid_username);
            tvLoginViaPhone.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }else if (!StringUtils.isGoodPassword(userPwd)){
            tvLoginViaPhone.setText(R.string.promote_invalid_password);
            tvLoginViaPhone.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }else if(StringUtils.isMobileNO(userName) && StringUtils.isGoodPassword(userPwd)){
            tvLoginViaPhone.setBackgroundColor(getResources().getColor(R.color.standard_color_yellow));
            tvLoginViaPhone.setText(R.string.login);
            status = true;
        }else{
            tvLoginViaPhone.setText(R.string.login);
            tvLoginViaPhone.setBackgroundColor(getResources().getColor(R.color.standard_color_black));
        }
        return status;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((BaseActivity)getActivity()).getEnhancedToolbar().getTitleTv().setText(R.string.login_via_phone);

        etUsername.requestFocus();
        etUsername.postDelayed(new Runnable() {

            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(etUsername, 0);
            }
        },350);
    }

    @Override
    public void onPause() {
        super.onPause();
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 用户登录
     */
    private void userLogin(String userName,String password) {

        if (!NetUtil.isNetworkConnected(getActivity())){
            Toast.makeText(getActivity(),R.string.network_error,Toast.LENGTH_SHORT).show();
            return;
        }
        errorView.showLoading();
        UserLoginPresenter.userLogin(getActivity(), userName,password, new UserLoginListener() {
                    @Override
                    public void loginSuccess(int type, String token, String appId) {
                        SPUtils.putLoginType(SPUtils.LOGIN_TYPE_PHONE);
                        MobclickAgent.onProfileSignIn("loginphone_c", AppContext.user.getUserId() + "");
                        getActivity().startActivity(new Intent(getActivity(), RecommendActivity.class).putExtra(RecommendActivity.REQUEST_KEY_FROM_LOGIN,true));
                        getActivity().finish();
                        errorView.hideView();
                    }

            @Override
            public void loginFail(String message) {
                if (!TextUtils.isEmpty(message)){
                    try {
                        JSONObject object = new JSONObject(message);
                        if (object.has("msg") && !object.isNull("msg")){
                            tvLoginViaPhone.setText(object.get("msg").toString());
                            tvLoginViaPhone.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                            tvForgetPassword.setVisibility(View.VISIBLE);
                            tvForgetPassword.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Fragment f = new PhoneValidationFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putBoolean(PhoneValidationFragment.BUNDLE_KEY_REGISTRATION,false);
                                    bundle.putString(PhoneValidationFragment.BUNDLE_KEY_PHONE_NUMBER,etUsername.getText().toString());
                                    f.setArguments(bundle);
                                    getFragmentManager().beginTransaction()
                                            .replace(R.id.flContent,f)
                                            .addToBackStack(TAG)
                                            .commitAllowingStateLoss();
                                    getFragmentManager().executePendingTransactions();
                                    ((BaseActivity)getActivity()).getEnhancedToolbar().show();
                                }
                            });
                        }
                        //TODO
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                errorView.hideView();
            }

        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        updateButtonStatus();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        updateButtonStatus();
    }
}
