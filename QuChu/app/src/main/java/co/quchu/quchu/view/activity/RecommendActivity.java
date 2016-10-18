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
import android.support.v4.util.ArrayMap;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.VolleyError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.BuildConfig;
import co.quchu.quchu.R;
import co.quchu.quchu.base.ActManager;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.AppLocationListener;
import co.quchu.quchu.base.GeTuiReceiver;
import co.quchu.quchu.im.IMPresenter;
import co.quchu.quchu.im.activity.ImMainActivity;
import co.quchu.quchu.model.CityModel;
import co.quchu.quchu.model.PushMessageBean;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.model.UpdateInfoModel;
import co.quchu.quchu.model.UserInfoModel;
import co.quchu.quchu.net.NetUtil;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.MeActivityPresenter;
import co.quchu.quchu.presenter.RecommendPresenter;
import co.quchu.quchu.presenter.VersionInfoPresenter;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.utils.KeyboardUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.view.fragment.ArticleFragment;
import co.quchu.quchu.view.fragment.RecommendFragment;
import co.quchu.quchu.widget.DrawerHeaderView;
import co.quchu.quchu.widget.DrawerItemView;
import io.rong.imkit.RongIM;

/**
 * RecommendActivity
 * User: Chenhs
 * Date: 2015-12-07
 * 趣处分类、推荐
 */
public class RecommendActivity extends ImMainActivity {

  @Override
  protected String getPageNameCN() {
    return null;
  }

  @Bind(R.id.recommend_title_location_tv) TextView recommendTitleLocationIv;

  @Bind(R.id.recommend_title_more_iv) ImageView recommendTitleMoreRl;

  @Bind(R.id.recommend_title_location_rl) View vLeft;

  @Bind(R.id.container) FrameLayout flContainer;
  @Bind(R.id.ivArrow) ImageView ivArrow;

  @Bind(R.id.rgTab) RadioGroup rbBottomTab;
  @Bind(R.id.title) View vTitle;

  @Bind(R.id.tvTitle) TextView tvTitle;

  @Bind(R.id.ivLeft) View ivLeft;
  @Bind(R.id.tvRight) TextView tvRight;

  @Bind(R.id.rbMine) RadioButton mRbMine;
  @Bind(R.id.unReadMassage) TextView mUnReadMassageView;
  @Bind(R.id.drawer_layout) DrawerLayout mDrawer;

  @Bind(R.id.drawerHeaderView) DrawerHeaderView mDrawerHeaderView;
  @Bind(R.id.drawerItemFavorite) DrawerItemView mDrawerItemFavorite;
  @Bind(R.id.drawerItemUserCenter) DrawerItemView mDrawerItemUserCenter;
  @Bind(R.id.drawerItemMessage) DrawerItemView mDrawerItemMessage;
  @Bind(R.id.drawerItemFeedback) DrawerItemView mDrawerItemFeedback;
  @Bind(R.id.drawerItemSetting) DrawerItemView mDrawerItemSetting;

  public long firstTime = 0;
  private ArrayList<CityModel> list = new ArrayList<>();
  public int viewPagerIndex = 0;
  private RecommendFragment recommendFragment;
  private ArticleFragment articleFragment;
  private NewMeFragment meFragment;
  private SearchFragment searchFragment;

  boolean checkUpdateRunning = false;

  private boolean mHasPushUnreadMessage;//个推消息
  private boolean mHasImUnreadMessage;//im消息

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
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

//    if (null == AppContext.user || AppContext.user.isIsVisitors()) {
//      tvTitle.setText("未知生物");
//      tvRight.setText(R.string.login);
//    } else {
//      tvTitle.setText(AppContext.user.getFullname());
//      tvRight.setText(R.string.edit);
//    }

    recommendTitleLocationIv.setText(SPUtils.getCityName());
    recommendFragment = new RecommendFragment();
//    articleFragment = new ArticleFragment();
//    meFragment = new NewMeFragment();
//    searchFragment = new SearchFragment();

    getSupportFragmentManager().beginTransaction()
        .add(R.id.container, recommendFragment, "page_1")
        .commitAllowingStateLoss();

//    initView();

//    initUnreadView();

    initDrawerView();

    VersionInfoPresenter.getIfForceUpdate(getApplicationContext());

    RecommendPresenter.getCityList(this, new RecommendPresenter.CityListListener() {
      @Override
      public void hasCityList(ArrayList<CityModel> pList) {
        list.clear();
        list.addAll(pList);
        checkIfCityChanged();
      }
    });

//    rbBottomTab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//      @Override
//      public void onCheckedChanged(RadioGroup group, int checkedId) {
//        switch (checkedId) {
//
//          case R.id.rbRecommend:
//            viewpagerSelected(0);
//            break;
//          case R.id.rbDiscovery:
//
//            viewpagerSelected(1);
//            UMEvent("discovery_c");
//            break;
//          case R.id.rbSearch:
//
//            viewpagerSelected(2);
//            UMEvent("search_c");
//            break;
//          case R.id.rbMine:
//            viewpagerSelected(3);
//            break;
//        }
//      }
//    });

