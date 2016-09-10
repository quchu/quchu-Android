package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.ArrayMap;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.im.IMPresenter;
import co.quchu.quchu.im.activity.ChatListFragment;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.widget.NoScrollViewPager;
import io.rong.imlib.RongIMClient;

/**
 * 消息首页
 * <p/>
 * Created by mwb on 16/8/19.
 */
public class MessageActivity extends BaseBehaviorActivity {

  @Bind(R.id.message_tabLayout) TabLayout tabLayout;
  @Bind(R.id.message_viewpager) NoScrollViewPager viewpager;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_message);
    ButterKnife.bind(this);

    EnhancedToolbar toolbar = getEnhancedToolbar();
    TextView titleTv = toolbar.getTitleTv();
    titleTv.setText("消息中心");

    MessageAdapter messageAdapter = new MessageAdapter(getSupportFragmentManager());
    viewpager.setAdapter(messageAdapter);
    tabLayout.setupWithViewPager(viewpager);

    isPushMessage(getIntent());
  }

  /**
   * 判断消息是否是 push 消息
   */
  private void isPushMessage(Intent intent) {
    if (intent == null || intent.getData() == null) {
      return;
    }

    String token = SPUtils.getRongYunToken();

    if (intent.getData().getScheme().equals("rong")
        && intent.getData().getQueryParameter("push") != null) {
      //push消息

      //通过intent.getData().getQueryParameter("push") 为true，判断是否是push消息
      if (intent.getData().getQueryParameter("push").equals("true")) {

        enterActivity();
      }

    } else {
      //程序切到后台，收到消息后点击进入,会执行这里
      if (!RongIMClient.getInstance().getCurrentConnectionStatus()
          .equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)) {

        enterActivity();
      }

      //enterFragment(mConversationType, mTargetId);
      reconnect(token);
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
   * 重连融云服务
   */
  private void reconnect(String token) {
    new IMPresenter().connectIMService(null);
  }

  /**
   * ViewPager适配器
   */
  private class MessageAdapter extends FragmentPagerAdapter {

    private final ChatListFragment chatListFragment;
    private final NoticeFragment noticeFragment;

    public MessageAdapter(FragmentManager fm) {
      super(fm);

      //im列表
      chatListFragment = new ChatListFragment();

      //通知
      noticeFragment = new NoticeFragment();
    }

    @Override public Fragment getItem(int position) {
      if (position == 0) {
        return chatListFragment;
      }
      return noticeFragment;
    }

    @Override public int getCount() {
      return 2;
    }

    @Override public CharSequence getPageTitle(int position) {
      if (position == 0) {
        return "私信";
      }
      return "通知";
    }
  }

  @Override public ArrayMap<String, Object> getUserBehaviorArguments() {
    return null;
  }

  @Override public int getUserBehaviorPageId() {
    return 0;
  }

  @Override protected int activitySetup() {
    return TRANSITION_TYPE_LEFT;
  }

  @Override protected String getPageNameCN() {
    return "消息中心";
  }
}
