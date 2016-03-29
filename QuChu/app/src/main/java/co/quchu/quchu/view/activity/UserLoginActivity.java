package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

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
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.view.fragment.PhoneLoginFragment;
import co.quchu.quchu.view.fragment.UserGuideFragment;
import co.quchu.quchu.view.fragment.UserLoginMainFragment;

/**
 * UserLoginActivity
 * User: Chenhs
 * Date: 2015-11-25
 * 用户登录界面
 */
public class UserLoginActivity extends BaseActivity implements UserLoginListener {

    private FragmentTransaction transaction;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* if (!StringUtils.isEmpty(SPUtils.getUserInfo(this))) {
            if (AppContext.user == null)
                AppContext.user = new Gson().fromJson(SPUtils.getUserInfo(this), UserInfoModel.class);
            enterApp();
        } else {*/
        setContentView(R.layout.activity_user_login);
        IsVisitorLogin = getIntent().getBooleanExtra("IsVisitorLogin", false);
        transaction = getSupportFragmentManager().beginTransaction();
         /*       transaction.setCustomAnimations(R.anim.in_push_right_to_left,R.anim.out_push_left_to_right);*/
        // transaction.replace(R.id.user_login_fl, new UserGuideFragment());

        transaction.replace(R.id.user_login_fl, new UserLoginMainFragment());
             /*   transaction.addToBackStack(null);*/
        transaction.commitAllowingStateLoss();
     /*   }*/
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
            if (!IsVisitorLogin)
                loginSuccess();
        } else {
            UserLoginPresenter.visitorRegiest(this, null);
        }
        super.onResume();
        MobclickAgent.onPageStart("LoginActivity");
    }


    public void mobileNoLogin() {
      /*  transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.user_login_fl, new UserLoginMainFragment());
        transaction.commit();*/
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.user_login_fl, new PhoneLoginFragment());
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    public void sinaLogin() {
        new WeiboHelper(this, this).weiboLogin(this);
    }

    public void weixinLogin() {
        new WechatHelper(this, this).login();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult
        //   LogUtils.json("requestCode=" + requestCode + "///resultCode=" + resultCode + "data=" + data);
        if (WeiboHelper.mSsoHandler != null) {
            WeiboHelper.mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    @Override
    public void loginSuccess() {
        if (AppContext.user != null && AppContext.user.isIsVisitors()) {
        } else {
            enterApp();
        }
        EventBus.getDefault().post(new QuchuEventModel(EventFlags.EVENT_USER_LOGIN_SUCCESS,null));
    }

    public void userRegiestSuccess() {
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.user_login_fl, new UserGuideFragment());
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
        SPUtils.initGuideIndex();
        KeyboardUtils.closeBoard(this, findViewById(R.id.user_login_fl));
        EventBus.getDefault().post(new QuchuEventModel(EventFlags.EVENT_USER_LOGIN_SUCCESS,null));
    }

    public void enterApp() {
        //   startActivity(new Intent(this, RecommendActivity.class));

        Toast.makeText(getApplicationContext(),R.string.login_success,Toast.LENGTH_SHORT).show();
        this.finish();
    }

    /**
     * 进入引导
     */
    public void starGuideView() {
        startActivity(new Intent(this, PlanetActivity.class));
    }


}
