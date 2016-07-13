package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.ArrayMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.ActManager;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.AppLocationListener;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.base.GeTuiReceiver;
import co.quchu.quchu.dialog.ConfirmDialogFg;
import co.quchu.quchu.dialog.LocationSelectedDialogFg;
import co.quchu.quchu.dialog.MenuSettingDialogFg;
import co.quchu.quchu.dialog.VisitorLoginDialogFg;
import co.quchu.quchu.model.CityModel;
import co.quchu.quchu.model.PushMessageBean;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.model.UserInfoModel;
import co.quchu.quchu.net.NetUtil;
import co.quchu.quchu.presenter.RecommendPresenter;
import co.quchu.quchu.presenter.VersionInfoPresenter;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.utils.KeyboardUtils;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.view.fragment.ArticleFragment;
import co.quchu.quchu.view.fragment.RecommendFragment;

/**
 * RecommendActivity
 * User: Chenhs
 * Date: 2015-12-07
 * 趣处分类、推荐
 */
public class RecommendActivity extends BaseBehaviorActivity {
    @Bind(R.id.recommend_title_location_tv)
    TextView recommendTitleLocationIv;

    @Bind(R.id.recommend_title_more_iv)
    ImageView recommendTitleMoreRl;

    @Bind(R.id.recommend_title_location_rl)
    View vLeft;

    @Bind(R.id.container)
    FrameLayout flContainer;
    @Bind(R.id.ivArrow)
    ImageView ivArrow;

    @Bind(R.id.rgTab)
    RadioGroup rbBottomTab;
    @Bind(R.id.title)
    View vTitle;

    @Bind(R.id.tvTitle)
    TextView tvTitle;

    @Bind(R.id.ivDivider)
    View vDivider;
    @Bind(R.id.ivLeft)
    View ivLeft;
    @Bind(R.id.tvRight)
    TextView tvRight;

    public long firstTime = 0;
    private ArrayList<CityModel> list = new ArrayList<>();
    public int viewPagerIndex = 0;
    private RecommendFragment recommendFragment;
    private ArticleFragment articleFragment;
    private MeFragment meFragment;

    public static final String REQUEST_KEY_FROM_LOGIN = "REQUEST_KEY_FROM_LOGIN";

    @Override
    public ArrayMap<String, Object> getUserBehaviorArguments() {
        return null;
    }

    @Override
    public int getUserBehaviorPageId() {
        return 110;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        ButterKnife.bind(this);


        recommendTitleLocationIv.setText(SPUtils.getCityName());
        recommendFragment = new RecommendFragment();
        articleFragment = new ArticleFragment();
        meFragment = new MeFragment();

        List<Fragment> fragments = getSupportFragmentManager().getFragments();


        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, recommendFragment, "page_1")
                .commitAllowingStateLoss();
        initView();


        UmengUpdateAgent.setUpdateListener(null);
        UmengUpdateAgent.update(AppContext.mContext);
        UmengUpdateAgent.setUpdateCheckConfig(true);

        VersionInfoPresenter.getIfForceUpdate(getApplicationContext());


        RecommendPresenter.getCityList(this, new RecommendPresenter.CityListListener() {
            @Override
            public void hasCityList(ArrayList<CityModel> pList) {
                list.clear();
                list.addAll(pList);
                checkIfCityChanged();
            }
        });

        if (getIntent().getBooleanExtra(REQUEST_KEY_FROM_LOGIN, false)) {
            rbBottomTab.check(R.id.rbMine);
            if (!meFragment.isAdded()) {
                getSupportFragmentManager().beginTransaction().add(R.id.container, meFragment, "page_3").commitAllowingStateLoss();
            }
            viewpagerSelected(2);

        }


