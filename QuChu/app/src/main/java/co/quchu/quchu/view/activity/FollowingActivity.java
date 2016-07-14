package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.model.FollowUserModel;
import co.quchu.quchu.presenter.FollowPresenter;
import co.quchu.quchu.presenter.PageLoadListener;
import co.quchu.quchu.view.adapter.AdapterBase;
import co.quchu.quchu.view.adapter.FriendsAdatper;

/**
 * FollowingActivity
 * User: Chenhs
 * Date: 2016-03-01
 * TA关注的  /我关注的
 */
public class FollowingActivity extends BaseActivity implements AdapterBase.OnLoadmoreListener, PageLoadListener<List<FollowUserModel>>, SwipeRefreshLayout.OnRefreshListener {
    public static final int TAFOLLOWING = 0x01;//TA关注的
    public static final int TAFOLLOWERS = 0x02;//关注TA的
    @Bind(R.id.follow_rv)
    RecyclerView followRv;
    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    private int followType = 0x01, userId = 0;

    FriendsAdatper adatper;
    private EnhancedToolbar toolbar;
    private int pageNo = 1;
    private FollowPresenter presenter;

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

        presenter = new FollowPresenter(this, this);


        refreshLayout.setOnRefreshListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.getFollow(userId, followType, false, pageNo);
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
        presenter.getFollow(userId, followType, false, pageNo + 1);
    }


    @Override
    public void initData(List<FollowUserModel> data) {
        refreshLayout.setRefreshing(false);

        adatper.initData(data);
    }

    @Override
    public void moreData(List<FollowUserModel> data) {
        refreshLayout.setRefreshing(false);

        pageNo++;
        adatper.addMoreData(data);
    }

    @Override
    public void nullData() {
        refreshLayout.setRefreshing(false);

        adatper.setLoadMoreEnable(false);
    }

    @Override
    public void netError(final int pageNo, String massage) {
        refreshLayout.setRefreshing(false);
        Toast.makeText(this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();

        adatper.setNetError(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getFollow(userId, followType, false, pageNo);
            }
        });
    }

    @Override
    public void onRefresh() {
        pageNo = 1;
        presenter.getFollow(userId, followType, false, pageNo);
    }
}
