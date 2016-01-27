package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.update.UmengUpdateAgent;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.analysis.UserAnalysisUtils;
import co.quchu.quchu.base.ActManager;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.LocationSelectedDialogFg;
import co.quchu.quchu.model.CityModel;
import co.quchu.quchu.net.NetUtil;
import co.quchu.quchu.presenter.RecommendPresenter;
import co.quchu.quchu.utils.AppKey;
import co.quchu.quchu.utils.KeyboardUtils;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.view.adapter.RecommendFragmentAdapter;
import co.quchu.quchu.view.fragment.ClassifyFragment;
import co.quchu.quchu.view.fragment.DefaultRecommendFragment;
import co.quchu.quchu.view.fragment.RecommendFragment;
import co.quchu.quchu.widget.AnimationViewPager.NoScrollViewPager;
import co.quchu.quchu.widget.AnimationViewPager.ZoomOutPageTransformer;
import co.quchu.quchu.widget.MoreButtonView;
import co.quchu.quchu.widget.RecommendTitleGroup;

/**
 * RecommendActivity
 * User: Chenhs
 * Date: 2015-12-07
 * 趣处分类、推荐
 */
public class RecommendActivity extends BaseActivity {
    @Bind(R.id.recommend_title_location_rl)
    RelativeLayout recommendTitleLocationRl;
    @Bind(R.id.recommend_title_location_iv)
    ImageView recommendTitleLocationIv;

    @Bind(R.id.title_content_tv)
    TextView titleContentTv;
    @Bind(R.id.recommend_title_more_rl)
    MoreButtonView recommendTitleMoreRl;
    @Bind(R.id.recommend_title_center_rtg)
    RecommendTitleGroup recommendTitleCenterRtg;
    @Bind(R.id.recommend_body_vp)
    NoScrollViewPager recommendBodyVp;


 /*   @Bind(R.id.title_content_tv)
    TextView titleContentTv;
    @Bind(R.id.title_back_rl)
    RelativeLayout titleBackRl;
    @Bind(R.id.title_back_iv)
    ImageView titleBackIv;
    @Bind(R.id.title_more_rl)
    MoreButtonView titleMoreRl;*/

