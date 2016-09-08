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


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseFragment;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.ArticleBannerModel;
import co.quchu.quchu.model.ArticleModel;
import co.quchu.quchu.model.ArticleWithBannerModel;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.net.NetUtil;
import co.quchu.quchu.presenter.ArticlePresenter;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.view.activity.ArticleDetailActivity;
import co.quchu.quchu.view.adapter.ArticleAdapter;
import co.quchu.quchu.view.adapter.CommonItemClickListener;
import co.quchu.quchu.widget.EndlessRecyclerOnScrollListener;
import co.quchu.quchu.widget.ErrorView;
import co.quchu.quchu.widget.SpacesItemDecoration;

/**
 * ArticleFragment
 * User: Chenhs
 * Date: 2015-12-07
 * 分类
 */
public class ArticleFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {


    @Override
    protected String getPageNameCN() {
        return getString(R.string.pname_f_qufaxian);
    }

    @Bind(R.id.fragment_firends_rv)
    RecyclerView recyclerView;
    @Bind(R.id.errorView)
    ErrorView errorView;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout refreshLayout;

    private List<ArticleModel> articleModels = new ArrayList<>();
    private List<ArticleBannerModel> articleBanner = new ArrayList<>();
    private ArticleAdapter mAdapter;
    private int mMaxPageNo = -1;
    private int mPageNo = 1;
    private EndlessRecyclerOnScrollListener mListener;
    public boolean mLoadMoreRunning = false;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_classify, container, false);
        ButterKnife.bind(this, view);

        mAdapter = new ArticleAdapter(getActivity(), articleModels, articleBanner);
        mAdapter.setOnItemClickListener(new CommonItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                String articleId = articleModels.get(position).getArticleId();
                String articleTitle = articleModels.get(position).getArticleName();
                ArticleDetailActivity.enterActivity(getActivity(), articleId, articleTitle,"趣发现");
            }
        });

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        //recyclerView.addItemDecoration(new SpacesItemDecoration(20));
        refreshLayout.setOnRefreshListener(this);
        mListener = new EndlessRecyclerOnScrollListener(recyclerView.getLayoutManager()) {
            @Override
            public void onLoadMore(int current_page) {
                loadMore();
            }
        };
        recyclerView.addOnScrollListener(mListener);


        if (!NetUtil.isNetworkConnected(getActivity()) || articleModels == null) {
            errorView.showViewDefault(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogUtil.showProgess(getActivity(), "加载中");
                    getArticles(true);
                }
            });
        }
        getArticles(true);


        return view;
    }



    public void loadMore() {
        if (mLoadMoreRunning) {
            Toast.makeText(getActivity(), R.string.process_running_please_wait, Toast.LENGTH_SHORT).show();
            return;
        }
        mPageNo += 1;
        if (mMaxPageNo != -1 && mMaxPageNo <= mPageNo) {
            mAdapter.showPageEnd(true);
            return;
        }
        DialogUtil.showProgess(getActivity(), R.string.loading_dialog_text);

        ArticlePresenter.getArticles(getActivity(), SPUtils.getCityId(), mMaxPageNo, new CommonListener<ArticleWithBannerModel>() {
            @Override
            public void successListener(ArticleWithBannerModel response) {
                articleModels.addAll(response.getArticleList().getResult());
                mAdapter.notifyDataSetChanged();
                DialogUtil.dismissProgessDirectly();
                mListener.loadingComplete();
            }

            @Override
            public void errorListener(VolleyError error, String exception, String msg) {
                DialogUtil.dismissProgessDirectly();
                mListener.loadingComplete();
            }
        });

    }


    /**
     * 获取分类信息
     */
    public void getArticles(final boolean firstLoad) {
        if (firstLoad) {
            DialogUtil.showProgess(getActivity(), R.string.loading_dialog_text);
        }
        ArticlePresenter.getArticles(getActivity(), SPUtils.getCityId(), 1, new CommonListener<ArticleWithBannerModel>() {
            @Override
            public void successListener(final ArticleWithBannerModel response) {
                if (null!=mAdapter){
                    mAdapter.showPageEnd(false);
                }
                if (firstLoad) {
                    DialogUtil.dismissProgessDirectly();
                }
                if (null == recyclerView) {
                    return;
                }
                mMaxPageNo = response.getArticleList().getPageCount();
                mPageNo = 1;
                recyclerView.setVisibility(View.VISIBLE);
                errorView.hideView();
                refreshLayout.setRefreshing(false);

                articleModels.clear();
                articleModels.addAll(response.getArticleList().getResult());

                articleBanner.clear();
                articleBanner.addAll(response.getArticleTitleList());
                recyclerView.setAdapter(mAdapter);

            }

            @Override
            public void errorListener(VolleyError error, String exception, String msg) {
                if (firstLoad) {
                    DialogUtil.dismissProgessDirectly();
                }
                recyclerView.setVisibility(View.GONE);
                errorView.showViewDefault(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtil.showProgess(getActivity(), "加载中");
                        getArticles(false);
                    }
                });
                refreshLayout.setRefreshing(false);
            }
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }



    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onMessageEvent(QuchuEventModel event) {
        switch (event.getFlag()) {
            case EventFlags.EVENT_DEVICE_NETWORK_AVAILABLE:
                getArticles(false);
                break;
        }
    }

    @Override
    public void onRefresh() {
        if (NetUtil.isNetworkConnected(getActivity())) {
            getArticles(false);
        } else {
            Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
            refreshLayout.setRefreshing(false);
        }
    }
}