    if (!getIntent().getBooleanExtra(REQUEST_KEY_FROM_LOGIN, false)) {
      accessPushMessage();
    }
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

    mDrawerHeaderView.setUser(AppContext.user);
    mDrawerHeaderView.setOnDrawerAvatarClickListener(new DrawerHeaderView.OnDrawerAvatarClickListener() {
      @Override
      public void onAvatarClick() {
        mDrawer.closeDrawer(GravityCompat.START);

        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
            if (AppContext.user != null && AppContext.user.isIsVisitors()) {
              startActivity(new Intent(RecommendActivity.this, LoginActivity.class));

            } else {
              UMEvent("profile_c");
              startActivity(new Intent(RecommendActivity.this, AccountSettingActivity.class));
            }
          }
        }, 200);
      }
    });

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
          case R.id.drawerItemFavorite:
            startActivity(FavoriteActivity.class);
            break;

          case R.id.drawerItemUserCenter:
            if (AppContext.user == null) {
              return;
            }

            Intent intent = new Intent(RecommendActivity.this, UserCenterActivityNew.class);
            intent.putExtra(UserCenterActivityNew.REQUEST_KEY_USER_ID, AppContext.user.getUserId());
            startActivity(intent);
            break;

          case R.id.drawerItemMessage:
            if (mDrawerItemMessage.getRedDotVisibility() == View.VISIBLE) {
              mDrawerItemMessage.hideRedDot();
            }

            startActivity(MessageActivity.class);
            break;

          case R.id.drawerItemFeedback:
            startActivity(FeedbackActivity.class);
            break;

          case R.id.drawerItemSetting:
            startActivity(SettingActivity.class);
            break;
        }
      }
    }, 200);
  }

  /**
   * 设置我的Tab红点
   */
