package co.quchu.quchu.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseFragment;
import co.quchu.quchu.model.EssayBean;
import co.quchu.quchu.presenter.PageLoadListener;
import co.quchu.quchu.view.adapter.AdapterBase;
import co.quchu.quchu.view.adapter.FavoriteEssayAdapter;

/**
 * Created by no21 on 2016/6/21.
 * email:437943145@qq.com
 * desc :
 */
public class FavoriteEssayFragment extends BaseFragment implements AdapterBase.OnLoadmoreListener,
        AdapterBase.OnItemClickListener<EssayBean>, PageLoadListener<EssayBean>, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    private FavoriteEssayAdapter adapter;
    private int pagesNo = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_refresh_recyclerview, container, false);
        ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        adapter = new FavoriteEssayAdapter();
        adapter.setLoadmoreListener(this);
        adapter.setItemClickListener(this);
        recyclerView.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(this);

        ArrayList<EssayBean> list = new ArrayList<>();
        list.add(new EssayBean());
        list.add(new EssayBean());
        list.add(new EssayBean());
        list.add(new EssayBean());
        list.add(new EssayBean());
        list.add(new EssayBean());
        list.add(new EssayBean());
        list.add(new EssayBean());
        list.add(new EssayBean());
        list.add(new EssayBean());
        list.add(new EssayBean());
        list.add(new EssayBean());
        list.add(new EssayBean());
        list.add(new EssayBean());
        list.add(new EssayBean());
        list.add(new EssayBean());
        adapter.initData(list);

        return view;
    }


    @Override
    public void onLoadmore() {
    }

    @Override
    public void itemClick(RecyclerView.ViewHolder holder, EssayBean item, int type, int position) {

    }

    @Override
    public void initData(EssayBean bean) {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void moreData(EssayBean data) {
        refreshLayout.setRefreshing(false);
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
            }
        });
    }


    @Override
    public void onRefresh() {
        pagesNo = 1;

    }
}
