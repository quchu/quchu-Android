package co.quchu.quchu.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseFragment;
import co.quchu.quchu.model.FollowUserModel;
import co.quchu.quchu.presenter.FollowPresenter;
import co.quchu.quchu.presenter.PageLoadListener;
import co.quchu.quchu.view.adapter.AdapterBase;
import co.quchu.quchu.view.adapter.FriendsAdatper;

/**
 * FriendsFollowerFg
 * User: Chenhs
 * Date: 2015-11-09
 */
public class FriendsFollowerFg extends BaseFragment implements AdapterBase.OnLoadmoreListener, PageLoadListener<List<FollowUserModel>> {
    View view;
    @Bind(R.id.fragment_firends_rv)
    RecyclerView fragmentFirendsRv;
    boolean mIsSubscribe = false;
    public static final String BUNDLE_KEY_IS_SUBSCRIBE = "BUNDLE_KEY_IS_SUBSCRIBE";
    public FriendsAdatper mAdapter;
    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    private int pageNo = 1;
    private FollowPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_friends_rv_view, container, false);
        ButterKnife.bind(this, view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        fragmentFirendsRv.setLayoutManager(mLayoutManager);
        mAdapter = new FriendsAdatper(getActivity());
        mAdapter.setLoadmoreListener(this);
        fragmentFirendsRv.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mIsSubscribe = getArguments().getBoolean(BUNDLE_KEY_IS_SUBSCRIBE, true);
        presenter = new FollowPresenter(getContext(), this);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                presenter.getFollow(AppContext.user.getUserId(), mIsSubscribe ? FollowPresenter.TAFOLLOWING : FollowPresenter.TAFOLLOWERS, false, pageNo++);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        pageNo = 1;
        presenter.getFollow(AppContext.user.getUserId(), mIsSubscribe ? FollowPresenter.TAFOLLOWING : FollowPresenter.TAFOLLOWERS, false, pageNo++);
    }

    @Override
    public void onLoadmore() {
        presenter.getFollow(AppContext.user.getUserId(), mIsSubscribe ? FollowPresenter.TAFOLLOWING : FollowPresenter.TAFOLLOWERS, false, pageNo++);
    }

    @Override
    public void initData(List<FollowUserModel> data) {
        mAdapter.initData(data);
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void moreData(List<FollowUserModel> data) {

        mAdapter.addMoreData(data);
    }

    @Override
    public void nullData() {
        mAdapter.setLoadMoreEnable(false);
    }

    @Override
    public void netError(final int pageNo, String massage) {
        refreshLayout.setRefreshing(false);
        mAdapter.setNetError(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getFollow(AppContext.user.getUserId(), mIsSubscribe ? FollowPresenter.TAFOLLOWING : FollowPresenter.TAFOLLOWERS, false, pageNo);

            }
        });
    }
}
