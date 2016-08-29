package co.quchu.quchu.view.activity;

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
import co.quchu.quchu.im.activity.ChatListFragment;
import co.quchu.quchu.widget.NoScrollViewPager;

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
  }

  /**
   * ViewPager适配器
   */
  private class MessageAdapter extends FragmentPagerAdapter {

    private final ChatListFragment chatListFragment;
    private final NoticeFragment noticeFragment;
    //private final NewChatListFragment chatListFragment;

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
