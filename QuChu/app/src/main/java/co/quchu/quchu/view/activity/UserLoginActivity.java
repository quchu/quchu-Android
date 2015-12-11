package co.quchu.quchu.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.gson.Gson;

import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.model.UserInfoModel;
import co.quchu.quchu.thirdhelp.UserLoginListener;
import co.quchu.quchu.thirdhelp.WechatHelper;
import co.quchu.quchu.thirdhelp.WeiboHelper;
import co.quchu.quchu.utils.KeyboardUtils;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.fragment.PhoneLoginFragment;
import co.quchu.quchu.view.fragment.UserEnterAppFragment;
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
        if (!StringUtils.isEmpty(SPUtils.getUserInfo(this))) {
            if (AppContext.user == null)
                AppContext.user = new Gson().fromJson(SPUtils.getUserInfo(this), UserInfoModel.class);
            enterApp();
        } else {
            setContentView(R.layout.activity_user_login);
            transaction = getSupportFragmentManager().beginTransaction();
         /*       transaction.setCustomAnimations(R.anim.in_push_right_to_left,R.anim.out_push_left_to_right);*/
            transaction.replace(R.id.user_login_fl, new UserLoginMainFragment());
             /*   transaction.addToBackStack(null);*/
            transaction.commit();

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (null != AppContext.user)
            loginSuccess();
        super.onResume();
    }


    public void mobileNoLogin() {
      /*  transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.user_login_fl, new UserLoginMainFragment());
        transaction.commit();*/
        Toast.makeText(this, "selected Phone", 0).show();
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.user_login_fl, new PhoneLoginFragment());
        transaction.addToBackStack(null);
        transaction.commit();
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
        LogUtils.json("requestCode="+requestCode+"///resultCode="+resultCode+"data="+data);
        if (WeiboHelper.mSsoHandler != null) {
            WeiboHelper.mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    @Override
    public void loginSuccess() {
        LogUtils.json("login success");
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.user_login_fl, new UserEnterAppFragment());
        transaction.addToBackStack(null);
        transaction.commit();
        KeyboardUtils.closeBoard(this, findViewById(R.id.user_login_fl));
    }

    public void enterApp() {
        startActivity(new Intent(this, RecommendActivity.class));
        this.finish();
    }

}
