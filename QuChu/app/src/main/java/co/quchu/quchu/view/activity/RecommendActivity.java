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
import android.support.design.widget.FloatingActionButton;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import co.quchu.quchu.widget.XiaoQFab;
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
import co.quchu.quchu.model.CityModel;
import co.quchu.quchu.model.PushMessageBean;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.model.SceneInfoModel;
import co.quchu.quchu.model.UpdateInfoModel;
import co.quchu.quchu.net.NetUtil;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.MeActivityPresenter;
import co.quchu.quchu.presenter.RecommendPresenter;
import co.quchu.quchu.presenter.VersionInfoPresenter;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.view.fragment.AIConversationFragment;
import co.quchu.quchu.widget.DrawerHeaderView;
import co.quchu.quchu.widget.DrawerItemView;

/**
 * RecommendActivity
 * User: Chenhs
 * Date: 2015-12-07
 * 趣处分类、推荐
 */
public class RecommendActivity extends BaseBehaviorActivity {

  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.appbar) AppBarLayout appbar;
  @Bind(R.id.llShit) LinearLayout llShit;
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

  @Bind(R.id.vFakeDrawer) View vLeft;
  @Bind(R.id.vFakeSearchBar) View vRight;
  @Bind(R.id.vSearchBar) View vSearchBar;

  public static final String REQUEST_KEY_FROM_LOGIN = "REQUEST_KEY_FROM_LOGIN";
  public long firstTime = 0;
  boolean checkUpdateRunning = false;
  private ArrayList<CityModel> list = new ArrayList<>();

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


    initPush();

    initDrawerView();

    getUnreadMessage();

    getSceneList();

    checkForceUpdate();

    tvCity.setText(SPUtils.getCityName());


    appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
      @Override
      public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        float offset = Math.abs(verticalOffset);
        float progress = offset / appbar.getTotalScrollRange();

        placeHolder.setAlpha(progress);
      }
    });

    initFragment();
  }

  /**
   * 添加Fragment
   */
  private void initFragment() {
    getSupportFragmentManager().beginTransaction().add(R.id.flContainer, new AIConversationFragment()).commitAllowingStateLoss();
  }

  /**
   * 获得场景列表
   */
  private void getSceneList() {
    RecommendPresenter.getSceneList(getApplicationContext(), new CommonListener<List<SceneInfoModel>>() {
      @Override
      public void successListener(List<SceneInfoModel> response) {

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
    RecommendPresenter.getCityList(this, new RecommendPresenter.CityListListener() {
      @Override
      public void hasCityList(ArrayList<CityModel> pList) {
        list.clear();
        list.addAll(pList);
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

    getUnreadMessage();
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

    ActionBarDrawerToggle toggle =
        new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    mDrawer.addDrawerListener(toggle);
    toggle.syncState();

    mDrawerHeaderView.setUser();
    mDrawerHeaderView.getGenes();

    mDrawerItemFavorite.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onDrawerItemClick(v);
      }
    });

    mDrawerItemUserCenter.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onDrawerItemClick(v);
      }
    });

    mDrawerItemMessage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onDrawerItemClick(v);
      }
    });

    mDrawerItemFeedback.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onDrawerItemClick(v);
      }
    });

    mDrawerItemSetting.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onDrawerItemClick(v);
      }
    });

    mDrawerItemShareApp.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onDrawerItemClick(v);
      }
    });
  }

  /**
   * Drawer Item Click
   */
  private void onDrawerItemClick(final View v) {
    mDrawer.closeDrawer(GravityCompat.START);

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        switch (v.getId()) {
          case R.id.drawerItemFavorite://收藏
            startActivity(FavoriteActivity.class);
            break;

          case R.id.drawerItemUserCenter://个人中心
            if (AppContext.user != null && !AppContext.user.isIsVisitors()) {
              startActivity(MeActivity.class);
            } else {
              startActivity(LoginActivity.class);
            }
            break;

          case R.id.drawerItemMessage://消息
            if (mDrawerItemMessage.getRedDotVisibility() == View.VISIBLE) {
              mDrawerItemMessage.hideRedDot();
            }

            startActivity(MessageCenterActivity.class);
            break;

          case R.id.drawerItemFeedback://意见反馈
            if (mDrawerItemFeedback.getRedDotVisibility() == View.VISIBLE) {
              mDrawerItemFeedback.hideRedDot();
            }

            startActivity(FeedbackActivity.class);
            break;

          case R.id.drawerItemSetting://设置
            startActivity(SettingActivity.class);
            break;

          case R.id.drawerItemShareApp://分享 App
            startActivity(SearchActivityNew.class);
            break;
        }
      }
    }, 200);
  }

  /**
   * 获取未读消息
   */
  private void getUnreadMessage() {
    new MeActivityPresenter(this).getUnreadMessageCound(new MeActivityPresenter.OnUnreadMessageCountListener() {
      @Override
      public void onUnreadMessageCount(int msgCount, int feedbackMsgCount) {
        showMsgUnreadView(msgCount);

        showFeedbackUnreadView(feedbackMsgCount);
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
        //                if (!inList) {
        //城市列表中没有当前位置
        //                    confirmDialogFg = ConfirmDialogFg.newInstance("切换城市", "你当前所在的城市尚未占领，是否要切换城市");
        //                } else
        if (inList) {                    //城市列表中有但不是当前位置

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

  @OnClick({R.id.tvCity,R.id.vSearchBar,R.id.vFakeDrawer,R.id.vFakeSearchBar})
  public void onClick(View view) {
    switch (view.getId()) {

      case R.id.vFakeDrawer:
        if (placeHolder.getAlpha()==1){
          mDrawer.openDrawer(GravityCompat.START);
        }
        break;
      case R.id.vFakeSearchBar:
        if (placeHolder.getAlpha()==1){
          startActivity(SearchActivityNew.class);
        }
        break;

      case R.id.vSearchBar:
        startActivity(SearchActivityNew.class);
        break;

      case R.id.tvCity:
        UMEvent("location_c");
        if (NetUtil.isNetworkConnected(getApplicationContext())) {
          if (list != null) {
            selectedCity();
          } else {
            RecommendPresenter.getCityList(this, new RecommendPresenter.CityListListener() {
              @Override
              public void hasCityList(ArrayList<CityModel> list) {
                RecommendActivity.this.list = list;
                if (RecommendActivity.this.list != null) {
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
    }
  }

  //@OnClick({
  //    R.id.recommend_title_location_rl, R.id.tvRight, R.id.ivLeft, R.id.recommend_title_more_iv
  //})
  //public void titleClick(View view) {
  //  if (KeyboardUtils.isFastDoubleClick()) return;
  //
  //  switch (view.getId()) {
  //
  //    case R.id.ivLeft:
  //      if (mDrawer != null) {
  //        mDrawer.openDrawer(GravityCompat.START);
  //      }
  //      break;
  //
  //    case R.id.recommend_title_more_iv:
  //      startActivity(SearchActivityNew.class);
  //      break;
  //
  //    case R.id.tvRight:
  //      UserInfoModel user = AppContext.user;
  //      if (user.isIsVisitors()) {
  //        //游客
  //        startActivity(new Intent(RecommendActivity.this, LoginActivity.class));
  //      } else {
  //        UMEvent("profile_c");
  //        startActivity(new Intent(RecommendActivity.this, AccountSettingActivity.class));
  //      }
  //      break;
  //
  //  }
  //}

  /**
   * 切换城市
   */
  private void selectedCity() {
    SelectedCityActivity.launch(this, list);
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
        }
        break;
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
