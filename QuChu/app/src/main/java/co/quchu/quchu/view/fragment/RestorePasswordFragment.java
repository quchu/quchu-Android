package co.quchu.quchu.view.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
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
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.presenter.UserLoginPresenter;
import co.quchu.quchu.thirdhelp.UserLoginListener;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.activity.RecommendActivity;
import co.quchu.quchu.widget.ErrorView;

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

    @Bind(R.id.ivIconPassword)
    ImageView ivIconPassword;
    @Bind(R.id.etPassword)
    EditText etPassword;
    @Bind(R.id.rlPasswordField)
    RelativeLayout rlPasswordField;
    @Bind(R.id.tvTips)
    TextView tvTips;
    @Bind(R.id.tvNext)
    TextView tvNext;


    @Override
    public void onResume() {
        super.onResume();
        ((BaseActivity)getActivity()).getEnhancedToolbar().getTitleTv().setText(R.string.forget_pwd_step_2);

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restore_password, container, false);
        ButterKnife.bind(this, view);

        if (null!=getArguments()){
            mVerifyCode = getArguments().getString(BUNDLE_KEY_VERIFY_CODE);
            mUserName = getArguments().getString(BUNDLE_KEY_USERNAME);
        }

        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mRequestRunning){
                    return;
                }
                mRequestRunning = true;
                etPassword.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (null==etPassword.getText()||StringUtils.isEmpty(etPassword.getText().toString())){
                            tvNext.setText(R.string.next);
                            tvNext.setBackgroundColor(getResources().getColor(R.color.standard_color_black));
                        }else {
                            tvNext.setText(R.string.next);
                            tvNext.setBackgroundColor(getResources().getColor(R.color.standard_color_yellow));
                        }
                    }
                });
                if (null!=etPassword.getText() && StringUtils.isGoodPassword(etPassword.getText().toString())){
                    UserLoginPresenter.resetPassword(getActivity(), mUserName, etPassword.getText().toString(), mVerifyCode, new UserLoginPresenter.UserNameUniqueListener() {
                        @Override
                        public void isUnique(JSONObject msg) {
                            UserLoginPresenter.userLogin(getActivity(), mUserName, etPassword.getText().toString(), new UserLoginListener() {
                                @Override
                                public void loginSuccess(int type, String token, String appId) {
                                    Toast.makeText(getActivity(),R.string.promote_password_update_success,Toast.LENGTH_SHORT).show();
                                    SPUtils.putLoginType(SPUtils.LOGIN_TYPE_PHONE);
                                    MobclickAgent.onProfileSignIn("loginphone_c", AppContext.user.getUserId() + "");
                                    getActivity().startActivity(new Intent(getActivity(), RecommendActivity.class));
                                    getActivity().finish();
                                    mRequestRunning = false;
                                }

                                @Override
                                public void loginFail(String errorMsg) {
                                    mRequestRunning = false;
                                    Toast.makeText(getActivity(),R.string.promote_password_update_success_login_manually,Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void notUnique(String msg) {
                            mRequestRunning = false;
                            Toast.makeText(getActivity(),R.string.promote_password_update_failure,Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    tvNext.setText(R.string.hint_new_password);
                    tvNext.setBackgroundColor(getResources().getColor(R.color.standard_color_red));
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
}
