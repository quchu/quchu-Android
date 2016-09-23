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
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseFragment;
import co.quchu.quchu.im.IMPresenter;
import co.quchu.quchu.view.activity.XiaoQActivity;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.UIConversation;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * im聊天列表
 * <p/>
 * Created by mwb on 16/8/25.
 */
public class ChatListFragment extends BaseFragment {

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_chat_list, container, false);

    //设置会话列表点击事件监听
    RongIM.setConversationListBehaviorListener(conversationListBehaviorListener);

    enterFragment();

    return view;
  }

  /**
   * 加载融云会话列表
   */
  private void enterFragment() {
    ConversationListFragmentEx fragment = new ConversationListFragmentEx();
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
        @Override
        public boolean onConversationPortraitClick(Context context,
                                                   Conversation.ConversationType conversationType, String s) {
          //点击头像
          return false;
        }

        @Override
        public boolean onConversationPortraitLongClick(Context context,
                                                       Conversation.ConversationType conversationType, String s) {
          //长按头像
          return false;
        }

        @Override
        public boolean onConversationLongClick(Context context, View view,
                                               UIConversation uiConversation) {
          //列表长按
          if (uiConversation.getConversationTargetId().equals(IMPresenter.xiaoqId)) {
            return true;
          }

          conversationLongClick(uiConversation.getConversationTargetId());

//          IMDialog dialog = new IMDialog(getActivity(), uiConversation.getConversationTargetId(),
//              uiConversation.isTop());
//          dialog.show();
          return true;
        }

        @Override
        public boolean onConversationClick(Context context, View view,
                                           UIConversation uiConversation) {
          //列表点击
          if (RongIMClient.getInstance() != null) {
            if (!RongIMClient.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)) {
              Toast.makeText(getActivity(), "IM连接出错，请检查网络或者重启应用", Toast.LENGTH_SHORT).show();
              return true;
            }
          }

          String targetId = uiConversation.getConversationTargetId();
          String title = uiConversation.getUIConversationTitle();

          if (targetId.equals(IMPresenter.xiaoqId)) {
            XiaoQActivity.launch(getActivity());
            return true;
          }

          if (RongIM.getInstance() != null) {
            RongIM.getInstance().startPrivateChat(getActivity(), targetId, title);
          }
          return true;
        }
      };

  /**
   * 会话列表长按
   */
  private void conversationLongClick(final String targetId) {
    new MaterialDialog.Builder(getActivity())
        .items("删除聊天")
        .cancelable(true)
        .itemsCallback(new MaterialDialog.ListCallback() {
          @Override
          public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
            new IMPresenter().removeConversation(targetId, null);
          }
        }).show();
  }

  @Override
  protected String getPageNameCN() {
    return null;
  }
}
