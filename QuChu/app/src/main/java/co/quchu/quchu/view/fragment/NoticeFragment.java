package co.quchu.quchu.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseFragment;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.MessageModel;
import co.quchu.quchu.net.NetUtil;
import co.quchu.quchu.presenter.MessagePresenter;
import co.quchu.quchu.presenter.PageLoadListener;
import co.quchu.quchu.view.activity.MyFootprintDetailActivity;
import co.quchu.quchu.view.activity.UserCenterActivityNew;
import co.quchu.quchu.view.adapter.AdapterBase;
import co.quchu.quchu.view.adapter.MessageCenterAdapter;
import co.quchu.quchu.widget.ErrorView;

/**
 * 通知
 * <p/>
 * Created by mwb on 16/8/19.
 */
public class NoticeFragment extends BaseFragment {

  @Bind(R.id.messages_rv) RecyclerView messagesRv;
  @Bind(R.id.refreshLayout) SwipeRefreshLayout refreshLayout;
  @Bind(R.id.message_error_view) ErrorView mErrorView;
  private MessageCenterAdapter adapter;
  private int pagesNo = 1;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_notice, container, false);
    ButterKnife.bind(this, view);

    messagesRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    adapter = new MessageCenterAdapter(getActivity());
    adapter.setLoadmoreListener(loadMoreListener);
    messagesRv.setAdapter(adapter);
    MessagePresenter.getMessageList(getActivity(), pagesNo, pageLoadListener);
    adapter.setItemClickListener(onItemClickListener);
    refreshLayout.setOnRefreshListener(onRefreshListener);

    if (!NetUtil.isNetworkConnected(getActivity())) {
      mErrorView.showViewDefault(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (!NetUtil.isNetworkConnected(getActivity())) {
            makeToast(R.string.network_error);
            return;
          }
          MessagePresenter.getMessageList(getActivity(), pagesNo, pageLoadListener);
        }
      });
    }

    return view;
  }

  /**
   * 加载更多
   */
  private AdapterBase.OnLoadmoreListener loadMoreListener = new AdapterBase.OnLoadmoreListener() {
    @Override
    public void onLoadmore() {
      if (!NetUtil.isNetworkConnected(getActivity())) {
        makeToast(R.string.network_error);
        return;
      }

      MessagePresenter.getMessageList(getActivity(), pagesNo + 1, pageLoadListener);
    }
  };

  /**
   * 下载数据
   */
  private PageLoadListener<MessageModel> pageLoadListener = new PageLoadListener<MessageModel>() {
    @Override
    public void initData(MessageModel data) {
      adapter.initData(data.getResult());
      refreshLayout.setRefreshing(false);

      mErrorView.hideView();
    }

    @Override
    public void moreData(MessageModel data) {
      refreshLayout.setRefreshing(false);

      pagesNo = data.getPagesNo();
      adapter.addMoreData(data.getResult());
    }

    @Override
    public void nullData() {
      if (refreshLayout != null) {
        refreshLayout.setRefreshing(false);
      }
      adapter.setLoadMoreEnable(false);

      mErrorView.hideView();
    }

    @Override
    public void netError(final int pageNo, String massage) {
      if (!isDetached()) {
        return;
      }
      Toast.makeText(getActivity(), getString(R.string.network_error), Toast.LENGTH_SHORT).show();
      refreshLayout.setRefreshing(false);

      adapter.setNetError(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          refreshLayout.setRefreshing(false);
          MessagePresenter.getMessageList(getActivity(), pageNo, pageLoadListener);
        }
      });
    }
  };

  /**
   * 列表点击
   */
  private AdapterBase.OnItemClickListener<MessageModel.ResultBean> onItemClickListener = new AdapterBase.OnItemClickListener<MessageModel.ResultBean>() {
    @Override
    public void itemClick(RecyclerView.ViewHolder holder, final MessageModel.ResultBean item, int type, @Deprecated int position) {
      switch (type) {
        case MessageCenterAdapter.CLICK_TYPE_FOLLOW://关注
          DialogUtil.showProgress(getActivity(), R.string.loading_dialog_text);
          MessagePresenter.followMessageCenterFriends(getActivity(), item.getFormId(),
              "yes".equals(item.getCome()), new MessagePresenter.MessageGetDataListener() {
                @Override
                public void onSuccess(MessageModel arrayList) {
                  if ("yes".equals(item.getCome())) {
                    item.setCome("no");
                    item.setInteraction(false);
                  } else {
                    item.setCome("yes");
                    item.setInteraction(true);
                  }
                  adapter.notifyDataSetChanged();
                  DialogUtil.dismissProgress();
                }

                @Override
                public void onError() {
                  DialogUtil.dismissProgress();
                }
              });
          break;
        case MessageCenterAdapter.CLICK_TYPE_USER_INFO://头像
          Intent intent = new Intent(getActivity(), UserCenterActivityNew.class);
          intent.putExtra(UserCenterActivityNew.REQUEST_KEY_USER_ID, item.getFormId());
          startActivity(intent);
          break;
        case MessageCenterAdapter.CLICK_TYPE_FOOTPRINT_COVER://脚印大图
          Intent intent1 = new Intent(getActivity(), MyFootprintDetailActivity.class);
          intent1.putExtra(MyFootprintDetailActivity.REQUEST_KEY_FOOTPRINT_ID, item.getTargetId());
          intent1.putExtra(MyFootprintDetailActivity.REQUEST_KEY_FROM_MESSAGE, true);
          startActivity(intent1);
          break;
      }
    }
  };

  /**
   * 刷新
   */
  private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
    @Override
    public void onRefresh() {
      if (!NetUtil.isNetworkConnected(getActivity())) {
        makeToast(R.string.network_error);
        refreshLayout.setRefreshing(false);
        return;
      }

      pagesNo = 1;
      MessagePresenter.getMessageList(getActivity(), pagesNo, pageLoadListener);
    }
  };

  @Override
  protected String getPageNameCN() {
    return getString(R.string.pname_message_center);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }
}
