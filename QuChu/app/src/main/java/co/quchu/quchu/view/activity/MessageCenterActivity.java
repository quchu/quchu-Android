package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.model.MessageModel;
import co.quchu.quchu.presenter.MessageCenterPresenter;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.view.adapter.MessageCenterAdapter;

/**
 * MessageCenterActivity
 * User: Chenhs
 * Date: 2016-01-11
 */
public class MessageCenterActivity extends BaseActivity {
    @Bind(R.id.messages_rv)
    RecyclerView messagesRv;
    @Bind(R.id.messages_srl)
    SwipeRefreshLayout messagesSrl;
    @Bind(R.id.action_buttton)
    TextView emptyViewOtherTv;
    @Bind(R.id.message_empty_view_fl)
    FrameLayout messageEmptyViewFl;
    @Bind(R.id.title_content_tv)
    TextView titleContentTv;
    private List<MessageModel> messageList;
    private MessageCenterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_center);
        ButterKnife.bind(this);
        initTitleBar();
        titleContentTv.setText(getTitle());
        messagesRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        messageList = new ArrayList<>();
        adapter = new MessageCenterAdapter(this, messageList);
        messagesRv.setAdapter(adapter);
        MessageCenterPresenter.getMessageList(this, new MessageCenterPresenter.MessageGetDataListener() {
            @Override
            public void onSuccess(List<MessageModel> arrayList) {
                LogUtils.json("message size ==" + arrayList.size());
                messageList = arrayList;
                messagesSrl.setVisibility(View.VISIBLE);
                messageEmptyViewFl.setVisibility(View.GONE);
                adapter.changeDateSet(messageList);
            }

            @Override
            public void onError() {
                messageEmptyViewFl.setVisibility(View.VISIBLE);
                messagesSrl.setVisibility(View.GONE);
            }
        });
    }

    @OnClick(R.id.action_buttton)
    public void emptyClick(View view) {
        this.finish();
    }
    @Override
    protected void onResume() {
        MobclickAgent.onPageStart("MessageCenterActivity");
        MobclickAgent.onResume(this);

        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MessageCenterActivity");
        MobclickAgent.onPause(this);
    }
}