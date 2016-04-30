package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.base.EnhancedToolbar;
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
public class MessageCenterActivity extends BaseActivity implements PageLoadListener<MessageModel>, AdapterBase.OnLoadmoreListener, AdapterBase.OnItemClickListener<MessageModel.ResultBean> {
    @Bind(R.id.messages_rv)
    RecyclerView messagesRv;
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
    }

    @Override
    public void moreData(MessageModel data) {
        pagesNo = data.getPagesNo();
        adapter.addMoreData(data.getResult());

    }

    @Override
    public void nullData() {
        adapter.setLoadMoreEnable(false);
    }

    @Override
    public void netError(final int pageNo, String massage) {
        adapter.setNetError(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageCenterPresenter.getMessageList(MessageCenterActivity.this, pageNo, MessageCenterActivity.this);
            }
        });
    }

    @Override
    public void onLoadmore() {
        MessageCenterPresenter.getMessageList(this, pagesNo + 1, this);
    }

    @Override
    public void itemClick(MessageModel.ResultBean item, int type, int position) {
        Intent intent = new Intent(this, UserCenterActivity.class);
        intent.putExtra(UserCenterActivity.REQUEST_KEY_USER_ID, item.getFormId());
        startActivity(intent);
    }
}