//  private void initUnreadView() {
//    final ViewTreeObserver observer = mRbMine.getViewTreeObserver();
//    observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//      @Override
//      public void onGlobalLayout() {
//        int width = mRbMine.getWidth();
//        int x = (int) mRbMine.getX();
//        int y = (int) mRbMine.getY();
//
//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mUnReadMassageView.getLayoutParams();
//        params.setMargins(x + width / 2 + 30, y, 0, 0);
//
//        if (observer.isAlive()) {
//          if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
//            observer.removeOnGlobalLayoutListener(this);
//          } else {
//            observer.removeGlobalOnLayoutListener(this);
//          }
//        }
//      }
//    });
//  }

  /**
   * 连接融云服务成功
   */
  @Override
  protected void onConnectImSuccess() {
    super.onConnectImSuccess();

    getUnreadMessage();
  }

  /**
   * 获取未读消息
   */
  private void getUnreadMessage() {
    //个推消息
    new MeActivityPresenter(this).getUnreadMassageCound(new CommonListener<Integer>() {
      @Override
      public void successListener(Integer response) {
        mHasPushUnreadMessage = response > 0 ? true : false;
        showUnreadView();
      }

      @Override
      public void errorListener(VolleyError error, String exception, String msg) {
      }
    });

    //IM消息
    new IMPresenter().getUnreadCount(new RongIM.OnReceiveUnreadCountChangedListener() {
      @Override
      public void onMessageIncreased(int i) {
        mHasPushUnreadMessage = false;
        mHasImUnreadMessage = i > 0 ? true : false;
        showUnreadView();
      }
    });
  }

  /**
   * 显示我的红点
   */
  private void showUnreadView() {
    if (mDrawerItemMessage == null) {
      return;
    }

    if (mHasPushUnreadMessage) {
      mDrawerItemMessage.showRedDot();
    } else {
      if (mHasImUnreadMessage) {
        mDrawerItemMessage.showRedDot();
      } else {
        mDrawerItemMessage.hideRedDot();
      }
    }
  }

  public void showUnreadView(boolean isShow) {
    if (mUnReadMassageView == null) {
      return;
    }

    mUnReadMassageView.setVisibility(isShow ? View.VISIBLE : View.GONE);
  }

  public void accessPushMessage() {
    Parcelable extra = getIntent().getParcelableExtra(GeTuiReceiver.REQUEST_KEY_MODEL);
    if (extra == null) return;

    PushMessageBean bean = (PushMessageBean) extra;
    //            说明： 类型：( 01 PGC新内容发布  02  新场景发布  03 事件营销 )
    //            eventId  : 根据类别，打开应用相应页面的ID  type: 01 为文章ID  02:场景ID  03：文章ID
    switch (bean.getType()) {
      case "01":
        rbBottomTab.check(R.id.rbDiscovery);
        ArticleDetailActivity.enterActivity(this, bean.getEventId(), bean.getTitle(), "场景");
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
                    updateRecommend();
                    recommendTitleLocationIv.setText(SPUtils.getCityName());
                  } else {
                    findViewById(R.id.recommend_title_location_rl).performClick();
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
    return TRANSITION_TYPE_LEFT;
  }

  @OnClick({
      R.id.recommend_title_location_rl, R.id.tvRight, R.id.ivLeft, R.id.recommend_title_more_iv
  })
  public void titleClick(View view) {
    if (KeyboardUtils.isFastDoubleClick()) return;
    switch (view.getId()) {

      case R.id.ivLeft:
        startActivity(SettingActivity.class);
        //                MenuSettingDialogFg.newInstance().show(getSupportFragmentManager(), "~");
        break;

      case R.id.tvRight:
        UserInfoModel user = AppContext.user;
        if (user.isIsVisitors()) {
          //游客
          startActivity(new Intent(RecommendActivity.this, LoginActivity.class));
        } else {
          UMEvent("profile_c");
          startActivity(new Intent(RecommendActivity.this, AccountSettingActivity.class));
        }
        break;
      case R.id.recommend_title_location_rl:

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
    }
  }

  /**
   * 切换城市
   */
  private void selectedCity() {
    SelectedCityActivity.launch(this, list);
  }

  /**
   * 城市切换后调用
   */
  public void updateRecommend() {
    //recommendFragment.initData();
    //TODO refresh
    articleFragment.getArticles(false);
  }

//  private void initView() {
//    viewpagerSelected(0);
//  }

//  private void viewpagerSelected(int index) {
//
//
//    LogUtils.json("selected == " + index);
//    recommendTitleMoreRl.setVisibility(View.GONE);
//
//    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//    transaction.setCustomAnimations(R.anim.default_dialog_in, R.anim.default_dialog_out);
//    if (index == 0) {
//      tvTitle.setText(R.string.app_name);
//      transaction.hide(articleFragment)
//          .hide(searchFragment)
//          .hide(meFragment)
//          .show(recommendFragment)
//          .commitAllowingStateLoss();
//
//
//      vLeft.setVisibility(View.VISIBLE);
//      ivLeft.setVisibility(View.GONE);
//      tvRight.setVisibility(View.GONE);
//      vTitle.setVisibility(View.VISIBLE);
//    } else if (index == 1) {
//      if (!articleFragment.isAdded()) {
//        transaction.add(R.id.container, articleFragment, "page_2");
//      }
//      transaction.hide(recommendFragment)
//          .hide(searchFragment)
//          .hide(meFragment)
//          .show(articleFragment)
//          .commitAllowingStateLoss();
//      ivLeft.setVisibility(View.GONE);
//      tvRight.setVisibility(View.GONE);
//      vLeft.setVisibility(View.GONE);
//      tvTitle.setText("趣发现");
//      vTitle.setVisibility(View.VISIBLE);
//    } else if (index == 2) {
//      if (!searchFragment.isAdded()) {
//        transaction.add(R.id.container, searchFragment, "page_3");
//      }
//      transaction.hide(articleFragment)
//          .hide(meFragment)
//          .hide(recommendFragment)
//          .show(searchFragment)
//          .commitAllowingStateLoss();
//      vTitle.setVisibility(View.GONE);
//    } else if (index == 3) {
//      if (!meFragment.isAdded()) {
//        transaction.add(R.id.container, meFragment, "page_4");
//      }
//      transaction.hide(articleFragment)
//          .hide(searchFragment)
//          .hide(recommendFragment)
//          .show(meFragment)
//          .commitAllowingStateLoss();
//      vLeft.setVisibility(View.GONE);
//
//      ivLeft.setVisibility(View.VISIBLE);
//      tvRight.setVisibility(View.VISIBLE);
//      vTitle.setVisibility(View.VISIBLE);
//
//      tvTitle.setText("");
//    }
//    viewPagerIndex = index;
//  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
      long secondTime = System.currentTimeMillis();
      if (secondTime - firstTime > 700) {// 如果两次按键时间间隔大于800毫秒，则不退出
        Toast.makeText(RecommendActivity.this, R.string.app_exit_text, Toast.LENGTH_SHORT).show();
        firstTime = secondTime;// 更新firstTime
        return true;
      } else {
        new IMPresenter().disconnect();

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
    switch (event.getFlag()) {
      case EventFlags.EVENT_NEW_CITY_SELECTED:
        ArrayMap<String, Object> arrayMap = new ArrayMap<>();
        arrayMap.put("城市名称", SPUtils.getCityName());
        ZGEvent(arrayMap, "选择城市");
        recommendTitleLocationIv.setText(SPUtils.getCityName());

        updateRecommend();
        break;

      case EventFlags.EVENT_USER_LOGIN_SUCCESS:
        if (viewPagerIndex == 3) {
          tvTitle.setText("");
          tvRight.setText(R.string.edit);
        }
        break;

      case EventFlags.EVENT_USER_LOGOUT:
        if (viewPagerIndex == 3) {
          tvTitle.setText("");
          tvRight.setText(R.string.login);
        }

        //用户退出登录
        if (mDrawerHeaderView != null) {
          mDrawerHeaderView.setUser(AppContext.user);
        }
        break;

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
          mDrawerHeaderView.setUser(AppContext.user);
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
      super.onBackPressed();
    }
  }

}
