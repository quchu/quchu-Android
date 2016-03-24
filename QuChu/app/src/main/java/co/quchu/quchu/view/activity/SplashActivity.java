package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
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
public class SplashActivity extends BaseActivity implements ViewTreeObserver.OnScrollChangedListener {

    private long viewDuration = 2 * 1000;
    private long visitorStartTime = 0L;
    @Bind(R.id.scrollView)
    ScrollView mScrollView;
    @Bind(R.id.ivCloudRight)
    ImageView mIvCloudRight;
    @Bind(R.id.ivCloudLeft)
    ImageView mIvCloudLeft;
    @Bind(R.id.ivArrow)
    ImageView mIvArrow;
    @Bind(R.id.ivBg)
    ImageView mIvBg;
    @Bind(R.id.ivBgSec)
    ImageView mIvBgSec;
    @Bind(R.id.rlRoot)
    RelativeLayout mRelativeLayout;
    @Bind(R.id.ivMiddleFinger)
    ImageView mIvMiddleFinger;
    @Bind(R.id.tvAppName)
    TextView mTvAppName;
    @Bind(R.id.tvCopyRight)
    TextView mTvCopyRight;
    @Bind(R.id.tvVersion)
    TextView mTvVersion;
    @Bind(R.id.vBg)
    View mViewBg;
    @Bind(R.id.tvTips)
    View mTvTips;
    @Bind(R.id.vSpace)
    View mVSpace;

    private float mScrollDistance = -1;
    private boolean mDisableScroll = false;
    boolean mAnimationStarted = false;
    private int mHalfWidth = -1;
    private AnimatorSet mAnimationSet;
    boolean mShowGuide = true;
    boolean mActionUp = false;
    boolean mAnimationEnd = false;

    @Override
    public void onScrollChanged() {

        float offset = (1-(mScrollView.getScrollY()/mScrollDistance));
        float offsetPx = offset*mHalfWidth;

        mIvCloudLeft.setTranslationX(-offsetPx);
        mIvCloudRight.setTranslationX(+offsetPx);

        mIvArrow.setAlpha(mScrollView.getScrollY() / (mScrollDistance) * .15f);
        if (mScrollView.getScrollY() <= 0 && !mAnimationStarted) {
            mAnimationStarted = true;
            mDisableScroll = true;
            startScaleAnimation();
        }

    }

    @Override
    public void onBackPressed() {
        if (mAnimationEnd){
            super.onBackPressed();
        }
    }

