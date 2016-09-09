package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.model.UserCenterInfo;
import co.quchu.quchu.presenter.UserCenterPresenter;
import co.quchu.quchu.view.fragment.FavoriteQuchuFragment;
import co.quchu.quchu.widget.CircleIndicator;

/**
 * UserCenterActivity
 * User: 个人主页
 * Date: 2016-02-23
 */
public class UserCenterActivityNew extends BaseActivity implements View.OnClickListener {

  @Bind(R.id.user_center_viewpager) ViewPager mUserCenterViewpager;
  @Bind(R.id.user_center_indicator) CircleIndicator mUserCenterIndicator;
  @Bind(R.id.follow) TextView follow;
  @Bind(R.id.friend) TextView friend;
  @Bind(R.id.follow_indicator_view) View mFollowIndicatorView;
  @Bind(R.id.friend_indicator_view) View mFriendIndicatorView;
  private TextView mFollowTv;

  public static final String REQUEST_KEY_USER_ID = "USERID";
  private int userId = 0;

  private UserCenterInfo userInfo;

  @Override
  protected String getPageNameCN() {
    return getString(R.string.pname_user_center);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user_center_new);
    ButterKnife.bind(this);

    userId = getIntent().getIntExtra(REQUEST_KEY_USER_ID, 0);

    EnhancedToolbar toolbar = getEnhancedToolbar();
    toolbar.getTitleTv().setText(getTitle());
    if (AppContext.user != null && AppContext.user.getUserId() != userId) {
      mFollowTv = toolbar.getRightTv();
      mFollowTv.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          followSomebody();
        }
      });
    }

    getSupportFragmentManager().beginTransaction()
        .add(R.id.container, FavoriteQuchuFragment.newInstance(false, userId)).commit();

    initData();
  }

  private void initData() {
    UserCenterPresenter
        .getUserCenterInfo(this, userId, new UserCenterPresenter.UserCenterInfoCallBack() {
          @Override
          public void onSuccess(UserCenterInfo userCenterInfo) {
            if (userCenterInfo != null) {
              userInfo = userCenterInfo;

              mUserCenterViewpager.setAdapter(new UserCenterViewPagerAdapter(getSupportFragmentManager()));
              mUserCenterIndicator.setViewPager(mUserCenterViewpager);

              if (mFollowTv != null) {
                mFollowTv.setText(userCenterInfo.isIsFollow() ? "取消关注" : "关注");
              }

              follow.setText(getString(R.string.follow_str, userCenterInfo.getHostNum()));
              friend.setText(getString(R.string.friend_str, userCenterInfo.getFollowNum()));

            } else {
              Toast.makeText(UserCenterActivityNew.this, "并无该用户", Toast.LENGTH_SHORT).show();
            }
          }

          @Override
          public void onError() {
            Toast.makeText(UserCenterActivityNew.this, "数据获取异常请稍后重试！", Toast.LENGTH_SHORT).show();
          }
        });
  }

  /**
   * 关注
   */
  private void followSomebody() {
    if (AppContext.user.isIsVisitors()) {
      showLoginDialog();
    } else {
      UserCenterPresenter.followSbd(this, userInfo.isIsFollow(), userId,
          new UserCenterPresenter.UserCenterInfoCallBack() {
            @Override
            public void onSuccess(UserCenterInfo userC) {
              if (userInfo.isIsFollow()) {
                userInfo.setIsFollow(false);
                userInfo.setFollowNum(userInfo.getFollowNum() - 1);
              } else {
                userInfo.setIsFollow(true);
                userInfo.setFollowNum(userInfo.getFollowNum() + 1);
              }
              friend.setText(getString(R.string.friend_str, userInfo.getFollowNum()));
              if (mFollowTv != null) {
                mFollowTv.setText(userInfo.isIsFollow() ? "取消关注" : "关注");
              }
            }

            @Override
            public void onError() {
              Toast.makeText(UserCenterActivityNew.this, getString(R.string.network_error),
                  Toast.LENGTH_SHORT).show();
            }
          });
    }
  }

  @OnClick({R.id.follow, R.id.friend})
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.follow:
        //我关注的
        mFollowIndicatorView.setVisibility(View.VISIBLE);
        mFriendIndicatorView.setVisibility(View.INVISIBLE);
        startActivity(
            new Intent(UserCenterActivityNew.this, FollowingActivity.class).putExtra("UserId", userId)
                .putExtra("FollowType", FollowingActivity.TAFOLLOWING));
        break;

      case R.id.friend:
        //我的趣粉
        mFriendIndicatorView.setVisibility(View.VISIBLE);
        mFollowIndicatorView.setVisibility(View.INVISIBLE);
        startActivity(
            new Intent(UserCenterActivityNew.this, FollowingActivity.class).putExtra("UserId", userId)
                .putExtra("FollowType", FollowingActivity.TAFOLLOWERS));
        break;
    }
  }

  @Override
  protected int activitySetup() {
    return TRANSITION_TYPE_LEFT;
  }

  /**
   * 头部ViewPager适配器
   */
  private class UserCenterViewPagerAdapter extends FragmentPagerAdapter {

    private final UserCenterAvatarFragment avatarFragment;
    private final MeGenFragment genFragment;

    public UserCenterViewPagerAdapter(FragmentManager fm) {
      super(fm);

      avatarFragment = UserCenterAvatarFragment.newInstance(userInfo);
      genFragment = MeGenFragment.newInstance(false, userId);
    }

    @Override
    public Fragment getItem(int position) {
      if (position == 0) {
        return avatarFragment;
      }
      return genFragment;
    }

    @Override
    public int getCount() {
      return 2;
    }
  }
}