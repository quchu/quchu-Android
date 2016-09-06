package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

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
import co.quchu.quchu.model.UserInfoModel;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.MeActivityPresenter;
import co.quchu.quchu.presenter.UserCenterPresenter;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.widget.CircleIndicator;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

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
  @Bind(R.id.unReadMassage)
  TextView unReadMassage;
  @Bind(R.id.massage_layout)
  RelativeLayout massageLayout;
  @Bind(R.id.feedback_layout)
  LinearLayout feedbackLayout;

  private MeActivityPresenter presenter;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_me, container, false);
    ButterKnife.bind(this, view);

    presenter = new MeActivityPresenter(getActivity());

    viewpager.setAdapter(new MyViewPagerAdapter(getActivity().getSupportFragmentManager()));
    indicator.setViewPager(viewpager);

    getUserCenterInfo();

    boolean isChat = getActivity().getIntent().getBooleanExtra(SplashActivity.INTENT_KEY_IM_CHAT, false);
    boolean isChatList = getActivity().getIntent().getBooleanExtra(SplashActivity.INTENT_KEY_IM_CHAT_LIST, false);

    if (isChat) {
      startChat();
    } else if (isChatList) {
      startChatList();
    }

    return view;
  }

  @Override
  public void onResume() {
    getUnreadMessage();

    if (AppContext.user != null && !AppContext.user.isIsVisitors()) {
      initXiaoQConversation();
    } else {
      IMPresenter.removeConversation(IMPresenter.xiaoqId);
    }

    super.onResume();
  }

  /**
   * 设置小Q会话
   */
  private void initXiaoQConversation() {
    IMPresenter.getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
      @Override
      public void onSuccess(List<Conversation> conversations) {
        //遍历当前会话列表，如果没有小Q会话，则主动发消息在本地显示会话
        boolean hasXiaoQConversation = false;

        if (conversations != null && conversations.size() > 0) {
          for (Conversation conversation : conversations) {
            if (conversation.getTargetId().equals(IMPresenter.xiaoqId)) {
              hasXiaoQConversation = true;
              break;
            }
          }
        }

        if (!hasXiaoQConversation) {
          if (RongIM.getInstance() != null) {
            TextMessage textMessage = TextMessage.obtain("");
            Message message = Message.obtain(IMPresenter.xiaoqId, Conversation.ConversationType.PRIVATE, textMessage);
            RongIM.getInstance().sendMessage(message, null, null, new IRongCallback.ISendMessageCallback() {
              @Override
              public void onAttached(Message message) {

              }

              @Override
              public void onSuccess(Message message) {
                int[] messageIds = new int[]{Integer.valueOf(message.getMessageId())};
                IMPresenter.deleteMessages(messageIds);
              }

              @Override
              public void onError(Message message, RongIMClient.ErrorCode errorCode) {

              }
            });
          }
        }
      }

      @Override
      public void onError(RongIMClient.ErrorCode errorCode) {

      }
    });

    //置顶小Q
    IMPresenter.setConversationToTop(IMPresenter.xiaoqId, true);
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
              String mark = userCenterInfo.getMark();
              SPUtils.setUserMark(mark);
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
    //推送通知
    presenter.getUnreadMassageCound(new CommonListener<Integer>() {
      @Override
      public void successListener(Integer response) {
        notReadMassage(response);
      }

      @Override
      public void errorListener(VolleyError error, String exception, String msg) {
      }
    });

    //im未读消息数
    IMPresenter.getUnreadCount(new RongIM.OnReceiveUnreadCountChangedListener() {
      @Override
      public void onMessageIncreased(int i) {
        notReadMassage(i);
      }
    });
  }

  public void notReadMassage(int count) {
    if (unReadMassage == null) {
      return;
    }
    if (count > 0) {
      unReadMassage.setText(String.valueOf(count));
      unReadMassage.setVisibility(View.VISIBLE);
    } else {
      unReadMassage.setVisibility(View.GONE);
    }
  }

  public void startChat() {
    if (RongIM.getInstance() != null) {
      RongIM.getInstance().startPrivateChat(getActivity(), SPUtils.getRongYunTargetId(), SPUtils.getRongYunTitle());
    }
  }

  public void startChatList() {
    startActivity(MessageActivity.class);
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
        break;
    }
  }

  @OnClick({R.id.friend_layout, R.id.quchu_layout, R.id.massage_layout, R.id.feedback_layout})
  public void onClick(View view) {
    UserInfoModel user = AppContext.user;
    switch (view.getId()) {
      case R.id.friend_layout:
        //趣友圈
        UMEvent("community_c");
        if (user.isIsVisitors()) {
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
        unReadMassage.setVisibility(View.INVISIBLE);
        break;

      case R.id.feedback_layout:
        //意见与反馈
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
      genFragment = new MeGenFragment();
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
