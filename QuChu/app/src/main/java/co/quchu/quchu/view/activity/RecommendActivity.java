package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
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
import co.quchu.quchu.base.ActManager;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.AppLocationListener;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.ConfirmDialogFg;
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

/**
 * RecommendActivity
 * User: Chenhs
 * Date: 2015-12-07
 * 趣处分类、推荐
 */
public class RecommendActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.recommend_title_location_tv)
    TextView recommendTitleLocationIv;

    @Bind(R.id.recommend_title_more_iv)
    ImageView recommendTitleMoreRl;
    @Bind(R.id.recommend_title_center_rtg)
    RecommendTitleGroup recommendTitleCenterRtg;
    @Bind(R.id.search_bar)
    RelativeLayout rlSearchBar;
    @Bind(R.id.search_input_et)
    TextView tvSearch;
    @Bind(R.id.container)
    FrameLayout flContainer;
    @Bind(R.id.ivArrow)
    ImageView ivArrow;

    public long firstTime = 0;
    private ArrayList<CityModel> list = new ArrayList<>();
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
        recommendTitleLocationIv.setText(SPUtils.getCityName());
        recommendFragment = new RecommendFragment();
        classifyFragment = new ClassifyFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container, recommendFragment, null).add(R.id.container, classifyFragment, null).hide(classifyFragment).commit();
        initView();
        recommendTitleMoreRl.setOnClickListener(this);
        UmengUpdateAgent.setUpdateListener(null);
        UmengUpdateAgent.update(AppContext.mContext);
        UmengUpdateAgent.setUpdateCheckConfig(true);
        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecommendActivity.this, SearchActivity.class));
            }
        });
        VersionInfoPresenter.getIfForceUpdate(getApplicationContext());

        RecommendPresenter.getCityList(this, new RecommendPresenter.CityListListener() {
            @Override
            public void hasCityList(ArrayList<CityModel> pList) {
                list.clear();
                list.addAll(pList);
                checkIfCityChanged();
            }
        });

    }

    private void checkIfCityChanged(){
        if (null!=list && null!= AppLocationListener.currentCity){
            String currentLocation = AppLocationListener.currentCity;
            int cityIdInList = -1;
            if (currentLocation.endsWith("市")){
                currentLocation = currentLocation.substring(0,currentLocation.length()-1);
            }
            boolean inList = false;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getCvalue().equals(currentLocation)){
                    inList = true;
                    cityIdInList = list.get(i).getCid();
                }
            }
            if (!currentLocation.equals(SPUtils.getCityName())){
                ConfirmDialogFg confirmDialogFg = null;
                if (!inList){
                    //城市列表中没有当前位置
                    confirmDialogFg = ConfirmDialogFg.newInstance("切换城市","当前所在城市还没有开放服务，是否要切换城市");
                }else if(inList){
                    //城市列表中有但不是当前位置
                    confirmDialogFg = ConfirmDialogFg.newInstance("切换城市","你目前在"+currentLocation+"，是否切换到"+currentLocation);
                }
                final boolean finalInList = inList;
                final int finalCityIdInList = cityIdInList;
                final String finalCurrentLocation = currentLocation;
                confirmDialogFg.setActionListener(new ConfirmDialogFg.OnActionListener() {
                    @Override
                    public void onClick(int index) {
                        if (ConfirmDialogFg.INDEX_OK == index){
                            if (finalInList){
                                SPUtils.setCityId(finalCityIdInList);
                                SPUtils.setCityName(finalCurrentLocation);
                                updateRecommend();
                            }else{
                                findViewById(R.id.recommend_title_location_rl).performClick();
                            }

                        }
                    }
                });
                confirmDialogFg.show(getSupportFragmentManager(),"~");
            }
        }
    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }

    @OnClick({R.id.recommend_title_location_rl})
    public void titleClick(View view) {
        if (KeyboardUtils.isFastDoubleClick())
            return;
        switch (view.getId()) {
            case R.id.recommend_title_location_rl:
                MobclickAgent.onEvent(this, "location_c");
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
        switch (v.getId()) {
            case R.id.recommend_title_more_iv:
                MobclickAgent.onEvent(this, "Profile_c");
                Intent intent = new Intent(this, MeActivity.class);

                startActivity(intent);
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
                    .translationY(-rlSearchBar.getHeight())
                    .alpha(0)
                    .setDuration(300)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .start();
            flContainer.animate()
                    .translationY(0)
                    .scaleY(1)
                    .setDuration(300)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .start();

            transaction.setCustomAnimations(R.anim.default_dialog_in, R.anim.default_dialog_out);
            transaction.hide(classifyFragment).show(recommendFragment).commit();
        } else {

            tvSearch.animate()
                    .translationY(0)
                    .alpha(1)
                    .setDuration(300)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .start();
            flContainer.animate().translationY(rlSearchBar.getHeight()/2)
                    .scaleY(((float)flContainer.getHeight()-rlSearchBar.getHeight())/flContainer.getHeight())
                    .setDuration(300)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .start();
            transaction.setCustomAnimations(R.anim.default_dialog_in, R.anim.default_dialog_out);
            transaction .hide(recommendFragment).show(classifyFragment).commit();
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
                ActManager.getAppManager().AppExit();
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
