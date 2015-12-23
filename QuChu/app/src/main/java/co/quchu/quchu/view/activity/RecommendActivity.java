package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.LocationSelectedDialogFg;
import co.quchu.quchu.model.CityModel;
import co.quchu.quchu.presenter.RecommendPresenter;
import co.quchu.quchu.utils.AppKey;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.adapter.RecommendFragmentAdapter;
import co.quchu.quchu.view.fragment.ClassifyFragment;
import co.quchu.quchu.view.fragment.RecommendFragment;
import co.quchu.quchu.widget.AnimationViewPager.NoScrollViewPager;
import co.quchu.quchu.widget.AnimationViewPager.ZoomOutPageTransformer;
import co.quchu.quchu.widget.MoreButtonView;

/**
 * RecommendActivity
 * User: Chenhs
 * Date: 2015-12-07
 * 趣处分类、推荐
 */
public class RecommendActivity extends BaseActivity {
    /*    @Bind(R.id.recommend_title_location_rl)
        RelativeLayout recommendTitleLocationRl;
        @Bind(R.id.recommend_title_more_rl)
        MoreButtonView recommendTitleMoreRl;
        @Bind(R.id.recommend_title_bar_rl)
        RelativeLayout recommendTitleBarRl;
   @Bind(R.id.recommend_title_center_rtg)
    RecommendTitleGroup recommendTitleCenterRtg;*/
    @Bind(R.id.recommend_body_vp)
    NoScrollViewPager recommendBodyVp;
    @Bind(R.id.title_content_tv)
    TextView titleContentTv;
    @Bind(R.id.title_back_rl)
    RelativeLayout titleBackRl;
    @Bind(R.id.title_back_iv)
    ImageView titleBackIv;
    @Bind(R.id.title_more_rl)
    MoreButtonView titleMoreRl;
    private Fragment recoFragment;
    private Fragment classifyFragment;
    private ArrayList<CityModel> list;

    private int viewPagerIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        ButterKnife.bind(this);
        RecommendPresenter.getCityList(this, new RecommendPresenter.CityListListener() {
            @Override
            public void hasCityList(ArrayList<CityModel> list) {
                RecommendActivity.this.list = list;
                initView();
            }
        });
        titleMoreRl.setMoreClick(this);
    }

    @OnClick({R.id.title_back_rl})
    public void titleClick(View view) {
        switch (view.getId()) {


            case R.id.title_back_rl:
                if (viewPagerIndex == 0) {
                    recommendBodyVp.setCurrentItem(1);
                    viewpagerSelected(1);
                } else {
                    LocationSelectedDialogFg lDialog = LocationSelectedDialogFg.newInstance();
                    lDialog.show(getFragmentManager(), "blur_sample");
                }
                break;
        }
    }

    private void initView() {
        InitViewPager();
        if (StringUtils.isEmpty(SPUtils.getValueFromSPMap(this, AppKey.USERSELECTEDCLASSIFY, ""))) {
       /*     recommendTitleCenterRtg.setInitSelected(true);
            recommendTitleCenterRtg.setViewsClickable(false);*/
            recommendBodyVp.setCurrentItem(1);
            viewpagerSelected(1);

        } else {
      /*      recommendTitleCenterRtg.setViewsClickable(true);
            recommendTitleCenterRtg.setInitSelected(false);*/
            recommendBodyVp.setCurrentItem(0);
            viewpagerSelected(0);

        }
        /*recommendTitleCenterRtg.setSelectedListener(new RecommendTitleGroup.RecoSelectedistener() {
            @Override
            public void onViewsClick(int flag) {
                if (flag == 0) {
                    LogUtils.json("selected == right");
                    viewpagerSelected(1);

                } else {
                    viewpagerSelected(0);
                }
            }
        });
        recommendTitleMoreRl.setMoreClick(new MoreButtonView.MoreClicklistener() {
            @Override
            public void moreClick() {
                RecommendActivity.this.startActivity(new Intent(RecommendActivity.this, MenusActivity.class));
            }
        });*/
    }

    /*
   * 初始化ViewPager
   */
    public void InitViewPager() {
        ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
        recoFragment = new RecommendFragment();
        classifyFragment = new ClassifyFragment();
        fragmentList.add(recoFragment);
        fragmentList.add(classifyFragment);
        //给ViewPager设置适配器
        recommendBodyVp.setAdapter(new RecommendFragmentAdapter(getSupportFragmentManager(), fragmentList));
        recommendBodyVp.setPageTransformer(true, new ZoomOutPageTransformer());
    }

    private void viewpagerSelected(int index) {
        if (index == 0) {
            LogUtils.json("selected == left");
            titleContentTv.setText(SPUtils.getValueFromSPMap(this, AppKey.USERSELECTEDCLASSIFY_CHS, ""));
            titleBackIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_title_back));
            recommendBodyVp.setCurrentItem(0);//设置当前显示标签页为第一页
            if (classifyFragment != null)
                ((ClassifyFragment) classifyFragment).hintClassify();
        } else if (index == 1) {
            titleBackIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_recommed_title_location));
            titleContentTv.setText("你会喜欢什么类型?");
            recommendBodyVp.setCurrentItem(1);//设置当前显示标签页为第二页
            if (classifyFragment != null)
                ((ClassifyFragment) classifyFragment).showClassify();
        }
        viewPagerIndex = index;
    }


    /**
     * 选中分类后处理跳转及数据刷新
     */
    public void selectedClassify() {
     /*   recommendTitleCenterRtg.selectedLeft();
        recommendTitleCenterRtg.setViewsClickable(true);*/
        //  recommendBodyVp.setCurrentItem(0);
        viewpagerSelected(0);
        if (recoFragment != null) {
            ((RecommendFragment) recoFragment).changeDataSetFromServer();
        }
    }
}
