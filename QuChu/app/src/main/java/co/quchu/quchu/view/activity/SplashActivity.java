package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.base.Constants;
import co.quchu.quchu.model.UserInfoModel;
import co.quchu.quchu.presenter.UserLoginPresenter;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;

/**
 * SplashActivity
 * User: Chenhs
 * Date: 2015-11-10
 */
public class SplashActivity extends BaseActivity {

    @Bind(R.id.splash_root_rl)
    ImageView splashRootRl;
    private long viewDuration = 2 * 1000;
    private long visitorStartTime = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        if (Constants.ISSTARTINGPKG) {
            splashRootRl.setImageResource(R.drawable.ic_splash_bg_360);
        } else {
            splashRootRl.setImageResource(R.drawable.ic_splash_bg);
        }
        AppContext.initLocation();
        if (AppContext.user != null) {
            new EnterAppTask().execute(viewDuration);
        } else {
            visitorStartTime = System.currentTimeMillis() / 1000;
            UserLoginPresenter.visitorRegiest(this, new UserLoginPresenter.UserNameUniqueListener() {
                @Override
                public void isUnique(JSONObject msg) {
                    if ((System.currentTimeMillis() / 1000 - visitorStartTime) > viewDuration) {
                        enterApp();
                    } else {
                        new EnterAppTask().execute(viewDuration - (System.currentTimeMillis() / 1000 - visitorStartTime));
                    }
                }

                @Override
                public void notUnique(String msg) {
                }
            });
        }
    }

    @Override
    protected void onResume() {
        MobclickAgent.onPageStart("SplashActivity");
        MobclickAgent.onResume(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("SplashActivity");
        MobclickAgent.onPause(this);
    }

    public void enterApp() {
        startActivity(new Intent(this, RecommendActivity.class));
        this.finish();
    }

    /**
     * 延时进入主页
     */
    class EnterAppTask extends AsyncTask<Long, Void, Void> {

        @Override
        protected Void doInBackground(Long... params) {
            long time = params[0];
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (!StringUtils.isEmpty(SPUtils.getUserInfo(SplashActivity.this))) {
                if (AppContext.user == null)
                    AppContext.user = new Gson().fromJson(SPUtils.getUserInfo(SplashActivity.this), UserInfoModel.class);
            }
            enterApp();
        }
    }

//    protected void setIcon() {
//        if (SPUtils.getBooleanFromSPMap(this, AppKey.IS_NEED_ICON, true)) {
//            Intent shortcutintent = new Intent(
//                    "com.android.launcher.action.INSTALL_SHORTCUT");
//            // 不允许重复创建
//            shortcutintent.putExtra("duplicate", false);
//            // 需要现实的名称
//            shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
//                    getResources().getString(R.string.app_name));
//            // 快捷图片
//            Parcelable icon = Intent.ShortcutIconResource.fromContext(
//                    this.getApplicationContext(), R.mipmap.ic_launcher);
//            shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
//            // 点击快捷图片，运行的程序主入口
//            shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT,
//                    new Intent(getApplicationContext(), SplashActivity.class));
//            // 发送广播
//            sendBroadcast(shortcutintent);
//            //不允许重复创建
//            SPUtils.putBooleanToSPMap(this, AppKey.IS_NEED_ICON, false);
//        }
//    }
}
