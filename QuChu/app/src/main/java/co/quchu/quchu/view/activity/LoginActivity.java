package co.quchu.quchu.view.activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.sina.weibo.sdk.auth.sso.SsoHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.Timer;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.presenter.UserLoginPresenter;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.view.fragment.LoginByPhoneFragment;
import co.quchu.quchu.view.fragment.LoginFragment;
import co.quchu.quchu.view.fragment.PhoneValidationFragment;
import co.quchu.quchu.view.fragment.RegistrationFragment;
import co.quchu.quchu.view.fragment.RestorePasswordFragment;
import co.quchu.quchu.widget.RitalinLayout;

public class LoginActivity extends BaseActivity {


    @Bind(R.id.flContent)
    RitalinLayout flContent;

    LoginFragment loginFragment;
    FragmentManager fragmentManager;
    public SsoHandler handler;
    public long mRequestVerifyCode = -1;

    @Bind(R.id.ivClose)
    ImageView ivClose;


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
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.finish();
            }
        });

        getEnhancedToolbar().hide();

        loginFragment = new LoginFragment();
        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().add(R.id.flContent,loginFragment,LoginFragment.TAG).commit();
        getFragmentManager().executePendingTransactions();


//        if (null!= AppContext.user ){
//            UserLoginPresenter.visitorRegiest(getApplicationContext(), new UserLoginPresenter.UserNameUniqueListener() {
//                @Override
//                public void isUnique(JSONObject msg) {}
//
//                @Override
//                public void notUnique(String msg) {}
//            });
//        }

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


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onMessageEvent(QuchuEventModel event) {
        switch (event.getFlag()) {
            case EventFlags.EVENT_LOGIN_ACTIVITY_HIDE_RETURN:
                ivClose.setVisibility(View.GONE);
                break;
            case EventFlags.EVENT_LOGIN_ACTIVITY_SHOW_RETURN:
                ivClose.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ivClose.setVisibility(View.VISIBLE);
                    }
                },300);
                break;
        }
    }

}
