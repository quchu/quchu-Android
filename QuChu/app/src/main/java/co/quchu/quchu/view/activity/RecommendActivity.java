package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.VolleyError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.BuildConfig;
import co.quchu.quchu.R;
import co.quchu.quchu.base.ActManager;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.AppLocationListener;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.base.GeTuiReceiver;
import co.quchu.quchu.dialog.ShareDialogFg;
import co.quchu.quchu.model.CityEntity;
import co.quchu.quchu.model.CityModel;
import co.quchu.quchu.model.PushMessageBean;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.model.SceneInfoModel;
import co.quchu.quchu.model.UpdateInfoModel;
import co.quchu.quchu.net.NetUtil;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.MessagePresenter;
import co.quchu.quchu.presenter.RecommendPresenter;
import co.quchu.quchu.presenter.VersionInfoPresenter;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.view.adapter.SceneListAdapter;
import co.quchu.quchu.view.fragment.AIConversationFragment;
import co.quchu.quchu.widget.DrawerHeaderView;
import co.quchu.quchu.widget.DrawerItemView;
import co.quchu.quchu.widget.DrawerMenu;
import co.quchu.quchu.widget.XiaoQFab;

/**
 * RecommendActivity
 * User: Chenhs
 * Date: 2015-12-07
 * 趣处分类、推荐
 */
