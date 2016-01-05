package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.testin.agent.TestinAgent;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.photo.AlbumHelper;
import co.quchu.quchu.utils.AppKey;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;

/**
 * SplashActivity
 * User: Chenhs
 * Date: 2015-11-10
 */
public class SplashActivity extends BaseActivity {
    @Bind(R.id.splash_iv)
    ImageView splashIv;
    @Bind(R.id.splash_app_name_tv)
    TextView splashAppNameTv;
    @Bind(R.id.splash_app_version_name_tv)
    TextView splashAppVersionNameTv;
    private long viewDuration = 2 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TestinAgent.init(this, "031712c9e95bb4dae7a581f25afff9e7");
        TestinAgent.setLocalDebug(true);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        setIcon();
        PackageManager pm = getPackageManager();//context为当前Activity上下文
        PackageInfo pi = null;
        String version = "";
        try {
            pi = pm.getPackageInfo(getPackageName(), 0);
            version = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (!StringUtils.isEmpty(version)) {
            splashAppVersionNameTv.setText("V" + version);
        }
  /*       rippleBackground=(CircleWaveView)findViewById(R.id.content);
        circleIv= (ImageView) findViewById(R.id.splash_circle_iv);*/
/*        rippleBackground.startRippleAnimation();*/
        // initCircleAnimation();
        handler.sendMessageDelayed(handler.obtainMessage(0x01), viewDuration);
        AlbumHelper.initAlbumHelper(this);
    }





 /* public void   initCircleAnimation(){
      animatorList= new ArrayList<Animator>();
      animatorSet = new AnimatorSet();
      final ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(circleIv, "ScaleX", 0.3f, 1.0f);
      scaleXAnimator.setDuration(durationTime);
      animatorList.add(scaleXAnimator);
      final ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(circleIv, "ScaleY",0.3f, 1.0f);
      scaleYAnimator.setDuration(durationTime);
      animatorList.add(scaleYAnimator);
      final ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(circleIv, "Alpha", 0.3f, 1.0f);
      alphaAnimator.setDuration(durationTime);
      animatorList.add(alphaAnimator);

      animatorSet.playTogether(animatorList);
      animatorSet.setInterpolator(new DecelerateInterpolator());
      animatorSet.addListener(new Animator.AnimatorListener() {
          @Override
          public void onAnimationStart(Animator animation) {
              circleIv.setVisibility(View.VISIBLE);
              Message msgs = handler.obtainMessage(0x00);
                handler.sendMessageDelayed(msgs,600);

          }

          @Override
          public void onAnimationEnd(Animator animation) {

          }

          @Override
          public void onAnimationCancel(Animator animation) {

          }

          @Override
          public void onAnimationRepeat(Animator animation) {

          }
      });
    }*/

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
              /*  case 0x00:
                    rippleBackground.startRippleAnimation();
                    break;*/
                case 0x01:
                    startActivity(new Intent(SplashActivity.this, UserLoginActivity.class));
                    SplashActivity.this.finish();
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
 /*       if (animatorSet !=null)
            animatorSet.start();
        rippleBackground.stopRippleAnimation();*/
    }

    protected void setIcon() {
        if (SPUtils.getBooleanFromSPMap(this, AppKey.IS_NEED_ICON, true)) {
            Intent shortcutintent = new Intent(
                    "com.android.launcher.action.INSTALL_SHORTCUT");
            // 不允许重复创建
            shortcutintent.putExtra("duplicate", false);
            // 需要现实的名称
            shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
                    getResources().getString(R.string.app_name));
            // 快捷图片
            Parcelable icon = Intent.ShortcutIconResource.fromContext(
                    this.getApplicationContext(), R.mipmap.ic_launcher);
            shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
            // 点击快捷图片，运行的程序主入口
            shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT,
                    new Intent(getApplicationContext(), SplashActivity.class));
            // 发送广播
            sendBroadcast(shortcutintent);
            //不允许重复创建
            SPUtils.putBooleanToSPMap(this, AppKey.IS_NEED_ICON, false);
        }

    }
}
