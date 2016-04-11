package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.view.fragment.FootprintDetailFragment;
import co.quchu.quchu.widget.MoreButtonView;

public class MyFootprintDetailActivity extends BaseActivity implements ViewPager.OnPageChangeListener {


    @Bind(R.id.title_back_iv)
    ImageView titleBackIv;
    @Bind(R.id.title_content_tv)
    TextView titleContentTv;
    @Bind(R.id.title_more_rl)
    MoreButtonView titleMoreRl;
    @Bind(R.id.container)
    ViewPager viewPager;
    private List<FootprintDetailFragment> fragments;
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_footprint_detail);
        ButterKnife.bind(this);

        fragments = new ArrayList<>();
        FootprintDetailFragment fragment = new FootprintDetailFragment();
        fragment.firstPage = true;
        fragments.add(fragment);
        fragments.add(new FootprintDetailFragment());
        fragments.add(new FootprintDetailFragment());
        fragments.add(new FootprintDetailFragment());
        fragments.add(new FootprintDetailFragment());

        mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), fragments);

        initListener();
    }

    private void initListener() {
        titleBackIv.setOnClickListener(this);

        viewPager.addOnPageChangeListener(this);
        viewPager.setAdapter(mPagerAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back_iv:
                finish();
                break;
        }
    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    private int firstPagePosition;

    @Override
    public void onPageSelected(int position) {
        fragments.get(position).showing();
        fragments.get(firstPagePosition).hint();
        firstPagePosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    public class PagerAdapter extends FragmentPagerAdapter {
        private List<FootprintDetailFragment> fragments;

        public PagerAdapter(FragmentManager fm, List<FootprintDetailFragment> fragments) {
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
}
