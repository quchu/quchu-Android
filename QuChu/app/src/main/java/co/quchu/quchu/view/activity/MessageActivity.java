package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.im.activity.ConversationListFragmentEx;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.view.fragment.NoticeFragment;
import co.quchu.quchu.widget.NoScrollViewPager;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * 消息首页
 * <p/>
 * Created by mwb on 16/8/19.
 */
public class MessageActivity extends BaseBehaviorActivity {

  @Bind(R.id.message_tabLayout) TabLayout tabLayout;
  @Bind(R.id.message_viewpager) NoScrollViewPager viewpager;

  private List<Fragment> mFragments = new ArrayList<>();
  private Fragment mConversationListFragment = null;
  private View mTabUnreadMsgView;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_message);
    ButterKnife.bind(this);

    EnhancedToolbar toolbar = getEnhancedToolbar();
    TextView titleTv = toolbar.getTitleTv();
    titleTv.setText("消息中心");

    MessageAdapter messageAdapter = new MessageAdapter(getSupportFragmentManager());
    viewpager.setAdapter(messageAdapter);
    tabLayout.setupWithViewPager(viewpager);

    initTab();

    isPushMessage(getIntent());
  }

  @Override
  protected void onResume() {
    super.onResume();

    if (tabLayout == null || mTabUnreadMsgView == null) {
      return;
    }

    if (SPUtils.getHasPushMsg()) {
      mTabUnreadMsgView.setVisibility(View.VISIBLE);
    }

    tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
      @Override
      public void onTabSelected(TabLayout.Tab tab) {
        int tag = (int) tab.getTag();
        if (tag == 1) {
          mTabUnreadMsgView.setVisibility(View.INVISIBLE);
          SPUtils.setHasPushMsg(false);
        }
      }

      @Override
      public void onTabUnselected(TabLayout.Tab tab) {

      }

      @Override
      public void onTabReselected(TabLayout.Tab tab) {

      }
    });
  }

  /**
   * 设置自定义的TabView,必须在设置ViewPager后设置
   */
  private void initTab() {
    TabLayout.Tab tab0 = tabLayout.getTabAt(0);
    tab0.setTag(0);
    tab0.setCustomView(R.layout.view_message_tab);
    TabLayout.Tab tab1 = tabLayout.getTabAt(1);
    tab1.setTag(1);
    tab1.setCustomView(R.layout.view_message_tab);

    View tabView0 = tabLayout.getTabAt(0).getCustomView();
    TextView tabTitleTv0 = (TextView) tabView0.findViewById(R.id.tabTitleTv);
    tabTitleTv0.setText("私信");

    View tabView1 = tabLayout.getTabAt(1).getCustomView();
    TextView tabTitleTv1 = (TextView) tabView1.findViewById(R.id.tabTitleTv);
    mTabUnreadMsgView = tabView1.findViewById(R.id.tabUnReadMsgTv);
    tabTitleTv1.setText("通知");
  }

  /**
   * 判断消息是否是 push 消息
   */
  private void isPushMessage(Intent intent) {
    if (intent == null || intent.getData() == null) {
      LogUtils.e("------MessageActivity", "intent or intent data is null");
      return;
    }

    if (intent.getData().getScheme().equals("rong") && intent.getData().getQueryParameter("isFromPush") != null) {
      //push消息
      if (intent.getData().getQueryParameter("isFromPush").equals("true")) {
        enterActivity();
      } else if (RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
        enterActivity();
      }
//      else {
//        enterActivity();
//      }

    } else {
      if (RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
        //融云未连接
        enterActivity();
      }
//      else {
//        enterActivity();
//      }
    }
  }

  /**
   * 应用处于后台且进程被杀死，进入应用主页
   */
  private void enterActivity() {
    Intent intent = new Intent(MessageActivity.this, SplashActivity.class);
    intent.putExtra(SplashActivity.INTENT_KEY_IM_CHAT_LIST, true);
    startActivity(intent);
    finish();
  }

  /**
   * ViewPager适配器
   */
  private class MessageAdapter extends FragmentPagerAdapter {

    public MessageAdapter(FragmentManager fm) {
      super(fm);

      //im会话列表
      Fragment conversationListFragment = initConversationList();

      mFragments.add(conversationListFragment);
      mFragments.add(new NoticeFragment());
    }

    @Override
    public Fragment getItem(int position) {
      return mFragments.get(position);
    }

    @Override
    public int getCount() {
      return mFragments.size();
    }

//    @Override
//    public CharSequence getPageTitle(int position) {
//      if (position == 0) {
//        return "私信";
//      }
//      return "通知";
//    }
  }

  /**
   * 初始化会话列表
   */
  private Fragment initConversationList() {
    if (mConversationListFragment == null) {
      ConversationListFragmentEx fragment = new ConversationListFragmentEx();
      Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
          .appendPath("conversationlist")
          .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
          .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
          .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//讨论组
          .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
          .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//公共服务号
          .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//系统
          .build();
      fragment.setUri(uri);
      return fragment;

    } else {
      return mConversationListFragment;
    }
  }

  @Override
  public ArrayMap<String, Object> getUserBehaviorArguments() {
    return null;
  }

  @Override
  public int getUserBehaviorPageId() {
    return 0;
  }

  @Override
  protected int activitySetup() {
    return TRANSITION_TYPE_LEFT;
  }

  @Override
  protected String getPageNameCN() {
    return "消息中心";
  }
}
