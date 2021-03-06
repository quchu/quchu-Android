package co.quchu.quchu.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseFragment;
import co.quchu.quchu.dialog.CommonDialog;
import co.quchu.quchu.model.FavoriteBean;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.presenter.FavoritePresenter;
import co.quchu.quchu.presenter.InterestingDetailPresenter;
import co.quchu.quchu.presenter.PageLoadListener;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.view.activity.QuchuDetailsActivity;
import co.quchu.quchu.view.adapter.AdapterBase;
import co.quchu.quchu.view.adapter.FavoriteQuchuAdapter;

/**
 * Created by no21 on 2016/6/21.
 * email:437943145@qq.com
 * desc :收藏的趣处
 */
public class FavoriteQuchuFragment extends BaseFragment implements AdapterBase.OnLoadmoreListener,
        SwipeRefreshLayout.OnRefreshListener, AdapterBase.OnItemClickListener<FavoriteBean.ResultBean>, PageLoadListener<FavoriteBean> {


    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    private FavoriteQuchuAdapter adapter;
    private FavoritePresenter presenter;
    private int pagesNo = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_refresh_recyclerview, container, false);
        ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        presenter = new FavoritePresenter(getContext());
        adapter = new FavoriteQuchuAdapter();
        adapter.setLoadmoreListener(this);
        adapter.setItemClickListener(this);
        recyclerView.setAdapter(adapter);
        presenter.getFavoriteData(pagesNo, this);

        refreshLayout.setOnRefreshListener(this);
        return view;
    }


    @Override
    public void onLoadmore() {
        presenter.getFavoriteData(pagesNo + 1, this);
    }

    RecyclerView.ViewHolder holder;
    FavoriteBean.ResultBean item;

    @Override
    public void itemClick(final RecyclerView.ViewHolder holder, final FavoriteBean.ResultBean item, int type, int position) {
        MobclickAgent.onEvent(getContext(), "detail_profile_c");
        this.holder = holder;
        this.item = item;
        switch (type) {
            case R.id.swipe_delete_action:
                setFavorite(holder, item);
                break;
            case R.id.swipe_delete_content:
                Intent intent = new Intent(getActivity(), QuchuDetailsActivity.class);
                intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_PID, item.getPid());
                intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_FROM, QuchuDetailsActivity.FROM_TYPE_PROFILE);
                startActivity(intent);
                if (!EventBus.getDefault().isRegistered(this)) {
                    EventBus.getDefault().register(this);
                }
                break;
        }
    }

    /**
     * 收藏
     */
    private void setFavorite(final RecyclerView.ViewHolder holder, final FavoriteBean.ResultBean item) {
        CommonDialog dialog = CommonDialog.newInstance("提示", "确定取消收藏吗?", "确定", "取消");

        dialog.setListener(new CommonDialog.OnActionListener() {
            @Override
            public boolean dialogClick(int clickId) {
                if (clickId == CommonDialog.CLICK_ID_ACTIVE) {
                    InterestingDetailPresenter.setDetailFavorite(getActivity(), item.getPid(), true, new InterestingDetailPresenter.DetailDataListener() {
                        @Override
                        public void onSuccessCall(String str) {
                            adapter.removeItem(holder, item);
                            Toast.makeText(getActivity(), "取消收藏!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onErrorCall(String str) {
                            Toast.makeText(getActivity(), getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                return true;
            }
        });
        dialog.show(getChildFragmentManager(), null);


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
        Toast.makeText(getActivity(), getString(R.string.network_error), Toast.LENGTH_SHORT).show();

        adapter.setNetError(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getFavoriteData(pagesNo, FavoriteQuchuFragment.this);
            }
        });
    }

    @Override
    public void onDestroy() {
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
