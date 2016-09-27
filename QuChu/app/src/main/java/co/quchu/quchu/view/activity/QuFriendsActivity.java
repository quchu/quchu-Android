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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.view.fragment.FriendsFollowerFg;

/**
 * QuFriendsActivity
 * User: Chenhs
 * Date: 2015-11-09
 * 趣星人
 */
public class QuFriendsActivity extends BaseActivity {


    @Override
    protected String getPageNameCN() {
        return getString(R.string.pname_qu_friends);
    }

    @Bind(R.id.recyclerView)
    ViewPager quFriendsVp;
    @Bind(R.id.tabLayout)
    TabLayout tabLayout;
    @Bind(R.id.headImage)
    SimpleDraweeView headImage;
    @Bind(R.id.whatIs)
    TextView whatIs;
    @Bind(R.id.gender)
    SimpleDraweeView gender;


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
        gender.setImageURI(Uri.parse("res:///" + (AppContext.user.getGender().equals("男") ? R.mipmap.ic_male : R.mipmap.ic_female)));

        FriendsFollowerFg fragment1 = new FriendsFollowerFg();
        fragment1.setArguments(bundleSub);
        FriendsFollowerFg fragment2 = new FriendsFollowerFg();
        fragment2.setArguments(bundleFlr);

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(fragment1);
        fragments.add(fragment2);
        quFriendsVp.setAdapter(new fragmentAdapter(getSupportFragmentManager(), fragments));
//        quFriendsVp.setPageTransformer(true, new RotatePageTransformer());
        tabLayout.setupWithViewPager(quFriendsVp);

        whatIs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuFriendsActivity.this, WhatIsActivity.class);
                startActivity(intent);
            }
        });
        EventBus.getDefault().register(this);
    }

    public void setFaloowNum(int hostNum, int followNum) {
        tabLayout.getTabAt(0).setText("关注" + (hostNum==0?"":hostNum));
        tabLayout.getTabAt(1).setText("趣粉" + (followNum==0?"":followNum));
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
        super.onResume();
    }

    @Subscribe
    public void onEventBus(QuchuEventModel model) {
        if (model.getFlag() == EventFlags.EVENT_GOTO_HOME_PAGE)
            finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
