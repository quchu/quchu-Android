package co.quchu.quchu.view.activity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.model.FollowUserModel;
import co.quchu.quchu.presenter.FollowPresenter;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.fragment.FriendsFollowerFg;
import co.quchu.quchu.widget.AnimationViewPager.RotatePageTransformer;
import co.quchu.quchu.widget.RoundProgressView;
import co.quchu.quchu.widget.planetanimations.Interpolator.BezierInterpolators;
import co.quchu.quchu.widget.planetanimations.MovePath;
import co.quchu.quchu.widget.planetanimations.MyAnimation;

/**
 * QuFriendsActivity
 * User: Chenhs
 * Date: 2015-11-09
 * 趣星人
 */
public class QuFriendsActivity extends BaseActivity implements ViewTreeObserver.OnGlobalLayoutListener{
    @Bind(R.id.planet_avatar_icon)
    ImageView planetAvatarIcon;
    @Bind(R.id.mid_luncher)
    FrameLayout midLuncher;
    @Bind(R.id.design_rpv)
    RoundProgressView designRpv;
    @Bind(R.id.pavilion_rpv)
    RoundProgressView pavilionRpv;
    @Bind(R.id.atmosphere_rpv)
    RoundProgressView atmosphereRpv;
    @Bind(R.id.stroll_rpv)
    RoundProgressView strollRpv;
    @Bind(R.id.cate_rpv)
    RoundProgressView cateRpv;
    @Bind(R.id.planet_gene_tv)
    TextView planetGeneTv;
    @Bind(R.id.qu_friends_tab_stl)
    SmartTabLayout quFriendsTabStl;
    @Bind(R.id.qu_friends_vp)
    ViewPager quFriendsVp;
    /**
     * title
     ***/
    @Bind(R.id.title_more_rl)
    RelativeLayout titleMoreRl;
    @Bind(R.id.title_content_tv)
    TextView title_content_tv;
    public static final String BUNDLE_KEY_SUBSCRIBERS = "BUNDLE_KEY_SUBSCRIBERS";
    public static final String BUNDLE_KEY_FOLLOWERS = "BUNDLE_KEY_FOLLOWERS";
    public static final String BUNDLE_KEY_FROM_SUBSCRIBE = "BUNDLE_KEY_FROM_SUBSCRIBE";
    private int mSubscribers;
    private int mFollowers;
    private boolean mFromSubscribe = false;
    private int AnimationDuration = 160 * 1000;
    private AnimatorSet animatorSet;
    private boolean isShowing = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qu_friends);
        ButterKnife.bind(this);
        initTitleBar();
        mSubscribers = getIntent().getIntExtra(BUNDLE_KEY_SUBSCRIBERS,0);
        mFollowers = getIntent().getIntExtra(BUNDLE_KEY_FOLLOWERS,0);
        mFromSubscribe = getIntent().getBooleanExtra(BUNDLE_KEY_FROM_SUBSCRIBE,false);
        mSubscribers = mSubscribers<0?0:mSubscribers;
        mFollowers = mFollowers<0?0:mFollowers;
        titleMoreRl.setVisibility(View.INVISIBLE);
        title_content_tv.setText(getTitle().toString());
        planetGeneTv.setText(getResources().getString(R.string.text_planet_discover_friends));
        StringUtils.alterBoldTextColor(planetGeneTv, 4, 7, R.color.gene_textcolor_yellow);

        Bundle bundleSub = new Bundle();
        Bundle bundleFlr = new Bundle();
        bundleSub.putBoolean(FriendsFollowerFg.BUNDLE_KEY_IS_SUBSCRIBE,true);
        bundleFlr.putBoolean(FriendsFollowerFg.BUNDLE_KEY_IS_SUBSCRIBE,false);
        planetAvatarIcon.setImageURI(Uri.parse(AppContext.user.getPhoto()));

        final FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add(String.format(getString(R.string.text_friends_following), mSubscribers), FriendsFollowerFg.class,bundleSub)
                .add(String.format(getString(R.string.text_friends_follower),mFollowers), FriendsFollowerFg.class,bundleFlr)
                .create());

        quFriendsVp.setAdapter(adapter);
        quFriendsTabStl.setViewPager(quFriendsVp);
        quFriendsVp.setPageTransformer(true, new RotatePageTransformer());

        quFriendsTabStl.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        pavilionRpv.setProgressText("");
        designRpv.setProgressText("");
        atmosphereRpv.setProgressText("");
        cateRpv.setProgressText("");
        strollRpv.setProgressText("");
        pavilionRpv.setVisibility(View.INVISIBLE);
        designRpv.setVisibility(View.INVISIBLE);
        atmosphereRpv.setVisibility(View.INVISIBLE);
        cateRpv.setVisibility(View.INVISIBLE);
        strollRpv.setVisibility(View.INVISIBLE);


        FollowPresenter.getCurrentUserFollowers(getApplicationContext(), false, FollowPresenter.TAFOLLOWING, 1, new FollowPresenter.GetFollowCallBack() {
            @Override
            public void onSuccess(ArrayList<FollowUserModel> lists) {
                if (null==lists){
                    return;
                }
                for (int i = 0; i < lists.size(); i++) {
                    switch (i) {
                        case 0:
                            designRpv.setImage(lists.get(i).getPhoto());
                            designRpv.setVisibility(View.VISIBLE);
                            break;
                        case 1:
                            pavilionRpv.setImage(lists.get(i).getPhoto());
                            pavilionRpv.setVisibility(View.VISIBLE);
                            break;
                        case 2:
                            atmosphereRpv.setImage(lists.get(i).getPhoto());
                            atmosphereRpv.setVisibility(View.VISIBLE);
                            break;
                        case 3:
                            cateRpv.setImage(lists.get(i).getPhoto());
                            cateRpv.setVisibility(View.VISIBLE);
                            break;
                        case 4:
                            strollRpv.setImage(lists.get(i).getPhoto());
                            strollRpv.setVisibility(View.VISIBLE);
                            break;
                    }
                }

            }

            @Override
            public void onError() {

            }
        });



        quFriendsVp.setCurrentItem(mFromSubscribe?0:1);
        ViewTreeObserver vto = planetAvatarIcon.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(this);
    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }

    @Override
    public void onGlobalLayout() {
//        int heigh = midLuncher.getHeight() / 2;
        midLuncher.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        initAnimation();
    }




    /**
     * 初始化  旋转动画
     *
     * @return
     */
    public void initAnimation() {
        final MovePath movePath = new MovePath();
        List animationList = new ArrayList();  //动画集合
        DisplayMetrics dm = new DisplayMetrics();
        //获取屏幕信息
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        //movePath.getCircleData 获取圆形移动路径
        List lis4t = movePath.getCircleData(designRpv, new float[]{StringUtils.dip2px(this, -26), StringUtils.dip2px(this, 88)});
        List list2 = movePath.getCircleData(pavilionRpv, new float[]{StringUtils.dip2px(this, -108), StringUtils.dip2px(this, 14)});
        List list3 = movePath.getCircleData(atmosphereRpv, new float[]{StringUtils.dip2px(this, -107), StringUtils.dip2px(this, -89)});
        List list1 = movePath.getCircleData(strollRpv, new float[]{StringUtils.dip2px(this, 1), StringUtils.dip2px(this, -88)});
        List list5 = movePath.getCircleData(cateRpv, new float[]{StringUtils.dip2px(this, 126), 0});
        MyAnimation moveAnimation = new MyAnimation();
        //将5个button 的移动动画加入list集合中
        animationList.add(moveAnimation.setTranslation(designRpv, (List) lis4t.get(0), (List) lis4t.get(1), AnimationDuration));
        animationList.add(moveAnimation.setTranslation(pavilionRpv, (List) list2.get(0), (List) list2.get(1), AnimationDuration));

        animationList.add(moveAnimation.setTranslation(atmosphereRpv, (List) list3.get(0), (List) list3.get(1), AnimationDuration));
        animationList.add(moveAnimation.setTranslation(strollRpv, (List) list1.get(0), (List) list1.get(1), AnimationDuration));
        animationList.add(moveAnimation.setTranslation(cateRpv, (List) list5.get(0), (List) list5.get(1), AnimationDuration));

        animatorSet = moveAnimation.playTogether(animationList); //动画集合
        animatorSet.setDuration(AnimationDuration);
        animatorSet.setInterpolator(new BezierInterpolators(0.03f, 0.08f, 0.1f, 0.1f));
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                LogUtils.json("planet animation is end");
                if (myHandler != null)
                    myHandler.sendMessageDelayed(myHandler.obtainMessage(1), 200);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        if (myHandler!=null)
            myHandler.sendMessageDelayed(myHandler.obtainMessage(0), 0);
    }

    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (isShowing)
                switch (msg.what) {
                    case 0:
                        if (null != animatorSet) {
                            animatorSet.start();
                        } else {
                            initAnimation();
                        }
                        break;
                    case 1:
                        if (null != animatorSet) {
                            if (!animatorSet.isRunning())
                                animatorSet.start();
                        } else {
                            initAnimation();
                        }
                        break;
                }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myHandler != null)
            myHandler = null;
        if (animatorSet != null) {
            animatorSet.cancel();
            animatorSet = null;
        }
        isShowing = false;
        ButterKnife.unbind(this);
    }

}
