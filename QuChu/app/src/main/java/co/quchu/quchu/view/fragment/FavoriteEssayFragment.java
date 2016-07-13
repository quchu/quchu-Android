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

import com.android.volley.VolleyError;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseFragment;
import co.quchu.quchu.dialog.CommonDialog;
import co.quchu.quchu.model.FavoriteEssayBean;
import co.quchu.quchu.presenter.ArticlePresenter;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.FavoritePresenter;
import co.quchu.quchu.presenter.PageLoadListener;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.view.activity.ArticleDetailActivity;
import co.quchu.quchu.view.adapter.AdapterBase;
import co.quchu.quchu.view.adapter.FavoriteEssayAdapter;

/**
 * Created by no21 on 2016/6/21.
 * email:437943145@qq.com
 * desc :
 */
public class FavoriteEssayFragment extends BaseFragment implements AdapterBase.OnLoadmoreListener,
        AdapterBase.OnItemClickListener<FavoriteEssayBean.ResultBean>, PageLoadListener<FavoriteEssayBean>, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    private FavoriteEssayAdapter adapter;
    private int pagesNo = 1;
    private FavoritePresenter presenter;

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

        presenter = new FavoritePresenter(getContext());
        presenter.getFavoriteEssay(SPUtils.getCityId(), pagesNo, this);

        return view;
    }


    @Override
    public void onLoadmore() {
        presenter.getFavoriteEssay(SPUtils.getCityId(), pagesNo + 1, this);
    }

    @Override
    public void itemClick(final RecyclerView.ViewHolder holder, final FavoriteEssayBean.ResultBean item, int type, int position) {
        if (type == R.id.swipe_delete_content) {
            ArticleDetailActivity.enterActivity(getActivity(), String.valueOf(item.getArticleId()), item.getArticleName());
        } else {
            CommonDialog dialog = CommonDialog.newInstance("提示", "确定取消收藏吗?", "确定", "取消");

            dialog.setListener(new CommonDialog.OnActionListener() {
                @Override
                public boolean dialogClick(int clickId) {
                    if (clickId == CommonDialog.CLICK_ID_ACTIVE) {
                        ArticlePresenter.delFavoriteArticle(getContext(), item.getArticleId(), new CommonListener() {
                            @Override
                            public void successListener(Object response) {
                                adapter.removeItem(holder, item);
                            }

                            @Override
                            public void errorListener(VolleyError error, String exception, String msg) {
                                Toast.makeText(getContext(), getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    return true;
                }
            });
            dialog.show(getChildFragmentManager(), null);
        }
    }

    @Override
    public void initData(FavoriteEssayBean bean) {
        refreshLayout.setRefreshing(false);
        adapter.initData(bean.getResult());
    }

    @Override
    public void moreData(FavoriteEssayBean data) {
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
                presenter.getFavoriteEssay(SPUtils.getCityId(), pagesNo, FavoriteEssayFragment.this);

            }
        });
    }


    @Override
    public void onRefresh() {
        pagesNo = 1;
        presenter.getFavoriteEssay(SPUtils.getCityId(), pagesNo, this);
    }
}
