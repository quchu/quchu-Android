package co.quchu.quchu.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseFragment;
import co.quchu.quchu.model.FavoriteBean;
import co.quchu.quchu.presenter.IFavoriteFragment;
import co.quchu.quchu.presenter.QuchuPresenter;
import co.quchu.quchu.view.activity.QuchuDetailsActivity;
import co.quchu.quchu.view.adapter.FavoriteAdapter;
import co.quchu.quchu.widget.ErrorView;

/**
 * Created by no21 on 2016/4/6.
 * email:437943145@qq.com
 * desc :收藏
 */
public class FavoriteFragment extends BaseFragment implements IFavoriteFragment, FavoriteAdapter.OnItemClickListener {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.errorView)
    ErrorView errorView;
    private QuchuPresenter presenter;

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
        presenter.getFavoriteData(1, this);
        errorView.showLoading();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void showData(boolean isError, FavoriteBean bean) {
        if (isVisible()) {
            if (isError) {
                recyclerView.setVisibility(View.GONE);

                errorView.showViewDefault(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.getFavoriteData(1, FavoriteFragment.this);
                    }
                });
            } else {
                FavoriteAdapter adapter = new FavoriteAdapter(bean.getResult());
                adapter.setListener(this);
                errorView.himeView();
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(adapter);

            }
        }
    }


    @Override
    public void itemClick(FavoriteBean.ResultBean item) {
        Intent intent = new Intent(getActivity(), QuchuDetailsActivity.class);
        intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_PID, item.getPid());
        startActivity(intent);
    }
}
