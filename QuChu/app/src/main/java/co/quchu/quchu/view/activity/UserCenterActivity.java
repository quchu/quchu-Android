package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.model.UserCenterInfo;
import co.quchu.quchu.presenter.UserCenterPresenter;
import co.quchu.quchu.view.fragment.FootprintListFragment;

/**
 * UserCenterActivity
 * User: 个人主页
 * Date: 2016-02-23
 */
public class UserCenterActivity extends BaseActivity implements View.OnClickListener {

  @Override protected String getPageNameCN() {
    return getString(R.string.pname_user_center);
  }

  @Bind(R.id.headImage) SimpleDraweeView headImage;
  @Bind(R.id.desc) TextView name;
  @Bind(R.id.alias) TextView alias;
  @Bind(R.id.editOrLoginTV) TextView followAction;
  @Bind(R.id.follow) TextView follow;
  @Bind(R.id.friend) TextView friend;
  @Bind(R.id.gender) SimpleDraweeView gender;
  @Bind(R.id.follow_indicator_view) View mFollowIndicatorView;
  @Bind(R.id.friend_indicator_view) View mFriendIndicatorView;
  private int userId = 0;
  public static final String REQUEST_KEY_USER_ID = "USERID";

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user_center);
    ButterKnife.bind(this);
    EnhancedToolbar toolbar = getEnhancedToolbar();
    toolbar.getTitleTv().setText(getTitle());
    userId = getIntent().getIntExtra(REQUEST_KEY_USER_ID, 0);
    followAction.setOnClickListener(this);
    follow.setOnClickListener(this);
    friend.setOnClickListener(this);
    getSupportFragmentManager().beginTransaction()
        .add(R.id.container, FootprintListFragment.newInstance(userId)).commit();

    if (userId == AppContext.user.getUserId()) {
      followAction.setVisibility(View.INVISIBLE);
    }
  }

  @Override protected int activitySetup() {
    return TRANSITION_TYPE_LEFT;
  }

  @Override protected void onResume() {
    super.onResume();
    initData();
  }

  private UserCenterInfo userInfo;

  private void initData() {
    UserCenterPresenter
        .getUserCenterInfo(this, userId, new UserCenterPresenter.UserCenterInfoCallBack() {
          @Override public void onSuccess(UserCenterInfo userCenterInfo) {
            if (userCenterInfo != null) {
              userInfo = userCenterInfo;

              headImage.setImageURI(Uri.parse(userInfo.getPhoto()));
              name.setText(userCenterInfo.getName());
              followAction.setText(userCenterInfo.isIsFollow() ? "取消关注" : "关注");
              alias.setText(userCenterInfo.getMark());

              follow.setText("关注" + userCenterInfo.getHostNum());
              friend.setText("趣粉" + userCenterInfo.getFollowNum());

              gender.setImageURI(Uri.parse(
                  "res:///" + (userCenterInfo.getGender().equals("男") ? R.mipmap.ic_male
                      : R.mipmap.ic_female)));
            } else {
              Toast.makeText(UserCenterActivity.this, "并无该用户", Toast.LENGTH_SHORT).show();
            }
          }

          @Override public void onError() {
            Toast.makeText(UserCenterActivity.this, "数据获取异常请稍后重试！", Toast.LENGTH_SHORT).show();
            //     UserCenterActivity.this.finish();
          }
        });
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.editOrLoginTV:
        followSomebody();
        break;
      case R.id.friend:
        mFriendIndicatorView.setVisibility(View.VISIBLE);
        mFollowIndicatorView.setVisibility(View.INVISIBLE);
        startActivity(
            new Intent(UserCenterActivity.this, FollowingActivity.class).putExtra("UserId", userId)
                .putExtra("FollowType", FollowingActivity.TAFOLLOWERS));
        break;
      case R.id.follow:
        mFollowIndicatorView.setVisibility(View.VISIBLE);
        mFriendIndicatorView.setVisibility(View.INVISIBLE);
        startActivity(
            new Intent(UserCenterActivity.this, FollowingActivity.class).putExtra("UserId", userId)
                .putExtra("FollowType", FollowingActivity.TAFOLLOWING));
        break;
    }
  }

  private void followSomebody() {
    if (AppContext.user.isIsVisitors()) {
      showLoginDialog();
    } else {
      UserCenterPresenter.followSbd(this, userInfo.isIsFollow(), userId,
          new UserCenterPresenter.UserCenterInfoCallBack() {
            @Override public void onSuccess(UserCenterInfo userC) {
              if (userInfo.isIsFollow()) {
                userInfo.setIsFollow(false);
                userInfo.setFollowNum(userInfo.getFollowNum() - 1);
              } else {
                userInfo.setIsFollow(true);
                userInfo.setFollowNum(userInfo.getFollowNum() + 1);
              }
              friend.setText("趣粉" + userInfo.getFollowNum());
              followAction.setText(userInfo.isIsFollow() ? "取消关注" : "关注");
            }

            @Override public void onError() {
              Toast.makeText(UserCenterActivity.this, getString(R.string.network_error),
                  Toast.LENGTH_SHORT).show();
            }
          });
    }
  }
}