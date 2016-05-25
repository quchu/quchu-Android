package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.presenter.UserLoginPresenter;
import co.quchu.quchu.thirdhelp.UserLoginListener;
import co.quchu.quchu.thirdhelp.WechatHelper;
import co.quchu.quchu.thirdhelp.WeiboHelper;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.utils.KeyboardUtils;
import co.quchu.quchu.view.fragment.PhoneLoginFragment;
import co.quchu.quchu.view.fragment.UserLoginMainFragment;

/**
 * UserLoginActivity
 * User: Chenhs
 * Date: 2015-11-25
 * 用户登录界面
 */
public class UserLoginActivity extends BaseActivity implements UserLoginListener {

    private FragmentTransaction transaction;
    private SsoHandler handler;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        IsVisitorLogin = getIntent().getBooleanExtra("IsVisitorLogin", false);
        transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.user_login_fl, new UserLoginMainFragment());
        transaction.commitAllowingStateLoss();
    }

    @Override
    protected int activitySetup() {
        return 0;
    }

    private boolean IsVisitorLogin = false;


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("LoginActivity");
    }

    @Override
    protected void onResume() {

        if (null != AppContext.user) {
            if (!IsVisitorLogin) {
                if (!AppContext.user.isIsVisitors()) {
                    enterApp();
                }
                EventBus.getDefault().post(new QuchuEventModel(EventFlags.EVENT_USER_LOGIN_SUCCESS));
            }
        } else {
            UserLoginPresenter.visitorRegiest(this, null);
        }
        super.onResume();
        MobclickAgent.onPageStart("LoginActivity");
    }


    public void mobileNoLogin() {
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.user_login_fl, new PhoneLoginFragment());
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    public void sinaLogin() {
        MobclickAgent.onEvent(this, "pop_loginweibo_c");
        WeiboHelper instance = WeiboHelper.getInstance(this);
        handler = new SsoHandler(this, instance.getmAuthInfo());
        instance.weiboLogin(handler, this, true);
    }

    public void weixinLogin() {
        MobclickAgent.onEvent(this, "pop_ loginwechat_c");

        WechatHelper.getInstance(this).login(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (handler != null)
            handler.authorizeCallBack(requestCode, resultCode, data);
    }

    @Override
    public void loginSuccess(int type, String token, String appId) {
        enterApp();
        EventBus.getDefault().post(new QuchuEventModel(EventFlags.EVENT_USER_LOGIN_SUCCESS));
    }

    @Override
    public void loginFail(String message) {

    }

    public void userRegiestSuccess() {
        startActivity(new Intent(this, RecommendActivity.class));
//        SPUtils.initGuideIndex();
        KeyboardUtils.closeBoard(this, findViewById(R.id.user_login_fl));
        EventBus.getDefault().post(new QuchuEventModel(EventFlags.EVENT_USER_LOGIN_SUCCESS));
        finish();
    }

    public void enterApp() {
        startActivity(new Intent(this, RecommendActivity.class));

        Toast.makeText(getApplicationContext(), R.string.login_success, Toast.LENGTH_SHORT).show();
    }


}
