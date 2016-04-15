package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.model.MessageModel;
import co.quchu.quchu.presenter.MessageCenterPresenter;
import co.quchu.quchu.view.adapter.MessageCenterAdapter;
import co.quchu.quchu.widget.ErrorView;

/**
 * MessageCenterActivity
 * User: Chenhs
 * Date: 2016-01-11
 */
public class MessageCenterActivity extends BaseActivity implements MessageCenterPresenter.MessageGetDataListener {
    @Bind(R.id.messages_rv)
    RecyclerView messagesRv;
    @Bind(R.id.title_content_tv)
    TextView titleContentTv;
    @Bind(R.id.errorView)
    ErrorView errorView;
    private MessageCenterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_center);
        ButterKnife.bind(this);
        initTitleBar();
        titleContentTv.setText(getTitle());
        errorView.showLoading();
        messagesRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new MessageCenterAdapter(this);
        messagesRv.setAdapter(adapter);
        MessageCenterPresenter.getMessageList(this, this);
    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }


    @Override
    protected void onResume() {
        MobclickAgent.onPageStart("MessageCenterActivity");

        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MessageCenterActivity");
    }

    @Override
    public void onSuccess(MessageModel arrayList) {
        errorView.himeView();
        adapter.initData(arrayList.getResult());
    }

    @Override
    public void onError() {
        errorView.showViewDefault(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorView.showLoading();
                MessageCenterPresenter.getMessageList(MessageCenterActivity.this, MessageCenterActivity.this);
            }
        });
    }
}
