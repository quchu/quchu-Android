package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewPager;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.view.fragment.FavoriteEssayFragment;
import co.quchu.quchu.view.fragment.FavoriteQuchuFragment;

public class FavoriteActivity extends BaseBehaviorActivity {

  @Override
  public ArrayMap<String, Object> getUserBehaviorArguments() {
    return null;
  }

  @Override
  public int getUserBehaviorPageId() {
    return 122;
  }

  @Override
  protected String getPageNameCN() {
    return getString(R.string.pname_favorite);
  }

  @Bind(R.id.tabLayout) TabLayout tabLayout;
  @Bind(R.id.viewpager) ViewPager viewpager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_favorite);
    ButterKnife.bind(this);
    EnhancedToolbar toolbar = getEnhancedToolbar();
    toolbar.getTitleTv().setText("");

    final FavoriteQuchuFragment quchuFragment = FavoriteQuchuFragment.newInstance(true, -1);
    final FavoriteEssayFragment essayFragment = new FavoriteEssayFragment();

    viewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
      @Override
      public Fragment getItem(int position) {
        if (position == 0) {
          return quchuFragment;
        } else
          return essayFragment;
      }

      @Override
      public CharSequence getPageTitle(int position) {
        if (position == 0) {
          return "趣处";
        } else {
          return "文章";
        }
      }

      @Override
      public int getCount() {
        return 2;
      }
    });

    tabLayout.setupWithViewPager(viewpager);
  }

  @Override
  protected int activitySetup() {
    return TRANSITION_TYPE_LEFT;
  }

}
