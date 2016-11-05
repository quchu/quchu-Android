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
import co.quchu.quchu.view.fragment.MessageFragment;
import co.quchu.quchu.view.fragment.NoticeFragment;
import co.quchu.quchu.widget.NoScrollViewPager;

/**
 * 消息中心
 * <p>
 * Created by mwb on 16/10/24.
 */
public class MessageCenterActivity extends BaseBehaviorActivity {

  @Bind(R.id.message_center_tab_layout) TabLayout mTabLayout;
  @Bind(R.id.message_center_viewpager) NoScrollViewPager mViewpager;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_message_center);
    ButterKnife.bind(this);

    EnhancedToolbar toolbar = getEnhancedToolbar();
    TextView titleTv = toolbar.getTitleTv();
    titleTv.setText("");

    mViewpager.setAdapter(new MessageTabAdapter(getSupportFragmentManager()));
    mTabLayout.setupWithViewPager(mViewpager);
  }

  /**
   * tab 适配器
   */
  private class MessageTabAdapter extends FragmentPagerAdapter {

    private final MessageFragment mMessageFragment;
    private final NoticeFragment mNoticeFragment;

    public MessageTabAdapter(FragmentManager fm) {
      super(fm);

      mMessageFragment = new MessageFragment();
      mNoticeFragment = new NoticeFragment();
    }

    @Override
    public Fragment getItem(int position) {
      if (position == 0) {
        return mMessageFragment;
      }
      return mNoticeFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      if (position == 0) {
        return "消息";
      }
      return "动态";
    }

    @Override
    public int getCount() {
      return 2;
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
    return "";
  }
}
