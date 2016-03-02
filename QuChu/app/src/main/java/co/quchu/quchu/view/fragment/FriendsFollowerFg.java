package co.quchu.quchu.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseFragment;
import co.quchu.quchu.view.adapter.FriendsAdatper;

/**
 * FriendsFollowerFg
 * User: Chenhs
 * Date: 2015-11-09
 */
public class FriendsFollowerFg extends BaseFragment {
    View view;
    @Bind(R.id.fragment_firends_rv)
    RecyclerView fragmentFirendsRv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_friends_rv_view, null);
        ButterKnife.bind(this, view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        fragmentFirendsRv.setLayoutManager(mLayoutManager);
        fragmentFirendsRv.setAdapter(new FriendsAdatper(getActivity()));
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
