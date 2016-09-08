package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.ArrayMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import co.quchu.quchu.dialog.CommonDialog;
import co.quchu.quchu.im.IMPresenter;
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
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.view.fragment.ArticleFragment;
import co.quchu.quchu.view.fragment.RecommendFragment;
import io.rong.imkit.RongIM;

/**
 * RecommendActivity
 * User: Chenhs
 * Date: 2015-12-07
 * 趣处分类、推荐
 */
public class RecommendActivity extends BaseBehaviorActivity {

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

  @Bind(R.id.ivDivider) View vDivider;
  @Bind(R.id.ivLeft) View ivLeft;
  @Bind(R.id.tvRight) TextView tvRight;

  @Bind(R.id.rbMine) RadioButton mRbMine;
  @Bind(R.id.unReadMassage) TextView mUnReadMassageView;

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
    setContentView(R.layout.activity_recommend);
    ButterKnife.bind(this);

    if (null == AppContext.user || AppContext.user.isIsVisitors()) {
      tvTitle.setText("未知生物");
      tvRight.setText(R.string.login);
    } else {
      tvTitle.setText(AppContext.user.getFullname());
      tvRight.setText(R.string.edit);
    }

    recommendTitleLocationIv.setText(SPUtils.getCityName());
    recommendFragment = new RecommendFragment();
    articleFragment = new ArticleFragment();
    meFragment = new NewMeFragment();
    searchFragment = new SearchFragment();

    getSupportFragmentManager().beginTransaction().add(R.id.container, recommendFragment, "page_1")
        .commitAllowingStateLoss();
    initView();

