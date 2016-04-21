package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.model.FollowUserModel;
import co.quchu.quchu.presenter.FollowPresenter;
import co.quchu.quchu.view.adapter.AdapterBase;
import co.quchu.quchu.view.adapter.FriendsAdatper;

/**
 * FollowingActivity
 * User: Chenhs
 * Date: 2016-03-01
 * TA关注的  /我关注的
 */
public class FollowingActivity extends BaseActivity implements AdapterBase.OnLoadmoreListener {
    public static final int TAFOLLOWING = 0x01;//TA关注的
    public static final int TAFOLLOWERS = 0x02;//关注TA的
    @Bind(R.id.follow_rv)
    RecyclerView followRv;
    private int followType = 0x01, userId = 0;

    FriendsAdatper adatper;
    private EnhancedToolbar toolbar;
    private int pageNo = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);
        ButterKnife.bind(this);
        toolbar = getEnhancedToolbar();
        followType = getIntent().getIntExtra("FollowType", 0x01);
        userId = getIntent().getIntExtra("UserId", 0x01);
        setTitleContentView();
        adatper = new FriendsAdatper(this);
        adatper.setIsInnerClick(true);
        adatper.setLoadmoreListener(this);
        followRv.setLayoutManager(new LinearLayoutManager(this));
        followRv.setAdapter(adatper);
        FollowPresenter.getFollowsList(this, userId, followType, pageNo, new FollowPresenter.GetFollowCallBack() {
            @Override
            public void onSuccess(ArrayList<FollowUserModel> lists) {
                adatper.initData(lists);
            }

            @Override
            public void onError() {
                adatper.setLoadMoreEnable(false);
            }
        });
    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }


    private void setTitleContentView() {
        switch (followType) {
            case TAFOLLOWING://TA关注的
                toolbar.getTitleTv().setText("TA关注的");
                break;
            case TAFOLLOWERS://关注TA的
                toolbar.getTitleTv().setText("关注TA的");
                break;
        }
    }

    @Override
    public void onLoadmore() {
        FollowPresenter.getFollowsList(this, userId, followType, pageNo + 1, new FollowPresenter.GetFollowCallBack() {
            @Override
            public void onSuccess(ArrayList<FollowUserModel> lists) {
                pageNo++;
                adatper.addMoreData(lists);

            }

            @Override
            public void onError() {
                adatper.setLoadMoreEnable(false);
            }
        });
    }


}