    private void startScaleAnimation() {
        mAnimationSet.playTogether(
                ObjectAnimator.ofFloat(mIvBg, "scaleX", 1, 1.5f),
                ObjectAnimator.ofFloat(mIvBg, "scaleY", 1, 1.5f)
        );
        ObjectAnimator.ofFloat(mIvCloudLeft, "translationX", mIvCloudLeft.getTranslationX(), -mHalfWidth*2).setDuration(1000).start();
        ObjectAnimator.ofFloat(mIvCloudRight, "translationX", mIvCloudRight.getTranslationX(), mHalfWidth*2).setDuration(1000).start();
        mIvArrow.clearAnimation();
        ((BitmapDrawable) mIvArrow.getDrawable()).getBitmap().recycle();
        mRelativeLayout.removeView(mIvArrow);

        mAnimationSet.setDuration(1000);
        mAnimationSet.setStartDelay(500);
        mAnimationSet.setInterpolator(new DecelerateInterpolator());
        mAnimationSet.start();
        mAnimationSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                ((BitmapDrawable) mIvCloudLeft.getDrawable()).getBitmap().recycle();
                ((BitmapDrawable) mIvCloudRight.getDrawable()).getBitmap().recycle();
                mIvCloudLeft.setImageBitmap(null);
                mIvCloudRight.setImageBitmap(null);
                mRelativeLayout.removeView(mIvCloudLeft);
                mRelativeLayout.removeView(mIvCloudRight);
                mRelativeLayout.invalidate();
                System.gc();
                startSecBgAnimation();
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });

        mScrollView.getViewTreeObserver().removeOnScrollChangedListener(SplashActivity.this);
    }

    private void startSecBgAnimation() {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ObjectAnimator.ofFloat(mIvBgSec, "alpha", 0, 1));
        animatorSet.setDuration(1000);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                ((BitmapDrawable) mIvBg.getDrawable()).getBitmap().recycle();
                mIvBg.setImageBitmap(null);
                System.gc();
                mIvBg.setScaleX(1);
                mIvBg.setScaleY(1);
                startThirdBgAnimation();
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
    }

    private void startThirdBgAnimation() {

        ((BitmapDrawable) mIvBgSec.getDrawable()).getBitmap().recycle();
        mIvBgSec.setImageBitmap(null);
        mRelativeLayout.removeView(mIvBgSec);
        System.gc();

        mIvBg.setImageResource(R.mipmap.background_fve);
        mIvBg.postDelayed(new Runnable() {
            @Override
            public void run() {
                startFinalAnimation();
            }
        },250);

    }

    private void startFinalAnimation(){
        mIvBg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ((BitmapDrawable) mIvBg.getDrawable()).getBitmap().recycle();
        mIvBg.setImageBitmap(null);
        mIvBg.setImageResource(R.mipmap.background_six);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(mIvMiddleFinger, "scaleX", 0, 1),
                ObjectAnimator.ofFloat(mIvMiddleFinger, "scaleY", 0, 1));
        animatorSet.setDuration(600);
        animatorSet.setInterpolator(new OvershootInterpolator());
        animatorSet.start();


        ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
        anim.setDuration(1000);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
            @Override public void onAnimationUpdate(ValueAnimator animation) {
                mViewBg.setAlpha((Float) animation.getAnimatedValue());
            }
        });
        anim.start();
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {

                ((BitmapDrawable) mIvBg.getDrawable()).getBitmap().recycle();
                mIvBg.setImageBitmap(null);
                mRelativeLayout.removeView(mIvBg);

            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });


        final AnimatorSet animatorSetText = new AnimatorSet();
        animatorSetText.playTogether(
                ObjectAnimator.ofFloat(mTvAppName, "translationY", mTvAppName.getTranslationY()/1.4f,0),
                ObjectAnimator.ofFloat(mTvCopyRight, "translationX", mHalfWidth,0),
                ObjectAnimator.ofFloat(mTvVersion, "translationY", mTvVersion.getTranslationY(),0),
                ObjectAnimator.ofFloat(mTvAppName, "alpha", 0, 1),
                ObjectAnimator.ofFloat(mTvCopyRight, "alpha", 0, 1),
                ObjectAnimator.ofFloat(mTvVersion, "alpha", 0, 1)
        );
        animatorSetText.setDuration(800);
        animatorSetText.setStartDelay(800);
        animatorSetText.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSetText.start();
        animatorSetText.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                mRelativeLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        System.gc();

                        initLogic();

                    }
                },500);
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mShowGuide = SPUtils.animationShown(getApplicationContext());
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

        if (!mShowGuide){
            mTvAppName.setVisibility(View.GONE);
            mTvCopyRight.setVisibility(View.GONE);
            mTvVersion.setVisibility(View.GONE);
            ((BitmapDrawable) mIvBg.getDrawable()).getBitmap().recycle();
            ((BitmapDrawable) mIvCloudLeft.getDrawable()).getBitmap().recycle();
            ((BitmapDrawable) mIvCloudRight.getDrawable()).getBitmap().recycle();
            ((BitmapDrawable) mIvBgSec.getDrawable()).getBitmap().recycle();
            ((BitmapDrawable) mIvMiddleFinger.getDrawable()).getBitmap().recycle();
            ((BitmapDrawable) mIvArrow.getDrawable()).getBitmap().recycle();
            mIvBg.setImageBitmap(null);
            mIvCloudLeft.setImageBitmap(null);
            mIvCloudRight.setImageBitmap(null);
            mIvBgSec.setImageBitmap(null);
            mIvMiddleFinger.setImageBitmap(null);
            mIvArrow.setImageBitmap(null);
            mRelativeLayout.removeView(mIvBg);
            mRelativeLayout.removeView(mIvCloudLeft);
            mRelativeLayout.removeView(mIvCloudRight);
            mRelativeLayout.removeView(mIvMiddleFinger);
            mRelativeLayout.removeView(mIvArrow);
            //mRelativeLayout.removeView(mScrollView);
            System.gc();
            mIvBgSec.setAdjustViewBounds(true);
            mIvBgSec.setAlpha(1f);
            mTvTips.setVisibility(View.GONE);
            mVSpace.setVisibility(View.GONE);
            initLogic();
        }else{
            mIvBgSec.setAlpha(.0f);
            mIvMiddleFinger.setScaleY(0f);
            mIvMiddleFinger.setScaleX(0f);
            mIvMiddleFinger.setAlpha(1f);
            mAnimationSet = new AnimatorSet();

            mScrollView.post(new Runnable() {
                @Override
                public void run() {
                    mScrollView.fullScroll(View.FOCUS_DOWN);
                    mScrollView.getViewTreeObserver().addOnScrollChangedListener(SplashActivity.this);
                }
            });

            mIvBg.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mHalfWidth = mIvBg.getWidth() / 2;
                    mScrollDistance = mScrollView.getChildAt(0).getHeight()-mScrollView.getHeight();
                    mIvBg.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            });

            mScrollView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if ((mScrollView.getScrollY() / (mScrollDistance))<.8f&&!mActionUp&&(event.getAction()==MotionEvent.ACTION_UP||event.getAction()==MotionEvent.ACTION_MOVE)){
                        mActionUp = true;
                        mDisableScroll = true;
                        mScrollView.post(new Runnable() {
                            @Override
                            public void run() {
                                ObjectAnimator scrollAnimator = ObjectAnimator.ofInt(mScrollView, "scrollY",  0).setDuration(1000);
                                scrollAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                                scrollAnimator.start();
                            }
                        });
                    }
                    return mDisableScroll;
                }
            });
            mIvArrow.setAlpha(.15f);
            ObjectAnimator anim = ObjectAnimator.ofFloat(mIvArrow, "translationY", 30f, 0);
            anim.setDuration(1000);
            anim.setRepeatMode(ValueAnimator.REVERSE);
            anim.setRepeatCount(ValueAnimator.INFINITE);
            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            anim.start();
        }


    }

    private void initLogic() {

        if (Constants.ISSTARTINGPKG) {
            mIvBg.setImageResource(R.mipmap.ic_splash_bg_360);
        } else {
            mIvBg.setImageResource(R.mipmap.ic_splash_bg);
        }
        mAnimationEnd = true;
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
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("SplashActivity");
    }

    public void enterApp() {
        startActivity(new Intent(this, RecommendActivity.class));
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
