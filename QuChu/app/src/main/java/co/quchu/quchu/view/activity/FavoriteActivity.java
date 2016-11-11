package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.ArrayMap;
import android.widget.TextView;

import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.view.fragment.FavoriteQuchuFragment;

public class FavoriteActivity extends BaseBehaviorActivity {

    @Override
    public ArrayMap<String, Object> getUserBehaviorArguments() {
        return null;
    }

    @Override
    public int getUserBehaviorPageId() {
        return 122;
    }

    @Override
    protected String getPageNameCN() {
        return getString(R.string.pname_favorite);
    }

//    @Bind(R.id.tabLayout) TabLayout tabLayout;
//    @Bind(R.id.viewpager) NoScrollViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        ButterKnife.bind(this);
        EnhancedToolbar toolbar = getEnhancedToolbar();
        TextView titleTv = toolbar.getTitleTv();
        titleTv.setText("收藏的趣处");

        FavoriteQuchuFragment quchuFragment = FavoriteQuchuFragment.newInstance(true, -1);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.favoriteContainer, quchuFragment);
        ft.commit();

//        TextView tag0 = (TextView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_detail_tab,tabLayout,false);
//        TextView tag1 = (TextView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_detail_tab,tabLayout,false);
//        tag0.setText("趣处");
//        tag1.setText("文章");
//        tabLayout.addTab(tabLayout.newTab().setCustomView(tag0).setTag(0));
//        tabLayout.addTab(tabLayout.newTab().setCustomView(tag1).setTag(1));
//
//        final FavoriteQuchuFragment quchuFragment = FavoriteQuchuFragment.newInstance(true, -1);
//        final FavoriteEssayFragment essayFragment = new FavoriteEssayFragment();
//
//
//        viewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
//            @Override
//            public Fragment getItem(int position) {
//                if (position == 0) {
//                    return quchuFragment;
//                } else
//                    return essayFragment;
//            }
//
//            @Override
//            public int getCount() {
//                return 2;
//            }
//        });
//
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewpager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }

}
