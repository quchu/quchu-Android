package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.zhuge.analysis.stat.ZhugeSDK;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.BuildConfig;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.model.CityEntity;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.model.UserInfoModel;
import co.quchu.quchu.net.NetUtil;
import co.quchu.quchu.presenter.RecommendPresenter;
import co.quchu.quchu.presenter.UserLoginPresenter;
import co.quchu.quchu.utils.AppUtil;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;

/**
 * SplashActivity
 * User: Chenhs
 * Date: 2015-11-10
 */
public class SplashActivity extends BaseActivity {

  public static String INTENT_KEY_IM_CHAT = "intent_key_im_chat";
  public static String INTENT_KEY_IM_CHAT_LIST = "intent_key_im_chat_list";
  private boolean mIsChat;
  private boolean mIsChatList;

  @Override protected String getPageNameCN() {
    return null;
  }

  private long viewDuration = 2 * 1000;
  private long visitorStartTime = 0L;
  @Bind(R.id.ivAppIcon) ImageView mIvAppIcon;
  @Bind(R.id.tvAppName) TextView mTvAppName;
  @Bind(R.id.tvCopyRight) TextView mTvCopyRight;
  @Bind(R.id.tvVersion) TextView mTvVersion;
  @Bind(R.id.vBg) View mViewBg;

  private int mHalfWidth = -1;
  boolean mAnimationEnd = false;

  @Override protected void onDestroy() {
    super.onDestroy();
    ButterKnife.unbind(this);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mIsChat = getIntent().getBooleanExtra(INTENT_KEY_IM_CHAT, false);
    mIsChatList = getIntent().getBooleanExtra(INTENT_KEY_IM_CHAT_LIST, false);

    if (BuildConfig.API_SERVER != 0) {
      ZhugeSDK.getInstance().openDebug();
    }

    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    View decorView = getWindow().getDecorView();
    decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        // hide nav bar
        | View.SYSTEM_UI_FLAG_FULLSCREEN
        // hide status bar
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
    if (!NetUtil.isNetworkConnected(getApplicationContext())) {
      mTvAppName.postDelayed(new Runnable() {
        @Override public void run() {
          Toast.makeText(SplashActivity.this, "失去与母星的通讯，请检查网络", Toast.LENGTH_LONG).show();
        }
      }, 500);
    } else {
      initLogic();
    }
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

      new MaterialDialog.Builder(this).title("提示")
          .content(SPUtils.getForceUpdateReason(getApplicationContext()))
          .positiveText("立即前往")
          .negativeText("容我三思")
          .cancelable(false)
          .onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
              Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                  Uri.parse(SPUtils.getForceUpdateUrl(getApplicationContext())));
              startActivity(browserIntent);
            }
          })
          .show();
    } else {

      mAnimationEnd = true;
      if (AppContext.user != null) {
        new EnterAppTask().execute(viewDuration);

        AppUtil.resignUser(getApplicationContext());
      } else {
        visitorStartTime = System.currentTimeMillis() / 1000;
        UserLoginPresenter.visitorRegiest(this, new UserLoginPresenter.UserNameUniqueListener() {
          @Override public void isUnique(JSONObject msg) {

            ArrayMap<String, Object> params = new ArrayMap<>();
            if (null != AppContext.user && null != AppContext.user.getUsername()) {
              params.put("用户名", AppContext.user.getUsername());
            }
            params.put("登陆方式", "游客模式");
            ZGEvent(params, "用户登陆");

            RecommendPresenter.getCityList(SplashActivity.this,
                new RecommendPresenter.CityListListener() {
                  @Override public void hasCityEntity(CityEntity response) {

                    if ((System.currentTimeMillis() / 1000 - visitorStartTime) > viewDuration) {
                      enterApp();
                    } else {
                      new EnterAppTask().execute(
                          viewDuration - (System.currentTimeMillis() / 1000 - visitorStartTime));
                    }
                  }
                });
          }

          @Override public void notUnique(String msg) {
          }
        });
      }
    }
  }

  @Override protected int activitySetup() {
    return TRANSITION_TYPE_LEFT;
  }

  @Override protected void onResume() {
    super.onResume();
  }

  @Override protected void onPause() {
    super.onPause();
  }

  public void enterApp() {

    if (!SPUtils.getShowGuild()) {
      startActivity(new Intent(SplashActivity.this, WizardActivity.class));
      SplashActivity.this.finish();
    } else {
      Intent intent = new Intent(this, RecommendActivity.class);
      intent.putExtra(INTENT_KEY_IM_CHAT, mIsChat);
      intent.putExtra(INTENT_KEY_IM_CHAT_LIST, mIsChatList);
      startActivity(intent);
      SplashActivity.this.finish();
    }
  }

  /**
   * 延时进入主页
   */
  class EnterAppTask extends AsyncTask<Long, Void, Void> {

    @Override protected Void doInBackground(Long... params) {
      long time = params[0];
      try {
        Thread.sleep(time);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return null;
    }

    @Override protected void onPostExecute(Void aVoid) {
      if (!StringUtils.isEmpty(SPUtils.getUserInfo(SplashActivity.this))) {
        if (AppContext.user == null) {
          AppContext.user =
              new Gson().fromJson(SPUtils.getUserInfo(SplashActivity.this), UserInfoModel.class);
        }
      }
      enterApp();
    }
  }

  @Subscribe public void onMessageEvent(QuchuEventModel event) {
    if (null == event) {
      return;
    }
    switch (event.getFlag()) {

      case EventFlags.EVENT_DEVICE_NETWORK_CONNECTED_OR_CONNECTING:
        Toast.makeText(SplashActivity.this, "尝试建立与母星的通讯，请稍后", Toast.LENGTH_LONG).show();
        break;

      case EventFlags.EVENT_DEVICE_NETWORK_AVAILABLE:
        initLogic();
        break;
    }
  }

  @Override public void onStart() {
    super.onStart();
    EventBus.getDefault().register(this);
  }

  @Override public void onStop() {
    EventBus.getDefault().unregister(this);
    super.onStop();
  }

  @Override public void finish() {
    super.finish();
    overridePendingTransition(R.anim.in_push_right_to_left, android.R.anim.fade_out);

  }
}