        rbBottomTab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (checkedId) {

                    case R.id.rbRecommend:
                        viewpagerSelected(0);
                        break;
                    case R.id.rbDiscovery:
                        if (!articleFragment.isAdded()) {
                            transaction.add(R.id.container, articleFragment, "page_2").commitAllowingStateLoss();
                        }
                        viewpagerSelected(1);
                        break;
                    case R.id.rbMine:
                        if (!meFragment.isAdded()) {
                            transaction.add(R.id.container, meFragment, "page_3").commitAllowingStateLoss();
                        }
                        viewpagerSelected(2);
                        break;
                }
            }
        });

        if (!getIntent().getBooleanExtra(REQUEST_KEY_FROM_LOGIN, false)) {
            accessPushMessage();
        }

    }


    public void accessPushMessage() {
        Parcelable extra = getIntent().getParcelableExtra(GeTuiReceiver.REQUEST_KEY_MODEL);
        if (extra == null)
            return;

        PushMessageBean bean = (PushMessageBean) extra;
//            说明： 类型：( 01 PGC新内容发布  02  新场景发布  03 事件营销 )
//            eventId  : 根据类别，打开应用相应页面的ID  type: 01 为文章ID  02:场景ID  03：文章ID
        switch (bean.getType()) {
            case "01":
                rbBottomTab.check(R.id.rbDiscovery);
                ArticleDetailActivity.enterActivity(this, bean.getEventId());
                break;
            case "03":
                rbBottomTab.check(R.id.rbDiscovery);
                break;
        }

    }

    private void checkIfCityChanged() {
        if (null != list && null != AppLocationListener.currentCity) {
            String currentLocation = AppLocationListener.currentCity;
            int cityIdInList = -1;
            if (currentLocation.endsWith("市")) {
                currentLocation = currentLocation.substring(0, currentLocation.length() - 1);
            }
            boolean inList = false;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getCvalue().equals(currentLocation)) {
                    inList = true;
                    cityIdInList = list.get(i).getCid();
                }
            }
            if (!currentLocation.equals(SPUtils.getCityName())) {
                ConfirmDialogFg confirmDialogFg = null;
                if (!inList) {
                    //城市列表中没有当前位置
                    confirmDialogFg = ConfirmDialogFg.newInstance("切换城市", "你当前所在的城市尚未占领，是否要切换城市");
                } else if (inList) {
                    //城市列表中有但不是当前位置
                    confirmDialogFg = ConfirmDialogFg.newInstance("切换城市", "检测到你在" + currentLocation + "，是否切换？");
                }
                final boolean finalInList = inList;
                final int finalCityIdInList = cityIdInList;
                final String finalCurrentLocation = currentLocation;
                confirmDialogFg.setActionListener(new ConfirmDialogFg.OnActionListener() {
                    @Override
                    public void onClick(int index) {
                        if (ConfirmDialogFg.INDEX_OK == index) {
                            if (finalInList) {
                                SPUtils.setCityId(finalCityIdInList);
                                SPUtils.setCityName(finalCurrentLocation);
                                updateRecommend();
                                recommendTitleLocationIv.setText(SPUtils.getCityName());
                            } else {
                                findViewById(R.id.recommend_title_location_rl).performClick();
                            }

                        }
                    }
                });
                confirmDialogFg.show(getSupportFragmentManager(), "~");
            }
        }
    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }

    @OnClick({R.id.recommend_title_location_rl, R.id.tvRight, R.id.ivLeft, R.id.recommend_title_more_iv})
    public void titleClick(View view) {
        if (KeyboardUtils.isFastDoubleClick())
            return;
        switch (view.getId()) {


            case R.id.recommend_title_more_iv:
                startActivity(new Intent(RecommendActivity.this, SearchActivity.class));
                break;

            case R.id.ivLeft:
                MenuSettingDialogFg.newInstance().show(getSupportFragmentManager(), "~");
                break;

            case R.id.tvRight:
                UserInfoModel user = AppContext.user;
                if (user.isIsVisitors()) {
                    //游客
                    VisitorLoginDialogFg dialogFg = VisitorLoginDialogFg.newInstance(0);
                    dialogFg.show(getSupportFragmentManager(), "");
                } else {
                    startActivity(new Intent(RecommendActivity.this, AccountSettingActivity.class));
                }
                break;
            case R.id.recommend_title_location_rl:
                MobclickAgent.onEvent(this, "location_c");

                if (NetUtil.isNetworkConnected(getApplicationContext())) {
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
                } else {
                    Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show();
                }


                break;
        }
    }


    private void showCityDialog() {
        ivArrow.animate().rotation(180).setDuration(300).setInterpolator(new AccelerateDecelerateInterpolator()).start();
        LocationSelectedDialogFg lDialog = LocationSelectedDialogFg.newInstance(list);
        lDialog.show(getSupportFragmentManager(), "blur_sample");
        lDialog.setOnDissMissListener(new LocationSelectedDialogFg.OnDissMissListener() {
            @Override
            public void onDissMiss() {
                ivArrow.animate().rotation(0).setDuration(300).setInterpolator(new AccelerateDecelerateInterpolator()).start();
            }
        });

    }

    private void initView() {
        viewpagerSelected(0);
    }


    private void viewpagerSelected(int index) {
        LogUtils.json("selected == " + index);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (index == 0) {
            tvTitle.setText("");
            transaction.setCustomAnimations(R.anim.default_dialog_in, R.anim.default_dialog_out);
            transaction.hide(articleFragment).hide(meFragment).show(recommendFragment).commitAllowingStateLoss();
            vTitle.animate().translationY(0).setDuration(300).withStartAction(new Runnable() {
                @Override
                public void run() {
                    vTitle.setVisibility(View.VISIBLE);
                }
            }).start();
            vDivider.setVisibility(View.VISIBLE);

            vLeft.setVisibility(View.VISIBLE);
            recommendTitleMoreRl.setVisibility(View.VISIBLE);
            ivLeft.setVisibility(View.GONE);
            tvRight.setVisibility(View.GONE);

        } else if (index == 1) {
            transaction.setCustomAnimations(R.anim.default_dialog_in, R.anim.default_dialog_out);
            transaction.hide(recommendFragment).hide(meFragment).show(articleFragment).commitAllowingStateLoss();
            vTitle.animate().translationY(-vTitle.getHeight()).setDuration(300).withEndAction(new Runnable() {
                @Override
                public void run() {
                    vTitle.setVisibility(View.GONE);
                }
            }).start();
            vDivider.setVisibility(View.GONE);

        } else if (index == 2) {
            if (null != AppContext.user && !AppContext.user.isIsVisitors()) {
                tvTitle.setText(AppContext.user.getFullname());
            } else {
                tvTitle.setText("未知生物");
            }
//            transaction.setCustomAnimations(R.anim.default_dialog_in, R.anim.default_dialog_out);
            transaction.hide(articleFragment).hide(recommendFragment).show(meFragment).commitAllowingStateLoss();
            vTitle.animate().translationY(0).setDuration(300).withStartAction(new Runnable() {
                @Override
                public void run() {
                    vTitle.setVisibility(View.VISIBLE);
                }
            }).start();

            vDivider.setVisibility(View.VISIBLE);
            vLeft.setVisibility(View.GONE);
            recommendTitleMoreRl.setVisibility(View.GONE);

            ivLeft.setVisibility(View.VISIBLE);
            tvRight.setVisibility(View.VISIBLE);

        }
        viewPagerIndex = index;
    }


    /**
     * 城市切换后调用
     */
    public void updateRecommend() {
        //recommendFragment.initData();
        //TODO refresh
        articleFragment.getArticles();
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
                ActManager.getAppManager().AppExit();
            }

        }
        return true;
    }

    @Override
    protected void onResume() {
        resumeUpdateDataTimes = 0;
        netHandler.sendMessageDelayed(netHandler.obtainMessage(0x02), 200);
        super.onResume();
    }


    private Handler netHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x00:
                    netHandler.sendMessageDelayed(netHandler.obtainMessage(0x01), 2000);
                    break;
            }
        }
    };
    private int resumeUpdateDataTimes = 0;


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
