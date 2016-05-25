package co.quchu.quchu.view.activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.sina.weibo.sdk.auth.sso.SsoHandler;

import java.util.Timer;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.view.fragment.LoginByPhoneFragment;
import co.quchu.quchu.view.fragment.LoginFragment;
import co.quchu.quchu.view.fragment.PhoneValidationFragment;
import co.quchu.quchu.view.fragment.RegistrationFragment;
import co.quchu.quchu.view.fragment.RestorePasswordFragment;

public class LoginActivity extends BaseActivity {


    @Bind(R.id.flContent)
    FrameLayout flContent;

    LoginByPhoneFragment loginByPhoneFragment;
    LoginFragment loginFragment;
    PhoneValidationFragment phoneValidationFragment;
    RegistrationFragment registrationFragment;
    RestorePasswordFragment restorePasswordFragment;
    FragmentManager fragmentManager;
    public SsoHandler handler;
    public long mRequestVerifyCode = -1;

    public void updateRequestVerifyCode(){
        mRequestVerifyCode = System.currentTimeMillis();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (handler != null)
            handler.authorizeCallBack(requestCode, resultCode, data);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        getEnhancedToolbar().hide();

        loginFragment = new LoginFragment();
        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().add(R.id.flContent,loginFragment,LoginFragment.TAG).commit();
        getFragmentManager().executePendingTransactions();


    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0 ) {
            getFragmentManager().popBackStack();
        }else{
            super.onBackPressed();
        }

    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }
}
