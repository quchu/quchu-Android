package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.model.FollowUserModel;
import co.quchu.quchu.presenter.FollowPresenter;
import co.quchu.quchu.view.adapter.FriendsAdatper;

/**
 * FollowingActivity
 * User: Chenhs
 * Date: 2016-03-01
 * TA关注的  /我关注的
 */
public class FollowingActivity extends BaseActivity implements View.OnClickListener {
    public static final int TAFOLLOWING = 0x01;//TA关注的
    public static final int TAFOLLOWERS = 0x02;//关注TA的
    @Bind(R.id.follow_rv)
    RecyclerView followRv;
    @Bind(R.id.title_content_tv)
    TextView titleContentTv;
    private int followType = 0x01, userId = 0;
    private ArrayList<FollowUserModel> list;

    FriendsAdatper adatper;
    FriendsListCallBack callBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);
        ButterKnife.bind(this);
        initTitleBar();
        followType = getIntent().getIntExtra("FollowType", 0x01);
        userId = getIntent().getIntExtra("UserId", 0x01);
        setTitleContentView();
        list = new ArrayList<>();
        adatper = new FriendsAdatper(this, list);
        adatper.setIsInnerClick(true);
        followRv.setLayoutManager(new LinearLayoutManager(this));
        callBack = new FriendsListCallBack();
        followRv.setAdapter(adatper);
        FollowPresenter.getFollowsList(this, userId, followType, 1, callBack);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    private void setTitleContentView() {
        switch (followType) {
            case TAFOLLOWING://TA关注的
                titleContentTv.setText("TA关注的");
                break;
            case TAFOLLOWERS://关注TA的
                titleContentTv.setText("关注TA的");
                break;
        }
    }

    class FriendsListCallBack implements FollowPresenter.GetFollowCallBack {

        @Override
        public void onSuccess(ArrayList<FollowUserModel> lists) {
            if (list == null)
                list = new ArrayList<FollowUserModel>();
            list.addAll(lists);
            adatper.notifyDataSetChanged();
        }

        @Override
        public void onError() {

        }
    }
}
