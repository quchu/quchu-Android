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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.presenter.UserLoginPresenter;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.activity.RecommendActivity;

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

    @Bind(R.id.ivIconUserName)
    ImageView ivIconUserName;
    @Bind(R.id.etUsername)
    EditText etUsername;
    @Bind(R.id.rlUserNameField)
    RelativeLayout rlUserNameField;
    @Bind(R.id.ivIconPassword)
    ImageView ivIconPassword;
    @Bind(R.id.etPassword)
    EditText etPassword;
    @Bind(R.id.rlPasswordField)
    RelativeLayout rlPasswordField;
    @Bind(R.id.tvLoginViaPhone)
    TextView tvLoginViaPhone;
    private boolean mEmptyForum = false;


    private boolean verifyForm() {
        boolean status = false;

        String userName = etUsername.getText().toString();
        String userPwd = etPassword.getText().toString();

        if(TextUtils.isEmpty(userName) || TextUtils.isEmpty(userPwd)){
            tvLoginViaPhone.setText(R.string.promote_empty_username_or_password);
            tvLoginViaPhone.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }else if (!StringUtils.isGoodPassword(userPwd)){
            tvLoginViaPhone.setText(R.string.promote_invalid_password);
            tvLoginViaPhone.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }else if(StringUtils.isGoodPassword(userPwd)){
            tvLoginViaPhone.setBackgroundColor(getResources().getColor(R.color.standard_color_yellow));
            tvLoginViaPhone.setText(R.string.next);
            status = true;
        }else{
            tvLoginViaPhone.setText(R.string.next);
            tvLoginViaPhone.setBackgroundColor(getResources().getColor(R.color.standard_color_black));
        }
        return status;
    }

    public void updateButtonStatus(){

        if (null==etUsername ||null==etPassword){
            return ;
        }
        String userName = null==etUsername.getText()?"":etUsername.getText().toString();
        String userPwd = null == etPassword.getText()?"":etPassword.getText().toString();
        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(userPwd)){
            tvLoginViaPhone.setText(R.string.next);
            mEmptyForum = false;
            tvLoginViaPhone.setBackgroundColor(getResources().getColor(R.color.standard_color_yellow));
        }else{
            mEmptyForum = true;
            tvLoginViaPhone.setText(R.string.next);
            tvLoginViaPhone.setBackgroundColor(getResources().getColor(R.color.standard_color_black));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        ButterKnife.bind(this, view);

        if (null!=getArguments()){
            mVerifyCode = getArguments().getString(BUNDLE_KEY_VERIFY_CODE);
            mUserName = getArguments().getString(BUNDLE_KEY_USERNAME);
            System.out.println("mUserName"+mUserName);
            System.out.println("mUserName"+mVerifyCode);
        }
        etUsername.postDelayed(new Runnable() {
            @Override
            public void run() {
                etUsername.requestFocus();
            }
        },30);
        etUsername.setOnFocusChangeListener(this);
        etPassword.setOnFocusChangeListener(this);
        etUsername.addTextChangedListener(this);
        etPassword.addTextChangedListener(this);
        tvLoginViaPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("1");
                if(!mEmptyForum&&verifyForm()) {
                    System.out.println("2");

                    if (mRequestRunning){
                        return;
                    }
                    mRequestRunning = true;
                    if (null!=AppContext.user && AppContext.user.isIsVisitors()){
                        System.out.println("3");

                        int visitorUid = AppContext.user.getUserId();
                        String pwd = etPassword.getText().toString();
                        String nickName = etUsername.getText().toString();

                        UserLoginPresenter.userRegiest(getActivity(), visitorUid, mUserName, pwd, nickName, mVerifyCode, new UserLoginPresenter.UserNameUniqueListener() {
                            @Override
                            public void isUnique(JSONObject msg) {

                                Toast.makeText(getActivity(),R.string.promote_account_create_success,Toast.LENGTH_SHORT).show();
                                SPUtils.putLoginType(SPUtils.LOGIN_TYPE_PHONE);
                                getActivity().startActivity(new Intent(getActivity(), RecommendActivity.class));
                                getActivity().finish();
                            }

                            @Override
                            public void notUnique(String msg) {
                                Toast.makeText(getActivity(),R.string.promote_account_create_success_login_manually,Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });

        return view;
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
