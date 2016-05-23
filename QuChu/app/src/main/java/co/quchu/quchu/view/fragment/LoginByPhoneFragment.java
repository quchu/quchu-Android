package co.quchu.quchu.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.utils.MD5;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseFragment;
import co.quchu.quchu.presenter.UserLoginPresenter;
import co.quchu.quchu.thirdhelp.UserLoginListener;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.activity.UserLoginActivity;

/**
 * Created by Nico on 16/5/13.
 */
public class LoginByPhoneFragment extends Fragment {
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

    public static final String TAG = "LoginByPhoneFragment";
    public boolean mDisplayPassword = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone_login, container, false);
        ButterKnife.bind(this, view);
        rlUserNameField.setTranslationY(200);
        rlPasswordField.setTranslationY(200);
        etUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && null!=etUsername){
                    ivIconClear.setVisibility(etUsername.getText().length()>0?View.VISIBLE:View.INVISIBLE);
                    if (!StringUtils.isMobileNO(etUsername.getText().toString())){
                        tvLoginViaPhone.setText(R.string.promote_invalid_username);
                        tvLoginViaPhone.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    }else if(StringUtils.isMobileNO(etUsername.getText().toString()) && StringUtils.isGoodPassword(etPassword.getText().toString())){
                        tvLoginViaPhone.setBackgroundColor(getResources().getColor(R.color.standard_color_yellow));
                        tvLoginViaPhone.setText(R.string.login);
                    }else{
                        restoreLoginButton();
                    }
                }
            }
        });
        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && null!=etPassword){
                    if (!StringUtils.isGoodPassword(etPassword.getText().toString())){
                        tvLoginViaPhone.setText(R.string.promote_invalid_password);
                        tvLoginViaPhone.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    }else if(StringUtils.isMobileNO(etUsername.getText().toString()) && StringUtils.isGoodPassword(etPassword.getText().toString())){
                        tvLoginViaPhone.setBackgroundColor(getResources().getColor(R.color.standard_color_yellow));
                        tvLoginViaPhone.setText(R.string.login);
                    }else{
                        restoreLoginButton();
                    }
                }
            }
        });

        tvLoginViaPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

    /**
     * 用户登录
     */
    private void userLogin(String userName,String password) {
        UserLoginPresenter.userLogin(getActivity(), userName,MD5.hexdigest(password), new UserLoginListener() {
                    @Override
                    public void loginSuccess(int type, String token, String appId) {
                        SPUtils.putLoginType(SPUtils.LOGIN_TYPE_PHONE);
                        MobclickAgent.onProfileSignIn("loginphone_c", AppContext.user.getUserId() + "");
                        ((UserLoginActivity) getActivity()).loginSuccess(type, token, appId);
                    }
                });
    }

    private void restoreLoginButton() {
        tvLoginViaPhone.setText(R.string.login);
        tvLoginViaPhone.setBackgroundColor(getResources().getColor(R.color.standard_color_black));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
