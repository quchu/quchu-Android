package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.view.adapter.RecommendFragmentAdapter;
import co.quchu.quchu.view.fragment.ClassifyFragment;
import co.quchu.quchu.view.fragment.RecommendFragment;
import co.quchu.quchu.widget.AnimationViewPager.ZoomOutPageTransformer;
import co.quchu.quchu.widget.MoreButtonView;
import co.quchu.quchu.widget.RecommendTitleGroup;

/**
 * RecommendActivity
 * User: Chenhs
 * Date: 2015-12-07
 */
public class RecommendActivity extends BaseActivity {
    @Bind(R.id.recommend_title_location_rl)
    RelativeLayout recommendTitleLocationRl;
    @Bind(R.id.recommend_title_more_rl)
    MoreButtonView recommendTitleMoreRl;
    @Bind(R.id.recommend_title_bar_rl)
    RelativeLayout recommendTitleBarRl;
    @Bind(R.id.recommend_body_vp)
    ViewPager recommendBodyVp;
    @Bind(R.id.recommend_title_center_rtg)
    RecommendTitleGroup recommendTitleCenterRtg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        ButterKnife.bind(this);

        initView();

    }

    @OnClick({R.id.recommend_title_more_rl, R.id.recommend_title_location_rl})
    public void titleClick(View view) {
        switch (view.getId()) {
            case R.id.recommend_title_more_rl:
                startActivity(new Intent(this, MenusActivity.class));
                break;
            case R.id.recommend_title_location_rl:

                break;
        }
    }

    private void initView() {
        InitViewPager();
        recommendTitleCenterRtg.setInitSelected(true);
        recommendBodyVp.setCurrentItem(1);
        recommendTitleCenterRtg.setSelectedListener(new RecommendTitleGroup.RecoSelectedistener() {
            @Override
            public void onViewsClick(int flag) {
                if (flag == 0) {
                    LogUtils.json("selected == right");
                    recommendBodyVp.setCurrentItem(1);//设置当前显示标签页为第二页
                } else {
                    LogUtils.json("selected == left");
                    recommendBodyVp.setCurrentItem(0);//设置当前显示标签页为第一页
                }
            }
        });

    }

    /*
   * 初始化ViewPager
   */
    public void InitViewPager() {
        ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
        Fragment recoFragment = new RecommendFragment();
        Fragment classifyFragment = new ClassifyFragment();
        fragmentList.add(recoFragment);
        fragmentList.add(classifyFragment);
        //给ViewPager设置适配器
        recommendBodyVp.setAdapter(new RecommendFragmentAdapter(getSupportFragmentManager(), fragmentList));
        recommendBodyVp.setPageTransformer(true, new ZoomOutPageTransformer());
    }
}
