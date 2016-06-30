package co.quchu.quchu.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseFragment;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.ArticleModel;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.ResponseListener;
import co.quchu.quchu.presenter.ArticlePresenter;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.utils.AppKey;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.view.activity.WebViewActivity;
import co.quchu.quchu.view.adapter.ArticleAdapter;
import co.quchu.quchu.widget.ErrorView;

/**
 * ArticleFragment
 * User: Chenhs
 * Date: 2015-12-07
 * 分类
 */
public class ArticleFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.fragment_firends_rv)
    RecyclerView recyclerView;
    @Bind(R.id.errorView)
    ErrorView errorView;
    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    private ArticleAdapter cAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_classify, container, false);
        ButterKnife.bind(this, view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.addItemDecoration(new ClassifyDecoration(getActivity()));
        refreshLayout.setOnRefreshListener(this);

        getRootTagsData();


        return view;
    }

    /**
     * 获取分类信息
     */
    public void getRootTagsData() {

        ArticlePresenter.getArticles(getActivity(), SPUtils.getCityId(), 1, new CommonListener<List<ArticleModel>>() {
            @Override
            public void successListener(List<ArticleModel> response) {
                DialogUtil.dismissProgessDirectly();
                recyclerView.setVisibility(View.VISIBLE);
                errorView.hideView();
                refreshLayout.setRefreshing(false);

                cAdapter = new ArticleAdapter(getActivity(), response);
                recyclerView.setAdapter(cAdapter);
                cAdapter.setOnItemCliskListener(new ArticleAdapter.ClasifyClickListener() {
                    @Override
                    public void cItemClick(View view, int position) {
                        //ArticleModel model = response.get(position);

                    }
                });
            }

            @Override
            public void errorListener(VolleyError error, String exception, String msg) {
                recyclerView.setVisibility(View.GONE);
                DialogUtil.dismissProgessDirectly();
                errorView.showViewDefault(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtil.showProgess(getActivity(), "加载中");
                        getRootTagsData();
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
    public void onResume() {
        MobclickAgent.onPageStart("h_discovery");
        super.onResume();
    }

    @Override
    public void onPause() {
        MobclickAgent.onPageEnd("h_discovery");
        super.onPause();
    }

    @Override
    public void onRefresh() {
        getRootTagsData();
    }
}