    initUnreadView();

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
        getSupportFragmentManager().beginTransaction().add(R.id.container, meFragment, "page_3")
            .commitAllowingStateLoss();
      }
      viewpagerSelected(3);
    }

    //im推送跳转
    boolean isChat = getIntent().getBooleanExtra(SplashActivity.INTENT_KEY_IM_CHAT, false);
    boolean isChatList = getIntent().getBooleanExtra(SplashActivity.INTENT_KEY_IM_CHAT_LIST, false);
    if (isChat) {
      startChat();
    } else if (isChatList) {
      startChatList();
    }

    if (!isChat && !isChatList) {
      //连接融云服务
      new IMPresenter().getToken(this, new IMPresenter.RongYunBehaviorListener() {
        @Override
        public void onSuccess(String msg) {
          getUnreadMessage();
        }

        @Override
        public void onError() {

        }
      });
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
            UMEvent("discovery_c");
            break;
          case R.id.rbSearch:
            if (!searchFragment.isAdded()) {
              transaction.add(R.id.container, searchFragment, "page_3").commitAllowingStateLoss();
            }
            viewpagerSelected(2);
            UMEvent("search_c");
            break;
          case R.id.rbMine:
            if (!meFragment.isAdded()) {
              transaction.add(R.id.container, meFragment, "page_4").commitAllowingStateLoss();
            }
            viewpagerSelected(3);
            break;
        }
      }
    });

    if (!getIntent().getBooleanExtra(REQUEST_KEY_FROM_LOGIN, false)) {
      accessPushMessage();
    }
  }

  /**
   * 设置我的Tab红点
   */
  private void initUnreadView() {
    final ViewTreeObserver observer = mRbMine.getViewTreeObserver();
    observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
      @Override
      public void onGlobalLayout() {
        int width = mRbMine.getWidth();
        int x = (int) mRbMine.getX();
        int y = (int) mRbMine.getY();

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mUnReadMassageView.getLayoutParams();
        params.setMargins(x + width / 2 + 30, y, 0, 0);

        if (observer.isAlive()) {
          if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            observer.removeOnGlobalLayoutListener(this);
          } else {
            observer.removeGlobalOnLayoutListener(this);
          }
        }
      }
    });
  }

  private void getUnreadMessage() {
    //推送通知
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

    //im未读消息数
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
    if (mUnReadMassageView == null) {
      return;
    }

    if (mHasPushUnreadMessage) {
      mUnReadMassageView.setVisibility(View.VISIBLE);
    } else {
      if (mHasImUnreadMessage) {
        mUnReadMassageView.setVisibility(View.VISIBLE);
      } else {
        mUnReadMassageView.setVisibility(View.INVISIBLE);
      }
    }
  }

  public void showUnreadView(boolean isShow) {
    if (mUnReadMassageView == null) {
      return;
    }

    mUnReadMassageView.setVisibility(isShow ? View.VISIBLE : View.GONE);
  }

  /**
   * 开启聊天界面
   */
  public void startChat() {
    if (RongIM.getInstance() != null) {
      RongIM.getInstance().startPrivateChat(this, SPUtils.getRongYunTargetId(), SPUtils.getRongYunTitle());
    }
  }

  /**
   * 开启聊天列表
   */
  public void startChatList() {
    startActivity(MessageActivity.class);
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

          final CommonDialog commonDialog =
              CommonDialog.newInstance("切换城市", "检测到你在" + currentLocation + "，是否切换？", "确定", "取消");
          commonDialog.setListener(new CommonDialog.OnActionListener() {
            @Override
            public boolean dialogClick(int id) {
              switch (id) {
                case CommonDialog.CLICK_ID_ACTIVE:
                  if (finalInList) {
                    SPUtils.setCityId(finalCityIdInList);
                    SPUtils.setCityName(finalCurrentLocation);
                    updateRecommend();
                    recommendTitleLocationIv.setText(SPUtils.getCityName());
                  } else {
                    findViewById(R.id.recommend_title_location_rl).performClick();
                  }

                  break;
                case CommonDialog.CLICK_ID_PASSIVE:
                  commonDialog.dismiss();
                  break;
              }
              return true;
            }
          });
          commonDialog.setCancelable(false);
          commonDialog.show(getSupportFragmentManager(), "");
        }
      }
    }
  }

  @Override
  protected int activitySetup() {
    return TRANSITION_TYPE_LEFT;
  }

  @OnClick({
      R.id.recommend_title_location_rl, R.id.tvRight, R.id.ivLeft, R.id.recommend_title_more_iv})
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

  private void initView() {
    viewpagerSelected(0);
  }

  private void viewpagerSelected(int index) {
    LogUtils.json("selected == " + index);
    recommendTitleMoreRl.setVisibility(View.GONE);

    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    if (index == 0) {
      tvTitle.setText(R.string.app_name);
      transaction.setCustomAnimations(R.anim.default_dialog_in, R.anim.default_dialog_out);
      transaction.hide(articleFragment).hide(searchFragment).hide(meFragment)
          .show(recommendFragment).commitAllowingStateLoss();

      vDivider.setVisibility(View.GONE);

      vLeft.setVisibility(View.VISIBLE);
      ivLeft.setVisibility(View.GONE);
      tvRight.setVisibility(View.GONE);
      vTitle.setVisibility(View.VISIBLE);
    } else if (index == 1) {
      transaction.setCustomAnimations(R.anim.default_dialog_in, R.anim.default_dialog_out);
      transaction.hide(recommendFragment).hide(searchFragment).hide(meFragment)
          .show(articleFragment).commitAllowingStateLoss();
      ivLeft.setVisibility(View.GONE);
      tvRight.setVisibility(View.GONE);
      vLeft.setVisibility(View.GONE);
      tvTitle.setText("趣发现");
      vDivider.setVisibility(View.VISIBLE);
      vTitle.setVisibility(View.VISIBLE);
    } else if (index == 2) {
      transaction.setCustomAnimations(R.anim.default_dialog_in, R.anim.default_dialog_out);
      transaction.hide(articleFragment).hide(meFragment).hide(recommendFragment)
          .show(searchFragment).commitAllowingStateLoss();
      vTitle.setVisibility(View.GONE);
    } else if (index == 3) {
      transaction.setCustomAnimations(R.anim.default_dialog_in, R.anim.default_dialog_out);
      transaction.hide(articleFragment).hide(searchFragment).hide(recommendFragment)
          .show(meFragment).commitAllowingStateLoss();
      vDivider.setVisibility(View.VISIBLE);
      vLeft.setVisibility(View.GONE);

      ivLeft.setVisibility(View.VISIBLE);
      tvRight.setVisibility(View.VISIBLE);
      vTitle.setVisibility(View.VISIBLE);

      tvTitle.setText("");
    }
    viewPagerIndex = index;
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
        break;
      case EventFlags.EVENT_APPLICATION_CHECK_UPDATE:

        if (!checkUpdateRunning) {
          checkUpdateRunning = true;
          VersionInfoPresenter
              .checkUpdate(getApplicationContext(), new CommonListener<UpdateInfoModel>() {
                @Override
                public void successListener(final UpdateInfoModel response) {
                  checkUpdateRunning = false;
                  if (BuildConfig.VERSION_CODE < response.getVersionCode()) {

                    final CommonDialog commonDialog =
                        CommonDialog.newInstance("有新版本更新", "检测到有新版本，是否下载更新？", "立即前往", "容我三思");
                    commonDialog.setListener(new CommonDialog.OnActionListener() {
                      @Override
                      public boolean dialogClick(int id) {
                        switch (id) {
                          case CommonDialog.CLICK_ID_ACTIVE:
                            Intent browserIntent =
                                new Intent(Intent.ACTION_VIEW, Uri.parse(response.getDownUrl()));
                            startActivity(browserIntent);
                            break;
                          case CommonDialog.CLICK_ID_PASSIVE:
                            commonDialog.dismiss();
                            break;
                        }
                        return true;
                      }
                    });
                    commonDialog.setCancelable(false);
                    commonDialog.show(getSupportFragmentManager(), "");
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
