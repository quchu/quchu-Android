package co.quchu.quchu.im.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseFragment;
import co.quchu.quchu.im.IMPresenter;
import co.quchu.quchu.utils.LogUtils;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * 聊天列表
 *
 * Created by mwb on 16/8/27.
 */
public class NewChatListFragment extends BaseFragment {

  @Bind(R.id.conversation_list_rv) RecyclerView conversationListRv;
  @Bind(R.id.refreshLayout) SwipeRefreshLayout refreshLayout;

  private List<Conversation> mConversations = new ArrayList<>();
  private ChatListAdapter mAdapter;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_chat_list_new, container, false);
    ButterKnife.bind(this, view);

    conversationListRv.setLayoutManager(
        new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    mAdapter = new ChatListAdapter(getActivity(), mConversations);
    mAdapter.setOnConversationItemClickListener(onItemClickListener);
    conversationListRv.setAdapter(mAdapter);
    refreshLayout.setOnRefreshListener(onRefreshListener);

    getConversationList();

    return view;
  }

  /**
   * 获取本地存储的会话列表
   */
  private void getConversationList() {
    IMPresenter.getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
      @Override public void onSuccess(List<Conversation> conversations) {
        LogUtils.e("ConversationListFragment", "getConversationList()-----onSuccess");
        refreshLayout.setRefreshing(false);

        mConversations.clear();
        mConversations.addAll(conversations);
        mAdapter.notifyDataSetChanged();
      }

      @Override public void onError(RongIMClient.ErrorCode errorCode) {
        LogUtils.e("ConversationListFragment", "getConversationList()-----onError");
        refreshLayout.setRefreshing(false);
      }
    });
  }

  /**
   * 列表点击
   */
  private ChatListAdapter.OnConversationItemClickListener onItemClickListener =
      new ChatListAdapter.OnConversationItemClickListener() {
        @Override public void itemClick(Conversation conversation) {
          String targetId = conversation.getTargetId();
          if (RongIM.getInstance() != null) {
            RongIM.getInstance().startPrivateChat(getActivity(), targetId, targetId);
          }
        }
      };

  /**
   * 刷新
   */
  private SwipeRefreshLayout.OnRefreshListener onRefreshListener =
      new SwipeRefreshLayout.OnRefreshListener() {
        @Override public void onRefresh() {
          getConversationList();
        }
      };

  @Override protected String getPageNameCN() {
    return null;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }
}
