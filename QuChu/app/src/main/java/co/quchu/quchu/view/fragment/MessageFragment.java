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
import co.quchu.quchu.model.MessageModel;
import co.quchu.quchu.net.NetUtil;
import co.quchu.quchu.presenter.MessagePresenter;
import co.quchu.quchu.presenter.PageLoadListener;
import co.quchu.quchu.view.activity.ArticleDetailActivity;
import co.quchu.quchu.view.activity.QuchuDetailsActivity;
import co.quchu.quchu.view.activity.SceneDetailActivity;
import co.quchu.quchu.view.adapter.AdapterBase;
import co.quchu.quchu.view.adapter.MessageAdapter;

import static android.R.attr.id;

/**
 * 消息
 * <p>
 * Created by mwb on 16/11/5.
 */
public class MessageFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

  @Bind(R.id.messages_rv) RecyclerView mRecyclerView;
  @Bind(R.id.message_refresh_layout) SwipeRefreshLayout mRefreshLayout;

  private int pagesNo = 1;
  private MessageAdapter mAdapter;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_message, container, false);
    ButterKnife.bind(this, view);

    initViews();

    return view;
  }

  private void initViews() {
    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    mAdapter = new MessageAdapter(getActivity());
    mAdapter.setLoadmoreListener(loadMoreListener);
    mRecyclerView.setAdapter(mAdapter);
    MessagePresenter.getSysMessageList(getActivity(), pagesNo, pageLoadListener);
    mAdapter.setOnMessageistListener(onItemClickListener);
    mRefreshLayout.setOnRefreshListener(this);
  }

  @Override
  public void onRefresh() {
    if (!NetUtil.isNetworkConnected(getActivity())) {
      makeToast(R.string.network_error);
      mRefreshLayout.setRefreshing(false);
      return;
    }
    pagesNo = 1;
    MessagePresenter.getSysMessageList(getActivity(), pagesNo, pageLoadListener);
  }

  private PageLoadListener<MessageModel> pageLoadListener = new PageLoadListener<MessageModel>() {
    @Override
    public void initData(MessageModel data) {
      mAdapter.initData(data.getResult());
      mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void moreData(MessageModel data) {
      mRefreshLayout.setRefreshing(false);

      pagesNo = data.getPagesNo();
      mAdapter.addMoreData(data.getResult());
    }

    @Override
    public void nullData() {
      if (mRefreshLayout != null) {
        mRefreshLayout.setRefreshing(false);
      }
      mAdapter.setLoadMoreEnable(false);
    }

    @Override
    public void netError(final int pageNo, String massage) {
      if (!isDetached()) {
        return;
      }
      Toast.makeText(getActivity(), getString(R.string.network_error), Toast.LENGTH_SHORT).show();
      mRefreshLayout.setRefreshing(false);

      mAdapter.setNetError(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          mRefreshLayout.setRefreshing(false);
          MessagePresenter.getSysMessageList(getActivity(), pageNo, pageLoadListener);
        }
      });
    }
  };

  private AdapterBase.OnLoadmoreListener loadMoreListener = new AdapterBase.OnLoadmoreListener() {
    @Override
    public void onLoadmore() {
      if (!NetUtil.isNetworkConnected(getActivity())) {
        makeToast(R.string.network_error);
        return;
      }

      MessagePresenter.getSysMessageList(getActivity(), pagesNo + 1, pageLoadListener);
    }
  };

  private MessageAdapter.OnMessageListListener onItemClickListener = new MessageAdapter.OnMessageListListener() {
    @Override
    public void onItemClick(MessageModel.ResultBean model) {
      Intent intent;
      switch (model.getTargetType()) {
        case MessageModel.TARGET_TYPE_QUCHU:
          intent = new Intent(getActivity(), QuchuDetailsActivity.class);
          intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_PID, Integer.valueOf(id));
          startActivity(intent);
          break;

        case MessageModel.TARGET_TYPE_SCENE:
          SceneDetailActivity.enterActivity(getActivity(), model.getTargetId(), "场景详情", true);
          break;

        case MessageModel.TARGET_TYPE_ARTICLE:
          ArticleDetailActivity.enterActivity(getActivity(), String.valueOf(model.getTargetId()), "文章详情", "小Q聊天界面");
          break;

        case MessageModel.TARGET_TYPE_ACTIVITY:
          break;

        case MessageModel.TARGET_TYPE_CITY:
          break;

        case MessageModel.TARGET_TYPE_TEXT:
          break;
      }
    }
  };

  @Override
  protected String getPageNameCN() {
    return "";
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }
}