    public long firstTime = 0;
    private RecommendFragment recoFragment;
    private ClassifyFragment classifyFragment;
    DefaultRecommendFragment defaultRecommendFragment;
    private ArrayList<CityModel> list;
    private boolean isGuide = false;
    public int viewPagerIndex = 0;
    public static final int PlaceDetail = 0x11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        ButterKnife.bind(this);
        isGuide = getIntent().getBooleanExtra("isGuide", false);
        if (isGuide) {
            startActivity(new Intent(this, PlanetActivity.class));
        } else {
            reconnection();
        }
        RecommendPresenter.getCityList(this, new RecommendPresenter.CityListListener() {
            @Override
            public void hasCityList(ArrayList<CityModel> list) {
                RecommendActivity.this.list = list;
                initView();
            }
        });
        recommendTitleMoreRl.setMoreClick(this);
        recommendBodyVp.setOffscreenPageLimit(2);
        UmengUpdateAgent.setUpdateListener(null);
        UmengUpdateAgent.update(AppContext.mContext);
        UmengUpdateAgent.setUpdateCheckConfig(true);
    }

    @OnClick({R.id.recommend_title_location_rl})
    public void titleClick(View view) {
        if (KeyboardUtils.isFastDoubleClick())
            return;
        switch (view.getId()) {
            case R.id.recommend_title_location_rl:
                if (viewPagerIndex == 2) {
                    recommendBodyVp.setCurrentItem(1);
                    viewpagerSelected(1);
                } else {
                    if (list != null) {
                        showCityDialog();
                    } else {
                        RecommendPresenter.getCityList(this, new RecommendPresenter.CityListListener() {
                            @Override
                            public void hasCityList(ArrayList<CityModel> list) {
                                RecommendActivity.this.list = list;
                                if (RecommendActivity.this.list != null) {
                                    showCityDialog();
                                }
                            }
                        });
                    }
                }
                break;
        }
    }

    private void showCityDialog() {
        LocationSelectedDialogFg lDialog = LocationSelectedDialogFg.newInstance(list);
        lDialog.show(getFragmentManager(), "blur_sample");
    }

    private void initView() {
        InitViewPager();
      /*  if (StringUtils.isEmpty(SPUtils.getValueFromSPMap(this, AppKey.USERSELECTEDCLASSIFY, ""))) {
            recommendTitleCenterRtg.setInitSelected(true);
            recommendTitleCenterRtg.setViewsClickable(false);
            recommendBodyVp.setCurrentItem(1);
            viewpagerSelected(1);

        } else {*/
        recommendTitleCenterRtg.setViewsClickable(true);
        recommendTitleCenterRtg.setInitSelected(false);
        recommendBodyVp.setCurrentItem(0);
        viewpagerSelected(0);

      /*  }*/
        recommendTitleCenterRtg.setSelectedListener(new RecommendTitleGroup.RecoSelectedistener() {
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
        });

    }


    /*
   * 初始化ViewPager
   */
    public void InitViewPager() {
        ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
        recoFragment = new RecommendFragment();
        classifyFragment = new ClassifyFragment();
        defaultRecommendFragment = new DefaultRecommendFragment();
        fragmentList.add(recoFragment);
        fragmentList.add(classifyFragment);
        fragmentList.add(defaultRecommendFragment);
        //给ViewPager设置适配器
        recommendBodyVp.setAdapter(new RecommendFragmentAdapter(getSupportFragmentManager(), fragmentList));
        recommendBodyVp.setPageTransformer(true, new ZoomOutPageTransformer());
    }

    //http://119.29.108.45:8080/appservices/login/android?j_username=13966682939&j_password=e10adc3949ba59abbe56e057f20f883e&equip=00000000-1e98-8990-03cd-75f30033c587
    private void viewpagerSelected(int index) {
        LogUtils.json("selected == " + index);
        if (index == 0) {
            recommendTitleLocationIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_recommed_title_location));
            recommendBodyVp.setCurrentItem(0);//设置当前显示标签页为第一页
            titleContentTv.setVisibility(View.INVISIBLE);
            recommendTitleCenterRtg.setViewVisibility(View.VISIBLE);
            if (recoFragment != null)
                recoFragment.show();
            if (classifyFragment != null)
                ((ClassifyFragment) classifyFragment).hintClassify();
            if (defaultRecommendFragment != null)
                defaultRecommendFragment.hint();
        } else if (index == 1) {
            recommendTitleLocationIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_recommed_title_location));
            recommendTitleCenterRtg.setViewVisibility(View.VISIBLE);
            titleContentTv.setVisibility(View.INVISIBLE);
            recommendBodyVp.setCurrentItem(1);//设置当前显示标签页为第二页
            if (classifyFragment != null)
                ((ClassifyFragment) classifyFragment).showClassify();
            if (recoFragment != null)
                recoFragment.hint();
            if (defaultRecommendFragment != null)
                defaultRecommendFragment.hint();
        } else if (index == 2) {
            recommendTitleLocationIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_title_back));
            titleContentTv.setText(SPUtils.getValueFromSPMap(this, AppKey.USERSELECTEDCLASSIFY_CHS, ""));
            titleContentTv.setVisibility(View.VISIBLE);
            recommendTitleCenterRtg.setViewVisibility(View.INVISIBLE);
            recommendBodyVp.setCurrentItem(2);
            if (defaultRecommendFragment != null)
                defaultRecommendFragment.show();
            if (classifyFragment != null)
                ((ClassifyFragment) classifyFragment).hintClassify();
            if (recoFragment != null)
                recoFragment.hint();
        }
        viewPagerIndex = index;
    }


    /**
     * 选中分类后处理跳转及数据刷新
     */
    public void selectedClassify() {
  /*      recommendTitleCenterRtg.selectedLeft();
        recommendTitleCenterRtg.setViewsClickable(true);*/
        //  recommendBodyVp.setCurrentItem(0);
        if (defaultRecommendFragment != null) {
            defaultRecommendFragment.changeDataSetFromServer();
        }
        viewpagerSelected(2);
    }

    public void updateRecommend() {
        if (recoFragment == null)
            recoFragment = new RecommendFragment();
        recoFragment.changeDataSetFromServer();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (viewPagerIndex == 2) {
                viewpagerSelected(1);
                return true;
            } else {
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 700) {// 如果两次按键时间间隔大于800毫秒，则不退出
                    Toast.makeText(RecommendActivity.this, R.string.app_exit_text,
                            Toast.LENGTH_SHORT).show();
                    firstTime = secondTime;// 更新firstTime

                    return true;
                } else {
                    UserAnalysisUtils.sendUserBehavior(RecommendActivity.this);
                    ActManager.getAppManager().AppExit();
                    AppContext.stopLocation();
                }
            }
        }
        return true;
    }

    @Override
    protected void onResume() {
        LogUtils.json("RecommendActivity  onResume===" + viewPagerIndex);
        if (isGuide) {
            isGuide = false;
            //    initView();
        }
        resumeUpdateDataTimes = 0;
        netHandler.sendMessageDelayed(netHandler.obtainMessage(0x02), 200);
        super.onResume();
    }

    @Override
    protected void onPause() {
        LogUtils.json("RecommendActivity  onPause");
        super.onPause();
    }

    private Handler netHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x00:
                    //    initView();
                    netHandler.sendMessageDelayed(netHandler.obtainMessage(0x01), 2000);
                    break;
                case 0x01:
                    reconnection();
                    break;
                case 0x02:
                    resumeUpdateData();
                    break;
            }
        }
    };
    private int resumeUpdateDataTimes = 0;

    private void resumeUpdateData() {
        if (AppContext.dCardListRemoveIndex != -1) {
            LogUtils.json("RecommendActivity onResume==removePosition==" + AppContext.dCardListRemoveIndex);
            if (viewPagerIndex == 0) {
                recoFragment.removeDataSet(AppContext.dCardListRemoveIndex);
            } else if (viewPagerIndex == 2) {
                defaultRecommendFragment.removeDataSet(AppContext.dCardListRemoveIndex);
            }
            AppContext.dCardListRemoveIndex = -1;
            AppContext.dCardListNeedUpdate = false;
            resumeUpdateDataTimes = 0;
        } else if (AppContext.dCardListNeedUpdate) {
            if (viewPagerIndex == 0) {
                recoFragment.updateDateSet();
            } else if (viewPagerIndex == 2) {
                defaultRecommendFragment.updateDateSet();
            }
            AppContext.dCardListNeedUpdate = false;
            resumeUpdateDataTimes = 0;
        } else {
            if (resumeUpdateDataTimes <= 3) {
                resumeUpdateDataTimes++;
                netHandler.sendMessageDelayed(netHandler.obtainMessage(0x02), 200);
            }
        }
    }

    /**
     * 无网络状态 时开启监听
     */
    public void reconnection() {
        if (NetUtil.isNetworkConnected(this)) {
            if (isNetWorkConnected)
                initView();
            isNetWorkConnected = false;
        } else {
            netHandler.sendMessageDelayed(netHandler.obtainMessage(0x00), 6000);
            isNetWorkConnected = true;
        }
    }

    private boolean isNetWorkConnected = false;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //  ButterKnife.unbind(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case PlaceDetail:

                break;
        }
    }
}
