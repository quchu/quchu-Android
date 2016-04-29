package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.umeng.analytics.MobclickAgent;

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
public class QuFriendsActivity extends BaseActivity {
    @Bind(R.id.planet_avatar_icon)
    ImageView planetAvatarIcon;
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
    @Bind(R.id.qu_friends_vp)
    ViewPager quFriendsVp;

    @Bind(R.id.tabLayout)
    TabLayout tabLayout;
    private AnimatorSet animatorSet;
    private boolean isShowing = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qu_friends);
        ButterKnife.bind(this);
        getEnhancedToolbar().getTitleTv().setText(getTitle().toString());
        planetGeneTv.setText(getResources().getString(R.string.text_planet_discover_friends));
        StringUtils.alterBoldTextColor(planetGeneTv, 4, 7, R.color.gene_textcolor_yellow);


        tabLayout.addTab(tabLayout.newTab().setText("我关注的"));
        tabLayout.addTab(tabLayout.newTab().setText("关注我的"));

        Bundle bundleSub = new Bundle();
        Bundle bundleFlr = new Bundle();
        bundleSub.putBoolean(FriendsFollowerFg.BUNDLE_KEY_IS_SUBSCRIBE, true);
        bundleFlr.putBoolean(FriendsFollowerFg.BUNDLE_KEY_IS_SUBSCRIBE, false);
        planetAvatarIcon.setImageURI(Uri.parse(AppContext.user.getPhoto()));

        FriendsFollowerFg fragment1 = new FriendsFollowerFg();
        fragment1.setArguments(bundleSub);
        FriendsFollowerFg fragment2 = new FriendsFollowerFg();
        fragment2.setArguments(bundleFlr);

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(fragment1);
        fragments.add(fragment2);
        quFriendsVp.setAdapter(new fragmentAdapter(getSupportFragmentManager(), fragments));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                quFriendsVp.setCurrentItem(tab.getPosition(), false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        quFriendsVp.setPageTransformer(true, new RotatePageTransformer());


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
                if (null == lists) {
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
                Toast.makeText(QuFriendsActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
        });

        initAnimation();
    }

    class fragmentAdapter extends FragmentPagerAdapter {
        List<Fragment> fragments;

        public fragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments == null ? 0 : fragments.size();
        }
    }


    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }


    /**
     * 初始化  旋转动画
     *
     * @return
     */
    public void initAnimation() {
        final MovePath movePath = new MovePath();
        List animationList = new ArrayList();  //动画集合

        //movePath.getCircleData 获取圆形移动路径
        List lis4t = movePath.getCircleData(designRpv, new float[]{StringUtils.dip2px(this, -26), StringUtils.dip2px(this, 88)});
        List list2 = movePath.getCircleData(pavilionRpv, new float[]{StringUtils.dip2px(this, -108), StringUtils.dip2px(this, 14)});
        List list3 = movePath.getCircleData(atmosphereRpv, new float[]{StringUtils.dip2px(this, -107), StringUtils.dip2px(this, -89)});
        List list1 = movePath.getCircleData(strollRpv, new float[]{StringUtils.dip2px(this, 1), StringUtils.dip2px(this, -88)});
        List list5 = movePath.getCircleData(cateRpv, new float[]{StringUtils.dip2px(this, 126), 0});
        MyAnimation moveAnimation = new MyAnimation();
        //将5个button 的移动动画加入list集合中
        int animationDuration = 160 * 1000;
        animationList.add(moveAnimation.setTranslation(designRpv, (List) lis4t.get(0), (List) lis4t.get(1), animationDuration));
        animationList.add(moveAnimation.setTranslation(pavilionRpv, (List) list2.get(0), (List) list2.get(1), animationDuration));

        animationList.add(moveAnimation.setTranslation(atmosphereRpv, (List) list3.get(0), (List) list3.get(1), animationDuration));
        animationList.add(moveAnimation.setTranslation(strollRpv, (List) list1.get(0), (List) list1.get(1), animationDuration));
        animationList.add(moveAnimation.setTranslation(cateRpv, (List) list5.get(0), (List) list5.get(1), animationDuration));

        animatorSet = moveAnimation.playTogether(animationList); //动画集合
        animatorSet.setDuration(animationDuration);
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
        if (myHandler != null)
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

    @Override
    protected void onResume() {
        MobclickAgent.onPageStart("social");
        super.onResume();

        planetGeneTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuFriendsActivity.this, WhatIsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPageEnd("social");
        super.onPause();
    }
}
