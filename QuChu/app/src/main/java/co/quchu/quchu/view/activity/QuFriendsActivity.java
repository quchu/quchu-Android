package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.fragment.FriendsFollowerFg;
import co.quchu.quchu.widget.AnimationViewPager.RotatePageTransformer;
import co.quchu.quchu.widget.RoundProgressView;

/**
 * QuFriendsActivity
 * User: Chenhs
 * Date: 2015-11-09
 * 趣星人
 */
public class QuFriendsActivity extends BaseActivity {
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
    @Bind(R.id.title_back_rl)
    RelativeLayout titleBackRL;
    @Bind(R.id.title_more_rl)
    RelativeLayout titleMoreRl;
    @Bind(R.id.title_content_tv)
    TextView title_content_tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qu_friends);
        ButterKnife.bind(this);
        initTitleBar();
        title_content_tv.setText(getTitle().toString());
        planetGeneTv.setText(getResources().getString(R.string.text_planet_discover_friends));
        StringUtils.alterBoldTextColor(planetGeneTv, 4, 7, R.color.gene_textcolor_yellow);

        final FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add(String.format(getString(R.string.text_friends_following), 8118), FriendsFollowerFg.class)
                .add(R.string.text_friends_follower, FriendsFollowerFg.class)
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
    }

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
    }

}