public class RecommendActivity extends BaseBehaviorActivity {

  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.appbar) AppBarLayout appbar;
  @Bind(R.id.rvScene) RecyclerView mRvScene;
  @Bind(R.id.fab) XiaoQFab fab;
  @Bind(R.id.tvCity) TextView tvCity;
  @Bind(R.id.drawer_layout) DrawerLayout mDrawer;

  @Bind(R.id.drawerHeaderView) DrawerHeaderView mDrawerHeaderView;
  @Bind(R.id.drawerItemFavorite) DrawerItemView mDrawerItemFavorite;
  @Bind(R.id.drawerItemUserCenter) DrawerItemView mDrawerItemUserCenter;
  @Bind(R.id.drawerItemMessage) DrawerItemView mDrawerItemMessage;
  @Bind(R.id.drawerItemFeedback) DrawerItemView mDrawerItemFeedback;
  @Bind(R.id.drawerItemSetting) DrawerItemView mDrawerItemSetting;
  @Bind(R.id.drawerItemShareApp) DrawerItemView mDrawerItemShareApp;
  @Bind(R.id.placeHolder) View placeHolder;

  @Bind(R.id.vFakeDrawer) DrawerMenu vFakeDrawer;
  @Bind(R.id.vDrawer) DrawerMenu vDrawer;
  @Bind(R.id.ivSearch) View ivSearch;
  @Bind(R.id.ivSwitchCity) View ivSwitchCity;
  @Bind(R.id.ivAllScene) View ivAllScene;
  @Bind(R.id.vSearchBar) View vSearchBar;

  public static final String REQUEST_KEY_FROM_LOGIN = "REQUEST_KEY_FROM_LOGIN";
  public static final String BUNDLE_KEY_FROM_PUSH = "BUNDLE_KEY_FROM_PUSH";


  public long firstTime = 0;
  boolean checkUpdateRunning = false;
  private ArrayList<CityModel> mCityList = new ArrayList<>();
  private CityEntity mCityEntity;
  private List<SceneInfoModel> mAllSceneList = new ArrayList<>();
  private AIConversationFragment mAIContent;

  @Override
  protected String getPageNameCN() {
    return null;
  }

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
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    EventBus.getDefault().register(this);

    initPush();

    initDrawerView();

    getSceneList();

    checkForceUpdate();

    initCity();

    appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
      @Override
      public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        float offset = Math.abs(verticalOffset);
        float progress = offset / appbar.getTotalScrollRange();

        placeHolder.setAlpha(progress);


        mRvScene.setAlpha(1 - progress);
        toolbar.setAlpha(1 - progress);
        //toolbar.setTranslationY(verticalOffset);
        if (null!=mAIContent){
          mAIContent.resetOffset(appbar.getTotalScrollRange() - offset);
          mAIContent.resetOffsetPassive(offset);
        }
      }
    });

    initFragment();

    startIntentIfFromPush(getIntent());
  }

  @Override
  protected void onResume() {
    resumeUpdateDataTimes = 0;
    netHandler.sendMessageDelayed(netHandler.obtainMessage(0x02), 200);
    super.onResume();

    getUnreadMessage();
  }

  private void startIntentIfFromPush(Intent intent) {
    if (null != intent && intent.getBooleanExtra(BUNDLE_KEY_FROM_PUSH, false)) {
      startActivity(MessageCenterActivity.class);
    }
  }

  /**
   * 添加Fragment
   */
  private void initFragment() {
    if (null==mAIContent){
      mAIContent = new AIConversationFragment();
    }
    getSupportFragmentManager().beginTransaction().add(R.id.flContainer, mAIContent).commitAllowingStateLoss();
  }

  /**
   * 获得场景列表
   */
  private void getSceneList() {
    RecommendPresenter.getSceneList(this, new CommonListener<List<SceneInfoModel>>() {
      @Override
      public void successListener(List<SceneInfoModel> response) {
        mAllSceneList.clear();
        mAllSceneList.addAll(response);
        SceneListAdapter adapter = new SceneListAdapter(getApplicationContext(), mAllSceneList, 4);
        mRvScene.setAdapter(adapter);
        mRvScene.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));
        adapter.setOnSceneListListener(new SceneListAdapter.OnSceneListListener() {
          @Override
          public void onItemClick(SceneInfoModel sceneInfoModel, int position) {
            if (position == 3) {
              SceneListActivity.launch(RecommendActivity.this, mAllSceneList);
              return;
            }

            SceneDetailActivity.enterActivity(RecommendActivity.this, sceneInfoModel.getSceneId(), sceneInfoModel.getSceneName(), true);
          }
        });
      }

      @Override
      public void errorListener(VolleyError error, String exception, String msg) {
      }
    });
  }

  /**
   * 初始化推送
   */
  private void initPush() {
    if (!getIntent().getBooleanExtra(REQUEST_KEY_FROM_LOGIN, false)) {
      accessPushMessage();
    }
  }

  /**
   * 检查更新
   */
  private void checkForceUpdate() {
    VersionInfoPresenter.getIfForceUpdate(getApplicationContext());
  }

  /**
   * 获取城市列表
   */
  private void initCity() {
    tvCity.setText(SPUtils.getCityName());

    RecommendPresenter.getCityList(this, new RecommendPresenter.CityListListener() {
      @Override
      public void hasCityEntity(CityEntity response) {
        mCityList.clear();
        mCityList.addAll(response.getPage().getResult());
        mCityEntity = response;
        checkIfCityChanged();
      }
    });
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);

    //登录和退出登录,开启 RecommendActivity 会进入这里,做相关操作

    LogUtils.e("RecommendActivity", "onNewIntent");

    if (mDrawerHeaderView != null) {
      mDrawerHeaderView.setUser();
      mDrawerHeaderView.getGenes();
    }
    showDrawerItemUserCenter();

    getUnreadMessage();

    startIntentIfFromPush(intent);
  }

  /**
   * 初始化 DrawerView
   */
  private void initDrawerView() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      mDrawer.setDrawerElevation(0);
    } else {
      mDrawer.setDrawerShadow(new ColorDrawable(Color.TRANSPARENT), GravityCompat.START);
    }

    //ActionBarDrawerToggle toggle =
    //    new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    //mDrawer.addDrawerListener(toggle);
    //toggle.syncState();

    mDrawerHeaderView.setUser();
    mDrawerHeaderView.getGenes();
    showDrawerItemUserCenter();

    mDrawerHeaderView.setOnDrawerHeaderClickListener(new DrawerHeaderView.OnDrawerHeaderClickListener() {
      @Override
      public void onLoginClick() {
        startActivity(LoginActivity.class);
      }
    });
  }

  /**
   * 设置侧滑菜单特定文本
   */
  private void showDrawerItemUserCenter() {
    if (AppContext.user == null || (AppContext.user != null && AppContext.user.isIsVisitors())) {
      mDrawerItemUserCenter.setVisibility(View.GONE);
    } else {
      mDrawerItemUserCenter.setVisibility(View.VISIBLE);
    }
  }

  /**
   * 获取未读消息
   */
  private void getUnreadMessage() {
    MessagePresenter.getUnreadMessageCount(this, new MessagePresenter.OnMessagePresenterListener() {
      @Override
      public void onUnreadMessageCount(int msgCount, int feedbackMsgCount) {
        showMsgUnreadView(msgCount);

        showFeedbackUnreadView(feedbackMsgCount);

        if (msgCount > 0 || feedbackMsgCount > 0) {
          vDrawer.showRedDot();
          vFakeDrawer.showRedDot();
        } else {
          vDrawer.hideRedDot();
          vFakeDrawer.hideRedDot();
        }
      }
    });
  }

  /**
   * 意见反馈未读消息
   */
  private void showFeedbackUnreadView(int feedbackMsgCount) {
    if (mDrawerItemMessage == null) {
      return;
    }

    if (feedbackMsgCount > 0)
      mDrawerItemFeedback.showRedDot(feedbackMsgCount);
    else
      mDrawerItemFeedback.hideRedDot();
  }

  /**
   * 消息未读消息数
   */
  private void showMsgUnreadView(int msgCount) {
    if (mDrawerItemMessage == null) {
      return;
    }

    if (msgCount > 0) {
      mDrawerItemMessage.showRedDot(msgCount);
    } else {
      mDrawerItemMessage.hideRedDot();
    }
  }

  public void accessPushMessage() {
    Parcelable extra = getIntent().getParcelableExtra(GeTuiReceiver.REQUEST_KEY_MODEL);
    if (extra == null) return;

    PushMessageBean bean = (PushMessageBean) extra;
    //            说明： 类型：( 01 PGC新内容发布  02  新场景发布  03 事件营销 )
    //            eventId  : 根据类别，打开应用相应页面的ID  type: 01 为文章ID  02:场景ID  03：文章ID
    switch (bean.getType()) {
      case "01":
        //rbBottomTab.check(R.id.rbDiscovery);
        ArticleDetailActivity.enterActivity(this, bean.getEventId(), bean.getTitle(), "场景");
        break;
      case "03":
        //rbBottomTab.check(R.id.rbDiscovery);
        break;
    }
  }

  private void checkIfCityChanged() {
    if (null != mCityList && null != AppLocationListener.currentCity) {
      String currentLocation = AppLocationListener.currentCity;
      int cityIdInList = -1;
      if (currentLocation.endsWith("市")) {
        currentLocation = currentLocation.substring(0, currentLocation.length() - 1);
      }

      boolean inList = false;
      for (int i = 0; i < mCityList.size(); i++) {
        if (mCityList.get(i).getCvalue().equals(currentLocation)) {
          inList = true;
          cityIdInList = mCityList.get(i).getCid();
        }
      }

      if (!currentLocation.equals(SPUtils.getCityName())) {
        if (!inList) {//城市列表中没有当前位置
          new MaterialDialog.Builder(this).title("切换城市")
              .content("您所在的城市并没有收录，我们已经为你切换至默认城市厦门")
              .positiveText("知道了")
              .cancelable(false)
              .onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                }
              })
              .show();

        } else {//城市列表中有但不是当前位置
          final boolean finalInList = inList;
          final int finalCityIdInList = cityIdInList;
          final String finalCurrentLocation = currentLocation;

          new MaterialDialog.Builder(this).title("切换城市")
              .content("检测到你在" + currentLocation + "，是否切换？")
              .positiveText("确定")
              .negativeText("取消")
              .cancelable(false)
              .onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                  if (finalInList) {
                    SPUtils.setCityId(finalCityIdInList);
                    SPUtils.setCityName(finalCurrentLocation);
                    tvCity.setText(SPUtils.getCityName());
                  } else {
                    tvCity.performClick();
                  }
                }
              })
              .show();
        }
      }
    }
  }

  @Override
  protected int activitySetup() {
    return TRANSITION_TYPE_NOTHING;
  }

  @OnClick({R.id.tvCity, R.id.vSearchBar, R.id.vFakeDrawer, R.id.vDrawer,
      R.id.ivSearch, R.id.ivAllScene, R.id.ivSwitchCity, R.id.fab,
      R.id.drawerItemFavorite, R.id.drawerItemUserCenter, R.id.drawerItemMessage,
      R.id.drawerItemFeedback, R.id.drawerItemSetting, R.id.drawerItemShareApp})
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.ivAllScene:
        if (null != mAllSceneList && mAllSceneList.size() >= 1) {
          SceneListActivity.launch(RecommendActivity.this, mAllSceneList);
        }
        break;

      case R.id.vDrawer:
        mDrawer.openDrawer(GravityCompat.START);
        break;
      case R.id.vFakeDrawer:
        if (placeHolder.getAlpha() == 1) {
          mDrawer.openDrawer(GravityCompat.START);
        }
        break;

      case R.id.ivSearch:
      case R.id.vSearchBar://搜索
        SearchActivityNew.launch(RecommendActivity.this, mAllSceneList);
        break;

      case R.id.ivSwitchCity:
      case R.id.tvCity://选择城市
        UMEvent("location_c");
        if (NetUtil.isNetworkConnected(getApplicationContext())) {
          if (mCityList != null) {
            selectedCity();
          } else {
            RecommendPresenter.getCityList(this, new RecommendPresenter.CityListListener() {
              @Override
              public void hasCityEntity(CityEntity response) {
                RecommendActivity.this.mCityList = response.getPage().getResult();
                if (RecommendActivity.this.mCityList != null) {
                  selectedCity();
                }
              }
            });
          }
        } else {
          Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
        break;

      case R.id.fab://历史记录
        startActivity(QuChuHistoryActivity.class);
        break;

      case R.id.drawerItemFavorite://收藏
        startActivity(FavoriteActivity.class);
        break;

      case R.id.drawerItemUserCenter://个人中心
        startActivity(MeActivity.class);
        break;

      case R.id.drawerItemMessage://消息
//        if (mDrawerItemMessage.getRedDotVisibility() == View.VISIBLE) {
//          mDrawerItemMessage.hideRedDot();
//        }

        startActivity(MessageCenterActivity.class);
        break;

      case R.id.drawerItemFeedback://意见反馈
//        if (mDrawerItemFeedback.getRedDotVisibility() == View.VISIBLE) {
//          mDrawerItemFeedback.hideRedDot();
//        }

        startActivity(FeedbackActivity.class);
        break;

      case R.id.drawerItemSetting://设置
        startActivity(SettingActivity.class);
        break;

      case R.id.drawerItemShareApp://分享 App
        ShareDialogFg shareDialogFg = ShareDialogFg.newInstance("http://www.quchu.co/shareApp/", "趣处", "");
        shareDialogFg.show(getSupportFragmentManager(), "share_dialog");
        break;
    }
  }

  /**
   * 切换城市
   */
  private void selectedCity() {
    if (mCityEntity != null) {
      SelectedCityActivity.launch(this, mCityEntity);
    }
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
    switch (event.getFlag()) {
      case EventFlags.EVENT_NEW_CITY_SELECTED:
        ArrayMap<String, Object> arrayMap = new ArrayMap<>();
        arrayMap.put("城市名称", SPUtils.getCityName());
        ZGEvent(arrayMap, "选择城市");
        tvCity.setText(SPUtils.getCityName());
        break;

//      case EventFlags.EVENT_USER_LOGIN_SUCCESS:
//        //登录成功更新用户信息
//        if (mDrawerHeaderView != null) {
//          mDrawerHeaderView.setUser();
//          mDrawerHeaderView.getUserInfo();
//        }
//        break;
//
//      case EventFlags.EVENT_USER_LOGOUT:
//        //退出登录更新用户信息
//        if (mDrawerHeaderView != null) {
//          mDrawerHeaderView.setUser();
//          mDrawerHeaderView.getUserInfo();
//        }
//        break;

      case EventFlags.EVENT_APPLICATION_CHECK_UPDATE:
        if (!checkUpdateRunning) {
          checkUpdateRunning = true;
          VersionInfoPresenter.checkUpdate(getApplicationContext(),
              new CommonListener<UpdateInfoModel>() {
                @Override
                public void successListener(final UpdateInfoModel response) {
                  checkUpdateRunning = false;
                  if (BuildConfig.VERSION_CODE < response.getVersionCode()) {

                    new MaterialDialog.Builder(RecommendActivity.this).title("有新版本更新")
                        .content("检测到有新版本，是否下载更新？")
                        .positiveText("立即前往")
                        .negativeText("容我三思")
                        .cancelable(false)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                          @Override
                          public void onClick(@NonNull MaterialDialog dialog,
                                              @NonNull DialogAction which) {
                            Intent browserIntent =
                                new Intent(Intent.ACTION_VIEW, Uri.parse(response.getDownUrl()));
                            startActivity(browserIntent);
                          }
                        })
                        .show();
                  } else {
                    Toast.makeText(getApplicationContext(), R.string.no_update_available,
                        Toast.LENGTH_LONG).show();
                  }
                }

                @Override
                public void errorListener(VolleyError error, String exception, String msg) {
                  Toast.makeText(getApplicationContext(), R.string.network_error, Toast.LENGTH_LONG)
                      .show();
                  checkUpdateRunning = false;
                }
              });
        }
        break;

      case EventFlags.EVENT_USER_INFO_UPDATE:
        //用户信息更新
        if (mDrawerHeaderView != null) {
          mDrawerHeaderView.setUser();
          mDrawerHeaderView.getGenes();
        }
        break;
    }
  }


  @Override
  protected void onDestroy() {
    super.onDestroy();
    EventBus.getDefault().unregister(this);

    ButterKnife.unbind(this);
  }

  @Override
  public void onBackPressed() {
    if (mDrawer.isDrawerOpen(GravityCompat.START)) {
      mDrawer.closeDrawer(GravityCompat.START);
    } else {
      long secondTime = System.currentTimeMillis();
      if (secondTime - firstTime > 700) {// 如果两次按键时间间隔大于800毫秒，则不退出
        Toast.makeText(RecommendActivity.this, R.string.app_exit_text, Toast.LENGTH_SHORT).show();
        firstTime = secondTime;// 更新firstTime
      } else {
//        new IMPresenter().disconnect();
        ActManager.getAppManager().AppExit();
        super.onBackPressed();
      }
    }
  }

}
