package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextPaint;
import android.widget.FrameLayout;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        getEnhancedToolbar().hide();

    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_NOTHING;
    }
}
