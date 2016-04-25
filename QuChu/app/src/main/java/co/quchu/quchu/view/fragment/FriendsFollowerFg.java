package co.quchu.quchu.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseFragment;
import co.quchu.quchu.model.FollowUserModel;
import co.quchu.quchu.presenter.FollowPresenter;
import co.quchu.quchu.view.adapter.AdapterBase;
import co.quchu.quchu.view.adapter.FriendsAdatper;

/**
 * FriendsFollowerFg
 * User: Chenhs
 * Date: 2015-11-09
 */
public class FriendsFollowerFg extends BaseFragment implements AdapterBase.OnLoadmoreListener {
    View view;
    @Bind(R.id.fragment_firends_rv)
    RecyclerView fragmentFirendsRv;
    boolean mIsSubscribe = false;
    public static final String BUNDLE_KEY_IS_SUBSCRIBE = "BUNDLE_KEY_IS_SUBSCRIBE";
    public FriendsAdatper mAdapter;
    private int pageNo = 1;

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


    }

    @Override
    public void onResume() {
        super.onResume();
        pageNo = 1;
        FollowPresenter.getCurrentUserFollowers(getActivity(), false, mIsSubscribe ? FollowPresenter.TAFOLLOWING : FollowPresenter.TAFOLLOWERS, 1, new FollowPresenter.GetFollowCallBack() {
            @Override
            public void onSuccess(ArrayList<FollowUserModel> lists) {
                mAdapter.initData(lists);
            }

            @Override
            public void onError() {
                mAdapter.setLoadMoreEnable(false);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onLoadmore() {
        FollowPresenter.getCurrentUserFollowers(getActivity(), false, mIsSubscribe ? FollowPresenter.TAFOLLOWING : FollowPresenter.TAFOLLOWERS, pageNo + 1, new FollowPresenter.GetFollowCallBack() {
            @Override
            public void onSuccess(ArrayList<FollowUserModel> lists) {
                mAdapter.addMoreData(lists);
            }

            @Override
            public void onError() {
                mAdapter.setLoadMoreEnable(false);

            }
        });
    }
}
