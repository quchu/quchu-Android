package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.view.fragment.FriendsFollowerFg;
import co.quchu.quchu.widget.AnimationViewPager.RotatePageTransformer;

/**
 * QuFriendsActivity
 * User: Chenhs
 * Date: 2015-11-09
 * 趣星人
 */
public class QuFriendsActivity extends BaseActivity {
    @Bind(R.id.recyclerView)
    ViewPager quFriendsVp;
    @Bind(R.id.tabLayout)
    TabLayout tabLayout;
    @Bind(R.id.headImage)
    SimpleDraweeView headImage;
    @Bind(R.id.whatIs)
    TextView whatIs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qu_friends);
        ButterKnife.bind(this);
        getEnhancedToolbar().getTitleTv().setText("我的趣友圈");


        tabLayout.addTab(tabLayout.newTab().setText("关注"));
        tabLayout.addTab(tabLayout.newTab().setText("趣粉"));

        Bundle bundleSub = new Bundle();
        Bundle bundleFlr = new Bundle();
        bundleSub.putBoolean(FriendsFollowerFg.BUNDLE_KEY_IS_SUBSCRIBE, true);
        bundleFlr.putBoolean(FriendsFollowerFg.BUNDLE_KEY_IS_SUBSCRIBE, false);

        headImage.setImageURI(Uri.parse(AppContext.user.getPhoto()));
//        ViewCompat.setTransitionName(headImage, KEY_TRANSITION_ANIMATION);

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

        whatIs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuFriendsActivity.this, WhatIsActivity.class);
                startActivity(intent);
            }
        });
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


    @Override
    protected void onResume() {
        MobclickAgent.onPageStart("social");
        super.onResume();
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPageEnd("social");
        super.onPause();
    }
}
