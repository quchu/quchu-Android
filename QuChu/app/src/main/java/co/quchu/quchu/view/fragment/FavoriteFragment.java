package co.quchu.quchu.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseFragment;
import co.quchu.quchu.model.FavoriteBean;
import co.quchu.quchu.presenter.PageLoadListener;
import co.quchu.quchu.presenter.QuchuPresenter;
import co.quchu.quchu.view.activity.QuchuDetailsActivity;
import co.quchu.quchu.view.adapter.AdapterBase;
import co.quchu.quchu.view.adapter.FavoriteAdapter;

/**
 * Created by no21 on 2016/4/6.
 * email:437943145@qq.com
 * desc :收藏
 */
public class FavoriteFragment extends BaseFragment implements PageLoadListener<FavoriteBean>, AdapterBase.OnLoadmoreListener, AdapterBase.OnItemClickListener<FavoriteBean.ResultBean> {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private QuchuPresenter presenter;
    private int pagesNo = 1;
    private FavoriteAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        presenter = new QuchuPresenter(getActivity());
        adapter = new FavoriteAdapter();
        adapter.setLoadmoreListener(this);
        adapter.setItemClickListener(this);
        recyclerView.setAdapter(adapter);
        presenter.getFavoriteData(pagesNo, this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onLoadmore() {
        presenter.getFavoriteData(pagesNo + 1, this);
    }

    @Override
    public void itemClick(FavoriteBean.ResultBean item, int type, int position) {
        MobclickAgent.onEvent(getContext(), "detail_profile_c");
        Intent intent = new Intent(getActivity(), QuchuDetailsActivity.class);
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
                presenter.getFavoriteData(pagesNo, FavoriteFragment.this);
            }
        });
    }
}
