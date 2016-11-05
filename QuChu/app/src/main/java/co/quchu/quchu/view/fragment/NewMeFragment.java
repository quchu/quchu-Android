package co.quchu.quchu.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuge.analysis.stat.ZhugeSDK;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.base.BaseFragment;
import co.quchu.quchu.im.IMPresenter;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.model.UserCenterInfo;
import co.quchu.quchu.net.NetUtil;
import co.quchu.quchu.presenter.MeActivityPresenter;
import co.quchu.quchu.presenter.UserCenterPresenter;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.view.activity.FavoriteActivity;
import co.quchu.quchu.view.activity.FeedbackActivity;
import co.quchu.quchu.view.activity.MessageActivity;
import co.quchu.quchu.view.activity.QuFriendsActivity;
import co.quchu.quchu.widget.CircleIndicator;
import co.quchu.quchu.widget.UserMarkDialog;
import io.rong.imkit.RongIM;

/**
 * 我的 TAB
 * <p/>
 * Created by mwb on 16/8/22.
 */
public class NewMeFragment extends BaseFragment {

  @Bind(R.id.me_viewpager)
  ViewPager viewpager;
  @Bind(R.id.me_indicator)
  CircleIndicator indicator;
  @Bind(R.id.friend_layout)
  LinearLayout friendLayout;
  @Bind(R.id.quchu_layout)
  LinearLayout quchuLayout;
  @Bind(R.id.unReadMessage)
  TextView unReadMessageView;
  @Bind(R.id.massage_layout)
  RelativeLayout massageLayout;
  @Bind(R.id.feedback_layout)
  LinearLayout feedbackLayout;

  private MeActivityPresenter presenter;

  private boolean mHasPushUnreadMessage;//个推消息
  private boolean mHasImUnreadMessage;//im消息

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_me, container, false);
    ButterKnife.bind(this, view);

    presenter = new MeActivityPresenter(getActivity());

    viewpager.setAdapter(new MyViewPagerAdapter(getActivity().getSupportFragmentManager()));
    indicator.setViewPager(viewpager);

    if (!NetUtil.isNetworkConnected(getActivity())){
      Toast.makeText(getActivity(),R.string.network_error,Toast.LENGTH_SHORT).show();
    }else{
      getUserCenterInfo();
    }

    return view;
  }

  @Override
  public void onResume() {
    getUnreadMessage();
    super.onResume();
  }

  /**
   * 获取用户信息
   */
  private void getUserCenterInfo() {
    if (AppContext.user == null) {
      return;
    }

    int userId = AppContext.user.getUserId();

    UserCenterPresenter
        .getUserCenterInfo(getActivity(), userId, new UserCenterPresenter.UserCenterInfoCallBack() {
          @Override
          public void onSuccess(UserCenterInfo userCenterInfo) {
            if (userCenterInfo != null) {
              String newMark = userCenterInfo.getMark();

              //称号不同则弹出对话框
              String oldMark = SPUtils.getUserMark();
              if (TextUtils.isEmpty(oldMark)) {
                UserMarkDialog markDialog = new UserMarkDialog(getActivity(), newMark);
                markDialog.show();
              } else if (!oldMark.equals(newMark)) {
                UserMarkDialog markDialog = new UserMarkDialog(getActivity(), newMark);
                markDialog.show();
              }

              SPUtils.setUserMark(newMark);
            }
          }

          @Override
          public void onError() {
          }
        });
  }

  /**
   * 获取未读消息
   */
  private void getUnreadMessage() {
//    //推送通知
//    presenter.getUnreadMassageCound(new CommonListener<Integer>() {
//      @Override
//      public void successListener(Integer response) {
//        mHasPushUnreadMessage = response > 0 ? true : false;
//        showUnreadView();
//      }
//
//      @Override
//      public void errorListener(VolleyError error, String exception, String msg) {
//      }
//    });

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
    if (unReadMessageView == null) {
      return;
    }

//    if (mHasPushUnreadMessage) {
//      unReadMassageView.setVisibility(View.VISIBLE);
//      ((RecommendActivity) getActivity()).showUnreadView(true);
//    } else {
//      if (mHasImUnreadMessage) {
//        unReadMassageView.setVisibility(View.VISIBLE);
//        ((RecommendActivity) getActivity()).showUnreadView(true);
//      } else {
//        unReadMassageView.setVisibility(View.INVISIBLE);
//        ((RecommendActivity) getActivity()).showUnreadView(false);
//      }
//    }
  }

  @Override
  protected String getPageNameCN() {
    return null;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  @Override
  public void onStart() {
    super.onStart();
    EventBus.getDefault().register(this);
  }

  @Override
  public void onStop() {
    EventBus.getDefault().unregister(this);
    super.onStop();
  }

  @Subscribe
  public void onMessageEvent(QuchuEventModel event) {
    switch (event.getFlag()) {
      case EventFlags.EVENT_DEVICE_NETWORK_AVAILABLE:
        getUnreadMessage();
        getUserCenterInfo();
        break;
    }
  }

  @OnClick({R.id.friend_layout, R.id.quchu_layout, R.id.massage_layout, R.id.feedback_layout})
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.friend_layout:
        //趣友圈
        UMEvent("community_c");
        if (AppContext.user != null && AppContext.user.isIsVisitors()) {
          ((BaseActivity) getActivity()).showLoginDialog();
        } else {
          startActivity(QuFriendsActivity.class);
        }
        break;

      case R.id.quchu_layout:
        //收藏
        UMEvent("collection_c");
        startActivity(FavoriteActivity.class);
        break;

      case R.id.massage_layout:
        //消息
        UMEvent("message_c");
        startActivity(MessageActivity.class);
        break;

      case R.id.feedback_layout:
        //意见与反馈
        ZhugeSDK.getInstance().track(getActivity(), "意见反馈");
        startActivity(FeedbackActivity.class);
        break;
    }
  }

  private class MyViewPagerAdapter extends FragmentPagerAdapter {

    private final MeAvatarFragment avatarFragment;
    private final MeGenFragment genFragment;

    public MyViewPagerAdapter(FragmentManager fm) {
      super(fm);

      avatarFragment = new MeAvatarFragment();
      genFragment = MeGenFragment.newInstance(true, -1);
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
