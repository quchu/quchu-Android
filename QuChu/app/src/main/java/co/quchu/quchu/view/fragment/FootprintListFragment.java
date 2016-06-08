package co.quchu.quchu.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseFragment;
import co.quchu.quchu.model.FootprintModel;
import co.quchu.quchu.model.PostCardItemModel;
import co.quchu.quchu.model.PostCardModel;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.presenter.MyFootprintPresenter;
import co.quchu.quchu.presenter.PageLoadListener;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.view.activity.MyFootprintDetailActivity;
import co.quchu.quchu.view.activity.QuchuDetailsActivity;
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

        presenter = new MyFootprintPresenter(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        pagesNo = 1;
        presenter.getMyFoiotrintList(userId, pagesNo, this);
    }

    @Override
    public void onLoadmore() {
        presenter.getMyFoiotrintList(userId, pagesNo + 1, this);
    }

    RecyclerView.ViewHolder holder;
    PostCardItemModel item;

    @Override
    public void itemClick(RecyclerView.ViewHolder holder, PostCardItemModel item, int type, int position) {
        this.holder = holder;
        this.item = item;
        Intent intent;
        switch (type) {
            case MyFootprintAdapter.CLICK_TYPE_IMAGE:
                intent = new Intent(getContext(), MyFootprintDetailActivity.class);
//                intent.putExtra(MyFootprintDetailActivity.REQUEST_KEY_IMAGE_LIST, item);
                ArrayList<FootprintModel.Entity> entitys = new ArrayList<>();
                int seletedPosition = 0;
                for (int i = 0, s = adapter.getData().size(); i < s; i++) {
                    if (i == position) {
                        seletedPosition = entitys.size();
                    }
                    PostCardItemModel model = adapter.getData().get(i);
                    List<FootprintModel.Entity> entity = model.convertToList();
                    entitys.addAll(entity);
                }
                intent.putParcelableArrayListExtra(MyFootprintDetailActivity.REQUEST_KEY_ENTITY_LIST, entitys);
                intent.putExtra(MyFootprintDetailActivity.REQUEST_KEY_SELECTED_POSITION, seletedPosition);

                getActivity().startActivity(intent);

                break;
            case MyFootprintAdapter.CLICK_TYPE_NAME:
                intent = new Intent(getContext(), QuchuDetailsActivity.class);
                intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_FROM, QuchuDetailsActivity.FROM_TYPE_SUBJECT);
                intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_PID, item.getPlaceId());
                startActivity(intent);
                break;
        }
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
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
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe
    public void FootprintChange(QuchuEventModel model) {
        switch (model.getFlag()) {
            case EventFlags.EVENT_POST_CARD_DELETED:
                if (item.getCardId() == (int) model.getContent()[0]) {
                    adapter.removeItem(holder, item);
                }
                break;
            case EventFlags.EVENT_GOTO_HOME_PAGE:
                getActivity().finish();
                break;
        }
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
                presenter.getMyFoiotrintList(userId, pageNo, FootprintListFragment.this);
            }
        });
    }
}
