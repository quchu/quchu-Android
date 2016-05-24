package co.quchu.quchu.view.activity;

import android.content.Intent;
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
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.model.FavoriteBean;
import co.quchu.quchu.presenter.PageLoadListener;
import co.quchu.quchu.presenter.QuchuPresenter;
import co.quchu.quchu.view.adapter.AdapterBase;
import co.quchu.quchu.view.adapter.FavoriteAdapter;

public class FavoriteActivity extends BaseActivity implements AdapterBase.OnLoadmoreListener, AdapterBase.OnItemClickListener<FavoriteBean.ResultBean>, PageLoadListener<FavoriteBean> {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
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


//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchCallback(adapter));
//        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }

    @Override
    public void onLoadmore() {
        presenter.getFavoriteData(pagesNo + 1, this);
    }

    @Override
    public void itemClick(RecyclerView.ViewHolder holder,FavoriteBean.ResultBean item, int type, int position) {
        MobclickAgent.onEvent(this, "detail_profile_c");
        Intent intent = new Intent(this, QuchuDetailsActivity.class);
        intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_PID, item.getPid());
        intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_FROM, QuchuDetailsActivity.FROM_TYPE_PROFILE);
        startActivity(intent);
    }

    @Override
    public void initData(FavoriteBean bean) {
        adapter.initData(bean.getResult());
        pagesNo = bean.getPagesNo();

    }

    @Override
    public void moreData(FavoriteBean data) {
        pagesNo = data.getPagesNo();
        adapter.addMoreData(data.getResult());
    }

    @Override
    public void nullData() {
        adapter.setLoadMoreEnable(false);
    }

    @Override
    public void netError(final int pagesNo, String massage) {
        adapter.setNetError(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getFavoriteData(pagesNo, FavoriteActivity.this);
            }
        });
    }
}
