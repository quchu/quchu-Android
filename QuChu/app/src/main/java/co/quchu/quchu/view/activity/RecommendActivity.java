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
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.base.GeTuiReceiver;
import co.quchu.quchu.im.IMPresenter;
import co.quchu.quchu.model.CityModel;
import co.quchu.quchu.model.PushMessageBean;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.model.UpdateInfoModel;
import co.quchu.quchu.model.UserCenterInfo;
import co.quchu.quchu.model.UserInfoModel;
import co.quchu.quchu.net.NetUtil;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.MeActivityPresenter;
import co.quchu.quchu.presenter.RecommendPresenter;
import co.quchu.quchu.presenter.UserCenterPresenter;
import co.quchu.quchu.presenter.VersionInfoPresenter;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.utils.KeyboardUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.view.fragment.RecommendFragment;
import co.quchu.quchu.widget.DrawerHeaderView;
import co.quchu.quchu.widget.DrawerItemView;

/**
 * RecommendActivity
 * User: Chenhs
 * Date: 2015-12-07
 * 趣处分类、推荐
 */
public class RecommendActivity extends BaseBehaviorActivity {

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

  boolean checkUpdateRunning = false;

  public static final String REQUEST_KEY_FROM_LOGIN = "REQUEST_KEY_FROM_LOGIN";

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

    recommendTitleLocationIv.setText(SPUtils.getCityName());
    recommendFragment = new RecommendFragment();

    getSupportFragmentManager().beginTransaction()
        .add(R.id.container, recommendFragment, "page_1")
        .commitAllowingStateLoss();

    initDrawerView();

    getUnreadMessage();

    getUserInfo();

    VersionInfoPresenter.getIfForceUpdate(getApplicationContext());

    RecommendPresenter.getCityList(this, new RecommendPresenter.CityListListener() {
      @Override
      public void hasCityList(ArrayList<CityModel> pList) {
        list.clear();
        list.addAll(pList);
        checkIfCityChanged();
      }
    });

    if (!getIntent().getBooleanExtra(REQUEST_KEY_FROM_LOGIN, false)) {
      accessPushMessage();
    }
  }

  /**
   * 获取用户信息,只为了取 mark 字段
   * 登录接口限制,后期根据接口调整
   */
  private void getUserInfo() {
    if (AppContext.user == null) {
      return;
    }

    int userId = AppContext.user.getUserId();

    UserCenterPresenter
        .getUserCenterInfo(this, userId, new UserCenterPresenter.UserCenterInfoCallBack() {
          @Override
          public void onSuccess(UserCenterInfo userCenterInfo) {
            if (userCenterInfo != null) {
              String mark = userCenterInfo.getMark();
              SPUtils.setUserMark(mark);
              mDrawerHeaderView.setMark(mark);
            } else {
              SPUtils.setUserMark("");
            }
          }

          @Override
          public void onError() {
          }
        });
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
            startActivity(MeActivity.class);
//            if (AppContext.user != null) {
//              if (AppContext.user.isIsVisitors()) {
//                startActivity(new Intent(RecommendActivity.this, LoginActivity.class));
//              } else {
//                UMEvent("profile_c");
//                Intent intent = new Intent(RecommendActivity.this, UserCenterActivityNew.class);
//                intent.putExtra(UserCenterActivityNew.REQUEST_KEY_USER_ID, Integer.valueOf(AppContext.user.getUserId()));
//                startActivity(intent);
//              }
//            }
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
          case R.id.drawerItemFavorite://收藏
            startActivity(FavoriteActivity.class);
            break;

          case R.id.drawerItemUserCenter://个人中心
            if (AppContext.user == null) {
              return;
            }

            Intent intent = new Intent(RecommendActivity.this, UserCenterActivityNew.class);
            intent.putExtra(UserCenterActivityNew.REQUEST_KEY_USER_ID, AppContext.user.getUserId());
            startActivity(intent);
            break;

          case R.id.drawerItemMessage://消息
            if (mDrawerItemMessage.getRedDotVisibility() == View.VISIBLE) {
              mDrawerItemMessage.hideRedDot();
            }

            startActivity(MessageActivityNew.class);
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
        }
      }
    }, 200);
  }

  /**
   * 获取未读消息
   */
  private void getUnreadMessage() {
    new MeActivityPresenter(this).getUnreadMassageCound(new MeActivityPresenter.OnUnreadMassageCountListener() {
      @Override
      public void onUnreadMassageCount(int msgCount, int feedbackMsgCount) {
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
        if (mDrawer != null) {
          mDrawer.openDrawer(GravityCompat.START);
        }
        break;

      case R.id.recommend_title_more_iv:
        startActivity(SearchActivityNew.class);
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
