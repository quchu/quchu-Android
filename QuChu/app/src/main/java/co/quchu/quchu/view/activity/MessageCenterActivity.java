package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.model.MessageModel;
import co.quchu.quchu.presenter.MessageCenterPresenter;

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
    @Bind(R.id.empty_view_other_tv)
    TextView emptyViewOtherTv;
    @Bind(R.id.message_empty_view_fl)
    FrameLayout messageEmptyViewFl;

    private ArrayList<MessageModel> messageList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_center);
        ButterKnife.bind(this);
        MessageCenterPresenter.getMessageList(this, new MessageCenterPresenter.MessageGetDataListener() {
            @Override
            public void onSuccess(ArrayList<MessageModel> arrayList) {
                messageList = arrayList;
                messagesSrl.setVisibility(View.VISIBLE);
                messageEmptyViewFl.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                messageEmptyViewFl.setVisibility(View.VISIBLE);
                messagesSrl.setVisibility(View.GONE);
            }
        });
    }
}
