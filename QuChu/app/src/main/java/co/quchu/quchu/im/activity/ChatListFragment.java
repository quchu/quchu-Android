package co.quchu.quchu.im.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseFragment;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.UIConversation;
import io.rong.imlib.model.Conversation;

/**
 * im聊天列表
 *
 * Created by mwb on 16/8/25.
 */
public class ChatListFragment extends BaseFragment {

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_chat_list, container, false);

    //设置小Q
    //IMPresenter.sendTextMessage(IMPresenter.userId1, "", null, null);
    //if (RongIM.getInstance() != null) {
    //  RongIM.getInstance()
    //      .setConversationToTop(Conversation.ConversationType.PRIVATE, IMPresenter.userId1, true,
    //          new RongIMClient.ResultCallback<Boolean>() {
    //            @Override public void onSuccess(Boolean aBoolean) {
    //            }
    //
    //            @Override public void onError(RongIMClient.ErrorCode errorCode) {
    //            }
    //          });
    //}

    //设置会话列表点击事件监听
    RongIM.setConversationListBehaviorListener(conversationListBehaviorListener);

    enterFragment();

    return view;
  }

  /**
   * 加载融云会话列表
   */
  private void enterFragment() {
    io.rong.imkit.fragment.ConversationListFragment fragment =
        new io.rong.imkit.fragment.ConversationListFragment();
    Uri uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
        .appendPath("conversationlist")
        .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(),
            "false") //设置私聊会话是否聚合显示
        .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//群组
        .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//讨论组
        .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(),
            "false")//公共服务号
        .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(),
            "false")//公共服务号
        .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//系统
        .build();
    fragment.setUri(uri);

    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.add(R.id.conversation_list_fragment, fragment);
    fragmentTransaction.commit();
  }

  /**
   * 列表点击事件
   *
   * @return
   */
  private RongIM.ConversationListBehaviorListener conversationListBehaviorListener =
      new RongIM.ConversationListBehaviorListener() {
        @Override public boolean onConversationPortraitClick(Context context,
            Conversation.ConversationType conversationType, String s) {
          //点击头像
          return false;
        }

        @Override public boolean onConversationPortraitLongClick(Context context,
            Conversation.ConversationType conversationType, String s) {
          //长按头像
          return false;
        }

        @Override public boolean onConversationLongClick(Context context, View view,
            UIConversation uiConversation) {
          //列表长按
          return false;
        }

        @Override public boolean onConversationClick(Context context, View view,
            UIConversation uiConversation) {
          //列表点击
          return false;
        }
      };

  @Override protected String getPageNameCN() {
    return null;
  }
}
