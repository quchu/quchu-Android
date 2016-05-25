package co.quchu.quchu.view.fragment;

import android.app.Fragment;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sina.weibo.sdk.utils.MD5;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
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

    public static final String TAG = "LoginByPhoneFragment";
    public boolean mDisplayPassword = false;

    public boolean updateButtonStatus(){

        if (null==etUsername ||null==etPassword){
            return false;
        }

        boolean status = false;
        String userName = null==etUsername.getText()?"":etUsername.getText().toString();
        String userPwd = null == etPassword.getText()?"":etPassword.getText().toString();
        ivIconClear.setVisibility(userName.length()>0?View.VISIBLE:View.INVISIBLE);
        if (TextUtils.isEmpty(userName)&& TextUtils.isEmpty(userPwd)){
            tvLoginViaPhone.setText(R.string.promote_empty_username_n_password);
            tvLoginViaPhone.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }else if(TextUtils.isEmpty(userName) || TextUtils.isEmpty(userPwd)){
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone_login, container, false);
        ButterKnife.bind(this, view);
        etUsername.setOnFocusChangeListener(this);
        etPassword.setOnFocusChangeListener(this);
        etUsername.addTextChangedListener(this);
        etPassword.addTextChangedListener(this);

        tvLoginViaPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(updateButtonStatus()) {
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

    /**
     * 用户登录
     */
    private void userLogin(String userName,String password) {
        errorView.showLoading();
        UserLoginPresenter.userLogin(getActivity(), userName,MD5.hexdigest(password), new UserLoginListener() {
                    @Override
                    public void loginSuccess(int type, String token, String appId) {
                        SPUtils.putLoginType(SPUtils.LOGIN_TYPE_PHONE);
                        MobclickAgent.onProfileSignIn("loginphone_c", AppContext.user.getUserId() + "");
                        getActivity().startActivity(new Intent(getActivity(), RecommendActivity.class));
                        getActivity().finish();
                        errorView.himeView();
                    }

            @Override
            public void loginFail(String message) {
                if (!TextUtils.isEmpty(message)){
                    try {
                        JSONObject object = new JSONObject(message);
                        if (object.has("msg") && !object.isNull("msg")){
                            tvLoginViaPhone.setText(object.get("msg").toString());
                            tvLoginViaPhone.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                errorView.himeView();
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
