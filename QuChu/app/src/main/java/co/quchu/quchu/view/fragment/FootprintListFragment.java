package co.quchu.quchu.view.fragment;

import android.content.Intent;
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
import co.quchu.quchu.model.PostCardItemModel;
import co.quchu.quchu.model.PostCardModel;
import co.quchu.quchu.presenter.MyFootprintPresenter;
import co.quchu.quchu.presenter.PageLoadListener;
import co.quchu.quchu.view.activity.MyFootprintDetailActivity;
import co.quchu.quchu.view.adapter.AdapterBase;
import co.quchu.quchu.view.adapter.MyFootprintAdapter;

/**
 * Created by no21 on 2016/5/23.
 * email:437943145@qq.com
 * desc :
 */
public class FootprintListFragment extends BaseFragment implements AdapterBase.OnLoadmoreListener, AdapterBase.OnItemClickListener<PostCardItemModel>, PageLoadListener<PostCardModel> {

    @Bind(R.id.recyclerView)
     RecyclerView recyclerView;

    private MyFootprintPresenter presenter;
    private int userId;
    private int pagesNo = 1;
    private MyFootprintAdapter adapter;
    public static final String REQUEST_KEY_USER_ID = "userId";

    public static FootprintListFragment newInstance(int userId) {
        FootprintListFragment fragment = new FootprintListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(REQUEST_KEY_USER_ID, userId);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_footprint_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        adapter = new MyFootprintAdapter();
        adapter.setLoadmoreListener(this);
        recyclerView.setAdapter(adapter);
        adapter.setItemClickListener(this);
        userId = getArguments().getInt(REQUEST_KEY_USER_ID);

        presenter = new MyFootprintPresenter(getContext(), this);
        presenter.getMyFoiotrintList(userId, pagesNo);
    }

    @Override
    public void onLoadmore() {
        presenter.getMyFoiotrintList(userId, pagesNo + 1);
    }

    @Override
    public void itemClick(PostCardItemModel item, int type, int position) {
        Intent intent = new Intent(getContext(), MyFootprintDetailActivity.class);
        intent.putExtra(MyFootprintDetailActivity.REQUEST_KEY_POSITION, position);
        intent.putParcelableArrayListExtra(MyFootprintDetailActivity.REQUEST_KEY_MODEL, (ArrayList) adapter.getData());
        startActivity(intent);
    }

    @Override
    public void initData(PostCardModel data) {
        pagesNo = data.getPagesNo();
        recyclerView.setVisibility(View.VISIBLE);
        adapter.initData(data.getResult());
    }

    @Override
    public void moreData(PostCardModel data) {
        pagesNo = data.getPagesNo();
        adapter.addMoreData(data.getResult());
    }

    @Override
    public void nullData() {
        adapter.setLoadMoreEnable(false);
    }

    @Override
    public void netError(final int pageNo, String massage) {
        adapter.setNetError(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getMyFoiotrintList(userId, pageNo);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
