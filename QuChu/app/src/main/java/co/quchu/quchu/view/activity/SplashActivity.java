package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.igexin.sdk.PushManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.ConfirmDialogFg;
import co.quchu.quchu.model.CityModel;
import co.quchu.quchu.model.UserInfoModel;
import co.quchu.quchu.presenter.RecommendPresenter;
import co.quchu.quchu.presenter.UserLoginPresenter;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;

/**
 * SplashActivity
 * User: Chenhs
 * Date: 2015-11-10
 */
public class SplashActivity extends BaseActivity {

    private long viewDuration = 2 * 1000;
    private long visitorStartTime = 0L;
    @Bind(R.id.ivAppIcon)
    ImageView mIvAppIcon;
    @Bind(R.id.tvAppName)
    TextView mTvAppName;
    @Bind(R.id.tvCopyRight)
    TextView mTvCopyRight;
    @Bind(R.id.tvVersion)
    TextView mTvVersion;
    @Bind(R.id.vBg)
    View mViewBg;

    private int mHalfWidth = -1;
    boolean mAnimationEnd = false;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }


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

        setContentView(R.layout.activity_landing_page);
        AppContext.initLocation();
        ButterKnife.bind(this);

        mTvVersion.setText(getVersionName());
        mIvAppIcon.setImageResource(R.mipmap.ic_user_loginview_logo);


//        final AnimatorSet animatorSetText = new AnimatorSet();
//        animatorSetText.playTogether(
//                ObjectAnimator.ofFloat(mIvAppIcon, "alpha", 0, 1),
//                ObjectAnimator.ofFloat(mTvAppName, "translationY", mTvAppName.getTranslationY() / 1.4f, 0),
//                ObjectAnimator.ofFloat(mTvCopyRight, "translationX", mHalfWidth, 0),
//                ObjectAnimator.ofFloat(mTvVersion, "translationY", mTvVersion.getTranslationY(), 0),
//                ObjectAnimator.ofFloat(mTvAppName, "alpha", 0, 1),
//                ObjectAnimator.ofFloat(mTvCopyRight, "alpha", 0, 1),
//                ObjectAnimator.ofFloat(mTvVersion, "alpha", 0, 1)
//        );
//        animatorSetText.setDuration(500);
//        animatorSetText.setStartDelay(500);
//        animatorSetText.setInterpolator(new AccelerateDecelerateInterpolator());
        //animatorSetText.start();
        mIvAppIcon.setVisibility(View.GONE);
        mTvCopyRight.setVisibility(View.GONE);
        mTvAppName.setVisibility(View.GONE);
        mTvVersion.setVisibility(View.GONE);

        //mViewBg.setAlpha(1);
        initLogic();
    }

    public String getVersionName() {
        String versionName = "1.0.0";
        try {

            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    private void initLogic() {
        if (SPUtils.getForceUpdateIfNecessary(getApplicationContext())) {
            ConfirmDialogFg confirmDialogFg = ConfirmDialogFg.newInstance("提示", SPUtils.getForceUpdateReason(getApplicationContext()));
            confirmDialogFg.setActionListener(new ConfirmDialogFg.OnActionListener() {
                @Override
                public void onClick(int index) {
                    switch (index) {
                        case ConfirmDialogFg.INDEX_OK:
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(SPUtils.getForceUpdateUrl(getApplicationContext())));
                            startActivity(browserIntent);
                            break;
                    }
                }
            });
            confirmDialogFg.show(getSupportFragmentManager(), "~");
        } else {

            mAnimationEnd = true;
            if (AppContext.user != null) {
                new EnterAppTask().execute(viewDuration);
            } else {
                visitorStartTime = System.currentTimeMillis() / 1000;
                UserLoginPresenter.visitorRegiest(this, new UserLoginPresenter.UserNameUniqueListener() {
                    @Override
                    public void isUnique(JSONObject msg) {

                        RecommendPresenter.getCityList(SplashActivity.this, new RecommendPresenter.CityListListener() {
                            @Override
                            public void hasCityList(ArrayList<CityModel> list) {

                                if ((System.currentTimeMillis() / 1000 - visitorStartTime) > viewDuration) {
                                    enterApp();
                                } else {
                                    new EnterAppTask().execute(viewDuration - (System.currentTimeMillis() / 1000 - visitorStartTime));
                                }
                            }
                        });

                    }

                    @Override
                    public void notUnique(String msg) {
                    }
                });
            }
        }


    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }

    @Override
    protected void onResume() {
        MobclickAgent.onPageStart("SplashActivity");
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("SplashActivity");
    }

    public void enterApp() {
        startActivity(new Intent(this, RecommendActivity.class));
//        startActivity(new Intent(this, LoginActivity.class));
        SplashActivity.this.finish();
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
