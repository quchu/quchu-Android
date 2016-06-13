package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.model.FavoriteBean;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.presenter.PageLoadListener;
import co.quchu.quchu.presenter.QuchuPresenter;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.view.adapter.AdapterBase;
import co.quchu.quchu.view.adapter.FavoriteAdapter;

public class FavoriteActivity extends BaseActivity implements AdapterBase.OnLoadmoreListener, AdapterBase.OnItemClickListener<FavoriteBean.ResultBean>, PageLoadListener<FavoriteBean>, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    private FavoriteAdapter adapter;
    private QuchuPresenter presenter;
    private int pagesNo = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        ButterKnife.bind(this);
        EnhancedToolbar toolbar = getEnhancedToolbar();
        TextView titleTv = toolbar.getTitleTv();
        titleTv.setText("收藏");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        presenter = new QuchuPresenter(this);
        adapter = new FavoriteAdapter();
        adapter.setLoadmoreListener(this);
        adapter.setItemClickListener(this);
        recyclerView.setAdapter(adapter);
        presenter.getFavoriteData(pagesNo, this);

        refreshLayout.setOnRefreshListener(this);
    }


    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }

    @Override
    public void onLoadmore() {
        presenter.getFavoriteData(pagesNo + 1, this);
    }

    RecyclerView.ViewHolder holder;
    FavoriteBean.ResultBean item;

    @Override
    public void itemClick(RecyclerView.ViewHolder holder, FavoriteBean.ResultBean item, int type, int position) {
        MobclickAgent.onEvent(this, "detail_profile_c");
        this.holder = holder;
        this.item = item;
        Intent intent = new Intent(this, QuchuDetailsActivity.class);
        intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_PID, item.getPid());
        intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_FROM, QuchuDetailsActivity.FROM_TYPE_PROFILE);
        startActivity(intent);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void initData(FavoriteBean bean) {
        refreshLayout.setRefreshing(false);

        adapter.initData(bean.getResult());
        pagesNo = bean.getPagesNo();

    }

    @Override
    public void moreData(FavoriteBean data) {
        pagesNo = data.getPagesNo();
        refreshLayout.setRefreshing(false);

        adapter.addMoreData(data.getResult());
    }

    @Override
    public void nullData() {
        refreshLayout.setRefreshing(false);

        adapter.setLoadMoreEnable(false);
    }

    @Override
    public void netError(final int pagesNo, String massage) {
        refreshLayout.setRefreshing(false);
        Toast.makeText(this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();

        adapter.setNetError(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getFavoriteData(pagesNo, FavoriteActivity.this);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe
    public void unFavorite(QuchuEventModel bean) {
        if (bean.getFlag() == EventFlags.EVENT_CANCLE_FAVORITE_QUCHU) {
            if (!(Boolean) bean.getContent()[0] && (int) bean.getContent()[1] == item.getPid()) {
                adapter.removeItem(holder, item);
            }
        }
    }

    @Override
    public void onRefresh() {
        pagesNo = 1;
        presenter.getFavoriteData(pagesNo, this);

    }
}
