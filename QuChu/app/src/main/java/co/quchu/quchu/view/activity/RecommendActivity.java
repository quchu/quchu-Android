package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.presenter.RecommendPresenter;
import co.quchu.quchu.presenter.VersionInfoPresenter;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.utils.KeyboardUtils;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.view.fragment.ClassifyFragment;
import co.quchu.quchu.view.fragment.RecommendFragment;
import co.quchu.quchu.widget.RecommendTitleGroup;
import cz.msebera.android.httpclient.util.VersionInfo;

/**
 * RecommendActivity
 * User: Chenhs
 * Date: 2015-12-07
 * 趣处分类、推荐
 */
public class RecommendActivity extends BaseActivity {
    @Bind(R.id.recommend_title_location_tv)
    TextView recommendTitleLocationIv;

    @Bind(R.id.title_content_tv)
    TextView titleContentTv;
    @Bind(R.id.recommend_title_more_iv)
    ImageView recommendTitleMoreRl;
    @Bind(R.id.recommend_title_center_rtg)
    RecommendTitleGroup recommendTitleCenterRtg;
    @Bind(R.id.search_bar)
    RelativeLayout rlSearchBar;
    @Bind(R.id.search_input_et)
    TextView tvSearch;

    public long firstTime = 0;
    private ArrayList<CityModel> list;
    private boolean isGuide = false;
    public int viewPagerIndex = 0;
    private RecommendFragment recommendFragment;
    private ClassifyFragment classifyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        ButterKnife.bind(this);
        isGuide = getIntent().getBooleanExtra("isGuide", false);
        if (isGuide) {
            startActivity(new Intent(this, PlanetActivity.class));
        }


        recommendTitleLocationIv.setText(SPUtils.getCityName());
        recommendFragment = new RecommendFragment();
        classifyFragment = new ClassifyFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.container, recommendFragment, null).add(R.id.container, classifyFragment, null).hide(classifyFragment).commit();

        initView();
        enableRightButton();
//        RecommendPresenter.getCityList(this, new RecommendPresenter.CityListListener() {
//            @Override
//            public void hasCityList(ArrayList<CityModel> list) {
//                RecommendActivity.this.list = list;
//            }
//        });

        recommendTitleMoreRl.setOnClickListener(this);
        UmengUpdateAgent.setUpdateListener(null);
        UmengUpdateAgent.update(AppContext.mContext);
        UmengUpdateAgent.setUpdateCheckConfig(true);
        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecommendActivity.this,SearchActivity.class));
            }
        });

//        startActivity(new Intent(RecommendActivity.this,AddFootprintActivity.class));

        VersionInfoPresenter.getIfForceUpdate(getApplicationContext());
    }

    @Override
    protected int activitySetup() {
        return 0;
    }

    @OnClick({R.id.recommend_title_location_rl})
    public void titleClick(View view) {
        if (KeyboardUtils.isFastDoubleClick())
            return;
        switch (view.getId()) {
            case R.id.recommend_title_location_rl:
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

                break;
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.recommend_title_more_iv:
                Intent intent = new Intent(this, MeActivity.class);
                intent.putExtra(MyFootprintActivity.REQUEST_KEY_USER_ID, AppContext.user.getUserId());
                intent.putExtra(MyFootprintActivity.REQUEST_KEY_USER_AGE, AppContext.user.getAge());
                intent.putExtra(MyFootprintActivity.REQUEST_KEY_USER_FOOTER_COUND, AppContext.user.getCardCount());
                intent.putExtra(MyFootprintActivity.REQUEST_KEY_USER_PHOTO, AppContext.user.getPhoto());
                startActivity(intent);
                break;
        }
    }

    private void showCityDialog() {
        LocationSelectedDialogFg lDialog = LocationSelectedDialogFg.newInstance(list);
        lDialog.show(getFragmentManager(), "blur_sample");
    }

    private void initView() {

        recommendTitleCenterRtg.setViewsClickable(true);
        recommendTitleCenterRtg.setInitSelected(false);
        viewpagerSelected(0);

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

    }


    private void viewpagerSelected(int index) {
        LogUtils.json("selected == " + index);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (index == 0) {
            tvSearch.animate()
                    .translationY(-tvSearch.getHeight())
                    .alpha(0)
                    .setDuration(300)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .withStartAction(new Runnable() {
                        @Override
                        public void run() {
                            rlSearchBar.setVisibility(View.GONE);
                        }
                    })
                    .start();

            //recommendTitleLocationIv.setImageResource(R.mipmap.ic_recommed_title_location);
            titleContentTv.setVisibility(View.INVISIBLE);
            recommendTitleCenterRtg.setViewVisibility(View.VISIBLE);

            transaction.
//                    setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out, R.anim.fragment_in, R.anim.fragment_out).
        hide(classifyFragment).show(recommendFragment).commit();

        } else {
            rlSearchBar.setVisibility(View.VISIBLE);

            transaction
//                    .setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out, R.anim.fragment_in, R.anim.fragment_out)
                    .hide(recommendFragment).show(classifyFragment).commit();

            tvSearch.animate()
                    .translationY(0)
                    .alpha(1)
                    .setDuration(300)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .withStartAction(new Runnable() {
                        @Override
                        public void run() {
                        }
                    })
                    .start();
            //recommendTitleLocationIv.setImageResource(R.mipmap.ic_recommed_title_location);
            recommendTitleCenterRtg.setViewVisibility(View.VISIBLE);
            titleContentTv.setVisibility(View.INVISIBLE);
        }
        viewPagerIndex = index;
    }


    /**
     * 城市切换后调用
     */
    public void updateRecommend() {
        recommendFragment.initData();
        classifyFragment.getRootTagsData();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
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
        return true;
    }

    @Override
    protected void onResume() {
        if (isGuide) {
            isGuide = false;
        }
        resumeUpdateDataTimes = 0;
        netHandler.sendMessageDelayed(netHandler.obtainMessage(0x02), 200);
        super.onResume();
        MobclickAgent.onPageStart("MainActivity");


    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MainActivity");
    }


    private Handler netHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x00:
                    netHandler.sendMessageDelayed(netHandler.obtainMessage(0x01), 2000);
                    break;
                case 0x02:
                    resumeUpdateData();
                    break;
            }
        }
    };
    private int resumeUpdateDataTimes = 0;

    private void resumeUpdateData() {
        if (AppContext.dCardListNeedUpdate) {
            if (viewPagerIndex == 0) {
                recommendFragment.updateDateSet();
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


    @Subscribe
    public void onMessageEvent(QuchuEventModel event) {
        if (event.getFlag() == EventFlags.EVENT_NEW_CITY_SELECTED) {
            recommendTitleLocationIv.setText(SPUtils.getCityName());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
