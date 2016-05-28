package co.quchu.quchu.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.UserLoginPresenter;
import co.quchu.quchu.utils.StringUtils;
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
    @Bind(R.id.errorView)
    ErrorView errorView;
    public static final String TAG = "PhoneValidationFragment";
    private boolean mEmptyForum = false;private long mRequestTimeStamp = -1;
    private Timer mCountingTimer;
    private boolean mIsRegistration = true;
    private int mVCRequestTime = 1;
    public static final String BUNDLE_KEY_REGISTRATION = "BUNDLE_KEY_REGISTRATION";
    private int mContainerId = -1;

    public void updateButtonStatus(){
        if (null==etUsername){
            return;
        }
        String userName = null==etUsername.getText()?"":etUsername.getText().toString();
        ivIconClear.setVisibility(userName.length()>0?View.VISIBLE:View.INVISIBLE);
        if (!TextUtils.isEmpty(userName)){
            mEmptyForum = false;
            tvNext.setBackgroundColor(getResources().getColor(R.color.standard_color_yellow));
        }else{
            mEmptyForum = true;
            tvNext.setBackgroundColor(getResources().getColor(R.color.standard_color_black));
        }
    }

    private boolean verifyForm() {
        boolean status = false;
        String userName = etUsername.getText().toString();

        if(TextUtils.isEmpty(userName) ){
            tvNext.setText(R.string.promote_empty_username);
            tvNext.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }else if (!StringUtils.isMobileNO(userName)){
            tvNext.setText(R.string.promote_invalid_username);
            tvNext.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }else if(StringUtils.isMobileNO(userName) ){
            tvNext.setBackgroundColor(getResources().getColor(R.color.standard_color_yellow));
            status = true;
        }else{
            tvNext.setBackgroundColor(getResources().getColor(R.color.standard_color_black));
        }
        return status;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_validation_phone, container, false);
        ButterKnife.bind(this, view);

        if (null!=getArguments()){
            mIsRegistration = getArguments().getBoolean(BUNDLE_KEY_REGISTRATION,true);
        }
        ((BaseActivity)getActivity()).getEnhancedToolbar().getTitleTv().setText(mIsRegistration?R.string.registration:R.string.reset_password);

        ivIconClear.setVisibility(View.INVISIBLE);
        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

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

        tvSendValidCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mEmptyForum &&verifyForm()){
                    getValidCode();
                }
            }
        });
        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContainerId = mContainerId == -1? ((ViewGroup)getView().getParent()).getId():mContainerId;
                if (mVCRequestTime>0){
                    verifySms();
                }
            }
        });
        return view;
    }

    boolean isVerifying = false;
    private void verifySms(){
        if (isVerifying){
            return;
        }
        isVerifying = true;
        UserLoginPresenter.verifyNext(getActivity(), etUsername.getText().toString(), etValidCode.getText().toString(), new CommonListener() {
            @Override
            public void successListener(Object response) {
                Toast.makeText(getActivity(),R.string.promote_verify_pass,Toast.LENGTH_SHORT).show();
                if (mIsRegistration){
                    RegistrationFragment registrationFragment = new RegistrationFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(RegistrationFragment.BUNDLE_KEY_USERNAME,etUsername.getText().toString());
                    bundle.putString(RegistrationFragment.BUNDLE_KEY_VERIFY_CODE,etValidCode.getText().toString());

                    registrationFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().setCustomAnimations(
                            R.animator.card_flip_horizontal_right_in,
                            R.animator.card_flip_horizontal_left_out,
                            R.animator.card_flip_horizontal_left_in,
                            R.animator.card_flip_horizontal_right_out)
                            .replace(mContainerId,registrationFragment)
                            .addToBackStack(TAG)
                            .commitAllowingStateLoss();
                    getFragmentManager().executePendingTransactions();
                    ((BaseActivity)getActivity()).getEnhancedToolbar().show();
                }else{
                    RestorePasswordFragment restorePasswordFragment = new RestorePasswordFragment();
                    Bundle bundleRestorePwd = new Bundle();
                    bundleRestorePwd.putString(RestorePasswordFragment.BUNDLE_KEY_USERNAME,etUsername.getText().toString());
                    bundleRestorePwd.putString(RestorePasswordFragment.BUNDLE_KEY_VERIFY_CODE,etValidCode.getText().toString());
                    restorePasswordFragment.setArguments(bundleRestorePwd);
                    getFragmentManager().beginTransaction().setCustomAnimations(
                            R.animator.card_flip_horizontal_right_in,
                            R.animator.card_flip_horizontal_left_out,
                            R.animator.card_flip_horizontal_left_in,
                            R.animator.card_flip_horizontal_right_out)
                            .replace(mContainerId,restorePasswordFragment)
                            .addToBackStack(TAG)
                            .commitAllowingStateLoss();
                    getFragmentManager().executePendingTransactions();
                    ((BaseActivity)getActivity()).getEnhancedToolbar().show();
                }
                isVerifying = false;
            }

            @Override
            public void errorListener(VolleyError error, String exception, String msg) {
                Toast.makeText(getActivity(),R.string.promote_verify_pass,Toast.LENGTH_SHORT).show();
                isVerifying = false;
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        ((BaseActivity)getActivity()).getEnhancedToolbar().getTitleTv().setText(mIsRegistration?R.string.registration:R.string.reset_password);
    }

    private boolean isRunning = false;
    private void getValidCode(){
        if (isRunning){
            return;
        }
        isRunning = true;

        UserLoginPresenter.decideMobileCanLogin(getActivity(), etUsername.getText().toString(), new UserLoginPresenter.UserNameUniqueListener() {
            @Override
            public void isUnique(JSONObject msg) {
                isRunning = false;
                if (mSecs >0){
                    return;
                }
                errorView.showLoading();

                UserLoginPresenter.requestVerifySms(getActivity(), etUsername.getText().toString(),mIsRegistration?UserLoginPresenter.getCaptcha_regiest:UserLoginPresenter.getCaptcha_reset, new UserLoginPresenter.UserNameUniqueListener() {
                    @Override
                    public void isUnique(JSONObject msg) {
                        errorView.hideView();
                        scheduleCountDownTask();
                        mVCRequestTime+=1;
                    }

                    @Override
                    public void notUnique(String msg) {
                        tvNext.setText("用户名已被占用,请尝试其他账号");
                        errorView.hideView();
                        scheduleCountDownTask();
                        mVCRequestTime+=1;
                    }
                });
            }

            @Override
            public void notUnique(String msg) {
                isRunning = false;
                Toast.makeText(getActivity(),R.string.promote_duplicate_username,Toast.LENGTH_SHORT).show();
            }
        });


    }


    private Handler mCountDownHandler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (null == tvSendValidCode){
                return;
            }
            if (mSecs<=0){
                tvSendValidCode.setText(R.string.send_valid_code);
            }else{
                tvSendValidCode.setText("("+mSecs+")秒后重新发送");
            }
        }
    };

    private void scheduleCountDownTask() {
        mSecs = 60;
        mCountingTimer = new Timer();
        mCountingTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mSecs<=0){
                    mCountingTimer.cancel();
                }
                mSecs-=1;
                mCountDownHandler.sendEmptyMessage(0);
            }
        },10,1000);
    }

    private int mSecs = 0;


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
