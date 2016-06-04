package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.MessageModel;
import co.quchu.quchu.presenter.MessageCenterPresenter;
import co.quchu.quchu.presenter.PageLoadListener;
import co.quchu.quchu.view.adapter.AdapterBase;
import co.quchu.quchu.view.adapter.MessageCenterAdapter;

/**
 * MessageCenterActivity
 * User: Chenhs
 * Date: 2016-01-11
 */
public class MessageCenterActivity extends BaseActivity implements PageLoadListener<MessageModel>, AdapterBase.OnLoadmoreListener, AdapterBase.OnItemClickListener<MessageModel.ResultBean>, SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.messages_rv)
    RecyclerView messagesRv;
    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout mRefreshLayout;
    private MessageCenterAdapter adapter;
    private int pagesNo = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_center);
        ButterKnife.bind(this);
        EnhancedToolbar toolbar = getEnhancedToolbar();
        toolbar.getTitleTv().setText(getTitle());
        messagesRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new MessageCenterAdapter(this);
        adapter.setLoadmoreListener(this);
        messagesRv.setAdapter(adapter);
        MessageCenterPresenter.getMessageList(this, pagesNo, this);
        adapter.setItemClickListener(this);
        mRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }


    @Override
    protected void onResume() {
        MobclickAgent.onPageStart("messages");

        super.onResume();
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPageEnd("messages");
        super.onPause();
    }


    @Override
    public void initData(MessageModel data) {
        adapter.initData(data.getResult());
        mRefreshLayout.setRefreshing(false);

    }

    @Override
    public void moreData(MessageModel data) {
        mRefreshLayout.setRefreshing(false);

        pagesNo = data.getPagesNo();
        adapter.addMoreData(data.getResult());
    }

    @Override
    public void nullData() {
        mRefreshLayout.setRefreshing(false);
        adapter.setLoadMoreEnable(false);
    }

    @Override
    public void netError(final int pageNo, String massage) {
        adapter.setNetError(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRefreshLayout.setRefreshing(false);
                MessageCenterPresenter.getMessageList(MessageCenterActivity.this, pageNo, MessageCenterActivity.this);
            }
        });
    }

    @Override
    public void onLoadmore() {
        MessageCenterPresenter.getMessageList(this, pagesNo + 1, this);
    }

    @Override
    public void itemClick(RecyclerView.ViewHolder holder, final MessageModel.ResultBean item, int type, int position) {
        switch (type) {
            case MessageCenterAdapter.CLICK_TYPE_FOLLOW://关注
                DialogUtil.showProgess(this, R.string.loading_dialog_text);
                MessageCenterPresenter.followMessageCenterFriends(this, item.getFormId(),
                        "yes".equals(item.getCome()), new MessageCenterPresenter.MessageGetDataListener() {
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
                                DialogUtil.dismissProgess();
                            }

                            @Override
                            public void onError() {
                                DialogUtil.dismissProgess();
                            }
                        });
                break;
            case MessageCenterAdapter.CLICK_TYPE_USER_INFO://头像
                Intent intent = new Intent(this, UserCenterActivity.class);
                intent.putExtra(UserCenterActivity.REQUEST_KEY_USER_ID, item.getFormId());
                startActivity(intent);
                break;
            case MessageCenterAdapter.CLICK_TYPE_FOOTPRINT_COVER://脚印大图
                Intent intent1 = new Intent(this, MyFootprintDetailActivity.class);
                intent1.putExtra(MyFootprintDetailActivity.REQUEST_KEY_FOOTPRINT_ID, item.getTargetId());
                intent1.putExtra(MyFootprintDetailActivity.REQUEST_KEY_FROM_MESSAGE, true);
                startActivity(intent1);
                break;
        }
    }

    @Override
    public void onRefresh() {
        pagesNo = 1;
        MessageCenterPresenter.getMessageList(MessageCenterActivity.this, pagesNo, MessageCenterActivity.this);
    }
}
